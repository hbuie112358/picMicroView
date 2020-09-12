package model;

public class Setf extends PicInstruction {

	private int freg;
	
	public Setf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		freg = dataMem.getRegAddress(getInstruction());
		dataMem.getGpMem()[freg].write(0xff);

	}

}
