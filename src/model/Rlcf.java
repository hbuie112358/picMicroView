package model;

public class Rlcf extends Instruction {

	private int freg, result, carry;
	
	public Rlcf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		freg = dataMem.getRegAddress(getInstruction());
		result = dataMem.gpMem[freg].read() << 1;
		carry = dataMem.status.getBit(0);
		result = result + carry;
		if((result & 0x100) == 0x100) {
			dataMem.status.setBit(0);
		}
		else {
			dataMem.status.clearBit(0);
		}
		result = result & 0xff;

		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((getInstruction() & 0x200) == 0x200)
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
		adjustZbit(result);
		adjustNbit(result);
//		freg = pic18.dataMem.getRegAddress(instruction);
//		result = pic18.dataMem.gpMem[freg].read() << 1;
//		carry = pic18.dataMem.status.getBit(0);
//		result = result + carry;
//		if((result & 0x100) == 0x100)
//			pic18.dataMem.status.setBit(0);
//		else pic18.dataMem.status.clearBit(0);
//		result = result & 0xff;
//		
//		//if bit 9 of instruction is high, write result to f register
//		//else write to wreg
//		if((instruction & 0x200) == 0x200) 
//			pic18.dataMem.gpMem[freg].write(result);
//		else pic18.dataMem.wreg.write(result);
	}

}
