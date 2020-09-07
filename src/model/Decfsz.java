package model;

public class Decfsz extends Instruction {
	
	private int freg, result;

	public Decfsz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute(){
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		freg = dataMem.getRegAddress(getInstruction());
		result = dataMem.gpMem[freg].read();
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
			dataMem.gpMem[freg].write(result);
		else dataMem.wreg.write(result);
	}

}
