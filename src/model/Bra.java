package model;


public class Bra extends Instruction {

	public Bra(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//System.out.println("command is : " + name);
		
		//subtract 2 since pc was incremented when rcall instruction was retrieved
		//pushes address of next instruction onto the stack
		//pic18.stack.push(pic18.pc.getReturnAddress() -2);
		int offset = instruction & 0x07ff;
		//System.out.println("calculated offset in bra execute is " + 
		//		Integer.toHexString(pic18.pc.calcOffset2048(offset)) + " hex");

		pic18.pc.setPc(pic18.pc.getPc() + pic18.pc.calcOffset2048(offset));
		//System.out.println("program counter is " + Integer.toHexString(pic18.pc.getPc())
		//		+ " hex");
		//System.out.println("pcL is " + Integer.toHexString(pic18.dataMem.pcL.read()));
		
		//increment pc to return address, push onto stack
		//pic18.pc.increment();
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;	
	}
}
