package picmicroview.model;

public class Bnn extends PicInstruction {

	public Bnn(int instruction, Pic18F452 pic18, String name) {
		super(instruction, pic18, name);
	}

	@Override
	public void execute() {
		Pic18F452 pic18 = getPic18();
		if(pic18.getDataMem().getStatus().getBit(4) == 0){
			pic18.setPcValue(getPic18().getPcValue() + pic18.getProgramCounter().calcOffset256(getInstruction() & 0xff));
		}

	}

}
