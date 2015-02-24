package net.sourceforge.picmicroview.controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sourceforge.picmicroview.model.Pic18F452;

/**
 * Sends requests received from View on to Model for processing.
 * @author hb
 *
 */
public class RequestController{
	private Pic18F452 pic18;
	private ReplyController repCont;
	static ExecutorService pool = Executors.newCachedThreadPool();
	
	public RequestController(Pic18F452 pic18, ReplyController repCont){
		 this.pic18 = pic18;
		 this.repCont = repCont;
	}

	public void loadAction(final String fileName) {
		pool.execute(new Runnable(){
			public void run() {
				pic18.loadHexFile(fileName);
				ArrayList<Integer> pm = pic18.getPgmMemory();
				repCont.updatePgmMemTable(pm);
				repCont.updatePc(pic18.getPc());

			}		
		});	
	}
	
	public void stepAction(){
		pool.execute(new Runnable(){
			public void run() {
				pic18.setStepState();
				pic18.run();
				updateDataMemory();
				repCont.updatePc(pic18.getPc());				
			}		
		});	
	}

	public void runAction() {
		pool.execute(new Runnable(){
			public void run() {
				pic18.setRunState();
				pic18.getClock().start();
			}		
		});		
	}
	
	public void stopAction(){
		pool.execute(new Runnable(){
			public void run() {
				pic18.getClock().stop();
				pic18.setStepState();
				pic18.run();
//				updateDataMemory();
				updateDataMemoryOnStop();
				repCont.updatePc(pic18.getPc());
				//pic18.initPic();
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

	//called as a thread by loadAction(), stepAction, and initialize()
	private void initialize_pvt() {
		//updateDataMemory();
		ArrayList<Integer> dm = getDataMemory_pvt();
		repCont.initDataMemTable(dm);
		repCont.initPortRegMemTable(dm);
		ArrayList<Integer> pm = getPgmMemory_pvt();
		repCont.initPgmMemTable(pm);
	}
	
	//called by RequestControler.stepAction() and RequestController.stopAction()
	private void updateDataMemory(){
		ArrayList<Integer> dm = getDataMemory_pvt();
		HashSet<Integer> changes = pic18.getChanges();
		Object[] dm_changes = new Object[2];
		dm_changes[0] = changes;	//address of change
		dm_changes[1] = dm;			//data memory array
		repCont.updateMemTables(dm_changes);
	}
	
	private void updateDataMemoryOnStop(){
		ArrayList<Integer> dm = getDataMemory_pvt();
		repCont.updateMemTables(dm);
	}
	
	private ArrayList<Integer> getDataMemory_pvt(){
		return pic18.getDataMemory();
	}
	
	private ArrayList<Integer> getPgmMemory_pvt(){
		return pic18.getPgmMemory();
	}
}
