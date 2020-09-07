package model;

public class Swapf extends Instruction {

	int freg, result, temp;
	
	public Swapf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int instruction = getInstruction();
		freg = dataMem.getRegAddress(instruction);
		result = dataMem.gpMem[freg].read();
		result = (((result & 0x0f) << 4) | ((result & 0xf0) >> 4));
		if((instruction & 0x200) == 0x200) 
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
	}

	@Override
	protected void initialize(int instruction) {
		setInstruction(instruction);
	}

}
