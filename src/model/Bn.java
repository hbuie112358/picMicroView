package model;

public class Bn extends PicInstruction {

	public Bn(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		if(pic18.getDataMem().status.getBit(4) == 1){
			pic18.setPcValue(pic18.getPcValue() + getPic18().getProgramCounter().calcOffset256(getInstruction() & 0xff));
		}

	}

}
