package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * Class used for handling text files. 
 * @author Robin_000
 *
 */
public class FileHandler {

	private FileChooser fileChooser;
	private Stage fileChooserDialog;

	/**
	 * Constructor for class.
	 */
	public FileHandler() {

		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

	}


	public FileChooser getFileChooser() {
		return fileChooser;
	}

	
	
	public void setFileChooser(FileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

	
	/**
	 * Read .txt-file line by line and returns full text as a String.
	 * @param file File to read. 
	 * @return Contents of txt-file as a String.
	 */
	public String readFile(File file) {
		String line;
		String allText = "";

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			while ((line = br.readLine()) != null) {

				allText += line + "\n";

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return allText;
	}

	
	/**
	 * Open a file using FileChooser class and returns selected file. 
	 * @return File choosen by user. 
	 * @see FileChooser
	 */
	public File openFile() {

		File chosenFile = fileChooser.showOpenDialog(fileChooserDialog);
		return chosenFile;
	}

	/**
	 * Save a string of text as a new txt-file.
	 * 
	 * @param file Destination file
	 * @param text Text to write to file
	 */
	public void saveFile(File file, String text) {
		
		if (file==null){
			return;	
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

			bw.write(text);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	
	/**
	 * Open save file dialog using FileChooser class to choose destination file.
	 * @param text String of text to write to file
	 * @see FileChooser
	 */
	public void saveFileAs(String text) {

		File tempFile;
		tempFile = fileChooser.showSaveDialog(fileChooserDialog);
		saveFile(tempFile, text);
		
	
		
	}

}
