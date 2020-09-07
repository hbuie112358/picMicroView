package model;

public class Btfsc extends Instruction {

	private int freg;

	public Btfsc(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {

		freg = getPic18().getDataMem().getRegAddress(getInstruction());
//		System.out.println("address to be checked is " + Integer.toHexString(freg));
		int bit = getInstruction() & 0x0e00;
		bit = (bit / 256) >> 1;

		Pic18F452 pic18 = getPic18();
		ProgramCounter pc  = pic18.getProgramCounter();
		if (getPic18().getDataMem().gpMem[freg].getBit(bit) == 0) {
			pc.increment();
			if (pic18.checkTwoCycle() == true) {
				pc.increment();
			}
		}
	}

	@Override
	protected void initialize(int instruction) {
		setInstruction(instruction);
	}
}