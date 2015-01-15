package net.sourceforge.picmicroview.model;


public class ProgramCounter {

	private Pic18F452 pic18;
	private int pc;
	
	public ProgramCounter(Pic18F452 pic18) {
		this.pic18 = pic18;
		pc = 0;
	}
	
	public void increment(){
		pc = pc + 2;
		//System.out.println("pc is " + Integer.toHexString(pc));
	}
	
	public void setPc(int value){
		pc = value;
		//System.out.println("in setPc, value is " + Integer.toHexString(value));
	}
	
	public int getPc(){
		return pc;
	}
	
	public int getWord(){
		int highByte = pic18.programMem[pc] * 256;
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

}
