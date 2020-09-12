package model;

public class Andwf extends PicInstruction {

	public Andwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int result = (dataMem.getGpMem()[freg].read() & dataMem.getWreg().read());
		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200) {
			dataMem.getGpMem()[freg].write(result);
		}
		else {
			dataMem.getWreg().write(result);
		}
		adjustZbit(result);
		adjustNbit(result);

	}

}
