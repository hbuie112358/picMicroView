package net.sourceforge.picmicroview.model;

public class Tstfsz extends Instruction {

	int freg, result;
	
	public Tstfsz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		freg = pic18.dataMem.getRegAddress(instruction);
		if(pic18.dataMem.gpMem[freg].read() == 0){
			if(pic18.checkTwoCycle() == true){
				pic18.pc.increment();
				pic18.pc.increment();
			}
			else{
				pic18.pc.increment();
			}
		}
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}

}
