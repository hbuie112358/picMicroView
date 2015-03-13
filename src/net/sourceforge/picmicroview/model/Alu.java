package net.sourceforge.picmicroview.model;

import java.util.HashSet;


public class Alu {
	private Pic18F452 pic18;
	private int result;
//	private int address;
	private int twosComp;
	private int freg, wreg, carry, origValue, lowNibble, highNibble;
	private boolean dc, ov;
//	private int highByte;
//	private int bsrVal = 0;
	HashSet<Integer> indfs;
	
	public Alu(Pic18F452 pic18){
		this.pic18 = pic18;
		
		//indfs is hashset of addresses that are INDFx registers: INDF, PLUSW,
		//POSTINC, PREINC, POSTDEC registers, when addressed, are supposed to 
		//disregard banked addressing. This causes a hole in all banks within
		//the ranges below. See addwf.asm or addwf.lst for more complete explanation.
		indfs = new HashSet<Integer>();
		int i;
		for(i = 0x0eb; i < 0x0f0; i++)	//add INDF0's to hashset
			indfs.add((Integer)i);
		for(i = 0x0e3; i < 0x0e8; i++)	//add INDF1's to hashset
			indfs.add((Integer)i);
		for(i = 0x0db; i < 0x0e0; i++)	//add INDF2's to hashset
			indfs.add((Integer)i);
		//Add FSR's as well
		indfs.add((Integer)0x0e9);	//FSR0L
		indfs.add((Integer)0x0ea);	//FSR0H
		indfs.add((Integer)0x0e1);	//FSR1L
		indfs.add((Integer)0x0e2);	//FSR1H
		indfs.add((Integer)0x0d9);	//FSR2L
		indfs.add((Integer)0x0da);	//FSR2H
	}
	
	public void execute(Addlw instruction){
		result = (instruction.instruction & 0xff) + pic18.dataMem.wreg.read();
		adjustDCbit(pic18.dataMem.wreg.read(), (instruction.instruction & 0xff));
		adjustOVbit("add", pic18.dataMem.wreg.read(), (instruction.instruction & 0xff));
		pic18.dataMem.wreg.write(result);
		adjustCbit();
		adjustZbit();
		adjustNbit();
//		System.out.println("in Alu.addlw, 2's complement of 0x37 is: " + 
//				Integer.toBinaryString(getTwosComplement(0x37)));
	}
	
	public void execute(Addwf instruction){	
		
		//get wreg value
		//wreg = pic18.dataMem.wreg.read();
		freg = pic18.dataMem.getRegAddress(instruction.instruction);		
//		System.out.println("in alu.addwf, freg address is: " + Integer.toHexString(freg));
		
		//find sum of wreg, value in gpMem[freg]
		result = pic18.dataMem.wreg.read() + pic18.dataMem.gpMem[freg].read();		
		adjustCbit();
		adjustZbit();
		adjustNbit();
		
		//if bit 9 of instruction is high, write result to f register
		//If f is a FSRxL register, then find out if FSR0, 1, or 2 and 
		//if carry bit is 1, increment corresponding FSRxH register.
		if((instruction.instruction & 0x200) == 0x200){
			pic18.dataMem.gpMem[freg].write(result);
			if(pic18.dataMem.status.getBit(0) == 1){
//				System.out.println("in alu.addwf, fsr0L address is " + Integer.toHexString(pic18.dataMem.fsr0L.address));
				if(freg == (pic18.dataMem.fsr0L.address & 0xff))	//& 0xff because register actually lives at 0xfe9,
					pic18.dataMem.fsr0h.increment();				//but is mapped to 0x0e9 for Access Memory
				if(freg == (pic18.dataMem.fsr1L.address & 0xff))
					pic18.dataMem.fsr1h.increment();
				if(freg == (pic18.dataMem.fsr2L.address & 0xff))
					pic18.dataMem.fsr2h.increment();
				
				//Clear carry bit since carry was reflected in FSRxH
				pic18.dataMem.status.clearBit(0);
			}			
		}
		else pic18.dataMem.wreg.write(result);
	}
	
	public void execute(Addwfc instruction){
//		highByte = instruction.instruction & 0xff00; //isolate command portion of instruction
		
		//get wreg value
		wreg = pic18.dataMem.wreg.read();
		
		//get register address
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
//		System.out.println("in alu.addwfc, freg address is: " + Integer.toHexString(freg));
		
		//get value of carry bit
		carry = pic18.dataMem.status.getBit(0);
		
		//find sum of wreg, value in gpMem[freg], + carry bit
		result = wreg + pic18.dataMem.gpMem[freg].read() + carry;
		
		//if bit 9 of instruction is high, write result to f register
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustCbit();
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Andlw instruction){
		int value = instruction.instruction & 0x00ff;
		result = value & pic18.dataMem.wreg.read();
		pic18.dataMem.wreg.write(result);
		//System.out.println("result of andlw is " + Integer.toBinaryString(result));
		adjustZbit();
		adjustNbit();
		//System.out.println("status register is " + Integer.toBinaryString(pic18.dataMem.status.read()));
	}
	
	public void execute(Andwf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		result = (pic18.dataMem.gpMem[freg].read() & pic18.dataMem.wreg.read());
		//if bit 9 of instruction is high, write result to f register
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Clrf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		pic18.dataMem.gpMem[freg].clear();
		adjustZbit();
	}
	
	public void execute(Comf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		result = ~pic18.dataMem.gpMem[freg].getContents();
		
		//if bit 9 of instruction is high, write result to f register
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Daw instruction){
		lowNibble = highNibble = 0;
		wreg = pic18.dataMem.wreg.read();
		if(((wreg & 0x0f) > 0x09) || pic18.dataMem.status.getBit(1) == 1){
			lowNibble = (((wreg & 0x0f) + 0x06) & 0x0f);
			highNibble = highNibble + 0x10;
		}
		else lowNibble = wreg & 0x0f;
		if(((wreg & 0xf0) > 0x90) || pic18.dataMem.status.getBit(0) == 1){
			highNibble = highNibble + (wreg & 0xf0) + 0x60;
			pic18.dataMem.status.setBit(0);
		}
		else highNibble = highNibble + (wreg & 0xf0);
		result = highNibble | lowNibble;
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
	}
	
	public void execute(Decf instruction){	
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		result = pic18.dataMem.gpMem[freg].read();
		if(result == 0)
			result = 0xff;
		else
			result--;
		
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Incf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		origValue = result = pic18.dataMem.gpMem[freg].read();
		result++;
		adjustCbit();
		if(result == 0x100)
			result = 0x00;

		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustDCbit(origValue, 1);
		adjustOVbit("", origValue, 1);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Iorlw instruction){
		wreg = pic18.dataMem.wreg.read();
		result = instruction.instruction | wreg;
		pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Iorwf instruction){
		
		//get register address
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
//		System.out.println("in iorwf.movf, freg address is: " + Integer.toHexString(freg));
		
		//perform OR function with wreg value
		result = pic18.dataMem.wreg.read() | pic18.dataMem.gpMem[freg].read();
		
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Movf instruction){
		
		//get register address
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		System.out.println("in alu.movf, freg address is: " + Integer.toHexString(freg));
		
		//find register value
		result = pic18.dataMem.gpMem[freg].read();
		
		//if bit 9 of instruction is high, write result to f register
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Negf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		origValue = pic18.dataMem.gpMem[freg].read();
		result = getTwosComplement(origValue & 0xff);
		adjustDCbit(~origValue, (0x01));
		pic18.dataMem.gpMem[freg].write(result);
		adjustZbit();
		adjustNbit();
		adjustCbit();
	}
	
	public void execute(Rlcf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		result = pic18.dataMem.gpMem[freg].read() << 1;
		carry = pic18.dataMem.status.getBit(0);
		result = result + carry;
		if((result & 0x100) == 0x100)
			pic18.dataMem.status.setBit(0);
		else pic18.dataMem.status.clearBit(0);
		result = result & 0xff;
		
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Rlncf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		result = pic18.dataMem.gpMem[freg].read() << 1;
		if((result & 0x100) == 0x100)
			result = result + 1;
		result = result & 0xff;
		
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Rrcf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		carry = pic18.dataMem.status.getBit(0) << 8;
		result = pic18.dataMem.gpMem[freg].read() | carry;
		if((result & 0x01) == 0x01)
			pic18.dataMem.status.setBit(0);
		else pic18.dataMem.status.clearBit(0);
		result = (result >> 1) & 0xff;
		
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Rrncf instruction){
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		result = pic18.dataMem.gpMem[freg].read();
		result = (((result & 0x01) << 8) | result) >> 1;
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Subfwb instruction){		
		//get wreg value
		wreg = pic18.dataMem.wreg.read();
		
		//get register address
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		origValue = pic18.dataMem.gpMem[freg].read();
		
		//borrow bit = not carry, so if carry bit is not set, add borrow to original register value
		if(pic18.dataMem.status.getBit(0) == 0)
			origValue = origValue + 1;
		
		//get two's complement
		twosComp = getTwosComplement(origValue & 0xff);
		
//		System.out.println("twos comp after adding borrow is: " + Integer.toHexString(twosComp));
		//find sum of wreg and two's complement of f 
		result = wreg + twosComp;
		adjustDCbit(twosComp, wreg);
		adjustOVbit("sub", twosComp, wreg);
		//if bit 9 of instruction is high, write result to f register
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustCbit();
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Sublw instruction){
		//get two's complement of value in wreg
		twosComp = getTwosComplement(pic18.dataMem.wreg.read() & 0xff);
		
		//mask "k" portion of instruction, add to two's complement of wreg value
		result = (instruction.instruction & 0xff) + twosComp;
		
		
		adjustDCbit(twosComp, (instruction.instruction & 0xff));
		adjustOVbit("sub", twosComp, (instruction.instruction & 0xff));
		pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
		adjustCbit();
	}
	
	public void execute(Subwf instruction){
		//get wreg value
		wreg = pic18.dataMem.wreg.read();
		
		//get register address
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		
		//get two's complement
		twosComp = getTwosComplement(wreg & 0xff);
		
//		System.out.println("twos comp is: " + Integer.toHexString(twosComp));
		//find sum of register f and two's complement of wreg
		result = pic18.dataMem.gpMem[freg].read() + twosComp;
		System.out.println("result is: " + Integer.toHexString(result));
		adjustDCbit(twosComp, pic18.dataMem.gpMem[freg].read());
		adjustOVbit("sub", twosComp, pic18.dataMem.gpMem[freg].read());
		//if bit 9 of instruction is high, write result to f register
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustCbit();
		adjustZbit();
		adjustNbit();
	}
	
	//f - wreg - borrow
	public void execute(Subwfb instruction){		
		//get wreg value
		wreg = pic18.dataMem.wreg.read();
		
		//get register address
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
		
		//borrow bit = not carry, so if carry bit is not set, add borrow to original register value
		if(pic18.dataMem.status.getBit(0) == 0)
			wreg = wreg + 1;
		
		//get two's complement
		twosComp = getTwosComplement(wreg & 0xff);
		
//		System.out.println("twos comp after adding borrow is: " + Integer.toHexString(twosComp));
		//find sum of wreg and two's complement of f 
		result = pic18.dataMem.gpMem[freg].read() + twosComp;
		adjustDCbit(twosComp, pic18.dataMem.gpMem[freg].read());
		adjustOVbit("sub", twosComp, pic18.dataMem.gpMem[freg].read());
		//if bit 9 of instruction is high, write result to f register
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustCbit();
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Xorlw instruction){
		wreg = pic18.dataMem.wreg.read();
		result = instruction.instruction ^ wreg;
		pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	public void execute(Xorwf instruction){
		//get register address
		freg = pic18.dataMem.getRegAddress(instruction.instruction);
//		System.out.println("in iorwf.movf, freg address is: " + Integer.toHexString(freg));
		
		//perform XOR function with wreg value
		result = pic18.dataMem.wreg.read() ^ pic18.dataMem.gpMem[freg].read();
		
		//if bit 9 of instruction is high, write result to f register
		//else write to wreg
		if((instruction.instruction & 0x200) == 0x200) 
			pic18.dataMem.gpMem[freg].write(result);
		else pic18.dataMem.wreg.write(result);
		adjustZbit();
		adjustNbit();
	}
	
	private void adjustZbit(){
		if((result & 0x00ff) == 0){
			pic18.dataMem.status.setBit(2);
			pic18.dataMem.status.clearBit(4);
		}
		else{
			pic18.dataMem.status.clearBit(2);
		}
		//System.out.println("status reg is: " + Integer.toBinaryString(pic18.dataMem.status.read()));
	}
	
	private void adjustNbit(){
		if((result & 0x80) == 0x80){
			pic18.dataMem.status.setBit(4);
			pic18.dataMem.status.clearBit(2);
		}
		else pic18.dataMem.status.clearBit(4);
	}
	
	private void adjustCbit(){
		if(result > 0xff){
			pic18.dataMem.status.setBit(0);
		}
		else if(pic18.dataMem.status.getBit(0) == 1){
			pic18.dataMem.status.clearBit(0);
		}
	}
	
	private void adjustDCbit(int arg1, int arg2){
		dc = (((arg1 & 0x0f) + (arg2 & 0x0f)) & 0x10) == 0x10;
//		System.out.println("arg1 & 0x0f: " + Integer.toBinaryString((arg1 & 0x0f)));
//		System.out.println("arg2 & 0x0f: " + Integer.toBinaryString((arg2 & 0x0f)));
		if(dc == true)
			pic18.dataMem.status.setBit(1);
		else pic18.dataMem.status.clearBit(1);
		

	}
	
	private void adjustOVbit(String operation, int arg1, int arg2){
//		System.out.println("arg1 & 0x7f: " + Integer.toBinaryString((arg1 & 0x7f)));
//		System.out.println("arg2 & 0x7f: " + Integer.toBinaryString((arg2 & 0x7f)));
		ov = (((arg1 & 0x7f) + (arg2 & 0x7f)) & 0x80) == 0x80;
		if(operation.equals("add") || operation.equals("sub")){
			if(ov)
				pic18.dataMem.status.setBit(3);
			else pic18.dataMem.status.clearBit(3);
		}
		//Clear ov bit in case of subtraction because manual is not clear
		//about how ov bit is affected during subtraction
		else pic18.dataMem.status.clearBit(3);
	}
	
	public int getTwosComplement(int arg){
//		System.out.println("in Alu.getTC arg is: " + Integer.toHexString(arg) + 
//				"~arg is: " + Integer.toHexString(~arg) + 
//				", two's complement is: " + Integer.toHexString((0xff & (~arg)) + 1));
		return (0xff & (~arg)) + 1;
	}
}


