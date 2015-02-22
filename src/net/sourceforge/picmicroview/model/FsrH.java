package net.sourceforge.picmicroview.model;

public class FsrH extends Register {

	public FsrH(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		// TODO Auto-generated constructor stub
	}
	
	//Masks off all but least significant nibble. Bits 4-7 of FSRHx are 
	//denoted as unimplemented in manual.
	void write(int value){
		this.contents = value & 0x000f;
		
		//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
	}
	
	void increment(){
		if(contents == 0x000f)
			contents = 0;
		else contents++;
	}
	
	void decrement(){
		if(contents == 0x00)
			contents = 0x0f;
		else contents--;
	}

}
