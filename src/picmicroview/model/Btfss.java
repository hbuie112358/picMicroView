package picmicroview.model;


public class Btfss extends PicInstruction {

	Btfss(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute() {
		Pic18F452 pic18 = getPic18();
		ProgramCounter pc  = pic18.getProgramCounter();
		int freg = pic18.getDataMem().getRegAddress(getInstruction());
		//System.out.println("address to be checked is " + Integer.toHexString(freg));
		int bit = getInstruction() & 0x0e00;
		bit = (bit / 256) >> 1;

		if(pic18.getDataMem().getGpMem()[freg].getBit(bit) == 1){
			pc.increment();
			if(pic18.checkTwoCycle()){
				pc.increment();
			}

		}
	}

}
