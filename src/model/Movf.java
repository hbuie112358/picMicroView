package model;


public class Movf extends PicInstruction {

	Movf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//System.out.println("command is " + name);
		//get register address
		int freg = dataMem.getRegAddress(getInstruction());
		System.out.println("in alu.movf, freg address is: " + Integer.toHexString(freg));

		//find register value
		int result = dataMem.getGpMem()[freg].read();

		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200)
			dataMem.getGpMem()[freg].write(result);
		else dataMem.getWreg().write(result);
		adjustZbit(result);
		adjustNbit(result);
		//System.out.println("contents of memory 03 is " + pic18.dataMem.getGpMem()[0x03].read());
	}

}
