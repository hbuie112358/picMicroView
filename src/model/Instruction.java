package model;


public abstract class Instruction {
	public int instruction;
	public Pic18F452 pic18;
	public String name;
	
	public Instruction (int instruction, Pic18F452 pic18, String name){
		this.instruction = instruction;
		this.pic18 = pic18;
		this.name = name;
		//execute();
	}
	
	protected abstract void execute();

	protected abstract void initialize(int instruction);
}
