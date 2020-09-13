package picmicroview.model;

public class Swapf extends PicInstruction {

	public Swapf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int instruction = getInstruction();
		int freg = dataMem.getRegAddress(instruction);
		int result = dataMem.getGpMem()[freg].read();
		result = (((result & 0x0f) << 4) | ((result & 0xf0) >> 4));
		if((instruction & 0x200) == 0x200) 
			dataMem.getGpMem()[freg].write(result);
		else dataMem.getWreg().write(result);
	}

}
