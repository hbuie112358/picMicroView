package net.sourceforge.picmicroview.model;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.picmicroview.controller.ReplyController;

public class Pic18F452{

	private HashMap<Integer, ArrayList<Integer> > program;
	private int DATA_MEMORY_SIZE = 65536;
	
	int[] programMem;
	Alu alu;
	Timer timer0;
	Clock clock;
	ProgramCounter pc;
	DataMemory dataMem;
	Stack stack;
	ReplyController repCont;
	
	//instructions:
	Goto GOTO;
	Movlb movlb;
	Movlw movlw;
	Movwf movwf;
	Decf decf;
	Decfsz decfsz;
	Bz bz;
	Rcall rcall;
	Bnz bnz;
	Return Return;
	Btg btg;
	Andlw andlw;
	Nop nop;
	Iorwf iorwf;
	Addwf addwf;
	Addwfc addwfc;
	Lfsr lfsr;
	Movf movf;
	Bsf bsf;
	Bcf bcf;
	Btfss btfss;
	Movff movff;
	Bra bra;
	
	public Pic18F452(ReplyController repCont){
		
		this.repCont = repCont;
		programMem = new int[DATA_MEMORY_SIZE];
		//programMem = new int[4194304];
		//int [] testMem = new int [524288];
		dataMem = new DataMemory(this);
		initPic();
		clock = new Clock(this);
		alu = new Alu(this);
		stack = new Stack(this);
		
		//create instruction objects:
		addwf = new Addwf(0, this, "addwf");
		addwfc = new Addwfc(0, this, "addwfc");
		andlw = new Andlw(0, this, "andlw");
		bcf = new Bcf(0, this, "bcf");
		bnz = new Bnz(0, this, "bnz");
		bra = new Bra(0, this, "bra");
		bsf = new Bsf(0, this, "bsf");
		btfss = new Btfss(0, this, "btfss");
		btg = new Btg(0, this, "btg");
		bz = new Bz(0, this, "bz");
		decf = new Decf(0, this, "decf");
		decfsz = new Decfsz(0, this, "decfsz");
		GOTO = new Goto(0, 0, this, "goto");
		iorwf = new Iorwf(0, this, "iorwf");
		lfsr = new Lfsr(0, this, "lfsr");
		movf = new Movf(0, this, "movf");
		movff = new Movff(0, this, "movff");
		movlb = new Movlb(0, this, "movlb");
		movlw = new Movlw(0, this, "movlw");
		movwf = new Movwf(0, this, "movwf");
		nop = new Nop(0, this, "nop");
		rcall = new Rcall(0, this, "rcall");
		Return = new Return(0, this, "return");
	}
	
	/*
	 * *****************************************************************************
	 * Public functions, external interface section
	 * *****************************************************************************
	 */
	
	public void initPic(){
	pc = new ProgramCounter(this);
	timer0 = new Timer(this, "Timer0");
	//clock = new Clock(this);
	}
	
	public Clock getClock(){
		return clock;
	}
	
	public int getPc(){
		return pc.getPc();
	}
	
	public int getNextInstructionAddress(){
		return pc.getNextAddress();
	}
	
	public ArrayList<Integer> getDataMemory(){
		ArrayList<Integer> dm = new ArrayList<Integer>();
		for(int i = 0; i < dataMem.gpMem.length; i++){
			dm.add(dataMem.gpMem[i].getContents());
		}
		return dm;
	}
	
	public int getWreg(){
		return dataMem.wreg.read();
	}
	
	public ArrayList<Integer> getPgmMemory(){
		ArrayList<Integer> pm = new ArrayList<Integer>();
		for(int i = 0; i < programMem.length; i++){
			pm.add((Integer)programMem[i]);
		}
		return pm;
	}

	/**
	 * loadHexFile() - makes Parser object, parser returns hashmap of <integer, arrayList of integers>. 
	 * The integer key is the memory offset for the associated arrayList value in the key/value pair.
	 * loadHexFile() then loads hashmap into program memory array
	 * @param fileName
	 */

	public void loadHexFile(String fileName){
		clearPgmMem();
		Parser parser = new Parser(new Scanner(fileName));
		program = parser.getProgram();
		//printProgram();

		//load program into memory
		Set<Integer> keys = program.keySet();
		int key;
		ArrayList<Integer> list = new ArrayList<Integer>();
		Iterator<Integer> iter = keys.iterator();
		while(iter.hasNext()){
			key = (int)iter.next();
			list = program.get(key);
			for (int item : list){
				programMem[key] = item;
				key++;
			}
		}
		//int limit = 500;
		//printPgmMem(limit);
		//System.out.println("finished loading file: " + fileName);
		repCont.setTitle(fileName);
		pc.setPc(0);
	}
	
	
	/** run():
	 * 
	 * fetch/decode: combine bytes from consecutive memory locations to re-assemble instruction.
	 * Mask off high word from high byte, filter based on that. Then mask off low word
	 * from high byte, sub-filter based on that. 
	 * execute: Instructions are executed in constructor of instruction class.
	 * Note: 2 word instructions are only sent what is necessary for the instruction. For
	 * instance, the goto instruction is only sent the low byte of the second word. This will
	 * limit the program memory address space to 64K, but that is the max without external
	 * memory anyway. If the high byte of the second word is needed for decoding, it will be 
	 * sent to the instruction class by the run() function. This is so that the instruction
	 * does not need to access the program memory array.
	 */
	
	@SuppressWarnings("unused")
	public void run(){

//		changes.clear();
		int instruction, i = 0, hByteHnibble, hByteLnibble, hByte, lByte, nextWord;
			
		//fetch
		instruction = pc.getWord();//increments program counter
			
		hByteHnibble = instruction & 0xf000;
		hByteLnibble = instruction & 0x0f00;
		lByte = instruction & 0x00ff;
		hByte = instruction & 0xff00;

		//decode
//		System.out.println("instruction is: " + Integer.toHexString(instruction));
		if (hByteHnibble == 0xe000){
			//System.out.println("command high nibble has been decoded as 0xE000");
			if(hByteLnibble == 0x0f00){
				//System.out.println("command low nibble has been decoded as 0X0F00");
				//pc.increment();
				nextWord = pc.getWord();
//				System.out.println("in Pic18, nextWord is: " + Integer.toHexString(nextWord));
				//Goto GOTO = new Goto(instruction, nextWord, this, "goto");
				GOTO.initialize(instruction, nextWord);
				GOTO.execute();
			}
			else if(hByteLnibble == 0x0e00){
				nextWord = pc.getWord();
				lfsr.initialize(instruction, nextWord);
				lfsr.execute();
			}
			else if(hByteLnibble == 0x0100){
				bnz.initialize(instruction);
				bnz.execute();
			}
			else if(hByteLnibble == 0x000){
				bz.initialize(instruction);
				bz.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble == 0x0000){
			//System.out.println("command high nibble has been decoded as 0x0000");
			if(instruction == 0){
				//System.out.println("command low nibble has been decoded as 0X0000");
				nop.initialize(instruction);
				nop.execute();
			}
			else if ((hByteLnibble == 0x0000) && ((lByte == 0x0012) || (lByte == 0x0013))){
				Return.initialize(instruction);
				Return.execute();
			}
			else if(hByteLnibble == 0x0b00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				andlw.initialize(instruction);
				andlw.execute();
			}	
			else if(hByteLnibble == 0x0e00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				movlw.initialize(instruction);
				movlw.execute();
			}	
			else if ((hByteLnibble == 0x0700) || (hByteLnibble == 0x0600) || (hByteLnibble == 0x0500) 
					|| (hByteLnibble == 0x0400)){
				decf.initialize(instruction);
				decf.execute();
			}
			
			else if (hByteLnibble == 0x0100){
				movlb.initialize(instruction);
				movlb.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}	
		else if(hByteHnibble ==	0x1000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				iorwf.initialize(instruction);
				iorwf.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble ==	0x2000){
//			System.out.println("command high nibble has been decoded as 0x2000");				
			if((hByteLnibble == 0x0700) || (hByteLnibble == 0x0600) || (hByteLnibble == 0x0500) 
					|| (hByteLnibble == 0x0400)){
//				System.out.println("in inner case addwf " + Integer.toHexString(hByteLnibble));
				addwf.initialize(instruction);
				addwf.execute();
			}
			else if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
//				System.out.println("in inner case addwfc " + Integer.toHexString(hByteLnibble));
				addwfc.initialize(instruction);
				addwfc.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				decfsz.initialize(instruction);
				decfsz.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble ==	0x5000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				movf.initialize(instruction);
				movf.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble == 0x6000){
			//System.out.println("command high nibble has been decoded as 0x6000");
			if((hByteLnibble == 0x0E00) || (hByteLnibble == 0x0F00)){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				movwf.initialize(instruction);
				movwf.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble ==	0x7000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				btg.initialize(instruction);
				btg.execute();
				
		}
		else if(hByteHnibble ==	0x8000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				bsf.initialize(instruction);
				bsf.execute();
		}
		else if(hByteHnibble ==	0x9000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				bcf.initialize(instruction);
				bcf.execute();
		}
		else if(hByteHnibble ==	0xa000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				btfss.initialize(instruction);
				btfss.execute();
				
		}
		else if(hByteHnibble ==	0xc000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				movff.initialize(instruction);
				movff.execute();
		}
		else if(hByteHnibble == 0xd000){
			//System.out.println("command high nibble has been decoded as 0xd000");
			if((hByteLnibble & 0x0800) == 0x0000){
				//System.out.println("command low nibble has been decoded as 0x0000");
				bra.initialize(instruction);
				bra.execute();
			}
			else if((hByteLnibble & 0x0800) == 0x0800){
				//System.out.println("command low nibble has been decoded as 0X0800");
				rcall.initialize(instruction);
				rcall.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}	
		else{
			System.out.println("instruction not implemented " + Integer.toHexString(instruction));
			System.exit(0);
		}
		i++; 
		//System.out.println("wreg after " + Integer.toHexString(instruction) + " is " + dataMem.wreg.read());
		//printStatusReg();
	
		//System.out.println("ran instruction: " + Integer.toHexString(instruction));
	}
	
	
	
	/*
	 * **********************************************************************
	 * "No access specifier" functions section, available only within package
	 * **********************************************************************
	 */
	
	int getDataMemorySize(){
		return DATA_MEMORY_SIZE;
	}
	
	void setProgramMem(int index, int value){
		programMem[index] = (byte)value;
	}
	
	int getProgramMem(int index){
		return (int)programMem[index];
	}
	
	void printProgram(){
		Set<Integer> keys = program.keySet();
		int key;
		ArrayList<Integer> list = new ArrayList<Integer>();
		Iterator<Integer> iter = keys.iterator();
		while(iter.hasNext()){
			key = (int)iter.next();
			list = program.get(key);
			System.out.println(key + " " + list);
		}		
	}
	
	@SuppressWarnings("unused")
	void clearPgmMem(){
		for ( int item : programMem){
			item = 0;
		}
	}
	
	boolean checkTwoCycle(){
		int nextInstruction = programMem[pc.getPc()];
		if((nextInstruction == 0xef) || nextInstruction == 0xc0){
			return true;
		}
		else return false;
	}
	
	void printStatusReg(){
		System.out.println("status reg is " + dataMem.status.read());
	}
	
	
	void printDataMem(){
		int i = 0;
		while(i < 0xff){
			System.out.println(Integer.toHexString(i) + " " +
				Integer.toHexString(dataMem.gpMem[i].read()));
			i++;
		}
	}
	
	void printPgmMem(int limit){
		for (int i = 0; i < programMem.length; i++){
			if (i < limit || (i > 12287 && i < 12299)) {
				System.out.println(i + " " + programMem[i] + " " + Integer.toHexString(programMem[i] & 0xff));
			}
		}
	}
}
