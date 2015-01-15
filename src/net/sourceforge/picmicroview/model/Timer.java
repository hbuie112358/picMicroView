package net.sourceforge.picmicroview.model;


public class Timer {

	private long count;
	private String name;
	private Pic18F452 pic18;
	
	
	public Timer(Pic18F452 pic18, String name) {
		clear();
		this.pic18 = pic18;
		this.name = name;
	}
	
	public void increment(){
		count = count + 1;
		//if((count & 0x10000) == 0x10000){
		if(count > 0x10000){
			clear();
			pic18.dataMem.intcon.setBit(2);
			//System.out.println("intcon is " + Integer.toBinaryString(pic18.dataMem.intcon.read()));
			//System.out.println("timer0 has been reset");
		}
		//System.out.println("timer0 value is " + Long.toHexString(count));
		//System.out.println("intcon is " + pic18.dataMem.intcon.read());
		//System.out.println("contents of memory 0x03 is " + pic18.dataMem.gpMem[0x03].read());

	}
	
	public void set(int count){
		this.count = count;
	}
	
	public void clear(){
		count = 0x0000;
	}
	
	public long getCount(){
		return count;
	}
	
	public void timeTest(){
		long a = 0, b = 1;
		while(a < 65536){
			a = a + b;
			//System.out.println(Long.toBinaryString(a));
		}
	}

}
