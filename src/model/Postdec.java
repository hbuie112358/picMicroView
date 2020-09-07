package model;

public class Postdec extends Register {
	
	int fullAddress, highAddress, lowAddress;
	int fsrl, fsrh, readReturn;

	public Postdec(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		regRunState = new PostdecRunState(this);
		regStepState = new PostdecStepState(this);
		if(name.equals("postdec0")){
			fsrl = 0x0e9;
			fsrh = 0x0ea;
		}
		else if(name.equals("postdec1")){
			fsrl = 0x0e1;
			fsrh = 0x0e2;
		}
		else{
			fsrl = 0x0d9;
			fsrh = 0x0da;
		}

		this.contents = 0;
	}
	
	//Gets full address based on whether it is an instance of indf0, indf1, or indf2.
	//It needs to know whether to get address from fsr0, fsr1, or fsr2.
	private void getFullAddress(){
		highAddress = pic18.getDataMem().gpMem[fsrh].read();
		lowAddress = pic18.getDataMem().gpMem[fsrl].read();
		highAddress = highAddress << 8;
		fullAddress = highAddress | lowAddress;
	}
	
	//This function overrides parent function.
	//The only entity that calls this function is another indf register. Its purpose is to 
	//cause an attempt by one indf to read another indf to return 0 by letting the function
	//know that it is being called by another indf. It is inherited from Register. After 
	//read, decrements FSR.
	int read(){
		getFullAddress();
		
		//tells callee that caller is an indf register
		readReturn = pic18.getDataMem().gpMem[fullAddress].read(this);
		pic18.getDataMem().gpMem[fsrl].decrement();
		return readReturn;
	}
	
	//Returns the register contents via the read() method. Used by pic18.getDataMemory()
	//to report memory contents to reply controller. Overridden in indirect registers 
	//postdec, postinc, preinc, plusw so that reading the contents for the sake of reporting
	//does not change the contents
	int getContents(){
		return contents;
	}
	
	class PostdecRunState extends RegRunState{
		
		public PostdecRunState(Register register){
			super(register);
		}
		
		//This function overrides parent function.
		//Writes value to memory location pointed to by fsrh:fsrL 
		//Gets full address based on whether this in an instance of indf0, indf1, or indf2, 
		//then writes value to that register using the write(int value, Register r) method so 
		//that if the register being written to is another indf register, the write will have 
		//no effect. After write operation, decrements FSR
		public void write(int value){
			getFullAddress();
			
			//tells callee that caller is an indf register
			pic18.getDataMem().gpMem[fullAddress].write(value, register);
			pic18.getDataMem().gpMem[fsrl].decrement();
		}
		
		//This function overrides parent function.
		//The only entity that calls this function is another indf register. Its purpose is to 
		//cause an attempt by one indf to access another indf to have no effect by letting the function
		//know that it is being called by another indf. It is inherited from Register.
		public void write(int value, Register r){
			return;
		}
		
		public void clear(){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].clear(register);
			pic18.getDataMem().gpMem[fsrl].decrement();
		}
		
		public void clear(Register r){
			return;
		}
		
		public void setBit(int bit){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].setBit(bit, register);
			pic18.getDataMem().gpMem[fsrl].decrement();
		}
		
		public void setBit(int bit, Register r){
			return;
		}
		
		public void clearBit(int bit){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].clearBit(bit, register);
			pic18.getDataMem().gpMem[fsrl].decrement();
		}
		
		public void clearBit(int bit, Register r){
			return;
		}
		
		public void decrement(){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].decrement(register);
			pic18.getDataMem().gpMem[fsrl].decrement();
		}
		
		public void decrement(Register r){
			return;
		}
		
		public void increment(){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].increment(register);
			pic18.getDataMem().gpMem[fsrl].decrement();
		}
		
		public void increment(Register r){
			return;
		}
		
	}
	
	class PostdecStepState extends RegStepState{
		
		public PostdecStepState(Register register){
			super(register);
		}
		
		//This function overrides parent function.
		//Writes value to memory location pointed to by fsrh:fsrL 
		//Gets full address based on whether this in an instance of indf0, indf1, or indf2, 
		//then writes value to that register using the write(int value, Register r) method so 
		//that if the register being written to is another indf register, the write will have 
		//no effect. After write operation, decrements FSR
		public void write(int value){
			getFullAddress();
			
			//tells callee that caller is an indf register
			pic18.getDataMem().gpMem[fullAddress].write(value, register);
			pic18.getDataMem().gpMem[fsrl].decrement();
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		//This function overrides parent function.
		//The only entity that calls this function is another indf register. Its purpose is to 
		//cause an attempt by one indf to access another indf to have no effect by letting the function
		//know that it is being called by another indf. It is inherited from Register.
		public void write(int value, Register r){
			return;
		}
		
		public void clear(){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].clear(register);
			pic18.getDataMem().gpMem[fsrl].decrement();
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void clear(Register r){
			return;
		}
		
		public void setBit(int bit){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].setBit(bit, register);
			pic18.getDataMem().gpMem[fsrl].decrement();
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void setBit(int bit, Register r){
			return;
		}
		
		public void clearBit(int bit){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].clearBit(bit, register);
			pic18.getDataMem().gpMem[fsrl].decrement();
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void clearBit(int bit, Register r){
			return;
		}
		
		public void decrement(){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].decrement(register);
			pic18.getDataMem().gpMem[fsrl].decrement();
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void decrement(Register r){
			return;
		}
		
		public void increment(){
			getFullAddress();
			pic18.getDataMem().gpMem[fullAddress].increment(register);
			pic18.getDataMem().gpMem[fsrl].decrement();
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void increment(Register r){
			return;
		}
	}
}
