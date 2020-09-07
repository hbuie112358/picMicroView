package model;


public class ProgramCounter {

	private Pic18F452 pic18;
	private int pcValue;
	
	public ProgramCounter(Pic18F452 pic18) {
		this.pic18 = pic18;
		pcValue = 0;
	}
	
	public void increment(){
		if(pcValue == pic18.getDataMemorySize() - 2)
			pcValue = 0;
		else
			pcValue = pcValue + 2;
	}
	
	public void setpcValue(int value){
		pcValue = value;
	}
	
	public int getpcValue(){
		return pcValue;
	}
	
	/**
	 * Gets high byte of instruction from memory, shifts bits to msb position
	 * in word (which shifts zeros into least significant 8 bytes, then gets 
	 * low byte from memory, adds high byte and low byte together to form 
	 * instruction word
	 * @return
	 */
	public int getWord(){
//		int highByte = pic18.programMem[pcValue] * 256;
		int highByte = pic18.getProgramMemory(pcValue) << 8;
		int lowByte = pic18.getProgramMemory(pcValue + 1);
		increment();
		return highByte + lowByte;
	}
	
	protected int calcOffset256(int value){
		if(value > 127)
			return 2 * (value - 256);
		else return 2 * value;
	}
	
	public int calcOffset2048(int value){
		if(value > 1023)
			return 2 * (value - 2048);
		else return 2 * value;
	}
	
	//not like book, but temporary 
	public int getReturnAddress(){
		return pcValue + 2;
	}
	
	public int getNextAddress(){
		return pcValue + 2;
	}
	
}
