package model;

//import java.util.Date;

public class Clock{
	
//	private long CYCLE = 400;
	long last;
	long now;
	//	private int inTimer;
	private boolean go;
//	private int data;
//	private Output output;
//	private Thread print;
	long time1;
	private final Pic18F452 pic18;
	private final int [] a;

	Clock(Pic18F452 pic18) {
		this.pic18 = pic18;
//		long outTimer = 0;
//		inTimer = 0;
		int clockDelay = 300;
		a = new int[clockDelay];
	}
	
	public void stop(){
		go = false;
	}
	
	public void start(){
		if(!go){
			go = true;
			int i;
			long [] time = new long[12];
			long timeAvg = 0;
			for(i = 0; i < time.length; i++){
				time1 = System.nanoTime();
				long time2 = System.nanoTime();
				time[i] = time2 - time1;
			}
			
			for(i = 2; i < time.length; i++){
				timeAvg += time[i];
			}
//			System.out.println(timeAvg / 10);

			while(go){

				for(i = 0; i < a.length; i++){
					a[i] = i;
				}
				pic18.getTimer0().increment();
				pic18.run();
				//printOutput();
				//i++;
			}
		}
		//pic18.printDataMem();
	}
	
//	private void printOutput(){
//		//System.out.print("porta is " + pic18.dataMem.porta.read());
//
//
//		if(data != pic18.dataMem.porta.read()){
//			data = pic18.dataMem.porta.read();
//			output = new Output(pic18);
//			print = new Thread(output);
//			print.start();
//		}
//		//System.out.println("intcon is " + pic18.dataMem.intcon.read());
//	}
	
}
