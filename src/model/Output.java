package model;


public class Output implements Runnable {

	private Pic18F452 pic18;
	public Output(Pic18F452 pic18) {
		this.pic18 = pic18;
	}

	@Override
	public void run() {
	
//		pic18.notify("porta is " + pic18.dataMem.porta.read() + " time is " + 
//				((System.nanoTime() - pic18.clock.time) / 1000000000.0) + " s");
//		System.out.println("porta is " + pic18.dataMem.porta.read() + " time is " + 
//			((System.nanoTime() - pic18.clock.time) / 1000000000.0) + " s");
		pic18.clock.time1 = System.nanoTime();
	}

}
