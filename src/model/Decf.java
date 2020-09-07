package model;


public class Decf extends Instruction {

	Decf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//System.out.println("command is " + name);
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int result = dataMem.gpMem[freg].read();
		if(result == 0)
			result = 0xff;
		else
			result--;

		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((getInstruction() & 0x200) == 0x200)
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
		adjustZbit(result);
		adjustNbit(result);
		//pic18.incrementPc();
	}

}
