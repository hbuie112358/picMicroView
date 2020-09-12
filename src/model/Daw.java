package model;

public class Daw extends PicInstruction {

	public Daw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {

		int lowNibble, highNibble = 0;
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int wreg = dataMem.getWreg().read();
		if(((wreg & 0x0f) > 0x09) || dataMem.getStatus().getBit(1) == 1){
			lowNibble = (((wreg & 0x0f) + 0x06) & 0x0f);
			highNibble = highNibble + 0x10;
		}
		else lowNibble = wreg & 0x0f;
		if(((wreg & 0xf0) > 0x90) || dataMem.getStatus().getBit(0) == 1){
			highNibble = highNibble + (wreg & 0xf0) + 0x60;
			dataMem.getStatus().setBit(0);
		}
		else highNibble = highNibble + (wreg & 0xf0);
		int result = highNibble | lowNibble;
		if((getInstruction() & 0x200) == 0x200)
			dataMem.getGpMem()[freg].write(result);
		else dataMem.getWreg().write(result);
	}

}
