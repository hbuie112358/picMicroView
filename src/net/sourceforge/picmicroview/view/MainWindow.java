package net.sourceforge.picmicroview.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import org.icepdf.ri.common.*;
import net.sourceforge.picmicroview.controller.RequestController;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 782592750591591329L;

	private LstFileWindow lstPanel;
	private String fileName = "";
	private String lstFileName = "";
	private RequestController reqCont;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem loadItem = new JMenuItem("Load");
	private JMenuItem newFileItem = new JMenuItem("New");
	private JMenuItem quitItem = new JMenuItem("Quit");
	private JFileChooser fc = new JFileChooser();
	
	private JMenu programMenu = new JMenu("Program");
	private JMenuItem runItem = new JMenuItem("Run");
	private JMenuItem stepItem = new JMenuItem("Step");
	private JMenuItem assembleItem = new JMenuItem("Assemble");
	private JMenuItem stopItem = new JMenuItem("Stop");
	
	private LoadAction loadAction = new LoadAction();
	private RunAction runAction = new RunAction();
	private StepAction stepAction = new StepAction();
	private StopAction stopAction = new StopAction();
	
	private JMenu examples = new JMenu("Examples");
	private JMenu instructions = new JMenu("Instructions");
	private JMenu basic = new JMenu("Basic");
	private JMenu inDepth = new JMenu("In-depth");
	private JMenu a = new JMenu("A");
	private JMenuItem addwf = new JMenuItem("addwf");
	private JMenuItem andlw = new JMenuItem("andlw");
	private JMenu b = new JMenu("B");
	private JMenuItem bz = new JMenuItem("bz");
	private JMenuItem bnz = new JMenuItem("bnz");
	private JMenuItem bsf = new JMenuItem("bsf");
	private JMenuItem btfss = new JMenuItem("btfss");
	private JMenuItem btg = new JMenuItem("btg");
	private JMenu d = new JMenu("D");
	private JMenuItem decf = new JMenuItem("decf");
	private JMenuItem decfsz = new JMenuItem("decfsz");
	private JMenu g = new JMenu("G");
	private JMenuItem gotoItem = new JMenuItem("goto");
	private JMenu i = new JMenu("I");
	private JMenuItem iorwf = new JMenuItem("iorwf");
	private JMenu l = new JMenu("L");
	private JMenuItem lfsr = new JMenuItem("lfsr");
	private JMenu m = new JMenu("M");
	private JMenuItem movf = new JMenuItem("movf");
	private JMenuItem movff = new JMenuItem("movff");
	private JMenuItem movlw = new JMenuItem("movlw");
	
	private JMenu mID = new JMenu("M");
	private JMenuItem addwfcIDItem = new JMenuItem("addwfc");

	private JMenu generalConcepts = new JMenu("General Concepts");
	private JMenu indirectAddressing = new JMenu("Indirect Addressing");
	private JMenuItem indfItem = new JMenuItem("indf");
	private JMenuItem pluswItem = new JMenuItem("plusw");
	private JMenuItem postdecItem = new JMenuItem("postdec");
	private JMenuItem postincItem = new JMenuItem("postinc");
	private JMenuItem preincItem = new JMenuItem("preinc");
	private JMenu loops = new JMenu("Loops");
	private JMenuItem fastPortAToggleItem = new JMenuItem("Fast PORTA Toggle");
	
	private JMenu helpMenu = new JMenu("Help");
	
	private JPanel mainPanel = new JPanel();
	
	private JButton runButton = new JButton("Run");
	private JButton stepButton = new JButton("Step");;
	private JButton stopButton = new JButton("Stop");;
	
	JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
	private MemoryTableModel dtm_data;
	
	private ProgramMemTable pgmMemTable = new ProgramMemTable();
	private DataMemTable dataMemTable = new DataMemTable();
	private PortRegTable portRegTable = new PortRegTable();
	private MemoryTableModel dtm_pgm;
	private DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	private TableCellRenderer colRenderer;
	
	private PortRegTableModel dtm_portReg;

	
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
		setSize(800, 620);
		setTitle("picMicroView");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		setJMenuBar(menuBar);
		
		menuBar.add(fileMenu);
		fileMenu.setMnemonic('F');
		
		fileMenu.add(loadItem);
		loadItem.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

		loadItem.addActionListener(loadAction);
		
		fileMenu.add(newFileItem);
		fileMenu.add(quitItem);
		quitItem.addActionListener(new QuitAction());

		examples.add(instructions);
		instructions.add(basic);
		instructions.add(inDepth);
		basic.add(a);
		basic.add(b);
		basic.add(d);
		basic.add(g);
		basic.add(i);
		basic.add(l);
		basic.add(m);
		a.add(addwf);
		addwf.addActionListener(new ExampleAction("basic", "addwf"));
		a.add(andlw);
		andlw.addActionListener(new ExampleAction("basic","andlw"));
		b.add(bnz);
		bnz.addActionListener(new ExampleAction("basic","bnz"));
		b.add(bsf);
		bsf.addActionListener(new ExampleAction("basic","bsf"));
		b.add(btfss);
		btfss.addActionListener(new ExampleAction("basic","btfss"));
		b.add(btg);
		btg.addActionListener(new ExampleAction("basic","btg"));
		b.add(bz);
		bz.addActionListener(new ExampleAction("basic","bz"));
		d.add(decf);
		decf.addActionListener(new ExampleAction("basic","decf"));
		d.add(decfsz);
		decfsz.addActionListener(new ExampleAction("basic","decfsz"));
		g.add(gotoItem);
		gotoItem.addActionListener(new ExampleAction("basic","goto"));
		i.add(iorwf);
		iorwf.addActionListener(new ExampleAction("basic","iorwf"));
		l.add(lfsr);
		lfsr.addActionListener(new ExampleAction("basic","lfsr"));
		m.add(movf);
		movf.addActionListener(new ExampleAction("basic","movf"));
		m.add(movff);
		movff.addActionListener(new ExampleAction("basic","movff"));
		m.add(movlw);
		movlw.addActionListener(new ExampleAction("basic","movlw"));
		
		inDepth.add(mID);
		mID.add(addwfcIDItem);
		addwfcIDItem.addActionListener(new ExampleAction("inDepth", "addwfc"));
		
		examples.add(generalConcepts);
		generalConcepts.add(indirectAddressing);
		indirectAddressing.add(indfItem);
		indfItem.addActionListener(new ExampleAction("indirectAddressing", "indf"));
		indirectAddressing.add(pluswItem);
		pluswItem.addActionListener(new ExampleAction("indirectAddressing", "plusw"));
		indirectAddressing.add(postdecItem);
		postdecItem.addActionListener(new ExampleAction("indirectAddressing", "postdec"));
		indirectAddressing.add(postincItem);
		postincItem.addActionListener(new ExampleAction("indirectAddressing", "postinc"));
		indirectAddressing.add(preincItem);
		preincItem.addActionListener(new ExampleAction("indirectAddressing", "preinc"));
		generalConcepts.add(loops);
		loops.add(fastPortAToggleItem);
		fastPortAToggleItem.addActionListener(new ExampleAction("loops", "fastPortAToggle"));

		menuBar.add(programMenu);
		programMenu.setMnemonic('P');
		runItem.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		runItem.addActionListener(runAction);
		runButton.addActionListener(runAction);
		programMenu.add(runItem);
		
		stepItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		stepItem.addActionListener(stepAction);
		stepButton.addActionListener(stepAction);
		programMenu.add(stepItem);
		
		programMenu.add(assembleItem);
		
		stopItem.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		stopItem.addActionListener(stopAction);
		stopButton.addActionListener(stopAction);
		programMenu.add(stopItem);
		
		menuBar.add(examples);
		menuBar.add(helpMenu);
		
		toolBar.add(stepButton);
		toolBar.add(runButton);
		toolBar.add(stopButton);
		
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		//program memory table setup
		String[] colNames = {"Address (0x)", "Data (0x)"};
		dtm_pgm = new MemoryTableModel(colNames);
		pgmMemTable.setModel(dtm_pgm);
		pgmMemTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		pgmMemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		colRenderer = new PgmColumnColorRenderer(leftRenderer);
		pgmMemTable.getColumnModel().getColumn(0).setCellRenderer(colRenderer);
		colRenderer = new PgmRtColumnColorRenderer(rightRenderer);
		pgmMemTable.getColumnModel().getColumn(1).setCellRenderer(colRenderer);
		
		//data memory table setup
		String[] colNames1  = {"Address (0x)", "Data"};
		dtm_data = new DataTableModel(colNames1);
		dataMemTable.setModel(dtm_data);
		dataMemTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		dataMemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		colRenderer = new PgmColumnColorRenderer(leftRenderer);
		dataMemTable.getColumnModel().getColumn(0).setCellRenderer(colRenderer);
		colRenderer = new ColumnColorRenderer(rightRenderer);
		dataMemTable.getColumnModel().getColumn(1).setCellRenderer(colRenderer);
		
		//port register table setup
		String[] colNames2 = {"Port/Register", "Data"};
		dtm_portReg = new PortRegTableModel(colNames2);
		portRegTable.setModel(dtm_portReg);
		portRegTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		portRegTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		colRenderer = new PgmColumnColorRenderer(leftRenderer);
		portRegTable.getColumnModel().getColumn(0).setCellRenderer(colRenderer);
		colRenderer = new ColumnColorRenderer(rightRenderer);
		portRegTable.getColumnModel().getColumn(1).setCellRenderer(colRenderer);
		
		JTabbedPane accessMemTab = new JTabbedPane();
		accessMemTab.add("Access Memory", dataMemTable);
		accessMemTab.setToolTipText("Address | Contents");
		JTabbedPane portRegTab = new JTabbedPane();
		portRegTab.add("Special Function", portRegTable);
		portRegTab.setToolTipText("Register | Contents");
		//portRegTab.setMaximumSize(new Dimension(200, 300));
		//make access memory / special function vertical split pane and load components
		JSplitPane splitPaneAccSpFunction = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		//splitPaneAccSpFunction.setTopComponent(new JScrollPane(spFunctionPanelComplete));
		splitPaneAccSpFunction.setTopComponent(new JScrollPane(portRegTab));
		splitPaneAccSpFunction.setBottomComponent(new JScrollPane(accessMemTab)); 
		splitPaneAccSpFunction.setDividerLocation(350);
		
		
		JTabbedPane pgmMemTab = new JTabbedPane();
		pgmMemTab.add("Program Memory", pgmMemTable);
		pgmMemTab.setToolTipText("Address | Contents");
		
		JSplitPane rightHalf = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		rightHalf.setLeftComponent(new JScrollPane(pgmMemTab));
		rightHalf.setRightComponent(splitPaneAccSpFunction);
		rightHalf.getLeftComponent().setMinimumSize(new Dimension(200, 300));
		rightHalf.getRightComponent().setMinimumSize(new Dimension(200, 300));
		
		String leftPdf = "./documentation/pic18C39500a.pdf";
		SwingController controller = new SwingController();
		SwingViewBuilder factory = new SwingViewBuilder(controller);
		JPanel viewerComponentPanel = factory.buildViewerPanel();
		ComponentKeyBinding.install(controller, viewerComponentPanel);
		controller.getDocumentViewController().setAnnotationCallback(
			      new org.icepdf.ri.common.MyAnnotationCallback(
			             controller.getDocumentViewController()));
		controller.openDocument(leftPdf);
		
		lstPanel = new LstFileWindow();
		JScrollPane lstPanelScroll = new JScrollPane(lstPanel);
		
	
		JSplitPane whole = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		whole.setLeftComponent(lstPanelScroll);
		whole.setRightComponent(rightHalf);
		whole.setResizeWeight(1);
		
		JTabbedPane mainTabs = new JTabbedPane();
		mainTabs.add("Microprocessor", whole);
		mainTabs.add("Reference", viewerComponentPanel);
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(mainTabs, BorderLayout.CENTER);

		getContentPane().add(mainPanel);
		initialize();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
		setVisible(true);		

	}
	
	class PortRegTable extends JTable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private HashSet<Integer> changes = new HashSet<Integer>();

		public boolean isCellEditable(int arg0, int arg1){
			return false;
		}
		
		public Component prepareRenderer(TableCellRenderer renderer,
                int row, int col) {
					Component c = super.prepareRenderer(renderer, row, col);
					if(changes.contains(row))
						c.setBackground(new Color(0xa9d0f5));
					return c;
		}
		
		public void addChange(Integer c){
			changes.add(c);
		}
		
		public void clearChanges(){
			changes.clear();
		}
	};
	
	class DataMemTable extends JTable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private HashSet<Integer> changes = new HashSet<Integer>();

		public boolean isCellEditable(int arg0, int arg1){
			return false;
		}
		
		public Component prepareRenderer(TableCellRenderer renderer,
                int row, int col) {
					Component c = super.prepareRenderer(renderer, row, col);
					if(changes.contains(row))
						c.setBackground(new Color(0xa9d0f5));
					return c;
		}
		
		public void addChange(Integer c){
			changes.add(c);
		}
		
		public void clearChanges(){
			changes.clear();
		}
	};
	
	class ProgramMemTable extends JTable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int pc = -2;

		public boolean isCellEditable(int arg0, int arg1){
			return false;
		}
		
		//whenever a cell is rendered, if the row matches the pc or pc+1
		//then that row is highlighted in a different color
		public Component prepareRenderer(TableCellRenderer renderer,
                int row, int col) {
					Component c = super.prepareRenderer(renderer, row, col);
					if(row == pc || row == pc + 1)
						c.setBackground(new Color(0xa9d0f5));
					return c;
		}
		
		//Called by MainWindow.updatePC(). Sets current value of pc, then calls fireTableDataChanged()
		//which calls prepareRenderer().
		public void highlightPc(int pc){
			this.pc = pc;
			dtm_pgm.fireTableDataChanged();
		}
	}
	//Changes the properties of a specific cell by getting the cell rendering component from 
	//the default renderer and changing something about that particular cell
	//Extends DefaultCellRenderer so it can access all those functions and override some
	//Regarding getTableCellRendererComponent():
	//From docs.oracle.com: "This interface defines the method required by any object 
	//that would like to be a renderer for cells in a JTable.Returns the component used for  
	//drawing the cell. This method is used to configure the renderer appropriately before drawing."
//	class ColumnRowBackgroundRenderer implements TableCellRenderer{
	class ColumnColorRenderer extends DefaultTableCellRenderer{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected DefaultTableCellRenderer delegate;
		Integer value;
		
		public ColumnColorRenderer(DefaultTableCellRenderer defaultRenderer){
			
			//pointer to table renderer, either rightRenderer or leftRenderer
			//depending on which column it is
			delegate = defaultRenderer;
		}
		@Override
		
		//gets the component used for drawing a cell, sets it to a color, returns it
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			//gets a particular component from the delegate based on row, column
			Component c = delegate.getTableCellRendererComponent(table, value, isSelected, 
					hasFocus, row, column);
			if(!isSelected){
				int modelCol = table.convertColumnIndexToModel(column);
				c.setBackground(modelCol == 1 ? new Color(0xfff0e0) : table.getBackground());
			}
			value = Integer.parseInt(delegate.getText(), 2);
			delegate.setToolTipText("Hexadecimal: " + Integer.toHexString((int)value) + ", Decimal: " +
					Integer.toString((int)value));
			return c;
		}
	}
	
	class CellChangedRenderer extends ColumnColorRenderer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public CellChangedRenderer(DefaultTableCellRenderer defaultRenderer){
			super(defaultRenderer);
		}
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			//gets a particular component from the delegate based on row, column
			Component c = delegate.getTableCellRendererComponent(table, value, isSelected, 
					hasFocus, row, column);
			if(!isSelected){
				c.setBackground(Color.blue);
			}
			return c;
		}
	}
	
	class PgmColumnColorRenderer extends DefaultTableCellRenderer{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private DefaultTableCellRenderer delegate;
		
		public PgmColumnColorRenderer(DefaultTableCellRenderer defaultRenderer){
			
			//pointer to table renderer, either rightRenderer or leftRenderer
			//depending on which column it is
			delegate = defaultRenderer;
	//		Integer value;
		}
		@Override
		
		//gets the component used for drawing a cell, sets it to a color, returns it
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			//gets a particular component from the delegate based on row, column
			Component c = delegate.getTableCellRendererComponent(table, value, isSelected, 
					hasFocus, row, column);
			if(!isSelected){
				int modelCol = table.convertColumnIndexToModel(column);
				c.setBackground(modelCol == 1 ? new Color(0xfff0e0) : table.getBackground());
			}
			return c;
		}
	}
	
	class PgmRtColumnColorRenderer extends DefaultTableCellRenderer{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private DefaultTableCellRenderer delegate;
		
		public PgmRtColumnColorRenderer(DefaultTableCellRenderer defaultRenderer){
			
			//pointer to table renderer, either rightRenderer or leftRenderer
			//depending on which column it is
			delegate = defaultRenderer;
//			Integer value;
		}
		@Override
		
		//gets the component used for drawing a cell, sets it to a color, returns it
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			//gets a particular component from the delegate based on row, column
			Component c = delegate.getTableCellRendererComponent(table, value, isSelected, 
					hasFocus, row, column);
			if(!isSelected){
				int modelCol = table.convertColumnIndexToModel(column);
				c.setBackground(modelCol == 1 ? new Color(0xfff0e0) : table.getBackground());
			}
			value = Integer.parseInt(delegate.getText(), 16);
			delegate.setToolTipText("Binary: " + Integer.toBinaryString((int)value));
			return c;
		}
	}
	
	class MemoryTableModel extends AbstractTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected String[] columnNames;
		protected Vector<Vector<String> > data;
	
		public MemoryTableModel(String[] columnNames){
			this.columnNames = columnNames;
			this.data = new Vector<Vector<String> >();
		}
		
		public void addRow(Vector<String> rowData){
			data.addElement(rowData);
			this.fireTableDataChanged();
		}
		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex).get(columnIndex);
		}
		
		public String getColumnName(int col){
			return columnNames[col];
		}
		
		public void setValueAt(int value, int row, int column){
			if(!(data.get(row).get(column).equals(Integer.toHexString(value)))){
				data.get(row).set(column, Integer.toHexString(value));
				while(data.get(row).get(column).length() < 2)
					data.get(row).set(column, "0" + data.get(row).get(column));
				this.fireTableDataChanged();
			}
		}
		
		public void update(){
			this.fireTableDataChanged();
		}
	}
	
	class PortRegTableModel extends MemoryTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public PortRegTableModel(String[] columnNames){
			super(columnNames);
		}
		
		public void setValueAt(int value, int row, int column){
			data.get(row).set(column, Integer.toBinaryString(value));
			while(data.get(row).get(column).length() < 8)
				data.get(row).set(column, "0" + data.get(row).get(column));
			super.fireTableDataChanged();
		}
	}
	
	class DataTableModel extends MemoryTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public DataTableModel(String[] columnNames){
			super(columnNames);
		}
		
		public void setValueAt(int value, int row, int col){
			data.get(row).set(col, Integer.toBinaryString(value));
			while(data.get(row).get(col).length() < 8)
				data.get(row).set(col, "0" + data.get(row).get(col));
			super.fireTableDataChanged();
		}
	}
	
	class LoadAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int a = fc.showOpenDialog(MainWindow.this);
			if(a == JFileChooser.APPROVE_OPTION)
				fileName = fc.getSelectedFile().toString();
			else return;
			reqCont.loadAction(fileName);	
			System.out.println(fileName);
			lstFileName = fileName.substring(0, fileName.length() - 3) + "lst";
			System.out.println("lstFileName is: " + lstFileName);
			lstPanel.loadLstFile();
			lstPanel.highlight("0000");
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
	
	class QuitAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}	
	}
	
	class LstAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!lstFileName.equals(""))
				//displayLstFile();
				lstPanel.loadLstFile();
			else JOptionPane.showMessageDialog(null, "Cannot display .lst file until a program is loaded", null, 2);
		}
	}
	
	class ExampleAction implements ActionListener{
		private String name = "", directory = "", subDirectory = "";
		
		public ExampleAction(String directory, String name){
			this.name = name;
			this.directory = directory;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(directory.equals("basic") || directory.equals("inDepth"))
				subDirectory = "/" + Character.toString(name.charAt(0))+ "/";
			else subDirectory = "/";
			System.out.println("directory is: " + directory);
			fileName = "./examples/" + directory  + subDirectory + name +
					"/" + name + ".hex";
			reqCont.loadAction(fileName);
			System.out.println(fileName);
			lstFileName = fileName.substring(0, fileName.length() - 3) + "lst";
			System.out.println("lstFileName is: " + lstFileName);
			lstPanel.loadLstFile();
		}
		
	}
	
//	class GeneralConceptsAction implements ActionListener{
//		private String name = "", directory = "";
//
//		public GeneralConceptsAction(String directory, String name){
//			this.name = name;
//			this.directory = directory;
//		}
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			fileName = "./examples/" + directory + "/" + name + "/" + name + ".hex";
//			reqCont.loadAction(fileName);
//			System.out.println(fileName);
//			lstFileName = fileName.substring(0, fileName.length() -3) + "lst";
//			System.out.println("lstFileName is: " + lstFileName);
//			lstPanel.loadLstFile();
//			
//		}
//		
//	}
//	
//	class InDepthAction implements ActionListener{
//		private String name = "", directory = "";
//		
//		public InDepthAction(String directory, String name){
//			this.name = name;
//			this.directory = directory;
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			fileName = "./examples/" + directory + "/" + name + "/" + name + ".hex";
//			reqCont.loadAction(fileName);
//			System.out.println(fileName);
//			lstFileName = fileName.substring(0, fileName.length() -3) + "lst";
//			System.out.println("lstFileName is: " + lstFileName);
//			lstPanel.loadLstFile();
//			
//		}
//	}
	
	class LstFileWindow extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int lineStartPos = 0, lineEndPos = -1, filePos = 0;
		
		//hashmap containing lines that start with a four digit hex value, 
		//which indicates that the line contains an instruction (based on .lst
		//file format). Key is "xxxx", value is integer array where [0] is 
		//line start character number, [1] is line end character number. Will
		//be used by highlighter to know range of characters to highlight when
		//key matches program counter value.
		private HashMap<String, int[]> lineList = new HashMap<String, int[]>();
		private JTextPane textArea = new JTextPane();
		private StringBuilder fileContents; 
		private Highlighter.HighlightPainter highlighter;
		
		//hexNums and hexChars are used to test characters at start of lines read from
		//.lst file to see if the line starts with a hex address, which indicates the  
		//line contains an instruction and thus will need to be highlighted.
		private final Character[] hexNums = {'0', '1', '2', '3', '4', '5', '6', '7', '8', 
				'9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F'};
		private HashSet<Character> hexChars = new HashSet<Character>(Arrays.asList(hexNums));
		private final int NUM_HEX_DIGITS = 4;
		String currentLine = "";
		Font font;
		
		public LstFileWindow(){
			super(new BorderLayout());
			initUI();
		}
		
		public void initUI(){
			highlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
			textArea.setText("No program currently loaded");
			font = new Font(Font.SANS_SERIF, 3, 20);
			textArea.setForeground(Color.BLUE);
			textArea.setFont(font);
			add(textArea);
		}
		
		public void loadLstFile(){
			lineStartPos = 0; lineEndPos = -1; filePos = 0;
			//remove all key value pairs from listList hashmap
			lineList.clear();
			fileContents = new StringBuilder();
			File file;
			String line = null, key;
			int count = 0;
			try{
				file = new File(lstFileName);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				System.out.println("in loadLstFile");
				
				//Reads each line from file, appends to StringBuilder after adding a new line.
				//Then checks beginning of each line to see if the line starts with a 4 digit
				//hex number, which according to the list file format means that the line is a
				//line containing an instruction. If the line contains an instruction, a key/value
				//pair is added to the lineList hashmap, the key containing the first four characters
				//of the line, which is the hexadecimal number (string form) that corresponds to the 
				//program counter value for that line. The corresponding hashmap value are the start 
				//character and end character numbers for that line, which will be used to highlight 
				//that particular line in the text pane. Lines that do not contain instructions are 
				//not placed into the hashmap.
				while((line = reader.readLine()) != null){
					key = "";
					line = line + "\n";
					fileContents.append(line);
					lineStartPos = filePos;
					count = 0;
					while((line.charAt(count) != '\n') && (hexChars.contains(line.charAt(count)))){
						count++;
					}
					if(count == NUM_HEX_DIGITS){
						key = line.substring(0, NUM_HEX_DIGITS);
						lineEndPos = lineStartPos + line.length();
						int [] lineLength = {lineStartPos, lineEndPos};
						lineList.put(key, lineLength);
					}
					filePos += line.length();
				}
				textArea.setText(fileContents.toString());
				font = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
				textArea.setForeground(Color.BLACK);
				textArea.setFont(font);
				reader.close();
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(this, ".lst file not found");
			}
		}
		
		public void highlight(String key){
			try{
				textArea.getHighlighter().removeAllHighlights();
				textArea.getHighlighter().addHighlight(lineList.get(key)[0], lineList.get(key)[1], highlighter);
				System.out.println("in LstFileWindow.highlight(), highlighting: " + lineList.get(key)[0]
						+ ", " + lineList.get(key)[1]);
			}
			catch(BadLocationException e){
				System.out.println(e.getMessage() + " highlight");
			}
			currentLine = key;
		}
	}
	
	private void initialize(){
		reqCont.initialize();
	}
	
	public void initDataMemTable(ArrayList<Integer> dm){
		Vector<String> ob;
		String address, value;
		for(int i = 0; i < dm.size(); i++){
			ob  = new Vector<String>();
			address = Integer.toHexString(i);
			while(address.length() < 3)
				address = "0" + address;
			ob.add(address);
			value = Integer.toBinaryString(dm.get(i));
			while(value.length() < 8)
				value = "0" + value;
			ob.add(value);
			dtm_data.addRow(ob);
		}
	}
	
	//called by ReplyController.updateMemTables(), originally initiated by
	//stepAction or stopAction
	public void updateDataMemTable(ArrayList<Integer> dm) {
		dataMemTable.clearChanges();
		portRegTable.clearChanges();
		for(int i = 0; i < dm.size(); i++){
			if(i == 0x0e8)
				dtm_portReg.setValueAt((int)dm.get(i), 0, 1);
			if(!(Integer.parseInt((String)dtm_data.getValueAt(i, 1), 2) ==(dm.get(i)))){
				if(i == 0x0d8){
					dtm_portReg.setValueAt((int)dm.get(i), 1, 1);
					portRegTable.addChange(1);
				}
				if(i == 0x0e8){
					dtm_portReg.setValueAt((int)dm.get(i), 0, 1);
					portRegTable.addChange(0);
				}
				if(i == 0x080){
					dtm_portReg.setValueAt((int)dm.get(i), 4, 1);
					portRegTable.addChange(4);
				}
				if(i == 0x081){
					dtm_portReg.setValueAt((int)dm.get(i), 6, 1);
					portRegTable.addChange(6);
				}
				if(i == 0x082){
					dtm_portReg.setValueAt((int)dm.get(i), 8, 1);
					portRegTable.addChange(8);
				}
				if(i == 0x083){
					dtm_portReg.setValueAt((int)dm.get(i), 10, 1);
					portRegTable.addChange(10);
				}
				if(i == 0x084){
					dtm_portReg.setValueAt((int)dm.get(i), 12, 1);
					portRegTable.addChange(12);
				}
				if(i == 0xfe0){
					dtm_portReg.setValueAt((int)dm.get(i), 13, 1);
					portRegTable.addChange(13);
				}
				if(i == 0x0e9){
					dtm_portReg.setValueAt((int)dm.get(i), 14, 1);
					portRegTable.addChange(14);
				}
				if(i == 0x0ea){
					dtm_portReg.setValueAt((int)dm.get(i), 15, 1);
					portRegTable.addChange(15);
				}
				if(i == 0x0e1){
					dtm_portReg.setValueAt((int)dm.get(i), 16, 1);
					portRegTable.addChange(16);
				}
				if(i == 0x0e2){
					dtm_portReg.setValueAt((int)dm.get(i), 17, 1);
					portRegTable.addChange(17);
				}
				if(i == 0x0d9){
					dtm_portReg.setValueAt((int)dm.get(i), 18, 1);
					portRegTable.addChange(18);
				}
				if(i == 0x0da){
					dtm_portReg.setValueAt((int)dm.get(i), 19, 1);
					portRegTable.addChange(19);
				}
				dtm_data.setValueAt((int)dm.get(i), i, 1);
				dataMemTable.addChange(i);
			}
		}
	}
	
	public void initPgmMemTable(ArrayList<Integer> pm){
		Vector<String> ob;
		String address, value;
		for(int i = 0; i < pm.size(); i++){
			ob = new Vector<String>();
			address = Integer.toHexString(i);
			while(address.length() < 4)
				address = "0" + address;
			ob.add(address);
			value = Integer.toHexString(pm.get(i));
			value = Integer.toHexString(0);
			if(value.length() < 2)
				value = "0" + value;
			ob.add(value);
			dtm_pgm.addRow(ob);
		}
	}
	/**
	 * 
	 * @param pm - ArrayList of values in pic18 program memory
	 */
	public void updatePgmMemTable(ArrayList<Integer> pm){
		for(int i = 0; i < pm.size(); i++){	
			if(!pm.get(i).equals(0)){
				dtm_pgm.setValueAt((int)pm.get(i), i, 1);
			}

		}	
	}
	
	public void updatePortA(int value){
		dtm_portReg.setValueAt(value, 4, 1);

	}
	
	public void updatePortB(int value){
		dtm_portReg.setValueAt(value, 6, 1);
	}
	
	public void updatePortC(int value){
		dtm_portReg.setValueAt(value, 8, 1);
	}
	
	public void updatePortD(int value){
		dtm_portReg.setValueAt(value, 10, 1);
	}
	
	public void updatePortE(int value){
		dtm_portReg.setValueAt(value, 12, 1);
	}
	
	/**
	 * LoadAction class listens for load menu item to be selected, 
	 * performs action of showing file system so that a file can be chosen
	 * @author hb
	 *
	 */

	
	void displayLstFile(){
		
//		lstFileWindow = new LstFileWindow();
		lstPanel.loadLstFile();
	}
	
//	void startDemo(){
//		DemoController demoController = new DemoController(this);
//	}
	
	/**
	 * Adds values to Ports/Registers table. Loads new table after Stop and Step.
	 * 
	 * @param dm
	 */
	
	public void initPortRegMemTable(ArrayList<Integer> dm){
		Vector<String> ob;
		int i;
		String value;
		
		//add wreg
		ob = new Vector<String>();
		i = 0x0e8;
		ob.add("WREG");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		
		//add status
		ob = new Vector<String>();
		i = 0x0d8;
		ob.add("STATUS");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add intcon
		ob = new Vector<String>();
		i = 0x0f2;
		ob.add("INTCON");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add TRISA
		ob = new Vector<String>();
		i = 0x0f92;
		ob.add("TRISA");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add portA
		ob = new Vector<String>();
		i = 0x080;
		ob.add("PORTA");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add TRISB
		ob = new Vector<String>();
		i = 0x0f93;
		ob.add("TRISB");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add portB
		ob = new Vector<String>();
		i = 0xf81;
		ob.add("PORTB");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add TRISC
		ob = new Vector<String>();
		i = 0x0f94;
		ob.add("TRISC");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add portC
		ob = new Vector<String>();
		i = 0xf82;
		ob.add("PORTC");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add TRISD
		ob = new Vector<String>();
		i = 0x0f95;
		ob.add("TRISD");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add portD
		ob = new Vector<String>();
		i = 0xf83;
		ob.add("PORTD");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add TRISE
		ob = new Vector<String>();
		i = 0x0f96;
		ob.add("TRISE");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add portE
		ob = new Vector<String>();
		i = 0xf84;
		ob.add("PORTE");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);	
		
		//add BSR
		ob = new Vector<String>();
		i = 0xfe0;
		ob.add("BSR");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8){
			value = "0" + value;
		}
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add Fsr0L
		ob = new Vector<String>();
		i = 0x0e9;
		ob.add("FSR0L");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8)
			value = "0" + value;
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add Fsr0h
		ob = new Vector<String>();
		i = 0x0ea;
		ob.add("FSR0H");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8)
			value = "0" + value;
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add Fsr1L
		ob = new Vector<String>();
		i = 0x0e1;
		ob.add("FSR1L");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8)
			value = "0" + value;
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add Fsr1h
		ob = new Vector<String>();
		i = 0x0e2;
		ob.add("FSR1H");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8)
			value = "0" + value;
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add Fsr2L
		ob = new Vector<String>();
		i = 0x0d9;
		ob.add("FSR2L");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8)
			value = "0" + value;
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add Fsr2h
		ob = new Vector<String>();
		i = 0x0da;
		ob.add("FSR2H");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8)
			value = "0" + value;
		ob.add(value);
		dtm_portReg.addRow(ob);
	}

	//Called by ReplyController.updatePC(), which is called by 
	//RequestController loadAction()
	//pc is the row number that needs to be highlighted
	//updates lstFileWindow if it is open, catches exception if it is not
	public void updatePc(int pc) {
		pgmMemTable.highlightPc(pc);
		String pcString = Integer.toHexString(pc);
		pcString = pcString.toUpperCase();
		while(pcString.length() < 4)
			pcString = "0" + pcString;
		try{
			lstPanel.highlight(pcString);
		}
		catch(Exception e){
			System.out.println(e.getMessage() + " exception thrown trying to call "
					+ "lstPanel.highlight(pcString) in updatePc");
		}
	}
}
