package picmicroview.model;

public class BSR extends Register {

	public BSR(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		regRunState = new BsrRunState(this);
		regStepState = new BsrStepState(this);
	}

	public void write(int value){
		registerState.write(value);
	}
	
	int read(){
		//return all 0's if bank not implemented (only 0-f is implemented)
		if(contents > 0x0f)
			return 0;
		else
			return contents;
	}
	
	class BsrRunState extends RegRunState{
		
		public BsrRunState(Register register){
			super(register);
		}
		
		@Override
		public void write(int value){
			//ignore write if bank not implemented (only 0-f is implemented)
			if(value < 0x10){
				//mask bits 7-4 so that only lower nibble is used
				int newValue = value & 0x000f;
				register.contents = newValue;

				//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
			}
		}
	}
	
	class BsrStepState extends RegStepState{
		
		public BsrStepState(Register register){
			super(register);
		}
		
		@Override
		public void write(int value){
			//ignore write if bank not implemented (only 0-f is implemented)
			if(value > 0x0f)
				return;
			else{
				//mask bits 7-4 so that only lower nibble is used
				value = value & 0x000f;
				register.contents = value;
				register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction

				//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
			}
		}
	}

}
