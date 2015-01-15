package net.sourceforge.picmicroview.model;


public class Btfss extends Instruction {

	public Btfss(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//System.out.println("command is " + name);

		int address = instruction & 0x00ff;
		//System.out.println("address to be checked is " + Integer.toHexString(address));
		//System.out.println("memory contents before toggle: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		int bit = instruction & 0x0e00;
		bit = (bit / 256) >> 1;
		//System.out.println("bit to be checked is: " + bit + 
		//		" and its value is " + pic18.dataMem.gpMem[address].getBit(bit));

		if(pic18.dataMem.gpMem[address].getBit(bit) == 1){
			if(pic18.checkTwoCycle() == true){
				pic18.pc.increment();
				pic18.pc.increment();
			}
			else{
				pic18.pc.increment();
			}
			//System.out.println("bit is set");
		}

		//System.out.println("memory contents after toggle: " + Integer.toBinaryString(pic18.dataMem.gpMem[address].read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}

}
