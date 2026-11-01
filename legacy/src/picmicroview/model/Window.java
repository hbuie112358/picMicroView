package picmicroview.model;

//import java.awt.BorderLayout;
import java.awt.Dimension;
//import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.FlowLayout;


public class Window extends JFrame implements MouseListener, PicListener{
	
	Pic18F452 pic18;
	private static final long serialVersionUID = 1L;
	private JPanel registers;
	public JLabel wreg;
	
	public Window(Pic18F452 pic18) throws HeadlessException {
        //Create and set up the window.

		this.pic18 = pic18;
	//	pic18.subscribe(this);
        setSize(700,700);
        setLocationRelativeTo(null);
        setTitle("picMicroView");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        registers = new JPanel();
        registers.setPreferredSize(new Dimension(600, 400));
        registers.setLayout(new FlowLayout(FlowLayout.LEFT));
        wreg = new JLabel("00000000");
		//wreg.setSize(new Dimension(50, 100));
        registers.add(wreg);
        getContentPane().add(registers);
 
        //JLabel emptyLabel = new JLabel("");
        //emptyLabel.setPreferredSize(new Dimension(600, 400));
        //getContentPane().add(emptyLabel, BorderLayout.CENTER);

 
        //Display the window.
        pack();
        setVisible(true);
		String filename;
		filename = "picFiles//p1ModEven.hex";



		//ArrayList<Long> exeTimes = new ArrayList<Long>();
		
		pic18.loadHexFile(filename);
	
		pic18.getClock().start();
	}
	
	public void update(String message){
		wreg.setText(message);
		registers.revalidate();

	}


//	public Window(GraphicsConfiguration gc) {
//		super(gc);
//		// TODO Auto-generated constructor stub
//	}
//
//	public Window(String title) throws HeadlessException {
//		super(title);
//		// TODO Auto-generated constructor stub
//	}
//
//	public Window(String title, GraphicsConfiguration gc) {
//		super(title, gc);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
