package model;

public class Cpfseq extends PicInstruction {

	public Cpfseq(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		if(dataMem.getWreg().read() == dataMem.getGpMem()[freg].read()){
			pic18.getProgramCounter().increment();
			if(pic18.checkTwoCycle()){
				pic18.getProgramCounter().increment();
			}
		}
	}

}
