package net.sourceforge.picmicroview.model;


public class Alu {
	private Pic18F452 pic18;
	private int result;
	private int address;
	private int highByte;
	
	public Alu(Pic18F452 pic18){
		this.pic18 = pic18;
	}
	
	public void execute(Addwf instruction){
		//System.exit(0);
		int highByte = instruction.instruction & 0xff00;
		int address = instruction.instruction & 0x00ff; //address held in data portion of instruction
		result = pic18.dataMem.wreg.read() + pic18.dataMem.gpMem[address].read();
		if ((highByte ^ 0x2400) == 0){//if d,a are both 0, result to wreg
		
			
			//pic18.setWreg(result);
			pic18.dataMem.wreg.write(result);
			adjustZbit();
		}
		else if ((highByte ^ 0x2600) == 0){ //if d = 1, a = 0, result to f
			//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
			pic18.dataMem.gpMem[address].write(result);
			adjustZbit();
		}
		else{
			System.out.println("unimplemented addwf command " + Integer.toHexString(highByte));
			System.exit(0);
		}
	}
	
	public void execute(Decf instruction){
		highByte = instruction.instruction & 0xff00; //isolate command portion of instruction
		address = instruction.instruction & 0x00FF; //address held in data portion of instruction	
		result = pic18.dataMem.gpMem[address].read();
		//System.out.println("result before decrement is " + result);
		result--;
		if ((highByte ^ 0x0400) == 0){ //if d,a are both 0, result to wreg
			pic18.dataMem.wreg.write(result);
			//System.out.println("value in wreg is " + pic18.dataMem.wreg.read());
			adjustZbit();
		}
		else if ((highByte ^ 0x0600) == 0){ //if d = 1, a = 0, result to f
			pic18.dataMem.gpMem[address].write(result);
			//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
			adjustZbit();
		}
		else{
			System.out.println("unimplemented decf command " + Integer.toHexString(highByte));
			System.exit(0);
		}
		//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
		//System.out.println("result is " + result);
		//System.out.println("");
	}
	
	public void execute(Iorwf instruction){
		result = 0;
		highByte = instruction.instruction & 0xff00; //isolate command portion of instruction
		address = instruction.instruction & 0x00FF; //isolate data portion of instruction
		int reg = pic18.dataMem.wreg.read();
		result = reg | pic18.dataMem.gpMem[address].read();
//		System.out.println("wreg: " + Integer.toHexString(pic18.dataMem.wreg.read()));
//		System.out.println("mem : " + Integer.toHexString(pic18.dataMem.gpMem[address].read()));
//		System.out.println("rslt: " + Integer.toHexString(result));
		if ((highByte ^ 0x1000) == 0){ //if d,a are both 0, result to wreg
			pic18.dataMem.wreg.write(result);
			//System.out.println("value in wreg is " + pic18.dataMem.wreg.read());
			adjustZbit();
		}
		else if ((highByte ^ 0x1200) == 0){ //if d = 1, a = 0, result to f
			pic18.dataMem.gpMem[address].write(result);
			//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
			adjustZbit();
		}
		else{
			System.out.println("unimplemented iorwf command " + Integer.toHexString(highByte));
			System.exit(0);
		}
	}
	
	public void execute(Andlw instruction){
		result = 0;
		int value = instruction.instruction & 0x00ff;
		result = value & pic18.dataMem.wreg.read();
		pic18.dataMem.wreg.write(result);
		//System.out.println("result of andlw is " + Integer.toBinaryString(result));
		adjustZbit();
		adjustNbit();
		//System.out.println("status register is " + Integer.toBinaryString(pic18.dataMem.status.read()));
	}
	
	public void execute(Movf instruction){
		result = 0;
		highByte = instruction.instruction & 0xff00; //isolate command portion of instruction
		address = instruction.instruction & 0x00FF; //isolate data portion of instruction
		result = pic18.dataMem.gpMem[address].read();
//		System.out.println("wreg: " + Integer.toHexString(pic18.dataMem.wreg.read()));
//		System.out.println("mem : " + Integer.toHexString(pic18.dataMem.gpMem[address].read()));
//		System.out.println("rslt: " + Integer.toHexString(result));
		if ((highByte ^ 0x5000) == 0){ //if d,a are both 0, result to wreg
			pic18.dataMem.wreg.write(result);
			//System.out.println("value in wreg is " + pic18.dataMem.wreg.read());
			adjustZbit();
			adjustNbit();
		}
		else if ((highByte ^ 0x5200) == 0){ //if d = 1, a = 0, result to f
			pic18.dataMem.gpMem[address].write(result);
			//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
			adjustZbit();
			adjustNbit();
		}
		else{
			System.out.println("unimplemented movf command " + Integer.toHexString(highByte));
			System.exit(0);
		}
		//System.out.println("status register is " + Integer.toBinaryString(pic18.dataMem.status.read()));
	}
	
	public void execute(Addwfc instruction){
		result = 0;
		highByte = instruction.instruction & 0xff00; //isolate command portion of instruction
		address = instruction.instruction & 0x00FF; //isolate data portion of instruction
		int wreg = pic18.dataMem.wreg.read();
		int freg = pic18.dataMem.gpMem[address].read();
		int carry = pic18.dataMem.status.getBit(0);
		result = wreg + freg + carry;
		adjustCbit();
		
//		System.out.println("wreg: " + Integer.toHexString(pic18.dataMem.wreg.read()));
//		System.out.println("mem : " + Integer.toHexString(pic18.dataMem.gpMem[address].read()));
//		System.out.println("rslt: " + Integer.toHexString(result));
		if ((highByte ^ 0x2000) == 0){ //if d,a are both 0, result to wreg
			pic18.dataMem.wreg.write(result & 0xff);
			//System.out.println("value in wreg is " + pic18.dataMem.wreg.read());
			adjustZbit();
			adjustNbit();
		}
		else if ((highByte ^ 0x2200) == 0){ //if d = 1, a = 0, result to f
			pic18.dataMem.gpMem[address].write(result & 0xff);
			//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
			adjustZbit();
			adjustNbit();
		}
		else{
			System.out.println("unimplemented addwfc command " + Integer.toHexString(highByte));
			System.exit(0);
		}
	//	System.out.println("status reg is " + Integer.toBinaryString(pic18.dataMem.status.read()));
//		System.out.println("contents of memory 03 is " + pic18.dataMem.gpMem[0x03].read());
//		System.out.println("");
	}
	
	private void adjustZbit(){
		if((result & 0x00ff) == 0){
			pic18.dataMem.status.setBit(2);
			pic18.dataMem.status.clearBit(4);
		}
		else{
			pic18.dataMem.status.clearBit(2);
		}
		//System.out.println("status reg is: " + Integer.toBinaryString(pic18.dataMem.status.read()));
	}
	
	private void adjustNbit(){
		if((result & 0x80) == 0x80){
			pic18.dataMem.status.setBit(4);
			pic18.dataMem.status.clearBit(2);
		}
		else pic18.dataMem.status.clearBit(4);
	}
	
	private void adjustCbit(){
		if(result > 0xff){
			pic18.dataMem.status.setBit(0);
		}
		else if(pic18.dataMem.status.getBit(0) == 1){
			pic18.dataMem.status.clearBit(0);
		}
	}
}


