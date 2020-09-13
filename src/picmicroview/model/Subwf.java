package picmicroview.model;

import static picmicroview.model.ArithmeticLogicUnitUtil.getTwosComplement;

public class Subwf extends PicInstruction {

	public Subwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//get wreg value
		int wreg = dataMem.getWreg().read();

		//get register address
		int freg = dataMem.getRegAddress(getInstruction());

		//get two's complement
		int twosComp = getTwosComplement(wreg & 0xff);

//		System.out.println("twos comp is: " + Integer.toHexString(twosComp));
		//find sum of register f and two's complement of wreg
		int result = dataMem.getGpMem()[freg].read() + twosComp;
		System.out.println("result is: " + Integer.toHexString(result));
		adjustDCbit(twosComp, dataMem.getGpMem()[freg].read());
		adjustOVbit("sub", twosComp, dataMem.getGpMem()[freg].read());
		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200)
			dataMem.getGpMem()[freg].write(result);
		else dataMem.getWreg().write(result);
		adjustCbit(result);
		adjustZbit(result);
		adjustNbit(result);

	}

}
