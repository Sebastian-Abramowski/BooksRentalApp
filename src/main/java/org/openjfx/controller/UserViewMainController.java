package org.openjfx.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openjfx.requests.GetTabs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class UserViewMainController {

    @FXML
    private TabPane tabPane;

	private Map<String, Tab> tabsByName = new HashMap<>();

	public static UserViewMainController instance;

    public void initialize() {

		// remove template tabs
		tabPane.getTabs().removeAll(tabPane.getTabs());

		// add new tabs
		for (var tabData : GetTabs.request(SceneController.getCurrentUser())) {
			try {
				var tab = new Tab(tabData.getTabName());
				//System.out.println(String.format("'%s'\n'%s'\n", tabData.TabName(), tabData.ViewFileName()));
				tab.setContent(SceneController.getParentFromFxml(tabData.getViewFileName()));
				tab.setClosable(false);
				tabPane.getTabs().add(tab);
				tabsByName.put(tabData.getTabName(), tab);
			}
			catch (IOException e) {
				System.out.println("Could not load tab of " + tabData.getTabName() + "\nerror:\n" + e);
			}
		}

		instance = this;
    }

	@FXML
	public void onLogOutClick(ActionEvent event) throws IOException {
		SceneController.signOut(event);
	}

	public void selectTab(String tabName) {
		if (!tabsByName.containsKey(tabName)) {
			System.out.println("Tab of name " + tabName + " does not exist");
			return;
		}
		var selection = tabPane.getSelectionModel();
		selection.select(tabsByName.get(tabName));
	}
}
