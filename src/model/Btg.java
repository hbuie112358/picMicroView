package model;


public class Btg extends Instruction {

	private int freg;
	
	public Btg(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {

		freg = pic18.dataMem.getRegAddress(instruction);
//		System.out.println("address is " + Integer.toHexString(freg));
//		System.out.println("memory contents before toggle: " + Integer.toBinaryString(pic18.dataMem.gpMem[freg].read()));
//		int bit = instruction & 0x0e00;
//		bit = (bit / 256) >> 1;
		int bit = (instruction & 0x0e00) >> 9;
//		System.out.println("bit is: " + bit + " value of bit is: " + pic18.dataMem.gpMem[freg].getBit(bit));
		if(pic18.dataMem.gpMem[freg].getBit(bit) == 1){
			pic18.dataMem.gpMem[freg].clearBit(bit);
		}
		else {
			pic18.dataMem.gpMem[freg].setBit(bit);
		}
//		System.out.println("memory contents after toggle: " + Integer.toBinaryString(pic18.dataMem.gpMem[freg].read()));
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}