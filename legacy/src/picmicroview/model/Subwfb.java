package picmicroview.model;

import static picmicroview.model.ArithmeticLogicUnitUtil.getTwosComplement;

public class Subwfb extends PicInstruction {

	public Subwfb(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//get wreg value
		int wreg = dataMem.getWreg().read();

		//get register address
		int freg = dataMem.getRegAddress(getInstruction());

		//borrow bit = not carry, so if carry bit is not set, add borrow to original register value
		if(dataMem.getStatus().getBit(0) == 0)
			wreg = wreg + 1;

		//get two's complement
		int twosComp = getTwosComplement(wreg & 0xff);

//		System.out.println("twos comp after adding borrow is: " + Integer.toHexString(twosComp));
		//find sum of wreg and two's complement of f 
		int result = dataMem.getGpMem()[freg].read() + twosComp;
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
