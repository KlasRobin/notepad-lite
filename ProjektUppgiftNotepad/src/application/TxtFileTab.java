package application;

import java.io.File;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


/**
 * Custom tab class.
 * @author Robin_000
 *
 */
public class TxtFileTab extends Tab {

	private TextArea textArea;
	private FileHandler fileHandler;
	private File file;
	

	/**
	 * 
	 * Constructor to create a new tab with opened file.
	 * Sets tabname to filename.
	 * 
	 * @param file File to open
	 * @param stage Stage to open tab in
	 */
	public TxtFileTab(File file, Stage stage) {
		super(file.getName());
		this.file = file;
		
		textArea = new TextArea();
	
		textArea.prefHeightProperty().bind(stage.heightProperty());
		textArea.prefWidthProperty().bind(stage.widthProperty());
		fileHandler = new FileHandler();
		setUserData(file);
		
		setContent(textArea);
		textArea.setText(fileHandler.readFile(this.file));

	}


	/**
	 * Constructor for creating a tab with no file.
	 * 
	 * @param stage Stage to open tab in.
	 */

	public TxtFileTab(Stage stage) {
		super("new txt-file");
		this.file = null;
		textArea = new TextArea();
		textArea.prefHeightProperty().bind(stage.heightProperty());
		textArea.prefWidthProperty().bind(stage.widthProperty());
		fileHandler = new FileHandler();
		setContent(textArea);

	}
	
	

}
