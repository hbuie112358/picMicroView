package net.sourceforge.picmicroview.model;

public class FsrL extends Register {
	
	//Access memory address of corresponding FSRH register
	int fsrh;

	public FsrL(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		
		//Find corresponding FSRH register
		if(name.equals("fsr0L"))
			fsrh = 0x0ea;
		else if(name.equals("fsr1L"))
			fsrh = 0x0e2;
		else fsrh = 0x0da;
			
	}
	
	//Rollover to 0x000 after reaching 0xfff. FSRL increments both FSRL and FSRH
	void increment(){
		if(contents == 0xff){
			contents = 0;
			pic18.dataMem.gpMem[fsrh].increment();
		}
		else contents++;
	}
	
	//Rollover to 0xfff after reaching 0x000. FSRL decrements both FSRL and FSRH
	void decrement(){
		System.out.println("in " + name + ", decrement() was called");
		if(contents == 0x00){
			contents = 0xff;
			pic18.dataMem.gpMem[fsrh].decrement();
		}
		else contents--;
	}

}
