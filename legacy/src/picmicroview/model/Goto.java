package picmicroview.model;

/*
 * 
 */
public class Goto extends PicInstruction {
	private int nextWord;
	
	Goto(int instruction, int nextWord, Pic18F452 pic18, String name){
		super(instruction, pic18, name);
		this.nextWord = nextWord;
	}

	public void execute(){
//		System.out.println("starting execute(), instruction is: " + Integer.toHexString(instruction) + " and pc is: " +
//				Integer.toHexString(pic18.pc.getPc()) + ", nextWord is: " + Integer.toHexString(nextWord));
		//lowByte: low 2 nibbles of instruction are k7 through k0 of pc, the "&" masks off 
		//"EF" portion of goto instruction
		int lowByte = getInstruction() & 0x00FF;
		
		//nextWord is the value in the next location of the program counter. The upper 
		//byte is masked off, leaving only the lower byte
		nextWord = nextWord & 0x00FF;
		int gotoAdress = nextWord * 256 + lowByte;
		getPic18().setPcValue(gotoAdress * 2);
//		System.out.println("command is " + name + " and pc is now " + Integer.toHexString(pic18.pc.getPc())
//				+ " hex");
//		System.out.println("pcL is " + Integer.toHexString(pic18.dataMem.pcL.read()));
		nextWord = 0;
		setInstruction(0);
	}
	
	protected void initialize(int instruction, int nextWord){
		this.nextWord = nextWord;
//		System.out.println("in goto.initialize, setting nextWord to: " + Integer.toHexString(nextWord));
		setInstruction(instruction);
	}

}
