package model;

public class Movlb extends Instruction {
	
	private int value;

	public Movlb(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute() {
		//Make sure only low nibble is written to bsr, then write value
		value = instruction & 0x000f;
		pic18.dataMem.bsr.write(value);

	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}

}
