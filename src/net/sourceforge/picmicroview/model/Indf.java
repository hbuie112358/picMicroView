package net.sourceforge.picmicroview.model;

public class Indf extends Register {

	int fullAddress, highAddress, lowAddress;
	
	public Indf(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);

		//indf contents is always 0
		this.contents = 0;
	}
	
	//This function overrides parent function.
	//Writes value to memory location pointed to by fsrh:fsrL 
	//Gets full address based on whether this in an instance of indf0, indf1, or indf2, 
	//then writes value to that register using the write(int value, Register r) method so 
	//that if the register being written to is another indf register, the write will have 
	//no effect.
	void write(int value){
		getFullAddress();
		
		//tells callee that caller is an indf register
		pic18.dataMem.gpMem[fullAddress].write(value, this);
		
		//System.out.println("in Register, written by register at address: " + Integer.toHexString(address));
	}
	
	//This function overrides parent function.
	//The only entity that calls this function is another indf register. Its purpose is to 
	//cause an attempt by one indf to access another indf to have no effect by letting the function
	//know that it is being called by another indf. It is inherited from Register.
	void write(int value, Register r){
		return;
	}
	
	//Gets full address based on whether it is an instance of indf0, indf1, or indf2.
	//It needs to know whether to get address from fsr0, fsr1, or fsr2
	private void getFullAddress(){
		if(this.name.equals("indf0")){
			highAddress = pic18.dataMem.fsr0h.read();
			lowAddress = pic18.dataMem.fsr0L.read();
		}
		else if(this.name.equals("indf1")){
			highAddress = pic18.dataMem.fsr1h.read();
			lowAddress = pic18.dataMem.fsr1L.read();
		}
		else{
			highAddress = pic18.dataMem.fsr2h.read();
			lowAddress = pic18.dataMem.fsr2L.read();
		}
		highAddress = highAddress << 8;
		fullAddress = highAddress | lowAddress;
	}
	
	//This function overrides parent function.
	//The only entity that calls this function is another indf register. Its purpose is to 
	//cause an attempt by one indf to read another indf to return 0 by letting the function
	//know that it is being called by another indf. It is inherited from Register.
	int read(){
		getFullAddress();
		
		//tells callee that caller is an indf register
		return pic18.dataMem.gpMem[fullAddress].read(this);

	}

}
