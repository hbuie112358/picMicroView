package net.sourceforge.picmicroview.model;

public class RegStepState implements RegisterState {
	String name = "RegStepState";
	Register register;

	public RegStepState(Register register) {
		this.register = register;
	}

	@Override
	public void write(int value) {
		register.contents = value & 0xff;
//		System.out.println("in RegStepState, writing " + Integer.toHexString(value) + " to "
//				+ register.name);
//		System.out.println("in RegStepState.write(value), writing " + Integer.toHexString(value) + " to "
//				+ register.name + " at " + Integer.toHexString(register.address));
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction

	}

	@Override
	public void write(int value, Register r) {
		register.contents = value & 0xff;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
//		System.out.println("in RegStepState.write(value, r), writing " + Integer.toHexString(value) + " to "
//				+ register.name + " at " + Integer.toHexString(register.address));
	}

	@Override
	public void clear() {
		register.contents = 0;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}
	
	public void clear(Register r){
		register.contents = 0;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}

	@Override
	public void setBit(int bit) {
		int orValue = register.baseSet << bit;
		register.contents = register.contents | orValue;
//		System.out.println("in RegStepState.setBit(bit), setting bit " + bit + " in "
//				+ register.name + " at " + Integer.toHexString(register.address));
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}
	
	public void setBit(int bit, Register r){
		int orValue = register.baseSet << bit;
		register.contents = register.contents | orValue;
//		System.out.println("in RegStepState.setBit(bit, r), setting bit " + bit + " in "
//				+ register.name + " at " + Integer.toHexString(register.address));
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}

	@Override
	public void clearBit(int bit) {
		//shifts a zero left and adds ones back in to the right
		int andValue = (register.baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
		
		//System.out.println("clearBit receives bit value of " + bit + ", decodes andValue as " + andValue);
//		System.out.println("in RegStepState, clearing bit " + bit + " of " + register.name + " " + Integer.toHexString(register.address));
		register.contents = register.contents & andValue;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction

	}
	
	public void clearBit(int bit, Register r){
		//shifts a zero left and adds ones back in to the right
		int andValue = (register.baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
		
		//System.out.println("clearBit receives bit value of " + bit + ", decodes andValue as " + andValue);
//		System.out.println("in RegStepState, clearing bit " + bit + " of " + register.name + " " + Integer.toHexString(register.address));
		register.contents = register.contents & andValue;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}

	@Override
	public void decrement() {
		register.contents--;
		register.contents = register.contents & 0xff;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}
	
	public void decrement(Register r){
		register.contents--;
		register.contents = register.contents & 0xff;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}

	@Override
	public void increment() {
		register.contents++;
		register.contents = register.contents & 0xff;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}
	
	public void increment(Register r){
		register.contents++;
		register.contents = register.contents & 0xff;
		register.pic18.changes.add((Integer)register.address);	//tracks changes pic state during instruction
	}

}
