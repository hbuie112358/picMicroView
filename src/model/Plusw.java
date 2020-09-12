package model;

public class Plusw extends Register {
	
	int fullAddress, highAddress, lowAddress;
	int fsrl, fsrh, wregVal;

	public Plusw(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		regRunState = new PluswRunState(this);
		regStepState = new PluswStepState(this);
		if(name.equals("plusw0")){
			fsrl = 0x0e9;
			fsrh = 0x0ea;
		}
		else if(name.equals("plusw1")){
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
		DataMemory dataMem = pic18.getDataMem();
		wregVal = dataMem.getWreg().read();
		lowAddress = dataMem.getGpMem()[fsrl].read();
		highAddress = dataMem.getGpMem()[fsrh].read();
//		System.out.println("in plusw just after read, lowAddress is: " + Integer.toHexString(lowAddress));
//		System.out.println("in plusw just after read, wregVal is: " + Integer.toHexString(wregVal));
		//if wregVal bit 7 is 1, then wreg is negative number 
		//(0x80 ^ 0x80 = 0)
		if((0x80 ^ (wregVal & 0x80)) == 0){
			
			//cast wregVal to byte and add signed value
			lowAddress = lowAddress + (byte)wregVal;
//			System.out.println("in plusw negative, lowAddress is: " + Integer.toHexString(lowAddress));
//			System.out.println("in plusw just negative, wregVal is: " + Integer.toHexString(wregVal));
			
			//if result is negative, decrement highAddress
			if(lowAddress < 0){
				if(highAddress == 0)
					highAddress = 0x0f;
				else highAddress--;
			}
			lowAddress = lowAddress & 0x0ff;	
		}
		else{
//			System.out.println("in plusw wreg pos, lowAddress is: " + Integer.toHexString(lowAddress));
//			System.out.println("in plusw wreg pos, wregVal is: " + Integer.toHexString(wregVal));
			lowAddress = lowAddress + wregVal;
//			System.out.println("in plusw wreg pos, lowAddress is: " + Integer.toHexString(lowAddress));
			//detect if carry bit, if so, increment highAddress
			if((0x100 ^ (lowAddress & 0x100)) == 0){
				if(highAddress == 0x0f)
					highAddress = 0x00;
				else highAddress++;
			}
			lowAddress = lowAddress & 0xff;
		}
		//highAddress = pic18.dataMem.getGpMem()[fsrh].read();
		fullAddress = (highAddress << 8) | lowAddress;
//		System.out.println("in plusw, fullAddress is: " + Integer.toHexString(fullAddress));
	}
	
	//This function overrides parent function.
	//The only entity that calls this function is another indf register. Its purpose is to 
	//cause an attempt by one indf to read another indf to return 0 by letting the function
	//know that it is being called by another indf. It is inherited from Register. After 
	//read, decrements FSR.
	int read(){
		getFullAddress();
		
		//tells callee that caller is an indf register
		return pic18.getDataMem().getGpMem()[fullAddress].read(this);
	}
	
	//Returns the register contents via the read() method. Used by pic18.getDataMemory()
	//to report memory contents to reply controller. Overridden in indirect registers 
	//postdec, postinc, preinc, plusw so that reading the contents for the sake of reporting
	//does not change the contents
	int getContents(){
		return contents;
	}
	
	class PluswRunState extends RegRunState{
		Register register;
		
		public PluswRunState(Register register){
//			this.register = register;
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
			pic18.getDataMem().getGpMem()[fullAddress].write(value, register);

			//System.out.println("in " + name", written by register at address: " + Integer.toHexString(address));
		}
		
		//This function overrides parent function.
		//The only entity that calls this function is another indf register. Its purpose is to 
		//cause an attempt by one indf to access another indf to have no effect by letting the function
		//know that it is being called by another indf. It is inherited from Register.
		public void write(int value, Register r){
			return;
		}

		@Override
		public void clear() {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].clear(register);
		}
		
		public void clear(Register r){
			return;
		}

		@Override
		public void setBit(int bit) {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].setBit(bit, register);
		}

		@Override
		public void clearBit(int bit) {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].clearBit(bit, register);
		}
		
		public void clearBit(int bit, Register r){
			return;
		}

		@Override
		public void decrement() {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].decrement(register);
		}
		
		public void decrement(Register r){
			return;
		}

		@Override
		public void increment() {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].increment(register);
		}
		
		public void increment(Register r){
			return;
		}
		
	}
	
	class PluswStepState extends RegStepState{
		Register register;
		
		public PluswStepState(Register register){
//			this.register = register;
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
			pic18.getDataMem().getGpMem()[fullAddress].write(value, register);
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction

			//System.out.println("in " + name", written by register at address: " + Integer.toHexString(address));
		}
		
		//This function overrides parent function.
		//The only entity that calls this function is another indf register. Its purpose is to 
		//cause an attempt by one indf to access another indf to have no effect by letting the function
		//know that it is being called by another indf. It is inherited from Register.
		public void write(int value, Register r){
			return;
		}

		@Override
		public void clear() {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].clear(register);
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void clear(Register r){
			return;
		}

		@Override
		public void setBit(int bit) {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].setBit(bit, register);
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void setBit(int bit, Register r){
			return;
		}

		@Override
		public void clearBit(int bit) {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].clear(register);
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void clearBit(int bit, Register r){
			return;
		}

		@Override
		public void decrement() {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].decrement(register);
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void decrement(Register r){
			return;
		}

		@Override
		public void increment() {
			getFullAddress();
			pic18.getDataMem().getGpMem()[fullAddress].increment(register);
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction
		}
		
		public void increment(Register r){
			return;
		}
	}
}

