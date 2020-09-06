package model;

public class Andlw extends Instruction {
	
	public Andlw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//System.out.println("command is " + name);
		pic18.alu.execute(this);
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
