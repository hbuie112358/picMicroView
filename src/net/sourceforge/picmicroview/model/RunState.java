package net.sourceforge.picmicroview.model;

public class RunState implements Pic18F452State {

	Pic18F452 pic18;
	State state;
	
	public RunState(Pic18F452 pic18) {
		this.pic18 = pic18;
		state = State.RUN;
	}

	@Override
	public void run() {
		//System.out.println("in RunState, changes.size(): " + pic18.changes.size());
		pic18.runInstruction();
	}
	
	public void stop(){
		pic18.clock.stop();
		pic18.setStepState();
		pic18.run();
	}

	@Override
	public State getState() {
		return state;
	}
	
}
