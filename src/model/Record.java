package model;


/*Parses S-record line from scanner and constructs record, converts all ascii to
 * int, constructs ArrayList of data, leaves data in little Endian format, advances
 * source to next S-record, assigns enum RecType*/

import java.util.ArrayList;

//records make themselves by getting current line from source.
class Record {

	private String r;
	private int recLen;
	private int loadOffset;
	private RecType recType;
	private ArrayList<Integer> infoData;
	private int chkSum;
	
	Record(Source source){

		//read current line into record
		this.r = source.currentLine();
		//set record length and load offset values
		setRecLen();
		setLoadOffset();
		//decode record type based on characters at position 7 and 8 in record, then
		//set record type to that value
		String type = Character.toString(r.charAt(7)) + r.charAt(8);
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
		
		infoData = new ArrayList<>();
		convertInfoData();
		setChkSum();		
		source.nextLine();	
	}
	
	int getRecLen() {
		return recLen;
	}
	
	//get byte 1 and 2 from current record, convert to string, convert string to 
	//integer value and set value of reclen to that value
	private void setRecLen() {
		String rl = "0x" + r.charAt(1) + r.charAt(2);
		recLen = (Integer.decode(rl));
	}
	
	int getLoadOffset() {
		return loadOffset;
	}
	
	//get byts 3 and 4 from current record, convert to string, convert string to
	//integer value and set value of loadOffset to that value
	private void setLoadOffset() {
		String ldofst = "0x" + r.charAt(3) + r.charAt(4) +
				r.charAt(5) + r.charAt(6);
		this.loadOffset = Integer.decode(ldofst);
	}
	
	RecType getRecType() {
		return recType;
	}
	
	private void setRecType(RecType recType) {
		this.recType = recType;
	}
	ArrayList<Integer> getInfoData() {
		return infoData;
	}
	
	void setInfoData(int index, int data){
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
				strByte = "0x" + r.charAt(sStart) +
						r.charAt(sStart + 1);
				data = Integer.decode(strByte);
				infoData.add(data);
				sStart = sStart + 2;
			}
		}
	}
	
	int getChkSum() {
		return chkSum;
	}
	
	private void setChkSum() {
		String chkSum = "0x" + r.charAt(r.length() -2)
				+ r.charAt(r.length() -1);
		this.chkSum = Integer.decode(chkSum);
	}
}
