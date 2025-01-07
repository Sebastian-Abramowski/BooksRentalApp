package org.openjfx.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import org.h2.mvstore.db.RowDataType.Factory;
import org.openjfx.database.Category;
import org.openjfx.database.Person;
import org.openjfx.database.User;
import org.openjfx.helpers.UIFormater;
import org.openjfx.requests.*;


public class AdminViewAddBookController implements Initializable {

	private class PersonCellData {
		private Person person;
		private boolean isEmpty;

		public PersonCellData() {
			person = null;
			isEmpty = false;
		}

		public PersonCellData(Person person) {
			this.person = person;
			isEmpty = false;
		}

		public void SetEmpty() { isEmpty = true; }
		public void SetConten(Person person) {this.person = person; }

		public boolean isEmpty() { return isEmpty; }
		public boolean isAdd() { return person == null && !isEmpty; }
		public Person getPerson() { return person; }
	}

	private class CustomPersonListCell extends ListCell<PersonCellData> {
        private final HBox hbox = new HBox(20);
        private final Label label = new Label();
        private final Button removeButton = new Button("Remove");
        private final ChoiceBox<Person> choiceBox = new ChoiceBox<>();

        public CustomPersonListCell() {
            super();
			label.setMinWidth(320);
            removeButton.setOnAction(event -> {
				getListView().getItems().remove(getIndex()).SetEmpty();
				FixLastCell();
				});
			refreshAuthors();
			choiceBox.setMinWidth(300);
            choiceBox.setOnAction(event -> {
                Person selectedPerson = choiceBox.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    getListView().getItems().get(getIndex()).SetConten(selectedPerson);
					choiceBox.getSelectionModel().clearSelection();
					FixLastCell();
                }
            });
        }

        @Override
        protected void updateItem(PersonCellData item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null || item.isEmpty()) {
				setGraphic(null);
				setText(null);
            }
			else if (item.isAdd()) {
                hbox.getChildren().setAll(choiceBox);
                setGraphic(hbox);
            }
			else {
                label.setText(item.person.getName());
                hbox.getChildren().setAll(removeButton, label);
                setGraphic(hbox);
            }
        }

		private void refreshAuthors() {
			ArrayList<Person> persons = GetPersons.request();
			ArrayList<User> users = GetUsers.request();

			// Filter persons whose IDs are not in user IDs
			ArrayList<Person> filteredAuthors = new ArrayList<>();
			for (Person person : persons) {
				boolean found = false;
				for (User user : users) {
					if (person.getId() == user.getId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					filteredAuthors.add(person);
				}
			}

			ObservableList<Person> authorObservableList = FXCollections.observableArrayList(filteredAuthors);
			choiceBox.setItems(authorObservableList);
		}

		public void FixLastCell() {
			var items = getListView().getItems();
			if (items.size() == 0 || !items.get(items.size() - 1).isAdd()) {
				items.add(new PersonCellData());
				}
			getListView().refresh();
		}
    }


	@FXML
	private Label labelInfo;
	@FXML
	private Label labelErrors;
	@FXML
	private TextField txtISBN;
	@FXML
	private TextField txtTitle;
	@FXML
	private ListView<PersonCellData> listAuthors;
	@FXML
	private ChoiceBox<Category> categoryChoiceBox;
	@FXML
	private Spinner<Integer> amount;
	@FXML
	private ChoiceBox<Integer> ratingChoiceBox;

	private final Integer[] possibleRatings = {1, 2, 3, 4, 5};

	private ObservableList<PersonCellData> selectedAuthors;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		refresh();

		ratingChoiceBox.getItems().addAll(possibleRatings);
		selectedAuthors = FXCollections.observableArrayList();
		selectedAuthors.add(new PersonCellData());
		listAuthors.setItems(selectedAuthors);

		listAuthors.setCellFactory(new Callback<ListView<PersonCellData>, ListCell<PersonCellData>>() {
            @Override
            public ListCell<PersonCellData> call(ListView<PersonCellData> listView) {
                return new CustomPersonListCell();
            }
        });

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1, 1);
		amount.setValueFactory(valueFactory);
	}

	@FXML
	void onSubmitClick(ActionEvent event) {
		labelInfo.setText("");
		labelErrors.setText("");

		try {

			System.out.println(txtISBN.getText());
			var book = AddBook.request(
					txtISBN.getText(),
					txtTitle.getText(),
					ratingChoiceBox.getValue(),
					categoryChoiceBox.getValue());



			if (book == null) {
				if (txtISBN.getText().isEmpty() || txtTitle.getText().isEmpty()){
					labelErrors.setText("Empty object cannot be added");
				}
				else {
					labelErrors.setText("ISBN of the book must be UNIQUE");
				}

				return;
			}

			labelInfo.setText(book.getTitle() + " was added...");
			for (int i=0; i<amount.getValue(); ++i) {
				AddBookInstance.request(book);
			}


			var authors = listAuthors.getItems();
			for (var authorData : authors) {
				if (authorData != null && authorData.getPerson() != null) {
					AddBookAuthor.request(book, authorData.getPerson());
				}
			}


			labelInfo.setText("New book was added: " + txtTitle.getText() + " written by " + UIFormater.formatAuthors(GetBookAuthors.request(book)));
		} catch (Exception e) {
			System.out.println(e);
			labelErrors.setText("Some error occurred. Check once again attributes of the book.");
		}
	}

	@FXML
	void onClearClick(ActionEvent event) {
		txtISBN.clear();
		txtTitle.clear();
		selectedAuthors.clear();
		selectedAuthors.add(new PersonCellData());
		categoryChoiceBox.getSelectionModel().clearSelection();
		ratingChoiceBox.getSelectionModel().clearSelection();
		labelInfo.setText("");
		labelErrors.setText("");
		amount.getValueFactory().setValue(1);
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		refresh();
	}

	private void refreshCategories() {
		ArrayList<Category> categories = GetCategories.request();
		ObservableList<Category> categoryObservableList = FXCollections.observableArrayList(categories);
		categoryChoiceBox.setItems(categoryObservableList);
	}

	private void refresh() {
		refreshCategories();
	}
}
