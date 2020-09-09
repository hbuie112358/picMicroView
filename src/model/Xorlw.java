package model;

public class Xorlw extends PicInstruction {

	public Xorlw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		int wreg = getPic18().getDataMem().wreg.read();
		int result = getInstruction() ^ wreg;
		getPic18().getDataMem().wreg.write(result);
		adjustZbit(result);
		adjustNbit(result);
	}

}
