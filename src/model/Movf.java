package model;


public class Movf extends Instruction {

	Movf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//System.out.println("command is " + name);
		//get register address
		int freg = dataMem.getRegAddress(getInstruction());
		System.out.println("in alu.movf, freg address is: " + Integer.toHexString(freg));

		//find register value
		int result = dataMem.gpMem[freg].read();

		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200)
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
		adjustZbit(result);
		adjustNbit(result);
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

}
