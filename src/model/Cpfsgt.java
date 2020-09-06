package model;

public class Cpfsgt extends Instruction {

	private int freg;
	
	public Cpfsgt(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		freg = pic18.dataMem.getRegAddress(instruction);
		if(pic18.dataMem.gpMem[freg].read() > pic18.dataMem.wreg.read()){
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
