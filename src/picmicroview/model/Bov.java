package picmicroview.model;

public class Bov extends PicInstruction {

	public Bov(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		if(getPic18().getDataMem().getStatus().getBit(3) == 1){
			pic18.setPcValue(pic18.getPcValue() + pic18.getProgramCounter().calcOffset256(getInstruction() & 0xff));
		}

	}

}
