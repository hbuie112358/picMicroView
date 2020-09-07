package model;


public class CycleTime extends Output {

	private Pic18F452 pic18;
	public CycleTime(Pic18F452 pic18) {
		super(pic18);
		this.pic18 = pic18;
		// TODO Auto-generated constructor stub
	}
	public void run() {
		//System.out.println("cycle time is " + ((System.nanoTime() - pic18.clock.time2) /1000.0));
		//System.out.println("now is " + pic18.clock.now + " and last is " + pic18.clock.last);
		System.out.println("now - last is " + (pic18.getClock().now - pic18.getClock().last));
	}
}
