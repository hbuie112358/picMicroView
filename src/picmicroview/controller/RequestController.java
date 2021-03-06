package picmicroview.controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import picmicroview.model.Pic18F452;

/**
 * Sends requests received from View on to Model for processing.
 * @author hb
 *
 */
public class RequestController{
	private final Pic18F452 pic18;
	private final ReplyController repCont;
	private static final ExecutorService pool = Executors.newCachedThreadPool();
	
	public RequestController(Pic18F452 pic18, ReplyController repCont){
		 this.pic18 = pic18;
		 this.repCont = repCont;
	}

	public boolean loadAction(final String fileName) {
		if(pic18.isRunning()){
			return false;
		}
		else{
			pool.execute(() -> {
				pic18.loadHexFile(fileName);
				ArrayList<Integer> pm = pic18.getPgmMemory();
				repCont.updatePgmMemTable(pm);
				repCont.updatePc(pic18.getPcValue());

			});
			return true;
		}
	}
	
	public void stepAction(){
		pool.execute(() -> {
			pic18.setStepState();
			pic18.run();
			updateDataMemory();
			repCont.updatePc(pic18.getPcValue());
		});
	}

	public void runAction() {
		//				pic18.setRunState();
//				pic18.getClock().start();
		pool.execute(pic18::start);
	}
	
	public void stopAction(){
		pool.execute(() -> {
			pic18.stop();
//				pic18.getClock().stop();
//				pic18.setStepState();
//				pic18.run();
//				updateDataMemory();
			updateDataMemoryOnStop();
			repCont.updatePc(pic18.getPcValue());
			//pic18.initPic();
		});
	}
	
	public void testAction(){
		pool.execute(pic18::executeTest);
	}
	
	public void initialize(){
		pool.execute(this::initializePvt);
	}

	//called as a thread by loadAction(), stepAction, and initialize()
	private void initializePvt() {
		//updateDataMemory();
		ArrayList<Integer> dm = getDataMemoryPvt();
		repCont.initDataMemTable(dm);
		repCont.initPortRegMemTable(dm);
		ArrayList<Integer> pm = getPgmMemoryPvt();
		repCont.initPgmMemTable(pm);
	}
	
	//called by RequestControler.stepAction() and RequestController.stopAction()
	private void updateDataMemory(){
		ArrayList<Integer> dm = getDataMemoryPvt();
		HashSet<Integer> changes = pic18.getChanges();
		Object[] dm_changes = new Object[2];
		dm_changes[0] = changes;	//address of change
		dm_changes[1] = dm;			//data memory array
		repCont.updateMemTables(dm_changes);
	}
	
	private void updateDataMemoryOnStop(){
		ArrayList<Integer> dm = getDataMemoryPvt();
		repCont.updateMemTables(dm);
	}
	
	private ArrayList<Integer> getDataMemoryPvt(){
		return pic18.getDataMemory();
	}
	
	private ArrayList<Integer> getPgmMemoryPvt(){
		return pic18.getPgmMemory();
	}
}
