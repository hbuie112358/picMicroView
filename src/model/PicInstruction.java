package model;


public abstract class PicInstruction implements Instruction {
	private int instruction;
	private Pic18F452 pic18;
	private String name;

	protected int getInstruction() {
		return instruction;
	}

	protected void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	protected Pic18F452 getPic18() {
		return pic18;
	}
	
	public PicInstruction(int instruction, Pic18F452 pic18, String name){
		this.instruction = instruction;
		this.pic18 = pic18;
		this.name = name;
		//execute();
	}
	
	public abstract void execute();

	void adjustDCbit(int arg1, int arg2){
		boolean dc = (((arg1 & 0x0f) + (arg2 & 0x0f)) & 0x10) == 0x10;
//		System.out.println("arg1 & 0x0f: " + Integer.toBinaryString((arg1 & 0x0f)));
//		System.out.println("arg2 & 0x0f: " + Integer.toBinaryString((arg2 & 0x0f)));
		if(dc)
			pic18.getDataMem().status.setBit(1);
		else pic18.getDataMem().status.clearBit(1);
	}

	void adjustOVbit(String operation, int arg1, int arg2){
//		System.out.println("arg1 & 0x7f: " + Integer.toBinaryString((arg1 & 0x7f)));
//		System.out.println("arg2 & 0x7f: " + Integer.toBinaryString((arg2 & 0x7f)));
		boolean ov = (((arg1 & 0x7f) + (arg2 & 0x7f)) & 0x80) == 0x80;
		if(operation.equals("add") || operation.equals("sub")){
			if(ov)
				pic18.getDataMem().status.setBit(3);
			else pic18.getDataMem().status.clearBit(3);
		}
		//Clear ov bit in case of subtraction because manual is not clear
		//about how ov bit is affected during subtraction
		else pic18.getDataMem().status.clearBit(3);
	}

	protected void adjustZbit(int result){
		if((result & 0x00ff) == 0){
			pic18.getDataMem().status.setBit(2);
			pic18.getDataMem().status.clearBit(4);
		}
		else{
			pic18.getDataMem().status.clearBit(2);
		}
		//System.out.println("status reg is: " + Integer.toBinaryString(pic18.getDataMem().status.read()));
	}

	protected void adjustNbit(int result){
		if((result & 0x80) == 0x80){
			pic18.getDataMem().status.setBit(4);
			pic18.getDataMem().status.clearBit(2);
		}
		else pic18.getDataMem().status.clearBit(4);
	}

	protected void adjustCbit(int result){
		if(result > 0xff){
			pic18.getDataMem().status.setBit(0);
		}
		else if(pic18.getDataMem().status.getBit(0) == 1){
			pic18.getDataMem().status.clearBit(0);
		}
	}
}
