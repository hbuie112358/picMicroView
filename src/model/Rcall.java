package model;


public class Rcall extends PicInstruction {

	Rcall(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute() {
		//System.out.println("command is : " + name);
		
		//subtract 2 since pc was incremented when rcall instruction was retrieved
		//pushes address of next instruction onto the stack
		Pic18F452 pic18 = getPic18();
		Stack stack = pic18.getStack();
		ProgramCounter pc = pic18.getProgramCounter();
		stack.push(pc.getReturnAddress() -2);
		int offset = getInstruction() & 0x07ff;
		//System.out.println("calculated offset in rcall execute is " + 
		//		Integer.toHexString(pic18.pc.calcOffset2048(offset)) + " hex");

		pic18.setPcValue(pic18.getPcValue() + pc.calcOffset2048(offset));
		//System.out.println("program counter is " + Integer.toHexString(pic18.pc.getPc())
		//		+ " hex");
		//System.out.println("pcL is " + Integer.toHexString(pic18.dataMem.pcL.read()));
		
		//increment pc to return address, push onto stack
		//pic18.pc.increment();
//		System.out.println("contents of memory 03 is " + pic18.dataMem.getGpMem()[0x03].read());
//		System.out.println("");
	}

}

