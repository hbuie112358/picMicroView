package model;

public class Mullw extends Instruction {
	private int highByte, lowByte, result;

	public Mullw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		highByte = 0;
		lowByte = 0;
		result = 0;
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		result = (getInstruction() & 0xff) * dataMem.wreg.read();
		highByte = (result & 0xff00) >> 8;
		lowByte = result & 0xff;
		dataMem.prodh.write(highByte);
		dataMem.prodL.write(lowByte);
	}

}
