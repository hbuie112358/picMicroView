package net.sourceforge.picmicroview.model;

public class RunState implements Pic18F452State {

	Pic18F452 pic18;
	
	public RunState(Pic18F452 pic18) {
		this.pic18 = pic18;
	}

	@Override
	public void run() {
		//System.out.println("in RunState, changes.size(): " + pic18.changes.size());
		pic18.runInstruction();
////		changes.clear();
//		int instruction, i = 0, hByteHnibble, hByteLnibble, hByte, lByte, nextWord;
//			
//		//fetch
//		instruction = pic18.pc.getWord();//increments program counter
//			
//		hByteHnibble = instruction & 0xf000;
//		hByteLnibble = instruction & 0x0f00;
//		lByte = instruction & 0x00ff;
//		hByte = instruction & 0xff00;
//
//		//decode
////		System.out.println("instruction is: " + Integer.toHexString(instruction));
//		if (hByteHnibble == 0xe000){
//			//System.out.println("command high nibble has been decoded as 0xE000");
//			if(hByteLnibble == 0x0f00){
//				//System.out.println("command low nibble has been decoded as 0X0F00");
//				//pc.increment();
//				nextWord = pic18.pc.getWord();
////				System.out.println("in Pic18, nextWord is: " + Integer.toHexString(nextWord));
//				//Goto GOTO = new Goto(instruction, nextWord, this, "goto");
//				pic18.GOTO.initialize(instruction, nextWord);
//				pic18.GOTO.execute();
//			}
//			else if(hByteLnibble == 0x0e00){
//				nextWord = pic18.pc.getWord();
//				pic18.lfsr.initialize(instruction, nextWord);
//				pic18.lfsr.execute();
//			}
//			else if(hByteLnibble == 0x0100){
//				pic18.bnz.initialize(instruction);
//				pic18.bnz.execute();
//			}
//			else if(hByteLnibble == 0x000){
//				pic18.bz.initialize(instruction);
//				pic18.bz.execute();
//			}
//			else{
//				System.out.println("instruction not implemented");
//				System.exit(0);
//			}
//		}
//		else if(hByteHnibble == 0x0000){
//			//System.out.println("command high nibble has been decoded as 0x0000");
//			if(instruction == 0){
//				//System.out.println("command low nibble has been decoded as 0X0000");
//				pic18.nop.initialize(instruction);
//				pic18.nop.execute();
//			}
//			else if ((hByteLnibble == 0x0000) && ((lByte == 0x0012) || (lByte == 0x0013))){
//				pic18.Return.initialize(instruction);
//				pic18.Return.execute();
//			}
//			else if(hByteLnibble == 0x0b00){
//				//System.out.println("command low nibble has been decoded as 0X0E00");
//				pic18.andlw.initialize(instruction);
//				pic18.andlw.execute();
//			}	
//			else if(hByteLnibble == 0x0e00){
//				//System.out.println("command low nibble has been decoded as 0X0E00");
//				pic18.movlw.initialize(instruction);
//				pic18.movlw.execute();
//			}	
//			else if ((hByteLnibble == 0x0700) || (hByteLnibble == 0x0600) || (hByteLnibble == 0x0500) 
//					|| (hByteLnibble == 0x0400)){
//				pic18.decf.initialize(instruction);
//				pic18.decf.execute();
//			}
//			
//			else if (hByteLnibble == 0x0100){
//				pic18.movlb.initialize(instruction);
//				pic18.movlb.execute();
//			}
//			else{
//				System.out.println("instruction not implemented");
//				System.exit(0);
//			}
//		}	
//		else if(hByteHnibble ==	0x1000){
//			//System.out.println("command high nibble has been decoded as 0x2000");				
//			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
//					|| (hByteLnibble == 0x0300)){
//				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
//				pic18.iorwf.initialize(instruction);
//				pic18.iorwf.execute();
//			}
//			else{
//				System.out.println("instruction not implemented");
//				System.exit(0);
//			}
//		}
//		else if(hByteHnibble ==	0x2000){
////			System.out.println("command high nibble has been decoded as 0x2000");				
//			if((hByteLnibble == 0x0700) || (hByteLnibble == 0x0600) || (hByteLnibble == 0x0500) 
//					|| (hByteLnibble == 0x0400)){
////				System.out.println("in inner case addwf " + Integer.toHexString(hByteLnibble));
//				pic18.addwf.initialize(instruction);
//				pic18.addwf.execute();
//			}
//			else if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
//					|| (hByteLnibble == 0x0300)){
////				System.out.println("in inner case addwfc " + Integer.toHexString(hByteLnibble));
//				pic18.addwfc.initialize(instruction);
//				pic18.addwfc.execute();
//			}
//			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
//					|| (hByteLnibble == 0x0f00)){
//				pic18.decfsz.initialize(instruction);
//				pic18.decfsz.execute();
//			}
//			else{
//				System.out.println("instruction not implemented");
//				System.exit(0);
//			}
//		}
//		else if(hByteHnibble ==	0x5000){
//			//System.out.println("command high nibble has been decoded as 0x2000");				
//			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
//					|| (hByteLnibble == 0x0300)){
//				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
//				pic18.movf.initialize(instruction);
//				pic18.movf.execute();
//			}
//			else{
//				System.out.println("instruction not implemented");
//				System.exit(0);
//			}
//		}
//		else if(hByteHnibble == 0x6000){
//			//System.out.println("command high nibble has been decoded as 0x6000");
//			if((hByteLnibble == 0x0E00) || (hByteLnibble == 0x0F00)){
//				//System.out.println("command low nibble has been decoded as 0X0E00");
//				pic18.movwf.initialize(instruction);
//				pic18.movwf.execute();
//			}
//			else{
//				System.out.println("instruction not implemented");
//				System.exit(0);
//			}
//		}
//		else if(hByteHnibble ==	0x7000){
//			//System.out.println("command high nibble has been decoded as 0x2000");				
//			pic18.btg.initialize(instruction);
//			pic18.btg.execute();
//				
//		}
//		else if(hByteHnibble ==	0x8000){
//			//System.out.println("command high nibble has been decoded as 0x2000");				
//			pic18.bsf.initialize(instruction);
//			pic18.bsf.execute();
//		}
//		else if(hByteHnibble ==	0x9000){
//			//System.out.println("command high nibble has been decoded as 0x2000");				
//			pic18.bcf.initialize(instruction);
//			pic18.bcf.execute();
//		}
//		else if(hByteHnibble ==	0xa000){
//			//System.out.println("command high nibble has been decoded as 0x2000");				
//			pic18.btfss.initialize(instruction);
//			pic18.btfss.execute();
//				
//		}
//		else if(hByteHnibble ==	0xc000){
//			//System.out.println("command high nibble has been decoded as 0x2000");				
//			pic18.movff.initialize(instruction);
//			pic18.movff.execute();
//		}
//		else if(hByteHnibble == 0xd000){
//			//System.out.println("command high nibble has been decoded as 0xd000");
//			if((hByteLnibble & 0x0800) == 0x0000){
//				//System.out.println("command low nibble has been decoded as 0x0000");
//				pic18.bra.initialize(instruction);
//				pic18.bra.execute();
//			}
//			else if((hByteLnibble & 0x0800) == 0x0800){
//				//System.out.println("command low nibble has been decoded as 0X0800");
//				pic18.rcall.initialize(instruction);
//				pic18.rcall.execute();
//			}
//			else{
//				System.out.println("instruction not implemented");
//				System.exit(0);
//			}
//		}	
//		else{
//			System.out.println("instruction not implemented " + Integer.toHexString(instruction));
//			System.exit(0);
//		}
//		i++; 
//		//System.out.println("wreg after " + Integer.toHexString(instruction) + " is " + dataMem.wreg.read());
//		//printStatusReg();
//	
//		//System.out.println("ran instruction: " + Integer.toHexString(instruction));
//	}
	}
}
