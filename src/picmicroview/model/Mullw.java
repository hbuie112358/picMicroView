package picmicroview.model;

public class Mullw extends PicInstruction {

	public Mullw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int result = (getInstruction() & 0xff) * dataMem.getWreg().read();
		int highByte = (result & 0xff00) >> 8;
		int lowByte = result & 0xff;
		dataMem.prodh.write(highByte);
		dataMem.prodL.write(lowByte);
	}

}
