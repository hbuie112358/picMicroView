package model;

public class Rlncf extends PicInstruction {

	public Rlncf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
        DataMemory dataMem = getPic18().getDataMem();
        int freg = dataMem.getRegAddress(getInstruction());
        int result = dataMem.getGpMem()[freg].read() << 1;
        if((result & 0x100) == 0x100)
            result = result + 1;
        result = result & 0xff;

        //if bit 9 of instruction is high, write result to f register
        //else write to wreg
        if((getInstruction() & 0x200) == 0x200) {
            dataMem.getGpMem()[freg].write(result);
        }
        else {
            dataMem.getWreg().write(result);
        }
        adjustZbit(result);
        adjustNbit(result);

	}

}
