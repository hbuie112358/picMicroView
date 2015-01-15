package net.sourceforge.picmicroview.model;


public class Register {
	protected int address;
	protected String name;
	protected Pic18F452 pic18;
	protected int contents;
	protected int baseSet;
	protected int baseClear;
	
	public Register(Pic18F452 pic18, int address,  String name){
		this.name = name;
		this.pic18 = pic18;
		this.address = address;
		baseSet = 0x1;
		baseClear = 0xfe;
		clear();
	}
	
	void write(int value){
		this.contents = value;
		//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
	}

	
	int read(){
		return contents;
	}
	
	void clear(){
		contents = 0;
	}
	
	void setBit(int bit){
		int orValue = baseSet << bit;
		contents = contents | orValue;
	}
	
	void clearBit(int bit){
		//shifts a zero left and adds ones back in to the right
		int andValue = (baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
		//System.out.println("clearBit receives bit value of " + bit + ", decodes andValue as " + andValue);

		contents = contents & andValue;
		//System.out.println("Contents of " + name + " at address " + address + ": " + contents);
	}
	
	void decrement(){
		contents--;
	}
	
	void increment(){
		contents++;
	}
	
	void setContents(int value){
		contents = value;
	}
	
	int getBit(int bit){
		int andValue = baseSet << bit;
		//System.out.println("getBit receives bit value of " + bit + ", decodes andValue as " + andValue);
		if((contents & andValue) == 0 )
			return 0;
		else return 1;
	}
	
	void printInfo(){
		System.out.println("Register name is: " + name + ", address is: " + Integer.toHexString(address));
	}
}
