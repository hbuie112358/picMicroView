package model;

public class Xorwf extends PicInstruction {

	Xorwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//get register address
		int freg = dataMem.getRegAddress(getInstruction());
//		System.out.println("in iorwf.movf, freg address is: " + Integer.toHexString(freg));

		//perform XOR function with wreg value
		int result = dataMem.wreg.read() ^ dataMem.gpMem[freg].read();

		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((getInstruction() & 0x200) == 0x200)
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
		adjustZbit(result);
		adjustNbit(result);

	}

}
