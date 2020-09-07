package model;

public class Negf extends Instruction {

	public Negf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int origValue = dataMem.gpMem[freg].read();
		int result = Alu.getTwosComplement(origValue & 0xff);
		adjustDCbit(~origValue, (0x01));
		dataMem.gpMem[freg].write(result);
		adjustZbit(result);
		adjustNbit(result);
		adjustCbit(result);

	}

}
