package picmicroview.model;


public class Bcf extends PicInstruction {

	private int freg;
	
	public Bcf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		freg = 0;
	}

	public void execute() {
		//System.out.println("command is " + name);

		freg = getPic18().getDataMem().getRegAddress(getInstruction());
//		System.out.println("in bcf, freg is: " + Integer.toHexString(freg));
		//int address = instruction & 0x00ff;
		int bit = getInstruction() & 0x0e00;
		bit = (bit / 256) >> 1;
		//System.out.println("memory contents before clear: " + Integer.toBinaryString(pic18.dataMem.getGpMem()[address].read()));
		getPic18().getDataMem().getGpMem()[freg].clearBit(bit);
		//System.out.println("memory contents after clear: " + Integer.toBinaryString(pic18.dataMem.getGpMem()[address].read()));
		//System.out.println("");
	}

}
