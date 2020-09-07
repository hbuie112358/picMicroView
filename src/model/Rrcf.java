package model;

public class Rrcf extends Instruction {

	public Rrcf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int carry = dataMem.status.getBit(0) << 8;
		int result = dataMem.gpMem[freg].read() | carry;
		if((result & 0x01) == 0x01) {
			dataMem.status.setBit(0);
		}
		else {
			dataMem.status.clearBit(0);
		}
		result = (result >> 1) & 0xff;

		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((getInstruction() & 0x200) == 0x200)
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
		adjustZbit(result);
		adjustNbit(result);

	}

}
