package net.sourceforge.picmicroview.model;

public class BSR extends Register {

	public BSR(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		// TODO Auto-generated constructor stub
	}

	void write(int value){
		//ignore write if bank not implemented (only 0-f is implemented)
		if(value > 0x0f)
			return;
		else{
			//mask bits 7-4 so that only lower nibble is used
			value = value & 0x000f;
			this.contents = value;
			
			//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
		}

	}
	
	int read(){
		//return all 0's if bank not implemented (only 0-f is implemented)
		if(contents > 0x0f)
			return 0;
		else
			return contents;
	}

}
