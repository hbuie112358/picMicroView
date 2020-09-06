package model;

import java.io.Serializable;

public class TestInstruction implements Serializable{
	
	 int instruction, address, value;

	public TestInstruction(int instruction, int address, int value) {
		this.instruction = instruction;
		this.address = address;
		this.value = value;
	}

}
