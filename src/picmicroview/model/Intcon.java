package picmicroview.model;

//future
public class Intcon extends Register {

	Intcon(Pic18F452 pic18, int address, String name) {
		super(pic18, address, name);
	}

	@Override
	public void write(int value) {
		//inherited function unimplemented for this class
	}

}
