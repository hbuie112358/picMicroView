import java.awt.EventQueue;

import controller.ReplyController;
import controller.RequestController;
import model.Pic18F452;
import view.MainWindow;

/**
 * Starting point of program. Creates all elements of the underlying 
 * Model-View-Controller (MVC) design pattern. In MVC, the "View," what
 * the user sees and interacts with, is completely isolated from the "Model,"
 * the code that does all the work, the back end. The "Controller" portion 
 * is split into two classes, a RequestController and a ReplyController. The
 * RequestController listens for events initiated by the user in the MainWindow
 * and then interprets these events and dispatches them to the Model. When the Model
 * has completed processing of the event, the Model replies to the ReplyController, 
 * which in turn updates the MainWindow by placing the information to be updated
 * on the Swing Event Dispatch Thread using the static EventQueue.invokeLater() method.
 * This ensures that the processing of data does not interfere with the operation of
 * the gui. The MVC design pattern also provides the capability of changing the 
 * front end (View) without having to change the back end (Model).
 */
public class PicMain {

	public static void main(String[] args) {


		final ReplyController replyController  = new ReplyController();
		Pic18F452 model = new Pic18F452(replyController);
		final RequestController requestController = new RequestController(model, replyController);
		
		/**
		 * Places task of creating MainWindow on Event Dispatch Thread. Once
		 * MainWindow is created, registers MainWindow with ReplyController.
		 */
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				MainWindow mw = new MainWindow(requestController);
				replyController.registerMembers(mw, requestController);
			}
		});
	}

}


