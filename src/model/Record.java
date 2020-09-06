package model;


/*Parses S-record line from scanner and constructs record, converts all ascii to
 * int, constructs ArrayList of data, leaves data in little Endian format, advances
 * source to next S-record, assigns enum RecType*/

import java.util.ArrayList;

//records make themselves by getting current line from source.
public class Record {

	@SuppressWarnings("unused")
	private Source source;
	private String r;
	private int recLen;
	private int loadOffset;
	private RecType recType;
	private ArrayList<Integer> infoData;
	private int chkSum;
	
	public Record(Source source){
		this.source = source;
		//read current line into record
		this.r = source.currentLine();
		//set record length and load offset values
		setRecLen();
		setLoadOffset();
		//decode record type based on characters at position 7 and 8 in record, then
		//set record type to that value
		String type = Character.toString(r.charAt(7)) + Character.toString(r.charAt(8));
		switch(type){
		case "00": setRecType(RecType.DATA);
					break;
		case "01": setRecType(RecType.EOF);
					break;
		case "02": setRecType(RecType.EXTENDED_SEG_ADDRESS);
					break;
		case "03": setRecType(RecType.START_SEG_ADDRESS);
					break;
		case "04": setRecType(RecType.EXTENDED_LIN_ADDRESS);
					break;
		case "05": setRecType(RecType.START_LIN_ADDRESS);
					break;
		default : setRecType(RecType.ERROR);
		}
		
		infoData = new ArrayList<Integer>();
		convertInfoData();
		setChkSum();		
		source.nextLine();	
	}
	
	public int getRecLen() {
		return recLen;
	}
	
	//get byte 1 and 2 from current record, convert to string, convert string to 
	//integer value and set value of reclen to that value
	private void setRecLen() {
		String rl = "0x" + Character.toString(r.charAt(1)) + Character.toString(r.charAt(2));
		recLen = (Integer.decode(rl));
	}
	
	public int getLoadOffset() {
		return loadOffset;
	}
	
	//get byts 3 and 4 from current record, convert to string, convert string to
	//integer value and set value of loadOffset to that value
	private void setLoadOffset() {
		String ldofst = "0x" + Character.toString(r.charAt(3)) + Character.toString(r.charAt(4)) +
				Character.toString(r.charAt(5)) + Character.toString(r.charAt(6));
		this.loadOffset = Integer.decode(ldofst);
	}
	
	public RecType getRecType() {
		return recType;
	}
	
	private void setRecType(RecType recType) {
		this.recType = recType;
	}
	public ArrayList<Integer> getInfoData() {
		return infoData;
	}
	
	public void setInfoData(int index, int data){
		infoData.set(index, data);
	}
	
	
	private void convertInfoData() {
		if (recType == RecType.EXTENDED_LIN_ADDRESS){
			String strtLoadAdrs = "0x" + r.charAt(11) + r.charAt(12) + r.charAt(9) + r.charAt(10);
			int strtAddress = Integer.decode(strtLoadAdrs);
			infoData.add(strtAddress);
		}
		else{
			String strByte;
			int data, numChars = recLen * 2, sStart = 9;
			int last = sStart + numChars;
			while(sStart < last){
				strByte = "0x" + Character.toString(r.charAt(sStart)) + 
						Character.toString(r.charAt(sStart + 1));
				data = Integer.decode(strByte);
				infoData.add(data);
				sStart = sStart + 2;
			}
		}
	}
	
	public int getChkSum() {
		return chkSum;
	}
	
	private void setChkSum() {
		String chkSum = "0x" + Character.toString(r.charAt(r.length() -2))
				+ Character.toString(r.charAt(r.length() -1));
		this.chkSum = Integer.decode(chkSum);
	}
}
