package model;

public class Setf extends Instruction {

	private int freg;
	
	public Setf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		freg = dataMem.getRegAddress(getInstruction());
		dataMem.gpMem[freg].write(0xff);

	}

	@Override
	protected void initialize(int instruction) {
		setInstruction(instruction);

	}

}
