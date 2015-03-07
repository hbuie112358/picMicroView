package net.sourceforge.picmicroview.model;


public class Bcf extends Instruction {

	private int freg;
	
	public Bcf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		freg = 0;
	}

	protected void execute() {
		//System.out.println("command is " + name);

		freg = pic18.dataMem.getRegAddress(instruction);
//		System.out.println("in bcf, freg is: " + Integer.toHexString(freg));
		//int address = instruction & 0x00ff;
		int bit = instruction & 0x0e00;
		bit = (bit / 256) >> 1;
		//System.out.println("memory contents before clear: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		pic18.dataMem.gpMem[freg].clearBit(bit);
		//System.out.println("memory contents after clear: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		//System.out.println("");
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
