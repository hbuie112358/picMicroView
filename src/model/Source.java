package model;


/*Opens hex file, provides current line and advances to next line*/

	import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Source {


	private File file;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private String currentLine;
	@SuppressWarnings("unused")
	private boolean eof;

			

			
	public Source(String fileName){
		try{	//open file
			
			//checks first for .hex file in the example directory in classpath
			bufferedReader = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream(fileName)));
		}
		
		catch(Exception e){
			printError("in source, " + fileName + " not found in the example directory");
			try{
				
				//if .hex file not found in examples, tries to find it in local file system
				file = new File(fileName);
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				System.out.println("in source, " + fileName + " was found in local file system");
			}
			catch(Exception ex){
				//if .hex file not found either place, prints error and exits
				printError(fileName + " not found in example directory or local file system");
				System.exit(0);
			}
		}
				
		try{	//read first line
			currentLine = bufferedReader.readLine();
		}
		catch(Exception e){
			printError("Error reading file");
			System.exit(0);
		}		
		eof = false;
	}
	
	public void nextLine(){
		try{
			currentLine = bufferedReader.readLine();
		}
		catch (Exception e){
			eof = true;
		}
	}
	
	public String currentLine(){
		return currentLine;
	}
		

					   
	private void printError(String error){
		System.out.println(error);
	}

}

