package picmicroview.model;

public class RegRunState implements RegisterState {
	Register register;

	public RegRunState(Register register) {
		this.register = register;
	}

	@Override
	public void write(int value) {
		register.contents = value & 0xff;
	}

	@Override
	public void write(int value, Register r) {
		register.contents = value & 0xff;
	}

	@Override
	public void clear() {
		register.contents = 0;
	}
	
	public void clear(Register r){
		register.contents = 0;
	}

	@Override
	public void setBit(int bit) {
		int orValue = register.baseSet << bit;
		register.contents = register.contents | orValue;
	}
	
	public void setBit(int bit, Register r){
		int orValue = register.baseSet << bit;
		register.contents = register.contents | orValue;
	}

	@Override
	public void clearBit(int bit) {
		int andValue = (register.baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
		//System.out.println("clearBit receives bit value of " + bit + ", decodes andValue as " + andValue);

		register.contents = register.contents & andValue;

	}
	
	public void clearBit(int bit, Register r){
		int andValue = (register.baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
		//System.out.println("clearBit receives bit value of " + bit + ", decodes andValue as " + andValue);

		register.contents = register.contents & andValue;
	}

	@Override
	public void decrement() {
		register.contents--;
		register.contents = register.contents & 0xff;
	}
	
	public void decrement(Register r){
		register.contents--;
		register.contents = register.contents & 0xff;
	}

	@Override
	public void increment() {
		register.contents++;
		register.contents = register.contents & 0xff;
	}
	
	public void increment(Register r){
		register.contents++;
		register.contents = register.contents & 0xff;
	}

}
