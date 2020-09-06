package model;

/**
 * Did not create a DisplayRegister subclass for Register and then make all
 * output ports subclass that because I wanted to make the calls to update
 * gui as simple as possible in order to make speed a priority. Therefore, 
 * port classes (Wreg, PortB, etc.) have much duplicated code in overriding 
 * Register methods that write the register contents. The only difference is 
 * in which ReplyController method is called (ex. updateWreg(), updatePortB()).
 * @author hb
 *
 */
public class PortB extends Register {

	public PortB(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		regRunState = new PortBRunState(this);
		regStepState = new PortBStepState(this);
	}

	public void write(int value){
		registerState.write(value);
	}
	
	public void setBit(int bit){
		
		registerState.setBit(bit);
	}
	
	public void clearBit(int bit){
		registerState.clearBit(bit);
	}
	
	public void decrement(){
		registerState.decrement();
	}
	
	public void increment(){
		registerState.increment();
	}
	
	void setContents(int value){
		contents = value;
		contents = contents & 0xff;
		pic18.repCont.updatePortB(contents);
//		pic18.changes.add((Integer)address);	//track writes to registers
	}
	
	public class PortBRunState extends RegRunState{
		Register register;
		
		public PortBRunState(Register register){
			super(register);
		}
		
		public void write(int value){
			contents = value;
			contents = contents & 0xff;
			pic18.repCont.updatePortB(contents);
		}
		
		public void setBit(int bit){
			int orValue = baseSet << bit;
			contents = contents | orValue;
			pic18.repCont.updatePortB(contents);
		}
		
		public void clearBit(int bit){
			//shifts a zero left and adds ones back in to the right
			int andValue = (baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
			contents = contents & andValue;
			pic18.repCont.updatePortB(contents);
		}
		
		public void decrement(){
			contents--;
			contents = contents & 0xff;
			pic18.repCont.updatePortB(contents);
		}
		
		public void increment(){
			contents++;
			contents = contents & 0xff;
			pic18.repCont.updatePortB(contents);
		}
	}
	
	public class PortBStepState extends RegStepState{
		Register register;
		
		public PortBStepState(Register register){
			super(register);
		}
		
		public void write(int value){
			contents = value;
			contents = contents & 0xff;
			pic18.repCont.updatePortB(contents);
			pic18.changes.add((Integer)address);	//track writes to registers
		}
		
		public void setBit(int bit){
			int orValue = baseSet << bit;
			contents = contents | orValue;
			pic18.repCont.updatePortB(contents);
			pic18.changes.add((Integer)address);	//track writes to registers
		}
		
		public void clearBit(int bit){
			//shifts a zero left and adds ones back in to the right
			int andValue = (baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
			contents = contents & andValue;
			pic18.repCont.updatePortB(contents);
			pic18.changes.add((Integer)address);	//track writes to registers
		}
		
		public void decrement(){
			contents--;
			contents = contents & 0xff;
			pic18.repCont.updatePortB(contents);
			pic18.changes.add((Integer)address);	//track writes to registers
		}
		
		public void increment(){
			contents++;
			contents = contents & 0xff;
			pic18.repCont.updatePortB(contents);
			pic18.changes.add((Integer)address);	//track writes to registers
		}
	}
}
