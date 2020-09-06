package model;

public class Dcfsnz extends Instruction {

	private int freg, result;
	
	public Dcfsnz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {	
		freg = pic18.dataMem.getRegAddress(instruction);
		result = pic18.dataMem.gpMem[freg].read();
		if(result == 0)
			result = 0xff;
		else
			result--;
		if(result != 0){
			if(pic18.checkTwoCycle() == true){
				pic18.pc.increment();
				pic18.pc.increment();
			}
			else{
				pic18.pc.increment();
			}
		}
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
	}


	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}

}