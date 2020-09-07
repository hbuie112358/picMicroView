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
	
	private int instruction = 0;
	
	private int[] programMemory;

	private Alu alu;
	private Timer timer0;
	private Clock clock;
	private ProgramCounter pc;

	private DataMemory dataMem;
	private Stack stack;
	private ReplyController repCont;
	
	//instructions:
	private Addlw addlw; private Addwf addwf; private Addwfc addwfc; private Andlw andlw; private Andwf andwf;
	private Bc bc; private Bcf bcf; private Bn bn; private Bnc bnc; private Bnn bnn; private Bnov bnov; private Bnz bnz;
	private Bov bov; private Bra bra; private Bsf bsf; private Btfsc btfsc; private Btfss btfss; private Btg btg; private Bz bz;
	private Clrf clrf; private Comf comf; private Cpfseq cpfseq; private Cpfsgt	cpfsgt; private Cpfslt	cpfslt;
	private Daw daw; private Dcfsnz	dcfsnz; private Decf decf; private Decfsz decfsz;
	private Goto GOTO;
	private Incf incf; private Incfsz incfsz; private Infsnz infsnz; private Iorlw iorlw; private Iorwf iorwf;
	private Lfsr lfsr;
	private Movf movf; private Movff movff; private Movlb movlb; private Movlw movlw; private Movwf movwf;
	private Mullw mullw; private Mulwf mulwf;
	private Negf negf; private Nop nop;
	private Rcall rcall; private Return Return; private Rlcf rlcf; private Rlncf rlncf; private Rrcf rrcf; private Rrncf rrncf; //50
	private Setf setf; private Subfwb subfwb; private Sublw sublw; private Subwf subwf; private Subwfb subwfb; private Swapf swapf;
	private Tstfsz tstfsz;
	private Xorlw xorlw; private Xorwf xorwf;
	//59
	
	public Pic18F452(ReplyController repCont){
		
		this.repCont = repCont;
		changes = new HashSet<Integer>();
		runState = new RunState(this);
		stepState = new StepState(this);
		picState = stepState;
		programMemory = new int[DATA_MEMORY_SIZE];
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


	public DataMemory getDataMem() {
		return dataMem;
	}
	
	public void initPic(){
		pc = new ProgramCounter(this);
		timer0 = new Timer(this, "Timer0");
	}
	
	public int getPcValue(){
		return pc.getpcValue();
	}

	public void setPcValue(int pcValue){ pc.setpcValue(pcValue);}

	public ProgramCounter getProgramCounter(){return pc;}

	public int getInstruction(){return instruction;}

	public Timer getTimer0(){return timer0;}

	public void stop(){
		picState.stop();
	}

	public Stack getStack(){return stack;}

	public ReplyController getReplyController(){return repCont;}

	public Clock getClock(){return clock;}
	
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
		for(int i = 0; i < programMemory.length; i++){
			pm.add((Integer)programMemory[i]);
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
				programMemory[address] = value;
				address++;
			}
		}

		//int limit = 500;
		//printPgmMem(limit);
		//System.out.println("finished loading file: " + fileName);
		repCont.setTitle(fileName);
		pc.setpcValue(0);
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
				nop = new Nop(instruction, this, "nop");
				nop.execute();
			}
			else if ((hByteLnibble == 0x0000) && ((lByte == 0x0012) || (lByte == 0x0013))){
				Return = new Return(instruction, this, "return");
				Return.execute();
			}
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				decf = new Decf(instruction, this, "decf");
				decf.execute();
			}
			else if (hByteLnibble == 0x0100){
				movlb = new Movlb(instruction, this, "movlb");
				movlb.execute();
			}
			else if((hByteLnibble == 0x0200) || (hByteLnibble == 0x0300)){
				mulwf = new Mulwf(instruction, this, "mulwf");
				mulwf.execute();
			}
			else if(hByteLnibble == 0x0800){
				sublw = new Sublw(instruction, this, "sublw");
				sublw.execute();
			}
			else if(hByteLnibble == 0x0900){
				iorlw = new Iorlw(instruction, this, "iorlw");
				iorlw.execute();
			}
			else if(hByteLnibble == 0x0a00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				xorlw = new Xorlw(instruction, this, "xorlw");
				xorlw.execute();
			}
			else if(hByteLnibble == 0x0b00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				andlw = new Andlw(instruction, this, "andlw");
				andlw.execute();
			}	
			else if (hByteLnibble == 0x0d00){
				mullw = new Mullw(instruction, this, "mullw");
				mullw.execute();
			}
			else if(hByteLnibble == 0x0e00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				movlw = new Movlw(instruction, this, "movlw");
				movlw.execute();
			}	
			else if((hByteLnibble == 0x0f00)){
				addlw  = new Addlw(instruction, this, "addlw");
				addlw.execute();
			}
			else if((hByteLnibble == 0x0000) && lByte == 0x07){
				daw = new Daw(instruction, this, "daw");
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
				iorwf = new Iorwf(instruction, this, "iorwf");
				iorwf.execute();
			}
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				andwf = new Andwf(instruction, this, "andwf");
				andwf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				xorwf = new Xorwf(instruction, this, "xorwf");
				xorwf.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				comf = new Comf(instruction, this, "comf");
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
				addwf = new Addwf(instruction, this, "addwf");
				addwf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				incf = new Incf(instruction, this, "incf");
				incf.execute();
			}
			else if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
//				System.out.println("in inner case addwfc " + Integer.toHexString(hByteLnibble));
				addwfc = new Addwfc(instruction, this, "addwfc");
				addwfc.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				decfsz = new Decfsz(instruction, this, "decfsz");
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
				rrcf = new Rrcf(instruction, this, "rccf");
				rrcf.execute();
			}
			if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				rlcf = new Rlcf(instruction, this, "rlcf");
				rlcf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				swapf = new Swapf(instruction, this, "swapf");
				swapf.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				incfsz = new Incfsz(instruction, this, "incfsz");
				incfsz.execute();
			}
		}
		else if(hByteHnibble == 0x4000){
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				rrncf = new Rrncf(instruction, this, "rrncf");
				rrncf.execute();
			}
			if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				rlncf = new Rlncf(instruction, this, "rlncf");
				rlncf.execute();
			}
			if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				infsnz = new Infsnz(instruction, this, "infsnz");
				infsnz.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				dcfsnz = new Dcfsnz(instruction, this, "dcfsnz");
				dcfsnz.execute();
			}
		}
		else if(hByteHnibble ==	0x5000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				movf = new Movf(instruction, this, "movf");
				movf.execute();
			}
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				subfwb = new Subfwb(instruction, this, "subfwb");
				subfwb.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				subwfb = new Subwfb(instruction, this, "subwfb");
				subwfb.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				subwf = new Subwf(instruction, this, "subwf");
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
				movwf = new Movwf(instruction, this, "movwf");
				movwf.execute();
			}
			else if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100)){
				cpfslt = new Cpfslt(instruction, this, "cpfslt");
				cpfslt.execute();
			}
			else if((hByteLnibble == 0x0200) || (hByteLnibble == 0x0300)){
				cpfseq = new Cpfseq(instruction, this, "cpfseq");
				cpfseq.execute();
			}
			else if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500)){
				cpfsgt = new Cpfsgt(instruction, this, "cpfsgt");
				cpfsgt.execute();
			}
			else if((hByteLnibble == 0x0600) || (hByteLnibble == 0x0700)){
				tstfsz = new Tstfsz(instruction, this, "tstfsz");
				tstfsz.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900)){
				setf = new Setf(instruction, this, "setf");
				setf.execute();
			}
			else if((hByteLnibble == 0x0a00) || (hByteLnibble == 0x0b00)){
				clrf = new Clrf(instruction, this, "clrf");
				clrf.execute();
			}
			
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00)){
				negf = new Negf(instruction, this, "negf");
				negf.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble ==	0x7000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				btg = new Btg(instruction, this, "btg");
				btg.execute();
				
		}
		else if(hByteHnibble ==	0x8000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				bsf = new Bsf(instruction, this, "bsf");
				bsf.execute();
		}
		else if(hByteHnibble ==	0x9000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				bcf = new Bcf(instruction, this, "bcf");
				bcf.execute();
		}
		else if(hByteHnibble ==	0xa000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				btfss = new Btfss(instruction, this, "btfss");
				btfss.execute();
				
		}
		else if (hByteHnibble == 0xb000){
			btfsc = new Btfsc(instruction, this, "btfsc");
			btfsc.execute();
		}
		else if(hByteHnibble ==	0xc000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				movff = new Movff(instruction, this, "movff");
				movff.execute();
		}
		else if(hByteHnibble == 0xd000){
			//System.out.println("command high nibble has been decoded as 0xd000");
			if((hByteLnibble & 0x0800) == 0x0000){
				//System.out.println("command low nibble has been decoded as 0x0000");
				bra = new Bra(instruction, this, "bra");
				bra.execute();
			}
			else if((hByteLnibble & 0x0800) == 0x0800){
				//System.out.println("command low nibble has been decoded as 0X0800");
				rcall = new Rcall(instruction, this, "rcall");
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
				GOTO = new Goto(instruction, nextWord, this, "goto");
				GOTO.execute();
			}
			else if(hByteLnibble == 0x0e00){
				nextWord = pc.getWord();
				lfsr = new Lfsr(nextWord, this, "lfsr");
				lfsr.execute();
			}
			else if(hByteLnibble == 0x0400){
				bov = new Bov(instruction, this, "bov");
				bov.execute();
			}
			else if(hByteLnibble == 0x0500){
				bnov = new Bnov(instruction, this, "bnov");
				bnov.execute();
			}
			else if(hByteLnibble == 0x0600){
				bn = new Bn(instruction, this, "bn");
				bn.execute();
			}
			else if(hByteLnibble == 0x0700){
				bnn = new Bnn(instruction, this, "bnn");
				bnn.execute();
			}
			else if(hByteLnibble == 0x0100){
				bnz = new Bnz(instruction, this, "bnz");
				bnz.execute();
			}
			else if(hByteLnibble == 0x0000){
				bz = new Bz(instruction, this, "bz");
				bz.execute();
			}
			else if(hByteLnibble == 0x0200){
				bc = new Bc(instruction, this, "bc");
				bc.execute();
			}
			else if(hByteLnibble == 0x0300){
				bnc = new Bnc(instruction, this, "bnc");
				bnc.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble == 0xf000){
				//System.out.println("command low nibble has been decoded as 0X0000");
				nop = new Nop(instruction, this, "nop");
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
	
	void setProgramMemory(int index, int value){
		programMemory[index] = (byte)value;
	}
	
	int getProgramMemory(int index){
		return (int)programMemory[index];
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
		for ( int item : programMemory){
			item = 0;
		}
	}
	
	boolean checkTwoCycle(){
		int nextInstruction = programMemory[pc.getpcValue()];
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
		for (int i = 0; i < programMemory.length; i++){
			if (i < limit || (i > 12287 && i < 12299)) {
				System.out.println(i + " " + programMemory[i] + " " + Integer.toHexString(programMemory[i] & 0xff));
			}
		}
	}
}
