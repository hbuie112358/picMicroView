package model;

public class Rrncf extends Instruction {

	public Rrncf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int result = dataMem.gpMem[freg].read();
		result = (((result & 0x01) << 8) | result) >> 1;
		if((getInstruction() & 0x200) == 0x200) {
			dataMem.gpMem[freg].write(result);
		}
		else {
			dataMem.wreg.write(result);
		}
		adjustZbit(result);
		adjustNbit(result);
	}

}
