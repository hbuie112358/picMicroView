package net.sourceforge.picmicroview.model;

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
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//picMicroView1600p.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//picBasicWithAddwf.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//picBasicGotoTrapStop.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//picBasicWithDecf.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//RelativeAddressTestFiles//picBnz.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//RelativeAddressTestFiles//picBnzAlternate.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//testPc.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//RelativeAddressTestFiles//picBz.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//bsfTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//btgTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//bcfTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//movffTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//btfssTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//iorwfTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//RelativeAddressTestFiles//rcallReturnTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//RelativeAddressTestFiles//braTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//andlwTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//movfTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//statusRegTestFiles//addwfcTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//clockPortToggleTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//portaTest.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//p1Test.hex";
		filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//p1ModEven.hex";
		//filename = "//home//hb//taylorplyr@gmail.com//summerDev//picFiles//p1orig//p1orig.hex";



		//ArrayList<Long> exeTimes = new ArrayList<Long>();
		
		pic18.loadHexFile(filename);
	
		pic18.clock.start();
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
