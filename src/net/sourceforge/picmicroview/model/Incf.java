package net.sourceforge.picmicroview.model;

public class Incf extends Instruction {
	
	private int freg, result;

	public Incf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		pic18.alu.execute(this);

	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}

}
