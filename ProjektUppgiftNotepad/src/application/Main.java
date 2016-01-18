package application;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem newFile, openFile, saveFile, saveFileAs, exit;
	private FileHandler fileHandler;
	private SingleSelectionModel<Tab> selectionModel;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 400, 400);
			primaryStage.getIcons().add(new Image("/image/notepad.png"));
			primaryStage.setTitle("Notepad Lite");
			primaryStage.setScene(scene);
			primaryStage.show();

			createMenu();
			
			TabPane tabPane = new TabPane();
			selectionModel = tabPane.getSelectionModel();
			fileHandler = new FileHandler();

			newFile.setOnAction(e -> {
				tabPane.getTabs().add(new TxtFileTab(primaryStage));
				saveFile.setDisable(false);
				saveFileAs.setDisable(false);
			});

			tabPane.setOnMouseClicked(e -> {
				if (e.getButton().equals(MouseButton.PRIMARY)) {
					if (e.getClickCount() == 2) {
						tabPane.getTabs().add(new TxtFileTab(primaryStage));
						saveFile.setDisable(false);
						saveFileAs.setDisable(false);
					}
				}
			});

			saveFile.setOnAction(e -> {

				TextArea tempArea = (TextArea) selectionModel.getSelectedItem().getContent();
				File tempFile = (File) selectionModel.getSelectedItem().getUserData();
				if (tempFile != null) {
					fileHandler.saveFile(tempFile, tempArea.getText());

				} else {
					tempFile = fileHandler.getFileChooser().showSaveDialog(new Stage());

					selectionModel.getSelectedItem().setText(tempFile.getName());
					fileHandler.saveFile(tempFile, tempArea.getText());
				}
			});
			saveFileAs.setOnAction(e -> {
				TextArea tempArea = (TextArea) selectionModel.getSelectedItem().getContent();
				fileHandler.saveFileAs(tempArea.getText());

			});

			openFile.setOnAction(e -> {
				File tempFile = fileHandler.openFile();
				if (tempFile != null) {
					tabPane.getTabs().add(new TxtFileTab(tempFile, primaryStage));
					saveFile.setDisable(false);
					saveFileAs.setDisable(false);
				} else {
					e.consume();
				}
			});

			exit.setOnAction(e -> {
				ObservableList<Tab> tabs = tabPane.getTabs();
				for (Tab tab : tabs) {

					showCloseTabAlert(tab, e);
					tabPane.getSelectionModel().select(tab);

				}

				Platform.exit();
			});

			primaryStage.setOnCloseRequest(e -> {
				ObservableList<Tab> tabs = tabPane.getTabs();
				for (Tab tab : tabs) {

					tabPane.getSelectionModel().select(tab);
					showCloseTabAlert(tab, e);

				}

			});

			tabPane.getTabs().addListener(new InvalidationListener() {
				ObservableList<Tab> tabs = tabPane.getTabs();

				@Override
				public void invalidated(Observable observable) {

					for (Tab tab : tabs) {
						tab.setOnCloseRequest(e -> {
							showCloseTabAlert(tab, e);

						});
						tab.setOnClosed(e -> {
							if (tabPane.getTabs().isEmpty()) {
								saveFile.setDisable(true);
								saveFileAs.setDisable(true);
							}
						});
					}

				}
			});

			root.setTop(menuBar);
			root.setCenter(tabPane);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void createMenu() {

		fileMenu = new Menu("File");
		newFile = new MenuItem("New");
		openFile = new MenuItem("Open...");
		saveFile = new MenuItem("Save");
		saveFileAs = new MenuItem("Save as..");
		exit = new MenuItem("Exit");
		fileMenu.getItems().addAll(newFile, openFile, saveFile, saveFileAs, exit);
		menuBar = new MenuBar(fileMenu);

		newFile.setAccelerator(KeyCombination.keyCombination("shortcut+n"));
		openFile.setAccelerator(KeyCombination.keyCombination("shortcut+o"));
		saveFile.setAccelerator(KeyCombination.keyCombination("shortcut+s"));

		newFile.setGraphic(new ImageView(new Image("image/new.png")));
		openFile.setGraphic(new ImageView(new Image("image/open.png")));
		saveFile.setGraphic(new ImageView(new Image("image/save.png")));

		saveFile.setDisable(true);
		saveFileAs.setDisable(true);
	}

	private void showCloseTabAlert(Tab tab, Event e) {

		Alert saveAlert = new Alert(AlertType.CONFIRMATION);
		saveAlert.setHeaderText("Save changes?");
		saveAlert.setContentText("Do you want to save changes you made to " + tab.getText() + "?");
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		saveAlert.getButtonTypes().setAll(yes, no, cancel);

		Optional<ButtonType> result = saveAlert.showAndWait();
		if (result.get() == yes) {
			File tempFile = (File) selectionModel.getSelectedItem().getUserData();
			TextArea tempArea = (TextArea) selectionModel.getSelectedItem().getContent();
			if (tempFile != null) {
				fileHandler.saveFile(tempFile, tempArea.getText());
			} else {
				tempFile = fileHandler.getFileChooser().showSaveDialog(new Stage());
				fileHandler.saveFile(tempFile, tempArea.getText());
			}
		} else if (result.get() == no) {

		} else {
			e.consume();
		}

	}

}
