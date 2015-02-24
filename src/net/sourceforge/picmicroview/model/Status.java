package net.sourceforge.picmicroview.model;


public class Status extends Register {

	public Status(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);		
	}

	@Override
	public void write(int value) {
		
	}
}
