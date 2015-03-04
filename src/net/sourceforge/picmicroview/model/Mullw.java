package net.sourceforge.picmicroview.model;

public class Mullw extends Instruction {
	int highByte, lowByte, result;

	public Mullw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		highByte = 0;
		lowByte = 0;
		result = 0;
	}

	@Override
	protected void execute() {
		result = (instruction & 0xff) * pic18.dataMem.wreg.read();
		highByte = (result & 0xff00) >> 8;
		lowByte = result & 0xff;
		pic18.dataMem.prodh.write(highByte);
		pic18.dataMem.prodL.write(lowByte);
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}

}
