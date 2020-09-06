package model;


public class Decf extends Instruction {

	public Decf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//System.out.println("command is " + name);
		pic18.alu.execute(this);
		//pic18.incrementPc();
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}
}
