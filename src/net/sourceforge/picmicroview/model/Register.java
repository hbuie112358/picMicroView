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
	
	//This method is overridden in indf for indirect addressing to have the effect that
	//if an indf register is read or written indirectly through another indf register, then the 
	//contents are read as 0 and a write has no effect. Sending the register as a parameter
	//allows indf to know who is calling for read/write. If not overridden, works same as 
	//write(int value). See Indf for overriding implementation
	void write(int value, Register r){
		this.contents = value;
		//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
	}

	
	int read(){
		return contents;
	}
	
	//This method is overridden in indf for indirect addressing to have the effect that
	//if an indf register is read or written indirectly through another indf register, then the 
	//contents are read as 0 and a write has no effect. Sending the register as a parameter
	//allows indf to know who is calling for read/write. If not overridden, works same as 
	//read(). See Indf for overriding implementation
	int read(Register r){
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
	
	//Returns the register contents via the read() method. Used by pic18.getDataMemory()
	//to report memory contents to reply controller. Overridden in indirect registers 
	//postdec, postinc, preinc, plusw so that reading the contents for the sake of reporting
	//does not change the contents
	int getContents(){
		return read();
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
