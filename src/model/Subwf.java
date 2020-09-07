package model;

import static model.Alu.getTwosComplement;

public class Subwf extends Instruction {

	public Subwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//get wreg value
		int wreg = dataMem.wreg.read();

		//get register address
		int freg = dataMem.getRegAddress(getInstruction());

		//get two's complement
		int twosComp = getTwosComplement(wreg & 0xff);

//		System.out.println("twos comp is: " + Integer.toHexString(twosComp));
		//find sum of register f and two's complement of wreg
		int result = dataMem.gpMem[freg].read() + twosComp;
		System.out.println("result is: " + Integer.toHexString(result));
		adjustDCbit(twosComp, dataMem.gpMem[freg].read());
		adjustOVbit("sub", twosComp, dataMem.gpMem[freg].read());
		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200)
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
		adjustCbit(result);
		adjustZbit(result);
		adjustNbit(result);

	}

	@Override
	protected void initialize(int instruction) {
		setInstruction(instruction);

	}

}
