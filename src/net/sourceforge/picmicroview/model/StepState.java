package net.sourceforge.picmicroview.model;

public class StepState implements Pic18F452State {

	Pic18F452 pic18;
	
	public StepState(Pic18F452 pic18) {
		this.pic18 = pic18;
	}

	@Override
	public void run() {
		pic18.changes.clear();
		pic18.runInstruction();
	}
	
	public void stop(){
		
	}
}
