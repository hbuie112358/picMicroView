package picmicroview.model;

//future
public class Status extends Register {

	Status(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);		
	}

	@Override
	public void write(int value) {
		//inherited function unimplemented for this class
	}
}
