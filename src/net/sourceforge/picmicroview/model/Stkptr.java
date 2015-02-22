package net.sourceforge.picmicroview.model;


public class Stkptr extends Register {

	public Stkptr(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
	}

	@Override
	void write(int value) {
		
	}
}
