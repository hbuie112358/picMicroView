package model;

public class Bn extends Instruction {

	public Bn(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		
	}

	@Override
	protected void execute() {
		Pic18F452 pic18 = getPic18();
		if(pic18.getDataMem().status.getBit(4) == 1){
			pic18.setPcValue(pic18.getPcValue() + getPic18().getProgramCounter().calcOffset256(getInstruction() & 0xff));
		}

	}

	@Override
	protected void initialize(int instruction) {
		setInstruction(instruction);

	}

}
