package net.sourceforge.picmicroview.model;


public class Bcf extends Instruction {

	public Bcf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//System.out.println("command is " + name);

		int address = instruction & 0x00ff;
		int bit = instruction & 0x0e00;
		bit = (bit / 256) >> 1;
		//System.out.println("memory contents before clear: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		pic18.dataMem.gpMem[address].clearBit(bit);
		//System.out.println("memory contents after clear: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		//System.out.println("");
		//System.out.println(System.nanoTime());
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
