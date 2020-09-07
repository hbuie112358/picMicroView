package model;


public class Bnz extends Instruction {

    Bnz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		Pic18F452 pic18 = getPic18();
		//System.out.println("command is : " + name);
		int offset = getInstruction() & 0x00FF;
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
		if(!((pic18.getDataMem().status.read() & 0x0004) == 0x0004)){
			//System.out.println("calculated offset in bnz execute is " + 
			//		Integer.toHexString(pic18.pc.calcOffset(offset)) + " hex");
			//pic18.setProgramCounter(pic18.pc.getPc() + pic18.calcOffset(offset));
			pic18.setPcValue(pic18.getPcValue() + pic18.getProgramCounter().calcOffset256(offset));
			//System.out.println("program counter is " + Integer.toHexString(pic18.pc.getPc())
			//		+ " hex");
			//System.out.println("pcL is " + Integer.toHexString(pic18.dataMem.pcL.read()));
		}
	}

}
