package model;

public class Addlw extends Instruction {

	public Addlw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute(){
		int result = (getInstruction() & 0xff) + getPic18().getDataMem().wreg.read();
		adjustDCbit(getPic18().getDataMem().wreg.read(), (getInstruction() & 0xff));
		adjustOVbit("add", getPic18().getDataMem().wreg.read(), (getInstruction() & 0xff));
		getPic18().getDataMem().wreg.write(result);
		adjustCbit(result);
		adjustZbit(result);
//	protected void execute() {
//		pic18.alu.execute(this);
//
	}
//	@Override
//	protected void initialize(int instruction) {
//		this.instruction = instruction;
//
//	}

}
