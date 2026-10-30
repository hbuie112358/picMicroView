package picmicroview.model;


public class Movlw extends PicInstruction {

	Movlw(int instruction, Pic18F452 pic18, String name){
		super(instruction, pic18, name);
	}

	public void execute(){
		//System.out.println("command is " + name );
		int value = getInstruction() & 0x00FF;
		getPic18().getDataMem().getWreg().write(value);
		//System.out.println("wreg is " + Integer.toBinaryString(pic18.dataMem.getWreg().read()));
		//System.out.println("status register is " + Integer.toBinaryString(pic18.dataMem.getStatus().read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.getGpMem()[0x03].read());
	}

}

