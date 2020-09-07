package model;

public class Lfsr extends Instruction{

	int fsrhVal, fsrlVal, fsrNum, nextWord;
	public Lfsr(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	protected void execute() {
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		fsrhVal = getInstruction() & 0x0f;
		fsrNum = (getInstruction() & 0x30) >> 4;
		fsrlVal = nextWord & 0xff;
//		System.out.println("in lfsr, fsrlVal is: " + Integer.toHexString(fsrlVal));
		if(fsrNum == 0){
			dataMem.fsr0L.write(fsrlVal);
			dataMem.fsr0h.write(fsrhVal);
		}
		else if(fsrNum == 1){
			dataMem.fsr1L.write(fsrlVal);
			dataMem.fsr1h.write(fsrhVal);
		}
		else{
			dataMem.fsr2L.write(fsrlVal);
			dataMem.fsr2h.write(fsrhVal);
		}
		
	}

	protected void initialize(int instruction, int nextWord) {
		setInstruction(instruction);
		this.nextWord = nextWord;
		
	}
	
	@Override
	protected void initialize(int instruction) {
		// TODO Auto-generated method stub
	}

}
