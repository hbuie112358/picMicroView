package net.sourceforge.picmicroview.controller;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sourceforge.picmicroview.model.Model;
import net.sourceforge.picmicroview.model.Pic18F452;

/**
 * Sends requests received from View on to Model for processing.
 * @author hb
 *
 */
public class RequestController{

	//private Model model;
	private Pic18F452 pic18;
	private ReplyController repCont;
	static ExecutorService pool = Executors.newCachedThreadPool();
	
	public RequestController(Pic18F452 pic18, ReplyController repCont){
		//this.model = model;
		 this.pic18 = pic18;
		 this.repCont = repCont;
	}


	public void loadAction(final String fileName) {
		pool.execute(new Runnable(){
			public void run() {
				pic18.loadHexFile(fileName);
				initialize_pvt();
			}		
		});	
	}
	
	public void stepAction(){
		pool.execute(new Runnable(){
			public void run() {
				pic18.run();
				initialize_pvt();
			}		
		});	
	}
	
//	private void loadActionThread(String fileName){
//		pool.execute(new Runnable(){
//			public void run() {
//				pic18.loadHexFile(fileName);
//			}		
//		});	
//	}

	public void runAction() {
		pool.execute(new Runnable(){
			public void run() {
				pic18.getClock().start();
			}		
		});		
	}
	
	public void stopAction(){
		pool.execute(new Runnable(){
			public void run() {
				pic18.getClock().stop();
				updateDataMemory();
				pic18.initPic();
			}		
		});		
	}
	
	public void initialize(){
		pool.execute(new Runnable(){
			public void run() {
				initialize_pvt();
			}		
		});	
	}

	private void initialize_pvt() {
//		ArrayList<Integer> dm = getDataMemory_pvt();
//		repCont.updateDataMemTable(dm);
		updateDataMemory();
		ArrayList<Integer> pm = getPgmMemory_pvt();
		repCont.updatePgmMemTable(pm);
		int wreg = getWreg_pvt();
		
	}
	
	private void updateDataMemory(){
		ArrayList<Integer> dm = getDataMemory_pvt();
		repCont.updateDataMemTable(dm);
	}
	
	private ArrayList<Integer> getDataMemory_pvt(){
		return pic18.getDataMemory();
	}
	
	private ArrayList<Integer> getPgmMemory_pvt(){
		return pic18.getPgmMemory();
	}
	
	private int getWreg_pvt(){
		return pic18.getWreg();
	}

//	public void sum(int left, int right){
//		pool.execute(new Runnable(){
//			public void run() {
//				model.sum(left, right);
//			}		
//		});
//
//	}
//	
//	public void increment(int val){
//		pool.execute(new Runnable(){
//			public void run() {
//				model.increment(val);
//			}		
//		});
//	}
}
