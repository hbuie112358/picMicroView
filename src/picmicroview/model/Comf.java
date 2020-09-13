package picmicroview.model;

public class Comf extends PicInstruction {

	public Comf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int result = dataMem.getGpMem()[freg].getContents();

		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200)
			dataMem.getGpMem()[freg].write(result);
		else dataMem.getWreg().write(result);
		adjustZbit(result);
		adjustNbit(result);

	}

}
