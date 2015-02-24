package net.sourceforge.picmicroview.model;

import java.util.HashSet;


public class Alu {
	private Pic18F452 pic18;
	private int result;
	private int address;
	private int freg, wreg, carry;
//	private int highByte;
	private int bsrVal = 0;
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
	
	public int getRegisterAddress(int instruction){
		getRegAddress(instruction);
		int returnAddress = freg;
		return returnAddress;
	}
	
	public void execute(Addwf instruction){	
		
		//get wreg value
		wreg = pic18.dataMem.wreg.read();
		getRegAddress(instruction.instruction);		
//		System.out.println("in alu.addwf, freg address is: " + Integer.toHexString(freg));
		
		//find sum of wreg, value in gpMem[freg]
		result = wreg + pic18.dataMem.gpMem[freg].read();		
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
		getRegAddress(instruction.instruction);
		System.out.println("in alu.addwfc, freg address is: " + Integer.toHexString(freg));
		
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
	
	public void execute(Decf instruction){	
		getRegAddress(instruction.instruction);
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
	
	public void execute(Iorwf instruction){
		
		//get register address
		getRegAddress(instruction.instruction);
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
		getRegAddress(instruction.instruction);
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
	
	//Checks to see if "a" bit, which is bit 8, of op code is set. If set, value in
	//bank select register is prepended to lower eight bits. If not set, address is
	//returned with zeros prepended, indicating access memory address.
	private void getRegAddress(int instruction){

//		System.out.println("instruction is: " + Integer.toHexString(instruction) + 
//				", bit 16 is: " + (instruction & 0x100));
		
		address = instruction & 0x00ff;	 //isolate address portion of instruction
		if((instruction & 0x100) == 0x100){	//If "a" bit is set
			if(indfs.contains(address)){	//If address is of an INDF register
//				System.out.println("in alu.addwf.if.indfs.contains, address is: " + address);
				freg = address;		//Set freg to lowest 8 bytes
			}
			else{
				bsrVal = pic18.dataMem.bsr.read();
				freg = (bsrVal << 8) | (instruction & 0xff);
			}
		}
		else freg = address;
		//return freg;
	}
}


