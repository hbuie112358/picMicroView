package model;


public class Return extends PicInstruction {

	Return(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute() {
		Pic18F452 pic18 = getPic18();
		//returns to instruction after calling instruction
		//System.out.println("command is : " + name);
		pic18.getProgramCounter().setpcValue(pic18.getStack().pop());
		//System.out.println("pc after pop is " + Integer.toHexString(pic18.pc.getPc()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

}
