package picmicroview.model;

public class Addwfc extends PicInstruction {

	Addwfc(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute() {
		//System.out.println("command is " + name);
		//		highByte = instruction.getInstruction() & 0xff00; //isolate command portion of instruction
		Pic18F452 pic18 = getPic18();
		//get wreg value
		int wreg = pic18.getDataMem().getWreg().read();

		//get register address
		int freg = pic18.getDataMem().getRegAddress(getInstruction());
//		System.out.println("in alu.addwfc, freg address is: " + Integer.toHexString(freg));

		//get value of carry bit
		int carry = pic18.getDataMem().getStatus().getBit(0);

		//find sum of wreg, value in gpMem[freg], + carry bit
		int result = wreg + pic18.getDataMem().getGpMem()[freg].read() + carry;

		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200)
			pic18.getDataMem().getGpMem()[freg].write(result);
		else pic18.getDataMem().getWreg().write(result);
		adjustCbit(result);
		adjustZbit(result);
		adjustNbit(result);
		//System.out.println("contents of memory 03 is " + pic18.dataMem.getGpMem()[0x03].read());
	}

}
