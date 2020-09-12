package model;


public class Movff extends PicInstruction {

	Movff(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	public void execute(){
		//System.out.println("command is " + name);
		int instruction = getInstruction();
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		int source = instruction & 0x00ff;
		//System.out.println("source address is " + Integer.toHexString(source) +
		//		"and contents is " + pic18.dataMem.getGpMem()[source].read());
		instruction = pic18.getProgramCounter().getWord();
		int destination = instruction & 0x00ff;
		//System.out.println("destination address is " + Integer.toHexString(destination));
		dataMem.getGpMem()[destination].write(dataMem.getGpMem()[source].read());
		//System.out.println("data at destination is " + 
		//		Integer.toHexString(pic18.dataMem.getGpMem()[destination].read()));
		//System.out.println("contents of memory 03 is " + pic18.dataMem.getGpMem()[0x03].read());
	}

}
