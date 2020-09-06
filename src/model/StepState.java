package model;

public class StepState implements Pic18F452State {

	Pic18F452 pic18;
	State state;
	
	public StepState(Pic18F452 pic18) {
		this.pic18 = pic18;
		state = State.STEP;
	}

	@Override
	public void run() {
		pic18.changes.clear();
		pic18.runInstruction();
	}
	
	public void stop(){
		
	}

	@Override
	public State getState() {
		return state;
	}
}
