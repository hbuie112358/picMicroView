package model;

public class Andwf extends Instruction {

	public Andwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int result = (dataMem.gpMem[freg].read() & dataMem.wreg.read());
		//if bit 9 of instruction is high, write result to f register
		if((getInstruction() & 0x200) == 0x200) {
			dataMem.gpMem[freg].write(result);
		}
		else {
			dataMem.wreg.write(result);
		}
		adjustZbit(result);
		adjustNbit(result);

	}

	@Override
	protected void initialize(int instruction) {
		setInstruction(instruction);

	}

}
