package net.sourceforge.picmicroview.model;

public class Bnc extends Instruction {

	public Bnc(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		if(pic18.dataMem.status.getBit(4) == 0){
			pic18.pc.setPc(pic18.pc.getPc() + pic18.pc.calcOffset256(instruction & 0xff));
		}

	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}

}
