package model;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import controller.ReplyController;

public class Pic18F452 implements SetState{

	private HashMap<Integer, ArrayList<Integer> > program;
	private final static int DATA_MEMORY_SIZE = 65536;
	
	//HashSet of register addresses that were written to during execution
	//of an instruction. Changes are monitored during step mode but not 
	//during run mode, which defines the difference between step and run modes.
	HashSet<Integer> changes;
	
	private Pic18F452State picState;
	private Pic18F452State runState;
	private Pic18F452State stepState;
	
	private TestMain testMain;
	
	int instruction = 0;
	
	int[] programMem;
	Alu alu;
	Timer timer0;
	Clock clock;
	ProgramCounter pc;
	DataMemory dataMem;
	Stack stack;
	ReplyController repCont;
	
	//instructions:
	Addlw addlw;
	Addwf addwf;
	Addwfc addwfc;
	Andlw andlw;
	Andwf andwf;
	Bc bc;
	Bcf bcf;
	Bn bn;
	Bnc bnc;
	Bnn bnn;	///10
	Bnov bnov;
	Bnz bnz;
	Bov bov;
	Bra bra;
	Bsf bsf;
	Btfsc btfsc;
	Btfss btfss;
	Btg btg;
	Bz bz;
	Clrf clrf;	///20
	Comf comf;
	Cpfseq cpfseq;
	Cpfsgt	cpfsgt;
	Cpfslt	cpfslt;
	Daw daw;
	Dcfsnz	dcfsnz;
	Decf decf;
	Decfsz decfsz;
	Goto GOTO;
	Incf incf;	///30
	Incfsz incfsz;
	Infsnz infsnz;
	Iorlw iorlw;
	Iorwf iorwf;
	Lfsr lfsr;
	Movf movf;
	Movff movff;
	Movlb movlb;
	Movlw movlw;
	Movwf movwf;	///40
	Mullw mullw;
	Mulwf mulwf;
	Negf negf;
	Nop nop;
	Rcall rcall;
	Return Return;
	Rlcf rlcf;
	Rlncf rlncf;
	Rrcf rrcf;
	Rrncf rrncf;	///50
	Setf setf;
	Subfwb subfwb;
	Sublw sublw;	
	Subwf subwf;
	Subwfb subwfb;
	Swapf swapf;	
	Tstfsz tstfsz;
	Xorlw xorlw;	
	Xorwf xorwf;	///////59
	
	public Pic18F452(ReplyController repCont){
		
		this.repCont = repCont;
		changes = new HashSet<Integer>();
		runState = new RunState(this);
		stepState = new StepState(this);
		picState = stepState;
		programMem = new int[DATA_MEMORY_SIZE];
		dataMem = new DataMemory(this);
		initPic();
		clock = new Clock(this);
		alu = new Alu(this);
		stack = new Stack(this);
		testMain = new TestMain(this);
		
		//create instruction objects:
		addlw = new Addlw(0, this, "addlw");
		addwf = new Addwf(0, this, "addwf");
		addwfc = new Addwfc(0, this, "addwfc");
		andlw = new Andlw(0, this, "andlw");
		andwf = new Andwf(0, this, "andwf");
		bc = new Bc(0, this, "bc");
		bcf = new Bcf(0, this, "bcf");
		bn = new Bn(0, this, "bn");
		bnc = new Bnc(0, this, "bnc");
		bnn = new Bnn(0, this, "bnn");
		bnov = new Bnov(0, this, "bnov");
		bnz = new Bnz(0, this, "bnz");
		bov = new Bov(0, this, "bnz");
		bra = new Bra(0, this, "bra");
		bsf = new Bsf(0, this, "bsf");
		btfsc = new Btfsc(0, this, "btfsc");
		btfss = new Btfss(0, this, "btfss");
		btg = new Btg(0, this, "btg");
		bz = new Bz(0, this, "bz");
		clrf = new Clrf(0, this, "clrf");
		comf = new Comf(0, this, "comf");
		cpfseq = new Cpfseq(0, this, "cpfseq");
		cpfsgt = new Cpfsgt(0, this, "cpfsgt");
		cpfslt = new Cpfslt(0, this, "cpfslt");
		daw = new Daw(0, this, "daw");
		dcfsnz = new Dcfsnz(0, this, "dcfsnz");
		decf = new Decf(0, this, "decf");
		decfsz = new Decfsz(0, this, "decfsz");
		GOTO = new Goto(0, 0, this, "goto");
		incf = new Incf(0, this, "incf");
		incfsz = new Incfsz(0, this, "incfsz");
		infsnz = new Infsnz(0, this, "infsnz");
		iorlw = new Iorlw(0, this, "iorlw");
		iorwf = new Iorwf(0, this, "iorwf");
		lfsr = new Lfsr(0, this, "lfsr");
		movf = new Movf(0, this, "movf");
		movff = new Movff(0, this, "movff");
		movlb = new Movlb(0, this, "movlb");
		movlw = new Movlw(0, this, "movlw");
		movwf = new Movwf(0, this, "movwf");
		mullw = new Mullw(0, this, "mullw");
		mulwf = new Mulwf(0, this, "mulwf");
		negf = new Negf(0, this, "negf");
		nop = new Nop(0, this, "nop");
		rcall = new Rcall(0, this, "rcall");
		Return = new Return(0, this, "return");
		rlcf = new Rlcf(0, this, "rlcf");
		rlncf = new Rlncf(0, this, "rlncf");
		rrcf = new Rrcf(0, this, "rrcf");
		rrncf = new Rrncf(0, this, "rrncf");
		setf = new Setf(0, this, "setf");
		subfwb = new Subfwb(0, this, "subfwb");
		sublw = new Sublw(0, this, "sublw");
		subwf = new Subwf(0, this, "subwf");
		subwfb = new Subwfb(0, this, "subwfb");
		swapf = new Swapf(0, this, "swapf");
		tstfsz = new Tstfsz(0, this, "tstfsz");
		xorlw = new Xorlw(0, this, "xorlw");
		xorwf = new Xorwf(0, this, "xorwf");
	}
	
	/*
	 * *****************************************************************************
	 * Public functions, external interface section
	 * *****************************************************************************
	 */
	
	public void initPic(){
		pc = new ProgramCounter(this);
		timer0 = new Timer(this, "Timer0");
	}
	
	public int getPc(){
		return pc.getPc();
	}
	
	public void stop(){
		picState.stop();
	}
	
	public void start(){
		setRunState();
		clock.start();
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
	
	public void executeTest(){
		testMain.execute();
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
		ArrayList<Integer> list;
		int address;
		for(Integer key : keys){
			list = program.get(key);
			address = key.intValue();
			for (int value : list){
				programMem[address] = value;
				address++;
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
	
	public void run(){
		picState.run();
	}
	
	public void setRunState(){
		picState = runState;
		for(int i = 0; i < dataMem.gpMem.length; i++)
			dataMem.gpMem[i].setRunState();
	}
	
	public void setStepState(){
		picState = stepState;
		for(int i = 0; i < dataMem.gpMem.length; i++){
			dataMem.gpMem[i].setStepState();
//			System.out.println("in Pic.setStepState, state for " + Integer.toHexString(i)+ "is: " + dataMem.gpMem[i].registerState);
		}
	}
	
	public boolean isRunning(){
		if(picState.getState() == State.RUN)
			return true;
		else return false;
	}
	
	public HashSet<Integer> getChanges(){
		return changes;
	}
	
	public void runInstruction(){

//		changes.clear();
		int i = 0, hByteHnibble, hByteLnibble, hByte, lByte, nextWord;
			
		//fetch
		instruction = pc.getWord();//increments program counter
			
		hByteHnibble = instruction & 0xf000;
		hByteLnibble = instruction & 0x0f00;
		lByte = instruction & 0x00ff;
		hByte = instruction & 0xff00;

		//decode
//		System.out.println("instruction is: " + Integer.toHexString(instruction));

		if(hByteHnibble == 0x0000){
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
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				decf.initialize(instruction);
				decf.execute();
			}
			else if (hByteLnibble == 0x0100){
				movlb.initialize(instruction);
				movlb.execute();
			}
			else if((hByteLnibble == 0x0200) || (hByteLnibble == 0x0300)){
				mulwf.initialize(instruction);
				mulwf.execute();
			}
			else if(hByteLnibble == 0x0800){
				sublw.initialize(instruction);
				sublw.execute();
			}
			else if(hByteLnibble == 0x0900){
				iorlw.initialize(instruction);
				iorlw.execute();
			}
			else if(hByteLnibble == 0x0a00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				xorlw.initialize(instruction);
				xorlw.execute();
			}
			else if(hByteLnibble == 0x0b00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				andlw.initialize(instruction);
				andlw.execute();
			}	
			else if (hByteLnibble == 0x0d00){
				mullw.initialize(instruction);
				mullw.execute();
			}
			else if(hByteLnibble == 0x0e00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				movlw.initialize(instruction);
				movlw.execute();
			}	
			else if((hByteLnibble == 0x0f00)){
				addlw.initialize(instruction);///////////////////
				addlw.execute();
			}
			else if((hByteLnibble == 0x0000) && lByte == 0x07){
				daw.initialize(instruction);
				daw.execute();
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
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				andwf.initialize(instruction);
				andwf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				xorwf.initialize(instruction);
				xorwf.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				comf.initialize(instruction);
				comf.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble ==	0x2000){
//			System.out.println("command high nibble has been decoded as 0x2000");				
			if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
//				System.out.println("in inner case addwf " + Integer.toHexString(hByteLnibble));
				addwf.initialize(instruction);
				addwf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				incf.initialize(instruction);
				incf.execute();
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
		else if(hByteHnibble == 0x3000){
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				rrcf.initialize(instruction);
				rrcf.execute();
			}
			if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				rlcf.initialize(instruction);
				rlcf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				swapf.initialize(instruction);
				swapf.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				incfsz.initialize(instruction);
				incfsz.execute();
			}
		}
		else if(hByteHnibble == 0x4000){
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				rrncf.initialize(instruction);
				rrncf.execute();
			}
			if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				rlncf.initialize(instruction);
				rlncf.execute();
			}
			if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				infsnz.initialize(instruction);
				infsnz.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				dcfsnz.initialize(instruction);
				dcfsnz.execute();
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
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				subfwb.initialize(instruction);
				subfwb.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				subwfb.initialize(instruction);
				subwfb.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				subwf.initialize(instruction);
				subwf.execute();
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
			else if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100)){
				cpfslt.initialize(instruction);
				cpfslt.execute();
			}
			else if((hByteLnibble == 0x0200) || (hByteLnibble == 0x0300)){
				cpfseq.initialize(instruction);
				cpfseq.execute();
			}
			else if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500)){
				cpfsgt.initialize(instruction);
				cpfsgt.execute();
			}
			else if((hByteLnibble == 0x0600) || (hByteLnibble == 0x0700)){
				tstfsz.initialize(instruction);
				tstfsz.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900)){
				setf.initialize(instruction);
				setf.execute();
			}
			else if((hByteLnibble == 0x0a00) || (hByteLnibble == 0x0b00)){
				clrf.initialize(instruction);
				clrf.execute();
			}
			
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00)){
				negf.initialize(instruction);
				negf.execute();
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
		else if (hByteHnibble == 0xb000){
			btfsc.initialize(instruction);
			btfsc.execute();
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
		
		else if (hByteHnibble == 0xe000){
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
			else if(hByteLnibble == 0x0400){
				bov.initialize(instruction);
				bov.execute();
			}
			else if(hByteLnibble == 0x0500){
				bnov.initialize(instruction);
				bnov.execute();
			}
			else if(hByteLnibble == 0x0600){
				bn.initialize(instruction);
				bn.execute();
			}
			else if(hByteLnibble == 0x0700){
				bnn.initialize(instruction);
				bnn.execute();
			}
			else if(hByteLnibble == 0x0100){
				bnz.initialize(instruction);
				bnz.execute();
			}
			else if(hByteLnibble == 0x0000){
				bz.initialize(instruction);
				bz.execute();
			}
			else if(hByteLnibble == 0x0200){
				bc.initialize(instruction);
				bc.execute();
			}
			else if(hByteLnibble == 0x0300){
				bnc.initialize(instruction);
				bnc.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble == 0xf000){
				//System.out.println("command low nibble has been decoded as 0X0000");
				nop.initialize(instruction);
				nop.execute();
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