package model;

public class FsrH extends Register {

	public FsrH(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		regRunState = new FsrHRunState(this);
		regStepState = new FsrHStepState(this);
	}
	
	//Masks off all but least significant nibble. Bits 4-7 of FSRHx are 
	//denoted as unimplemented in manual.
	public void write(int value){
//		this.contents = value & 0x000f;
		registerState.write(value);
		
		//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
	}
	
	public void increment(){
		registerState.increment();
//		if(contents == 0x000f)
//			contents = 0;
//		else contents++;
	}
	
	public void decrement(){
		registerState.decrement();
//		if(contents == 0x00)
//			contents = 0x0f;
//		else contents--;
	}

	class FsrHRunState extends RegRunState{
		
		public FsrHRunState(Register register){
			super(register);
		}
		//Masks off all but least significant nibble. Bits 4-7 of FSRHx are 
		//denoted as unimplemented in manual.
		public void write(int value){
			contents = value & 0x000f;
			
			//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
		}
		
		public void increment(){
			if(contents == 0x000f)
				contents = 0;
			else contents++;
		}
		
		public void decrement(){
			if(contents == 0x00)
				contents = 0x0f;
			else contents--;
		}
	}
	
	class FsrHStepState extends RegStepState{
		
		public FsrHStepState(Register register){
			super(register);
		}
		//Masks off all but least significant nibble. Bits 4-7 of FSRHx are 
		//denoted as unimplemented in manual.
		public void write(int value){
			contents = value & 0x000f;
			pic18.changes.add((Integer)address);	//tracks changes pic state during instruction

			
			//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
		}
		
		public void increment(){
			if(contents == 0x000f)
				contents = 0;
			else contents++;
			pic18.changes.add((Integer)address);	//tracks changes pic state during instruction

		}
		
		public void decrement(){
			if(contents == 0x00)
				contents = 0x0f;
			else contents--;
			pic18.changes.add((Integer)address);	//tracks changes pic state during instruction

		}
	}
}
