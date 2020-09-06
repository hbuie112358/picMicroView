package model;


public class Return extends Instruction {

	public Return(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//returns to instruction after calling instruction
		//System.out.println("command is : " + name);
		pic18.pc.setPc(pic18.stack.pop());
		//System.out.println("pc after pop is " + Integer.toHexString(pic18.pc.getPc()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

	@Override
	protected void initialize(int instruction) {
		// TODO Auto-generated method stub
	}
}
