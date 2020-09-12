package model;

public class Movlb extends PicInstruction {

	Movlb(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		//Make sure only low nibble is written to bsr, then write value
		int value = getInstruction() & 0x000f;
		getPic18().getDataMem().bsr.write(value);

	}

}
