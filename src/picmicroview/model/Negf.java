package picmicroview.model;

public class Negf extends PicInstruction {

	public Negf(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		DataMemory dataMem = getPic18().getDataMem();
		int freg = dataMem.getRegAddress(getInstruction());
		int origValue = dataMem.getGpMem()[freg].read();
		int result = Alu.getTwosComplement(origValue & 0xff);
		adjustDCbit(~origValue, (0x01));
		dataMem.getGpMem()[freg].write(result);
		adjustZbit(result);
		adjustNbit(result);
		adjustCbit(result);

	}

}
