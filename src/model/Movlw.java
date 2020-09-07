package model;


public class Movlw extends Instruction{
	
	private int value;
	
	Movlw(int instruction, Pic18F452 pic18, String name){
		super(instruction, pic18, name);
	}
	
	protected void execute(){
		//System.out.println("command is " + name );
		value = getInstruction() & 0x00FF;
		getPic18().getDataMem().wreg.write(value);
		//System.out.println("wreg is " + Integer.toBinaryString(pic18.dataMem.wreg.read()));
		//System.out.println("status register is " + Integer.toBinaryString(pic18.dataMem.status.read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

}

