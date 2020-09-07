package controller;

import java.awt.EventQueue;
import java.util.ArrayList;

import view.MainWindow;

	/**
	 * ReplyController receives input from the Model and sends its output
	 * to the MainWindow. All its gui update functions are placed on the 
	 * Swing Event Dispatch Thread in order to improve gui performance.
	 * @author hb
	 *
	 */
public class ReplyController {

	/**
	 * ReplyController has a reference to the MainWindow as part of
	 * this implementation of Model-View-Controller design pattern
	 */
	private MainWindow mw;
//	private long now; //testing, remove when no longer needed
	
	/**
	 * Causes mw data member to point to MainWindow instance, which completes
	 * return path for MVC design pattern. The design of this program tries to
	 * enforce separation of Model and View. There are times, however when they
	 * need to know something of how the other works in order to perform operations.
	 * Stepping is an example. In stepping, the Model has to "run" a single instruction, 
	 * then the view has to be updated with the new current state of the Model (its registers, 
	 * for example). In order not to have the EDT (gui thread) wait for the Model to complete
	 * its task, the RequestController will start a RequestController thread which will run some
	 * function in the Model, as usual, then quickly return in order to free the EDT. In this
	 * case, the thread started by the RequestController needs to start its own thread which
	 * runs the function in the Model, waits for it to complete, and the calls functions in the 
	 * ReplyController which update the appropriate portions of the gui by calling the 
	 * EventQueue.invokeLater() static function, which places each update request on the 
	 * EDT (Event Dispatch Queue). The author decided that, in situations like this, the 
	 * Controller module was the best place for operations requiring "common knowledge"
	 * to take place (The Controller module being the RequestController and the ReplyController
	 * as a group).
	 * @param mw reference to MainWindow object created in PicMain
	 */
	public void registerMembers(MainWindow mw){
		this.mw = mw;
	}
	
	public void setTitle(final String fileName){
		EventQueue.invokeLater(() -> mw.setTitle("picMicroView: " + fileName));
	}
	
	//Called by RequestController.updateDataMemory()
	void updateMemTables(Object[] dm_changes){
		EventQueue.invokeLater(() -> mw.updateDataMemTable(dm_changes));
	}
	
	//Called by RequestController.stopAction
	void updateMemTables(ArrayList<Integer> dm){
		EventQueue.invokeLater(() -> mw.updateDataMemTable(dm));
	}

	void initDataMemTable(final ArrayList<Integer> dm){
		EventQueue.invokeLater(() -> mw.initDataMemTable(dm));
	}
	
	//Sends MainWindow value of program counter, called by 
	//RequestController.loadAction();
	void updatePc(int pc){
		EventQueue.invokeLater(() -> mw.updatePc(pc));
	}

	/**
	 * Called by request controller initialize_pvt. Calls mw.updatePgmMemTable()
	 * which loads values from pic18 program memory into main window's pgmMemTable's
	 * TableModel. Called when request controller initializes on pgm boot, when a file
	 * is loaded, and each time step button is pressed
	 * @param pm - program memory values from pic18
	 */
	void updatePgmMemTable(final ArrayList<Integer> pm) {
		EventQueue.invokeLater(() -> mw.updatePgmMemTable(pm));
	}

	void initPgmMemTable(final ArrayList<Integer> pm){
		EventQueue.invokeLater(() -> mw.initPgmMemTable(pm));
	}

	void initPortRegMemTable(final ArrayList<Integer> dm){
		EventQueue.invokeLater(() -> mw.initPortRegMemTable(dm));
	}
	
	public void updatePortA(final int value){
		EventQueue.invokeLater(() -> mw.updatePortA(value));
	}
	
	public void updatePortB(final int value){
		EventQueue.invokeLater(() -> mw.updatePortB(value));
	}
	
	public void updatePortC(final int value){
		EventQueue.invokeLater(() -> mw.updatePortC(value));
	}
	
	public void updatePortD(final int value){
		EventQueue.invokeLater(() -> mw.updatePortD(value));
	}
	
	public void updatePortE(final int value){
		EventQueue.invokeLater(() -> mw.updatePortE(value));
	}
}
