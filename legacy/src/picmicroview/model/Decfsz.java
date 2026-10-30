package picmicroview.model;

public class Decfsz extends PicInstruction {

	public Decfsz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute(){
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int result = dataMem.getGpMem()[freg].read();
		if(result == 0)
			result = 0xff;
		else
			result--;
		if(result == 0){
			pic18.getProgramCounter().increment();
			if(getPic18().checkTwoCycle()){
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
