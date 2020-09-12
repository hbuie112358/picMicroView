package model;

public class Bc extends PicInstruction {

	public Bc(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		if(pic18.getDataMem().getStatus().getBit(0) == 1){
			pic18.setPcValue(pic18.getPcValue() + getPic18().getProgramCounter().calcOffset256(getInstruction() & 0xff));
		}

	}

}
