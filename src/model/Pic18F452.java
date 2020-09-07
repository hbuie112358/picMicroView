package model;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import controller.ReplyController;

public class Pic18F452 implements SetState{

	private final static int DATA_MEMORY_SIZE = 65536;
	
	//HashSet of register addresses that were written to during execution
	//of an instruction. Changes are monitored during step mode but not 
	//during run mode, which defines the difference between step and run modes.
    final HashSet<Integer> changes;
	
	private Pic18F452State picState;
	private final Pic18F452State runState;
	private final Pic18F452State stepState;
	
	private final TestMain testMain;
	
	private int instruction = 0;
	
	private final int[] programMemory;

	private final Alu alu;
	private Timer timer0;
	private final Clock clock;
	private ProgramCounter pc;

	private final DataMemory dataMem;
	private final Stack stack;
	private final ReplyController repCont;
	
	public Pic18F452(ReplyController repCont){
		
		this.repCont = repCont;
		changes = new HashSet<>();
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
	}
	
	/*
	 * *****************************************************************************
	 * Public functions, external interface section
	 * *****************************************************************************
	 */


	public DataMemory getDataMem() {
		return dataMem;
	}
	
	private void initPic(){
		pc = new ProgramCounter(this);
		timer0 = new Timer(this, "Timer0");
	}
	
	public int getPcValue(){
		return pc.getpcValue();
	}

	public void setPcValue(int pcValue){ pc.setpcValue(pcValue);}

	public ProgramCounter getProgramCounter(){return pc;}

	public int getInstruction(){return instruction;}

	Timer getTimer0(){return timer0;}

	public void stop(){
		picState.stop();
	}

	Stack getStack(){return stack;}

	ReplyController getReplyController(){return repCont;}

	Clock getClock(){return clock;}
	
	public void start(){
		setRunState();
		clock.start();
	}
	
	public ArrayList<Integer> getDataMemory(){
		ArrayList<Integer> dm = new ArrayList<>();
		for(int i = 0; i < dataMem.gpMem.length; i++){
			dm.add(dataMem.gpMem[i].getContents());
		}
		return dm;
	}
	
	public void executeTest(){
		testMain.execute();
	}
	
	public ArrayList<Integer> getPgmMemory(){
		ArrayList<Integer> pm = new ArrayList<>();
        for (int index : programMemory) {
            pm.add(index);
        }
		return pm;
	}

    /**
     loadHexFile() - makes Parser object, parser returns hashmap of <integer, arrayList of integers>.
     * The integer key is the memory offset for the associated arrayList value in the key/value pair.
     * loadHexFile() then loads hashmap into program memory array
     */

	public void loadHexFile(String fileName){
		clearPgmMem();
		Parser parser = new Parser(new Scanner(fileName));
        HashMap<Integer, ArrayList<Integer> > program = parser.getProgram();
		//printProgram();

		//load program into memory
		Set<Integer> keys = program.keySet();
		ArrayList<Integer> list;
		//int address;
		for(Integer address : keys){
			list = program.get(address);

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
        return picState.getState() == State.RUN;
	}
	
	public HashSet<Integer> getChanges(){
		return changes;
	}
	
	void runInstruction(){

//		changes.clear();
		int hByteHnibble, hByteLnibble, lByte, nextWord;
			
		//fetch
		instruction = pc.getWord();//increments program counter
			
		hByteHnibble = instruction & 0xf000;
		hByteLnibble = instruction & 0x0f00;
		lByte = instruction & 0x00ff;

		//decode
//		System.out.println("instruction is: " + Integer.toHexString(instruction));

		if(hByteHnibble == 0x0000){
			//System.out.println("command high nibble has been decoded as 0x0000");
			if(instruction == 0){
				//System.out.println("command low nibble has been decoded as 0X0000");
				Nop nop = new Nop(instruction, this, "nop");
				nop.execute();
			}
			else if ((hByteLnibble == 0x0000) && ((lByte == 0x0012) || (lByte == 0x0013))){
				Return Return = new Return(instruction, this, "return");
				Return.execute();
			}
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				Decf decf = new Decf(instruction, this, "decf");
				decf.execute();
			}
			else if (hByteLnibble == 0x0100){
				Movlb movlb = new Movlb(instruction, this, "movlb");
				movlb.execute();
			}
			else if((hByteLnibble == 0x0200) || (hByteLnibble == 0x0300)){
				Mulwf mulwf = new Mulwf(instruction, this, "mulwf");
				mulwf.execute();
			}
			else if(hByteLnibble == 0x0800){
				Sublw sublw = new Sublw(instruction, this, "sublw");
				sublw.execute();
			}
			else if(hByteLnibble == 0x0900){
				Iorlw iorlw = new Iorlw(instruction, this, "iorlw");
				iorlw.execute();
			}
			else if(hByteLnibble == 0x0a00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				Xorlw xorlw = new Xorlw(instruction, this, "xorlw");
				xorlw.execute();
			}
			else if(hByteLnibble == 0x0b00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				Andlw andlw = new Andlw(instruction, this, "andlw");
				andlw.execute();
			}	
			else if (hByteLnibble == 0x0d00){
				Mullw mullw = new Mullw(instruction, this, "mullw");
				mullw.execute();
			}
			else if(hByteLnibble == 0x0e00){
				//System.out.println("command low nibble has been decoded as 0X0E00");
				Movlw movlw = new Movlw(instruction, this, "movlw");
				movlw.execute();
			}	
			else if((hByteLnibble == 0x0f00)){
				Addlw addlw  = new Addlw(instruction, this, "addlw");
				addlw.execute();
			}
			else if((hByteLnibble == 0x0000) && lByte == 0x07){
				Daw daw = new Daw(instruction, this, "daw");
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
				Iorwf iorwf = new Iorwf(instruction, this, "iorwf");
				iorwf.execute();
			}
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				Andwf andwf = new Andwf(instruction, this, "andwf");
				andwf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				Xorwf xorwf = new Xorwf(instruction, this, "xorwf");
				xorwf.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				Comf comf = new Comf(instruction, this, "comf");
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
				Addwf addwf = new Addwf(instruction, this, "addwf");
				addwf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				Incf incf = new Incf(instruction, this, "incf");
				incf.execute();
			}
			else if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
//				System.out.println("in inner case addwfc " + Integer.toHexString(hByteLnibble));
				Addwfc addwfc = new Addwfc(instruction, this, "addwfc");
				addwfc.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				Decfsz decfsz = new Decfsz(instruction, this, "decfsz");
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
				Rrcf rrcf = new Rrcf(instruction, this, "rccf");
				rrcf.execute();
			}
			if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				Rlcf rlcf = new Rlcf(instruction, this, "rlcf");
				rlcf.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				Swapf swapf = new Swapf(instruction, this, "swapf");
				swapf.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				Incfsz incfsz = new Incfsz(instruction, this, "incfsz");
				incfsz.execute();
			}
		}
		else if(hByteHnibble == 0x4000){
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				Rrncf rrncf = new Rrncf(instruction, this, "rrncf");
				rrncf.execute();
			}
			if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				Rlncf rlncf = new Rlncf(instruction, this, "rlncf");
				rlncf.execute();
			}
			if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				Infsnz infsnz = new Infsnz(instruction, this, "infsnz");
				infsnz.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				Dcfsnz dcfsnz = new Dcfsnz(instruction, this, "dcfsnz");
				dcfsnz.execute();
			}
		}
		else if(hByteHnibble ==	0x5000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
			if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100) || (hByteLnibble == 0x0200) 
					|| (hByteLnibble == 0x0300)){
				//System.out.println("in inner case " + Integer.toHexString(hByteLnibble));
				Movf movf = new Movf(instruction, this, "movf");
				movf.execute();
			}
			else if ((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500) || (hByteLnibble == 0x0600) 
					|| (hByteLnibble == 0x0700)){
				Subfwb subfwb = new Subfwb(instruction, this, "subfwb");
				subfwb.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900) || (hByteLnibble == 0x0a00)
					|| hByteLnibble == 0x0b00){
				Subwfb subwfb = new Subwfb(instruction, this, "subwfb");
				subwfb.execute();
			}
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00) || (hByteLnibble == 0x0e00)
					|| (hByteLnibble == 0x0f00)){
				Subwf subwf = new Subwf(instruction, this, "subwf");
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
				Movwf movwf = new Movwf(instruction, this, "movwf");
				movwf.execute();
			}
			else if((hByteLnibble == 0x0000) || (hByteLnibble == 0x0100)){
				Cpfslt cpfslt = new Cpfslt(instruction, this, "cpfslt");
				cpfslt.execute();
			}
			else if((hByteLnibble == 0x0200) || (hByteLnibble == 0x0300)){
				Cpfseq cpfseq = new Cpfseq(instruction, this, "cpfseq");
				cpfseq.execute();
			}
			else if((hByteLnibble == 0x0400) || (hByteLnibble == 0x0500)){
				Cpfsgt cpfsgt = new Cpfsgt(instruction, this, "cpfsgt");
				cpfsgt.execute();
			}
			else if((hByteLnibble == 0x0600) || (hByteLnibble == 0x0700)){
				Tstfsz tstfsz = new Tstfsz(instruction, this, "tstfsz");
				tstfsz.execute();
			}
			else if((hByteLnibble == 0x0800) || (hByteLnibble == 0x0900)){
				Setf setf = new Setf(instruction, this, "setf");
				setf.execute();
			}
			else if((hByteLnibble == 0x0a00) || (hByteLnibble == 0x0b00)){
				Clrf clrf = new Clrf(instruction, this, "clrf");
				clrf.execute();
			}
			
			else if((hByteLnibble == 0x0c00) || (hByteLnibble == 0x0d00)){
				Negf negf = new Negf(instruction, this, "negf");
				negf.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble ==	0x7000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				Btg btg = new Btg(instruction, this, "btg");
				btg.execute();
				
		}
		else if(hByteHnibble ==	0x8000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				Bsf bsf = new Bsf(instruction, this, "bsf");
				bsf.execute();
		}
		else if(hByteHnibble ==	0x9000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				Bcf bcf = new Bcf(instruction, this, "bcf");
				bcf.execute();
		}
		else if(hByteHnibble ==	0xa000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				Btfss btfss = new Btfss(instruction, this, "btfss");
				btfss.execute();
				
		}
		else if (hByteHnibble == 0xb000){
			Btfsc btfsc = new Btfsc(instruction, this, "btfsc");
			btfsc.execute();
		}
		else if(hByteHnibble ==	0xc000){
			//System.out.println("command high nibble has been decoded as 0x2000");				
				Movff movff = new Movff(instruction, this, "movff");
				movff.execute();
		}
		else if(hByteHnibble == 0xd000){
			//System.out.println("command high nibble has been decoded as 0xd000");
			if((hByteLnibble & 0x0800) == 0x0000){
				//System.out.println("command low nibble has been decoded as 0x0000");
				Bra bra = new Bra(instruction, this, "bra");
				bra.execute();
			}
			else if((hByteLnibble & 0x0800) == 0x0800){
				//System.out.println("command low nibble has been decoded as 0X0800");
				Rcall rcall = new Rcall(instruction, this, "rcall");
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
				Goto GOTO = new Goto(instruction, nextWord, this, "goto");
				GOTO.execute();
			}
			else if(hByteLnibble == 0x0e00){
				nextWord = pc.getWord();
				Lfsr lfsr = new Lfsr(nextWord, this, "lfsr");
				lfsr.execute();
			}
			else if(hByteLnibble == 0x0400){
				Bov bov = new Bov(instruction, this, "bov");
				bov.execute();
			}
			else if(hByteLnibble == 0x0500){
				Bnov bnov = new Bnov(instruction, this, "bnov");
				bnov.execute();
			}
			else if(hByteLnibble == 0x0600){
				Bn bn = new Bn(instruction, this, "bn");
				bn.execute();
			}
			else if(hByteLnibble == 0x0700){
				Bnn bnn = new Bnn(instruction, this, "bnn");
				bnn.execute();
			}
			else if(hByteLnibble == 0x0100){
				Bnz bnz = new Bnz(instruction, this, "bnz");
				bnz.execute();
			}
			else if(hByteLnibble == 0x0000){
				Bz bz = new Bz(instruction, this, "bz");
				bz.execute();
			}
			else if(hByteLnibble == 0x0200){
				Bc bc = new Bc(instruction, this, "bc");
				bc.execute();
			}
			else if(hByteLnibble == 0x0300){
				Bnc bnc = new Bnc(instruction, this, "bnc");
				bnc.execute();
			}
			else{
				System.out.println("instruction not implemented");
				System.exit(0);
			}
		}
		else if(hByteHnibble == 0xf000){
				//System.out.println("command low nibble has been decoded as 0X0000");
				Nop nop = new Nop(instruction, this, "nop");
				nop.execute();
		}
		else{
			System.out.println("instruction not implemented " + Integer.toHexString(instruction));
			System.exit(0);
		}
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
	
	int getProgramMemory(int index){
		return programMemory[index];
	}
//
//	void printProgram(){
//		Set<Integer> keys = program.keySet();
//		int key;
//		ArrayList<Integer> list;
//        for (Integer key1 : keys) {
//            key = key1;
//            list = program.get(key);
//            System.out.println(key + " " + list);
//        }
//	}

	@SuppressWarnings("UnusedAssignment")
    private void clearPgmMem(){
		for ( int item : programMemory){
			item = 0;
		}
	}
	
	protected boolean checkTwoCycle(){
		int nextInstruction = programMemory[pc.getpcValue()];
        return (nextInstruction == 0xef) || nextInstruction == 0xc0;
	}
//
//	void printStatusReg(){
//		System.out.println("status reg is " + dataMem.status.read());
//	}
//
//
//	void printDataMem(){
//		int i = 0;
//		while(i < 0xff){
//			System.out.println(Integer.toHexString(i) + " " +
//				Integer.toHexString(dataMem.gpMem[i].read()));
//			i++;
//		}
//	}
//
//	void printPgmMem(int limit){
//		for (int i = 0; i < programMemory.length; i++){
//			if (i < limit || (i > 12287 && i < 12299)) {
//				System.out.println(i + " " + programMemory[i] + " " + Integer.toHexString(programMemory[i] & 0xff));
//			}
//		}
//	}
}
