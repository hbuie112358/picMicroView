package net.sourceforge.picmicroview.model;


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
		String name = "/basic/a/addwf/addwf.hex";
		try{	//open file
			
			//for development purposes in eclipse, to modify 
			//example files while working
//			file = new File(fileName);
//			fileReader = new FileReader(file);
//			bufferedReader = new BufferedReader(fileReader);
			
			//for jar file creation
			bufferedReader = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream(fileName)));
		}
		catch(Exception e){
			printError(name + ": File not found");
			System.exit(0);
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

