package model;

public class Negf extends Instruction {

	public Negf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
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
