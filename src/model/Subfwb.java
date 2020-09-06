package model;

public class Subfwb extends Instruction {

	public Subfwb(int instruction, Pic18F452 pic18, String name) {
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