package model;

public class Mulwf extends Instruction {

	int highByte, lowByte, result, address;
	
	public Mulwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		highByte = 0;
		lowByte = 0;
		result = 0;
		address = 0;
	}

	@Override
	protected void execute() {
		address = pic18.dataMem.getRegAddress(instruction);
		result = pic18.dataMem.gpMem[address].read() * pic18.dataMem.wreg.read();
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
