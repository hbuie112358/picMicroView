package model;

public class Mulwf extends PicInstruction {

	private int highByte, lowByte, result, address;
	
	public Mulwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		highByte = 0;
		lowByte = 0;
		result = 0;
		address = 0;
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		address = dataMem.getRegAddress(getInstruction());
		result = dataMem.getGpMem()[address].read() * dataMem.getWreg().read();
		highByte = (result & 0xff00) >> 8;
		lowByte = result & 0xff;
		dataMem.prodh.write(highByte);
		dataMem.prodL.write(lowByte);

	}

}
