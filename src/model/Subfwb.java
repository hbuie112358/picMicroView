package model;

public class Subfwb extends Instruction {

	public Subfwb(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//get wreg value
		int wreg = dataMem.wreg.read();

		//get register address
		int freg = dataMem.getRegAddress(getInstruction());
		int origValue = dataMem.gpMem[freg].read();

		//borrow bit = not carry, so if carry bit is not set, add borrow to original register value
		if(dataMem.status.getBit(0) == 0)
			origValue = origValue + 1;

		//get two's complement
		int twosComp = Alu.getTwosComplement(origValue & 0xff);

//		System.out.println("twos comp after adding borrow is: " + Integer.toHexString(twosComp));
		//find sum of wreg and two's complement of f
		int result = wreg + twosComp;
		adjustDCbit(twosComp, wreg);
		adjustOVbit("sub", twosComp, wreg);
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
