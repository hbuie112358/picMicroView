package picmicroview.model;

public class Sublw extends PicInstruction {

	public Sublw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		//get two's complement of value in wreg
		int twosComp = ArithmeticLogicUnitUtil.getTwosComplement(dataMem.getWreg().read() & 0xff);

		//mask "k" portion of instruction, add to two's complement of wreg value
		int result = (getInstruction() & 0xff) + twosComp;


		adjustDCbit(twosComp, (getInstruction() & 0xff));
		adjustOVbit("sub", twosComp, (getInstruction() & 0xff));
		dataMem.getWreg().write(result);
		adjustZbit(result);
		adjustNbit(result);
		adjustCbit(result);

	}

}
