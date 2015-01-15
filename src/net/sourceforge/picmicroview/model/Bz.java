package net.sourceforge.picmicroview.model;


public class Bz extends Instruction {

	public Bz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		//System.out.println("command is : " + name);
		int offset = instruction & 0x00FF;	
		
		if((pic18.dataMem.status.read() & 0x0004) == 0x0004){
			//System.out.println("calculated offset in bz execute is " + 
			//		Integer.toHexString(pic18.pc.calcOffset(offset)) + " hex");
			//pic18.setProgramCounter(pic18.pc.getPc() + pic18.calcOffset(offset));
			pic18.pc.setPc(pic18.pc.getPc() + pic18.pc.calcOffset256(offset));
			//System.out.println("program counter is " + Integer.toHexString(pic18.pc.getPc())
			//		+ " hex");
			//System.out.println("pcL is " + Integer.toHexString(pic18.dataMem.pcL.read()));
		}
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
