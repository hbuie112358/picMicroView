package net.sourceforge.picmicroview.model;


public class ProgramCounter {

	private Pic18F452 pic18;
	private int pc;
	
	public ProgramCounter(Pic18F452 pic18) {
		this.pic18 = pic18;
		pc = 0;
	}
	
	public void increment(){
		if(pc == pic18.getDataMemorySize() - 2)
			pc = 0;
		else
			pc = pc + 2;
	}
	
	public void setPc(int value){
		pc = value;
	}
	
	public int getPc(){
		return pc;
	}
	
	/**
	 * Gets high byte of instruction from memory, shifts bits to msb position
	 * in word (which shifts zeros into least significant 8 bytes, then gets 
	 * low byte from memory, adds high byte and low byte together to form 
	 * instruction word
	 * @return
	 */
	public int getWord(){
//		int highByte = pic18.programMem[pc] * 256;
		int highByte = pic18.programMem[pc] << 8;
		int lowByte = pic18.programMem[pc + 1];
		increment();
		return highByte + lowByte;
	}
	
	public int calcOffset256(int value){
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
		return pc + 2;
	}
	
	public int getNextAddress(){
		return pc + 2;
	}
	
}
