package model;

public class Incf extends PicInstruction {

	public Incf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int result;
		int origValue = result = dataMem.getGpMem()[freg].read();
		result++;
		adjustCbit(result);
		if(result == 0x100)
			result = 0x00;

		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((getInstruction() & 0x200) == 0x200)
			dataMem.getGpMem()[freg].write(result);
		else dataMem.getWreg().write(result);
		adjustDCbit(origValue, 1);
		adjustOVbit("", origValue, 1);
		adjustZbit(result);
		adjustNbit(result);

	}

}
