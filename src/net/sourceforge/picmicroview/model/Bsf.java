package net.sourceforge.picmicroview.model;


public class Bsf extends Instruction {
	
	private int freg;

	public Bsf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}


	protected void execute() {
		//System.out.println("command is " + name);

		freg = pic18.dataMem.getRegAddress(instruction);
//		int address = instruction & 0x00ff;
		//System.out.println("memory contents before toggle: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		int bit = instruction & 0x0e00;
		bit = (bit / 256) >> 1;
		System.out.println("in bsf.execute(), calling register " + Integer.toHexString(freg) + ".setBit(bit)");
		//System.out.println("memory contents before set: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		pic18.dataMem.gpMem[freg].setBit(bit);

		//System.out.println("memory contents after set: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
		//System.out.println(System.nanoTime());
	}


	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
