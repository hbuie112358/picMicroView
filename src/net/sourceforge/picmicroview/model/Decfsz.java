package net.sourceforge.picmicroview.model;

public class Decfsz extends Instruction {

	public Decfsz(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	protected void execute(){
		int highByte = instruction & 0xff00; //isolate command portion of instruction
		int address = instruction & 0x00FF; //address held in data portion of instruction	
		int result = pic18.dataMem.gpMem[address].read();
		//System.out.println("result before decrement is " + result);
		
		//rollover to 255 if register value to be decremented is 0
		if(result == 0)
			result = 0xff;
		else
			result--;
		if ((highByte ^ 0x2c00) == 0){ //if d,a are both 0, result to wreg (xor)
			pic18.dataMem.wreg.write(result);
			//if result == 0 skip next instruction
			if(result == 0)
				pic18.pc.increment();
			//System.out.println("value in wreg is " + pic18.dataMem.wreg.read());
		}
		else if ((highByte ^ 0x2e00) == 0){ //if d = 1, a = 0, result to f
			pic18.dataMem.gpMem[address].write(result);
			//if result == 0 skip next instruction
			if(result == 0)
				pic18.pc.increment();
			//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
		}
		else{
			System.out.println("unimplemented decfsz command " + Integer.toHexString(highByte));
			System.exit(0);
		}
		//System.out.println("value at address " + address + " is " + pic18.dataMem.gpMem[address].read());
		//System.out.println("result is " + result);
		//System.out.println("");
	}

	@Override
	protected void initialize(int instruction) {
		this.instruction = instruction;

	}
}
