package net.sourceforge.picmicroview.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.picmicroview.controller.RequestController;



public class MainWindow extends JFrame{

	private static final long serialVersionUID = 782592750591591329L;

	private RequestController reqCont;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem load;
	private JMenuItem newFile;
	private JMenuItem quit;
	private JFileChooser fc;
	
	private JMenu program;
	private JMenuItem run;
	private JMenuItem step;
	private JMenuItem assemble;
	private JMenuItem stop;
	
	private JMenu window;
	private JMenu chip;
	private JMenu view;
	private JMenu help;
	
	private JPanel mainPanel;
	private Box vBox;
	private Box hBox;
	private JPanel rightPanel;
	private JPanel upperRightPanel;
	private JPanel lowerRightPanel;
	private JPanel leftPanel;
	private JPanel centerPanel;
	
	private JButton upperRight;
	private JButton lowerRight;
	private JButton left;
	private JButton center;
	
	private JTable dataMemTable;
	private DefaultTableModel dtm_data;
	
	private JTable pgmMemTable;
	private DefaultTableModel dtm_pgm;
	private DefaultTableCellRenderer right;
	
	private JScrollPane pgmPane;
	
	private JLabel porta;
	
	/**
	 * MainWindow is the "View" portion of the MVC design pattern.
	* It sends all requests to the RequestController, which it has a 
	* reference to, and receives all updates from the ReplyController. 
	* MainWindow is completely oblivious of the Model.
	* @author hb
	* 
	* Implement separate ActionListener class for each menu option.
	*
	*/
	
	public MainWindow(RequestController reqCont){
		initUI(reqCont);
	}
	
	private void initUI(RequestController reqCont){
		this.reqCont = reqCont;
		setFocusable(true);
		setSize(800, 680);
		setTitle("picMicroView");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		file = new JMenu("File");
		menuBar.add(file);
		file.setMnemonic('F');
		
		load = new JMenuItem("Load");
		file.add(load);
		load.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

		fc = new JFileChooser();
		load.addActionListener(new LoadAction());
		
		newFile = new JMenuItem("New");
		file.add(newFile);
		quit = new JMenuItem("Quit");
		file.add(quit);

		program = new JMenu("Program");
		menuBar.add(program);
		program.setMnemonic('P');
		run = new JMenuItem("Run");
		run.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		program.add(run);
		run.addActionListener(new RunAction());
		
		step = new JMenuItem("Step");
		program.add(step);
		step.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		assemble = new JMenuItem("Assemble");
		program.add(assemble);
		stop = new JMenuItem("Stop");
		program.add(stop);
		stop.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		step.addActionListener(new StepAction());
		stop.addActionListener(new StopAction());
		
		window = new JMenu("Window");
		menuBar.add(window);
		chip = new JMenu("Chip");
		menuBar.add(chip);
		view = new JMenu("View");
		menuBar.add(view);
		help = new JMenu("Help");
		menuBar.add(help);
		
		mainPanel = new JPanel();
		vBox = Box.createVerticalBox();
		rightPanel = new JPanel();
		upperRightPanel = new JPanel();
		lowerRightPanel = new JPanel();
		leftPanel = new JPanel();
		centerPanel = new JPanel();
		JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
		toolBar.add(new JButton("Step"));
		toolBar.add(new JButton("Run"));
		toolBar.add(new JButton("Stop"));

		
//		upperRight = new JButton("Registers");
		porta = new JLabel("****");
		
		dataMemTable = new JTable(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int arg0, int arg1){
				return false;
			}
		};
		
		right = new DefaultTableCellRenderer();
		right.setHorizontalAlignment(SwingConstants.RIGHT);

		JScrollPane dataPane = new JScrollPane(dataMemTable);
		dataPane.setPreferredSize(new Dimension(180, 462));
		lowerRightPanel.add(dataPane);
		
		pgmMemTable = new JTable(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int arg0, int arg1){
				return false;
			}
		};
		pgmPane = new JScrollPane(pgmMemTable);
		
		pgmPane.setPreferredSize(new Dimension(180, 500));
		leftPanel.add(pgmPane);
		
		
		left = new JButton("Program Memory");
		center = new JButton("Input/Output");
		
		mainPanel.setLayout(new BorderLayout());
		upperRightPanel.add(porta);
		vBox.add(upperRightPanel);
		vBox.add(lowerRightPanel);
		rightPanel.add(vBox);
		mainPanel.add(rightPanel, BorderLayout.EAST);
		
		centerPanel.add(center);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(leftPanel, BorderLayout.WEST);
		//add(mainPanel);
		add(mainPanel);
		initialize();
		setVisible(true);
		
	}
	
	class DataTableModel extends AbstractTableModel{
		
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private void initialize(){
		reqCont.initialize();
	}
	
	public void updateDataMemTable(ArrayList<Integer> dm){
		String[] colNames = {"Address", "Data"};
		dtm_data = new DefaultTableModel(colNames, 0);
		dataMemTable.setModel(dtm_data);
		dataMemTable.getColumnModel().getColumn(1).setCellRenderer(right);
		Object[] ob = new Object[2];
		for(int i = 0; i < dm.size(); i++){
			ob[0] = Integer.toHexString(i);
			ob[1] = Integer.toBinaryString(dm.get(i));
			dtm_data.addRow(ob);
		}
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
					dtm_data.fireTableDataChanged();
			}
		}); 
	}
	
	public void updatePgmMemTable(ArrayList<Integer> pm){
		String[] colNames = {"Address", "Data"};
		dtm_pgm = new DefaultTableModel(colNames, 0);
		pgmMemTable.setModel(dtm_pgm);
		pgmMemTable.getColumnModel().getColumn(1).setCellRenderer(right);
		Object[] ob = new Object[2];
		for(int i = 0; i < pm.size(); i++){
			ob[0] = Integer.toHexString(i);
			ob[1] = Integer.toHexString(pm.get(i));
			dtm_pgm.addRow(ob);
		}	
	}
	
	public void updatePortA(int value){
		porta.setText(Integer.toBinaryString(value));
//		System.out.println("in mw, writing porta");
		validate();
	}
	
	/**
	 * LoadAction class listens for load menu item to be selected, 
	 * performs action of showing file system so that a file can be chosen
	 * @author hb
	 *
	 */
	class LoadAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String fileName = null;
			int a = fc.showOpenDialog(MainWindow.this);
			if(a == JFileChooser.APPROVE_OPTION)
				fileName = fc.getSelectedFile().toString();
			else return;
			//System.out.println("chose file: " + fileName);
			reqCont.loadAction(fileName);		
		}	
	}
	
	class StepAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			reqCont.stepAction();
			
		}
		
	}
	
	class RunAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			reqCont.runAction();			
		}	
	}
	
	class StopAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			reqCont.stopAction();
		}
	}
}
