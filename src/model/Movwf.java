package model;


public class Movwf extends Instruction{
	
	private int freg;
	
	public Movwf(int instruction, Pic18F452 pic18, String name){
		super(instruction, pic18, name);
	}
	
	protected void execute(){
		
//		int address = pic18.dataMem.getRegAddress(instruction);
		DataMemory dataMem = getPic18().getDataMem();
		freg = dataMem.getRegAddress(getInstruction());
		//isolate a bit for bsr bank from op code
//		int address = instruction & 0x00FF;
//	//	System.out.println("in movwf, address is: " + Integer.toHexString(address));
//		
//		//isolate a bit from address
//		int bank = instruction & 0x0100;
//		//System.out.println("in movwf, bank is: " + Integer.toHexString(bank));
//		
//		//if a bit is set, add bsr
//		if((bank ^ 0x0100) == 0){
//			//add value in BSR to bits 8-11 of address value
//			int extendedAddress = pic18.dataMem.bsr.read();
//			extendedAddress = extendedAddress << 8;
//			address = address | extendedAddress;
////			System.out.println("in movlw, after added bsr value, extendedAddress is: " + Integer.toHexString(extendedAddress));
////			System.out.println("in movlw, after added bsr value, address is: " + Integer.toHexString(address));
////			pic18.dataMem.gpMem[address].write(pic18.dataMem.wreg.read());
//		}
		
		dataMem.gpMem[freg].write(dataMem.wreg.read());
		//System.out.println("command is " + name);
		//System.out.println("in movwf, contents of register: " + pic18.dataMem.gpMem[address].read());
	}
	
	@Override
	protected void initialize(int instruction) {
		this.setInstruction(instruction);
	}

}
