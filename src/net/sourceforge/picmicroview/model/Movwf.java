package net.sourceforge.picmicroview.model;


public class Movwf extends Instruction{
	
	public Movwf(int instruction, Pic18F452 pic18, String name){
		super(instruction, pic18, name);
	}
	
	protected void execute(){
		int address = instruction & 0x00FF;
		pic18.dataMem.gpMem[address].write(pic18.dataMem.wreg.read());
		//System.out.println("command is " + name);
		//System.out.println("in movwf, contents of register: " + pic18.dataMem.gpMem[address].read());
	}
	
	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
