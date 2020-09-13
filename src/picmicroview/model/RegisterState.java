package picmicroview.model;

interface RegisterState {

	void write(int value);
	
	void write(int value, Register r);

	void clear();
	
	void clear(Register r);
	
	void setBit(int bit);
	
	void setBit(int bit, Register r);
	
	void clearBit(int bit);
	
	void clearBit(int bit, Register r);
	
	void decrement();
	
	void decrement(Register r);
	
	void increment();
	
	void increment(Register r);

}
