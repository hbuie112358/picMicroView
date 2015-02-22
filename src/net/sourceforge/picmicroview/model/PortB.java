package net.sourceforge.picmicroview.model;

/**
 * Did not create a DisplayRegister subclass for Register and then make all
 * output ports subclass that because I wanted to make the calls to update
 * gui as simple as possible in order to make speed a priority. Therefore, 
 * port classes (Wreg, PortA, etc.) have much duplicated code in overriding 
 * Register methods that write the register contents. The only difference is 
 * in which ReplyController method is called (ex. updateWreg(), updatePortA()).
 * @author hb
 *
 */
public class PortB extends Register {

	public PortB(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		// TODO Auto-generated constructor stub
	}

	void write(int value){
		this.contents = value;
		contents = contents & 0xff;
		pic18.repCont.updatePortB(contents);
		//System.out.println("in portB write()");
	}
	
	void setBit(int bit){
		int orValue = baseSet << bit;
		contents = contents | orValue;
		pic18.repCont.updatePortB(contents);
	}
	
	void clearBit(int bit){
		//shifts a zero left and adds ones back in to the right
		int andValue = (baseClear << bit) + ((int)(Math.pow(2, bit) - 1));
		//System.out.println("clearBit receives bit value of " + bit + ", decodes andValue as " + andValue);

		contents = contents & andValue;
		pic18.repCont.updatePortB(contents);
		//System.out.println("Contents of " + name + " at address " + address + ": " + contents);
	}
	
	void decrement(){
		contents--;
		contents = contents & 0xff;
		pic18.repCont.updatePortB(contents);
	}
	
	void increment(){
		contents++;
		contents = contents & 0xff;
		pic18.repCont.updatePortB(contents);
	}
	
	void setContents(int value){
		contents = value;
		pic18.repCont.updatePortB(contents);
	}
}
