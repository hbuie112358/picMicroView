package net.sourceforge.picmicroview.model;


public class Btg extends Instruction {

	public Btg(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		//System.out.println("command is " + name);

		int address = instruction & 0x00ff;
		//System.out.println("address is " + Integer.toHexString(address));
		//System.out.println("memory contents before toggle: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		int bit = instruction & 0x0e00;
		bit = (bit / 256) >> 1;
		//System.out.println("bit to be checked is: " + pic18.dataMem.gpMem[address].getBit(bit));
		if(pic18.dataMem.gpMem[address].getBit(bit) == 1){
			pic18.dataMem.gpMem[address].clearBit(bit);
		}
		else {
			pic18.dataMem.gpMem[address].setBit(bit);
		}
		//System.out.println("memory contents after toggle: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
		//System.out.println("");
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
