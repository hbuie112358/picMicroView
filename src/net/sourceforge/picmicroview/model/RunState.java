package net.sourceforge.picmicroview.model;

public class RunState implements Pic18F452State {

	Pic18F452 pic18;
	
	public RunState(Pic18F452 pic18) {
		this.pic18 = pic18;
	}

	@Override
	public void run() {
		//System.out.println("in RunState, changes.size(): " + pic18.changes.size());
		pic18.runInstruction();
	}
}
