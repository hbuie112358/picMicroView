package model;

public class Iorlw extends PicInstruction {

	public Iorlw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int wreg = dataMem.wreg.read();
		int result = getInstruction() | wreg;
		dataMem.wreg.write(result);
		adjustZbit(result);
		adjustNbit(result);
	}

}
