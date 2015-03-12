package net.sourceforge.picmicroview.model;

public class Setf extends Instruction {

	private int freg;
	
	public Setf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		freg = pic18.dataMem.getRegAddress(instruction);
		pic18.dataMem.gpMem[freg].write(0xff);

	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}

}
