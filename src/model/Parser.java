package model;


/*Parser has a scanner, scanner has a source. Scanner returns records, records advance source to next line.
 * Parser loads hashmap with program, pic gets hashmap from parser.*/

import java.util.HashMap;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Parser {
	private Scanner scanner;
	private Record s;
	private HashMap<Integer, ArrayList<Integer> > program;
	private int memOffset;
	
	public Parser(Scanner scanner){
		this.scanner = scanner;
		this.program = new HashMap<Integer, ArrayList<Integer> >();
	}
	
	//parse() - gets first record, parses rest of file in while loop
	private void parse(){
		s = scanner.nextRecord();
		//printRecord(s);
		parse(s);
		while((s.getRecType() != RecType.EOF) && (s.getRecType() != RecType.ERROR)){
			s = scanner.nextRecord();
			//printRecord(s);
			parse(s);
		}
	}
	
	//parse(s) - checks record type, sets appropriate offset, adds record to hashmap
	private void parse(Record s){
			//change memory loading offset if record type is 04
		if(s.getRecType() == RecType.EXTENDED_LIN_ADDRESS){
			//System.out.println("startLoadAddress is: " + s.getInfoData());
			memOffset = s.getInfoData().get(0);
		}
			//change from little Endian to big Endian, add to program hashMap
		else if(s.getRecType() == RecType.DATA){
			int temp, index = 0;
			while(index < s.getInfoData().size()){
				temp = s.getInfoData().get(index);
				s.setInfoData(index, s.getInfoData().get(index + 1));
				s.setInfoData(index + 1, temp);
				index = index + 2;
			}
			//System.out.println("getInfoData is: " + s.getInfoData());
			program.put(s.getLoadOffset() + memOffset, s.getInfoData());			
		}
		else if(s.getRecType() == RecType.EOF){
			return;
		}
		else{
			System.out.println("Incorrect record type encountered, exiting program");
			System.exit(0);
		}
	}
	
	public void printRecord(Record r){
		System.out.println(r.getRecLen() + " " + r.getLoadOffset() + " " + r.getRecType()
			+ " " + r.getInfoData() + " " + r.getChkSum());
	}
	
	//public interface for parser
	public HashMap<Integer, ArrayList<Integer> > getProgram(){
		parse();

		return program;
	}
}
