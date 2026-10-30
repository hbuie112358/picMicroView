package picmicroview.model;

public class Cpfslt extends PicInstruction {

	public Cpfslt(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		if(dataMem.getGpMem()[freg].read() < dataMem.getWreg().read()){
			System.out.println("in less than");
			pic18.getProgramCounter().increment();
			if(pic18.checkTwoCycle()){
				pic18.getProgramCounter().increment();
			}
		}
	}

}
