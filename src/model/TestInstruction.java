package model;

import java.io.Serializable;

 class TestInstruction implements Serializable{
	
	 int instruction, address, value;
	 TestInstruction(int instruction, int address, int value) {
		this.instruction = instruction;
		this.address = address;
		this.value = value;
	}

}
