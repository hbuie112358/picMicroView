package net.sourceforge.picmicroview.model;

interface RegisterState {

	void write(int value);
	
	void write(int value, Register r);

	void clear();
	
	void setBit(int bit);
	
	void clearBit(int bit);
	
	void decrement();
	
	void increment();

}
