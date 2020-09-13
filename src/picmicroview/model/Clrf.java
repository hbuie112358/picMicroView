package picmicroview.model;

public class Clrf extends PicInstruction {

	public Clrf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		dataMem.getGpMem()[freg].clear();
		adjustZbit(0); //this may not have worked before

	}

}
