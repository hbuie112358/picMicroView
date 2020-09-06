package model;


public class Btfss extends Instruction {

	private int freg;
	
	public Btfss(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		freg = pic18.dataMem.getRegAddress(instruction);
		//System.out.println("address to be checked is " + Integer.toHexString(freg));
		int bit = instruction & 0x0e00;
		bit = (bit / 256) >> 1;

		if(pic18.dataMem.gpMem[freg].getBit(bit) == 1){
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
