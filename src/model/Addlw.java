package model;

public class Addlw extends PicInstruction {

	public Addlw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute(){
		int result = (getInstruction() & 0xff) + getPic18().getDataMem().getWreg().read();
		adjustDCbit(getPic18().getDataMem().getWreg().read(), (getInstruction() & 0xff));
		adjustOVbit("add", getPic18().getDataMem().getWreg().read(), (getInstruction() & 0xff));
		getPic18().getDataMem().getWreg().write(result);
		adjustCbit(result);
		adjustZbit(result);
//	public void execute() {
//		pic18.alu.execute(this);
//
	}
//	@Override
//	protected void initialize(int instruction) {
//		this.instruction = instruction;
//
//	}

}
