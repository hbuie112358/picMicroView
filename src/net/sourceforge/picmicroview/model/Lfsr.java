package net.sourceforge.picmicroview.model;

public class Lfsr extends Instruction{

	int fsrhVal, fsrlVal, fsrNum, nextWord;
	public Lfsr(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		fsrhVal = instruction & 0x0f;
		fsrNum = (instruction & 0x30) >> 4;
		fsrlVal = nextWord & 0xff;
		if(fsrNum == 0){
			pic18.dataMem.fsr0L.write(fsrlVal);
			pic18.dataMem.fsr0h.write(fsrhVal);
		}
		else if(fsrNum == 1){
			pic18.dataMem.fsr1L.write(fsrlVal);
			pic18.dataMem.fsr1h.write(fsrhVal);
		}
		else{
			pic18.dataMem.fsr2L.write(fsrlVal);
			pic18.dataMem.fsr2h.write(fsrhVal);
		}
		
	}

	protected void initialize(int instruction, int nextWord) {
		this.instruction = instruction;
		this.nextWord = nextWord;
		
	}
	
	@Override
	protected void initialize(int instruction) {
		// TODO Auto-generated method stub
	}

}
