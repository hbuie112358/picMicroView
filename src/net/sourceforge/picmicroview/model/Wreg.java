package net.sourceforge.picmicroview.model;


public class Wreg extends Register {
	
	
	public Wreg(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
	}

	@Override
	void write(int value) {
		this.contents = value;
		//System.out.println("in porta write()");
		
	}
	
//	void printInfo(){
//		System.out.println("Register name is: " + name + ", address is: " + Integer.toHexString(address));
//	}

}
