package net.sourceforge.picmicroview.model;


public class PCL extends Register {
	
	public PCL(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
		regRunState = new PclRunState(this);
		regStepState = new PclStepState(this);
	}
	
	//PCL lsb only allowed to be zero. This is to keep pc at instruction border
	//A write to PCL loads program counter with contents of pclatU, pclatH, and PCL
	//pcu bits 23-21 are maintained at zero
	public void write(int value){
		registerState.write(value);
	}
	
	int read(){	
		int pclatH = pic18.pc.getPc() & 0x0000ff00;
		//pic18.dataMem.pclatH.setContents(pclatH);
		pic18.dataMem.pclatH.write(pclatH);
		
		int pclatU = pic18.pc.getPc() & 0x00ff0000;
		//pic18.dataMem.pclatU.setContents(pclatU);
		pic18.dataMem.pclatU.write(pclatU);

		return pic18.pc.getPc() & 0xff;	
	}
	
	public class PclRunState extends RegRunState{
		
		public PclRunState(Register register){
			super(register);
		}
		//PCL lsb only allowed to be zero. This is to keep pc at instruction border
		//A write to PCL loads program counter with contents of pclatU, pclatH, and PCL
		//pcu bits 23-21 are maintained at zero
		public void write(int value){
//			System.out.println("in PCL write()");
			this.write(value); 
			clearBit(0);
			int newValue, pch, pcu;
			pch = pic18.dataMem.pclatH.read() * 256;
			pcu = pic18.dataMem.pclatU.read() & 0x1f;
			pcu = pcu * 65536;
			newValue = pcu + pch + read();	
			pic18.pc.setPc(newValue);
		}	
	}

	public class PclStepState extends RegStepState{
		
		public PclStepState(Register register){
			super(register);
		}
		//PCL lsb only allowed to be zero. This is to keep pc at instruction border
		//A write to PCL loads program counter with contents of pclatU, pclatH, and PCL
		//pcu bits 23-21 are maintained at zero
		public void write(int value){
			//System.out.println("in PCL write()");
			this.write(value); 
			clearBit(0);
			int newValue, pch, pcu;
			pch = pic18.dataMem.pclatH.read() * 256;
			pcu = pic18.dataMem.pclatU.read() & 0x1f;
			pcu = pcu * 65536;
			newValue = pcu + pch + read();	
			pic18.pc.setPc(newValue);
			register.pic18.changes.add((Integer)address);	//tracks changes pic state during instruction

		}	
	}
}
