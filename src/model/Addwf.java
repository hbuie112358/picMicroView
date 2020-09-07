package model;

public class Addwf extends Instruction {

	Addwf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	protected void execute() {
		//System.out.println("command is " + name);
		//get wreg value
		//wreg = pic18.getDataMem().wreg.read();
		Pic18F452 pic18 = getPic18();
		DataMemory dataMem = pic18.getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());

//		System.out.println("in alu.addwf, freg address is: " + Integer.toHexString(freg));

		//find sum of wreg, value in gpMem[freg]
		int result = dataMem.wreg.read() + dataMem.gpMem[freg].read();
		adjustCbit(result);
		adjustZbit(result);
		adjustNbit(result);

		//if bit 9 of instruction is high, write result to f register
		//If f is a FSRxL register, then find out if FSR0, 1, or 2 and
		//if carry bit is 1, increment corresponding FSRxH register.
		if((getInstruction() & 0x200) == 0x200){
			dataMem.gpMem[freg].write(result);
			if(pic18.getDataMem().status.getBit(0) == 1){
//				System.out.println("in alu.addwf, fsr0L address is " + Integer.toHexString(pic18.getDataMem().fsr0L.address));
				if(freg == (dataMem.fsr0L.address & 0xff))	//& 0xff because register actually lives at 0xfe9,
					dataMem.fsr0h.increment();				//but is mapped to 0x0e9 for Access Memory
				if(freg == (dataMem.fsr1L.address & 0xff))
					dataMem.fsr1h.increment();
				if(freg == (dataMem.fsr2L.address & 0xff))
					dataMem.fsr2h.increment();

				//Clear carry bit since carry was reflected in FSRxH
				dataMem.status.clearBit(0);
			}
		}
		else dataMem.wreg.write(result);
	}

}
