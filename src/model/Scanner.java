package model;


/*returns record to parser*/

public class Scanner {

	private Source source;
	
	//scanner makes source object.
	public Scanner(String fileName){		
		source = new Source(fileName);
	}
		
	//a record is a line in the file, essentially returns a line at a time
	public Record nextRecord(){
		return new Record(source);	

		

	}
		

			   
//	private void printError(String error){
//		System.out.println(error);
//	}

}