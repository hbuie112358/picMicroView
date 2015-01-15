package net.sourceforge.picmicroview.model;

public class DisplayRegister extends Register {

	public DisplayRegister(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		// TODO Auto-generated constructor stub
	}
	
//	void write(int value){
//		this.contents = value;
//		pic18.repCont.updatePortA(address, contents);
//		//System.out.println("in porta write()");
//	}
//	
//	void setBit(int bit){
//		int orValue = baseSet << bit;
//		contents = contents | orValue;
//		pic18.repCont.updatePortA(address, contents);
//	}
//	
//	void clearBit(int bit){
//		//shifts a zero left and adds ones back in to the right
//		int andValue = (baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
//		//System.out.println("clearBit receives bit value of " + bit + ", decodes andValue as " + andValue);
//
//		contents = contents & andValue;
//		pic18.repCont.updatePortA(address, contents);
//		//System.out.println("Contents of " + name + " at address " + address + ": " + contents);
//	}
//	
//	void decrement(){
//		contents--;
//		pic18.repCont.updatePortA(address, contents);
//	}
//	
//	void increment(){
//		contents++;
//		pic18.repCont.updatePortA(address, contents);
//	}
//	
//	void setContents(int value){
//		contents = value;
//		pic18.repCont.updatePortA(address, contents);
//	}

}
