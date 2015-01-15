package net.sourceforge.picmicroview.model;

import net.sourceforge.picmicroview.controller.ReplyController;

/**
 * Processes requests from RequestController and returns results to 
 * ReplyController
 * @author hb
 *
 */
public class Model {

	private ReplyController repCont;
	
	public Model(ReplyController repCont){
		this.repCont = repCont;
	}
	
//	public void sum(int left, int right){
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		repCont.setSum(left + right);
//	}
//	
//	public void increment(int val){
//		repCont.setIncVal(++val);
//	}
}
