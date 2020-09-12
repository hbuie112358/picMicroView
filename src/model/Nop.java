package model;


public class Nop extends PicInstruction {
	
//	private int instruction;
//	private Pic18F452 pic18;
//	private String name = "nop";
	
	Nop(int instruction, Pic18F452 pic18, String name){
//		this.instruction = instruction;
//		this.pic18 = pic18;
		super(instruction, pic18, name);
	}
	
	public void execute(){
		//pic18.incrementPc();
		//System.out.println("command is " + name);
		//System.out.println("alivecnt is " + pic18.dataMem.getGpMem()[0x03].read());
	}

}
