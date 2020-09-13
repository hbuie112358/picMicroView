package picmicroview.model;

import java.util.HashSet;

class Alu {

	private HashSet<Integer> indfs;
	
	Alu(){

		//indfs is hashset of addresses that are INDFx registers: INDF, PLUSW,
		//POSTINC, PREINC, POSTDEC registers, when addressed, are supposed to 
		//disregard banked addressing. This causes a hole in all banks within
		//the ranges below. See addwf.asm or addwf.lst for more complete explanation.
		indfs = new HashSet<>();
		int i;
		for(i = 0x0eb; i < 0x0f0; i++)	//add INDF0's to hashset
			indfs.add(i);
		for(i = 0x0e3; i < 0x0e8; i++)	//add INDF1's to hashset
			indfs.add(i);
		for(i = 0x0db; i < 0x0e0; i++)	//add INDF2's to hashset
			indfs.add(i);
		//Add FSR's as well
		indfs.add(0x0e9);	//FSR0L
		indfs.add(0x0ea);	//FSR0H
		indfs.add(0x0e1);	//FSR1L
		indfs.add(0x0e2);	//FSR1H
		indfs.add(0x0d9);	//FSR2L
		indfs.add(0x0da);	//FSR2H
	}
	
	static int getTwosComplement(int arg){
//		System.out.println("in Alu.getTC arg is: " + Integer.toHexString(arg) + 
//				"~arg is: " + Integer.toHexString(~arg) + 
//				", two's complement is: " + Integer.toHexString((0xff & (~arg)) + 1));
		return (0xff & (~arg)) + 1;
	}
}


