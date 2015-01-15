package net.sourceforge.picmicroview.model;


public class Movff extends Instruction {

	public Movff(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	protected void execute(){
		//System.out.println("command is " + name);
		int source = instruction & 0x00ff;
		//System.out.println("source address is " + Integer.toHexString(source) +
		//		"and contents is " + pic18.dataMem.gpMem[source].read());
		instruction = pic18.pc.getWord();
		int destination = instruction & 0x00ff;
		//System.out.println("destination address is " + Integer.toHexString(destination));
		pic18.dataMem.gpMem[destination].write(pic18.dataMem.gpMem[source].read());
		//System.out.println("data at destination is " + 
		//		Integer.toHexString(pic18.dataMem.gpMem[destination].read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
