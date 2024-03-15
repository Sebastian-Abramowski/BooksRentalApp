.PHONY: all
all:
	mvn clean javafx:run

.PHONY: pull
pull:
	git stash
	git pull
	git stash apply
	git stash drop
