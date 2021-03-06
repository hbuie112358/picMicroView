package picmicroview.model;

class Preinc extends Register {
	
	private int fullAddress;
	private int fsrl;
	private int fsrh;

	Preinc(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		regRunState = new PreincRunState(this);
		regStepState = new PreincStepState(this);
		if(name.equals("preinc0")){
			fsrl = 0x0e9;
			fsrh = 0x0ea;
		}
		else if(name.equals("preinc1")){
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
		int highAddress = pic18.getDataMem().getGpMem()[fsrh].read();
		int lowAddress = pic18.getDataMem().getGpMem()[fsrl].read();
		highAddress = highAddress << 8;
		fullAddress = highAddress | lowAddress;
	}
	
	//This function overrides parent function.
	//The only entity that calls this function is another indf register. Its purpose is to 
	//cause an attempt by one indf to read another indf to return 0 by letting the function
	//know that it is being called by another indf. It is inherited from Register. After 
	//read, decrements FSR.
	int read(){
		
		//increment before getting full address
		pic18.getDataMem().getGpMem()[fsrl].increment();
		getFullAddress();
		
		//tells callee that caller is an indf register
		return pic18.getDataMem().getGpMem()[fullAddress].read(this);
	}
	
	//Returns the register contents via the read() method. Used by pic18.getDataMemory()
	//to report memory contents to reply picmicroview.controller. Overridden in indirect registers
	//postdec, postinc, preinc, plusw so that reading the contents for the sake of reporting
	//does not change the contents
	int getContents(){
		return contents;
	}
	
	class PreincRunState extends RegRunState{
		
		PreincRunState(Register register){
			super(register);
		}
		
		//This function overrides parent function.
		//Writes value to memory location pointed to by fsrh:fsrL 
		//Gets full address based on whether this in an instance of indf0, indf1, or indf2, 
		//then writes value to that register using the write(int value, Register r) method so 
		//that if the register being written to is another indf register, the write will have 
		//no effect. After write operation, decrements FSR
		public void write(int value){
			//increment before getting address
			pic18.getDataMem().getGpMem()[fsrl].increment();
			getFullAddress();
		
			//tells callee that caller is an indf register
			pic18.getDataMem().getGpMem()[fullAddress].write(value, register);
		}
		
		//This function overrides parent function.
		//The only entity that calls this function is another indf register. Its purpose is to 
		//cause an attempt by one indf to access another indf to have no effect by letting the function
		//know that it is being called by another indf. It is inherited from Register.
		public void write(int value, Register r){
			//inherited function unimplemented for this class
		}
		
		public void clear(){
			pic18.getDataMem().getGpMem()[fsrl].increment();
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].clear(register);
		}
		
		public void clear(Register r){
			//inherited function unimplemented for this class
		}
		
		public void setBit(int bit){
			pic18.getDataMem().getGpMem()[fsrl].increment();
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].setBit(bit, register);
		}
		
		public void setBit(int bit, Register r){
			//inherited function unimplemented for this class
		}
		
		public void clearBit(int bit){
			pic18.getDataMem().getGpMem()[fsrl].increment();
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].clearBit(bit, register);
		}
		
		public void clearBit(int bit, Register r){
			//inherited function unimplemented for this class
		}
		
		public void decrement(){
			pic18.getDataMem().getGpMem()[fsrl].increment();
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].decrement();
		}
		
		public void decrement(Register r){
			//inherited function unimplemented for this class
		}
		
		public void increment(){
			pic18.getDataMem().getGpMem()[fsrl].increment();
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].increment(register);
		}
		
		public void increment(Register r){
			//inherited function unimplemented for this class
		}
	}
	
	class PreincStepState extends RegStepState{
		
		PreincStepState(Register register){
			super(register);
		}
		
		//This function overrides parent function.
		//Writes value to memory location pointed to by fsrh:fsrL 
		//Gets full address based on whether this in an instance of indf0, indf1, or indf2, 
		//then writes value to that register using the write(int value, Register r) method so 
		//that if the register being written to is another indf register, the write will have 
		//no effect. After write operation, decrements FSR
		public void write(int value){
			//increment before getting address
			pic18.getDataMem().getGpMem()[fsrl].increment();
			getFullAddress();
			
			//tells callee that caller is an indf register
			pic18.getDataMem().getGpMem()[fullAddress].write(value, register);
			register.pic18.changes.add(address);	//tracks changes pic state during instruction
		}
		
		//This function overrides parent function.
		//The only entity that calls this function is another indf register. Its purpose is to 
		//cause an attempt by one indf to access another indf to have no effect by letting the function
		//know that it is being called by another indf. It is inherited from Register.
		public void write(int value, Register r){
			//inherited function unimplemented for this class
		}
	}
}

