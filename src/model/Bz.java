package model;


public class Bz extends PicInstruction {

	public Bz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		//System.out.println("command is : " + name);
		int offset = getInstruction() & 0x00FF;
		Pic18F452 pic18 = getPic18();
		if((pic18.getDataMem().getStatus().read() & 0x0004) == 0x0004){
			//System.out.println("calculated offset in bz execute is " + 
			//		Integer.toHexString(pic18.pc.calcOffset(offset)) + " hex");
			//pic18.setProgramCounter(pic18.pc.getPc() + pic18.calcOffset(offset));
			pic18.setPcValue(pic18.getPcValue() + pic18.getProgramCounter().calcOffset256(offset));
			//System.out.println("program counter is " + Integer.toHexString(pic18.pc.getPc())
			//		+ " hex");
			//System.out.println("pcL is " + Integer.toHexString(pic18.dataMem.pcL.read()));
		}
	}

}
