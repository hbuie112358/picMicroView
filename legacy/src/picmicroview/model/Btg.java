package picmicroview.model;


public class Btg extends PicInstruction {

	public Btg(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
//		System.out.println("address is " + Integer.toHexString(freg));
//		System.out.println("memory contents before toggle: " + Integer.toBinaryString(pic18.dataMem.getGpMem()[freg].read()));
//		int bit = instruction & 0x0e00;
//		bit = (bit / 256) >> 1;
		int bit = (getInstruction() & 0x0e00) >> 9;
//		System.out.println("bit is: " + bit + " value of bit is: " + pic18.dataMem.getGpMem()[freg].getBit(bit));
		if(dataMem.getGpMem()[freg].getBit(bit) == 1){
			dataMem.getGpMem()[freg].clearBit(bit);
		}
		else {
			dataMem.getGpMem()[freg].setBit(bit);
		}
//		System.out.println("memory contents after toggle: " + Integer.toBinaryString(pic18.dataMem.getGpMem()[freg].read()));
	}

}
