package model;


public class Register implements SetState{
	protected int address;
	protected String name;
	protected Pic18F452 pic18;
	protected int contents;
	protected int baseSet;
	protected int baseClear;
	RegisterState registerState;
	RegisterState regRunState;
	RegisterState regStepState;
	
	public Register(Pic18F452 pic18, int address,  String name){
		this.name = name;
		this.pic18 = pic18;
		this.address = address;
		regRunState = new RegRunState(this);
		regStepState = new RegStepState(this);
		registerState = regStepState;
		baseSet = 0x1;
		baseClear = 0xfe;
		clear();
	}
	
	public void setRunState(){
		registerState = regRunState;
	}
	
	public void setStepState(){
		registerState = regStepState;
//		System.out.println("in Register.setStepState(), setting " + name + " to regStepState");
	}
	
	public void write(int value){
//		System.out.println("in Register.write(value), calling registerState.write(value)");
		registerState.write(value);	
	}
	
	//This method is overridden in indf for indirect addressing to have the effect that
	//if an indf register is read or written indirectly through another indf register, then the 
	//contents are read as 0 and a write has no effect. Sending the register as a parameter
	//allows indf to know who is calling for read/write. If not overridden, works same as 
	//write(int value). See Indf for overriding implementation
	public void write(int value, Register r){
//		System.out.println("in Register.write(value, r), calling registerState.write(value, r)");
		registerState.write(value, r);
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
	
	public void clear(){
		registerState.clear();
	}
	
	public void clear(Register r){
		registerState.clear(r);
	}
	
	public void setBit(int bit){
//		System.out.println("in Register.setBit(bit), calling registerState.setBit(bit) ");
		registerState.setBit(bit);
	}
	
	public void setBit(int bit, Register r){
//		System.out.println("in Register.setBit(bit, r), calling registerState.setBit(bit, r)");
		registerState.setBit(bit, r);
	}
	
	public void clearBit(int bit){
		registerState.clearBit(bit);
	}
	
	public void clearBit(int bit, Register r){
		registerState.clearBit(bit, r);
	}
	
	public void decrement(){
		registerState.decrement();
	}
	
	public void decrement(Register r){
		registerState.decrement(r);
	}
	
	public void increment(){
		registerState.increment();
	}
	
	public void increment(Register r){
		registerState.increment(r);
	}
	
	//Returns the register contents via the read() method. Used by pic18.getDataMemory()
	//to report memory contents to reply controller. Overridden in indirect registers 
	//postdec, postinc, preinc, plusw so that reading the contents for the sake of reporting
	//does not change the contents
	int getContents(){
		return read();
	}
	
	//Returns 1 if bit is set, 0 if bit is clear
	int getBit(int bit){
		int andValue = baseSet << bit;
		//System.out.println("getBit receives bit value of " + bit + ", decodes andValue as " + andValue);
		if((read() & andValue) == 0 )
			return 0;
		else return 1;
	}
	
	void printInfo(){
		System.out.println("Register name is: " + name + ", address is: " + Integer.toHexString(address));
	}
}
