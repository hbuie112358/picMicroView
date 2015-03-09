package net.sourceforge.picmicroview.model;

public class Cpfslt extends Instruction {

	private int freg;
	
	public Cpfslt(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		freg = pic18.dataMem.getRegAddress(instruction);
		if(pic18.dataMem.gpMem[freg].read() < pic18.dataMem.wreg.read()){
			System.out.println("in less than");
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
