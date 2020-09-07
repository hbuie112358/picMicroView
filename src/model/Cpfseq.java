package model;

import javax.xml.crypto.Data;

public class Cpfseq extends Instruction {

	private int freg;
	
	public Cpfseq(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		freg = dataMem.getRegAddress(getInstruction());
		if(dataMem.wreg.read() == dataMem.gpMem[freg].read()){
			pic18.getProgramCounter().increment();
			if(pic18.checkTwoCycle() == true){
				pic18.getProgramCounter().increment();
			}
		}
	}

	@Override
	protected void initialize(int instruction) {
		setInstruction(instruction);

	}

}
