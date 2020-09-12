package model;

public class Incfsz extends PicInstruction {

	private int freg, result;
	
	public Incfsz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		freg = dataMem.getRegAddress(getInstruction());
		result = dataMem.getGpMem()[freg].read();
		if(result == 0xff)
			result = 0x00;
		else
			result++;
		if(result == 0){
			pic18.getProgramCounter().increment();
			if(pic18.checkTwoCycle()){
				pic18.getProgramCounter().increment();
			}
		}
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((getInstruction() & 0x200) == 0x200)
			dataMem.getGpMem()[freg].write(result);
		else dataMem.getWreg().write(result);

	}

}
