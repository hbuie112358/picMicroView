package model;

public class Andlw extends PicInstruction {
	
	Andlw(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	public void execute() {
		//System.out.println("command is " + name);
		Pic18F452 pic18 = getPic18();
		int value = getInstruction() & 0x00ff;
		int result = value & pic18.getDataMem().wreg.read();
		pic18.getDataMem().wreg.write(result);
		//System.out.println("result of andlw is " + Integer.toBinaryString(result));
		adjustZbit(result);
		adjustNbit(result);
		//System.out.println("status register is " + Integer.toBinaryString(pic18.getDataMem().status.read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
	}

}
