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
import java.io.IOException;
import java.io.InputStreamReader;
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
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

//import org.icepdf.ri.common.*;





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
	private JMenuItem addlw = new JMenuItem("addlw");
	private JMenuItem addwf = new JMenuItem("addwf");
	private JMenuItem addwfc = new JMenuItem("addwfc");
	private JMenuItem andlw = new JMenuItem("andlw");
	private JMenu b = new JMenu("B");
	private JMenuItem bc = new JMenuItem("bc");
	private JMenuItem bcf = new JMenuItem("bcf");
	private JMenuItem bn = new JMenuItem("bn");
	private JMenuItem bnc = new JMenuItem("bnc");
	private JMenuItem bnn = new JMenuItem("bnn");
	private JMenuItem bnov = new JMenuItem("bnov");
	private JMenuItem bnz = new JMenuItem("bnz");
	private JMenuItem bov = new JMenuItem("bov");
	private JMenuItem bra = new JMenuItem("bra");
	private JMenuItem bsf = new JMenuItem("bsf");
	private JMenuItem btfsc = new JMenuItem("btfsc");
	private JMenuItem btfss = new JMenuItem("btfss");
	private JMenuItem btg = new JMenuItem("btg");
	private JMenuItem bz = new JMenuItem("bz");
	private JMenu c = new JMenu("C");
	private JMenuItem clrf = new JMenuItem("clrf");
	private JMenuItem comf = new JMenuItem("comf");
	private JMenuItem cpfseq = new JMenuItem("cpfseq");
	private JMenuItem cpfsgt = new JMenuItem("cpfsgt");
	private JMenuItem cpfslt = new JMenuItem("cpfslt");
	private JMenu d = new JMenu("D");
	private JMenuItem dcfsnz = new JMenuItem("dcfsnz");
	private JMenuItem decf = new JMenuItem("decf");
	private JMenuItem decfsz = new JMenuItem("decfsz");
	private JMenu g = new JMenu("G");
	private JMenuItem gotoItem = new JMenuItem("goto");
	private JMenu i = new JMenu("I");
	private JMenuItem incf = new JMenuItem("incf");
	private JMenuItem incfsz = new JMenuItem("incfsz");
	private JMenuItem infsnz = new JMenuItem("infsnz");
	private JMenuItem iorwf = new JMenuItem("iorwf");
	private JMenu l = new JMenu("L");
	private JMenuItem lfsr = new JMenuItem("lfsr");
	private JMenu m = new JMenu("M");
	private JMenuItem movf = new JMenuItem("movf");
	private JMenuItem movff = new JMenuItem("movff");
	private JMenuItem movlb = new JMenuItem("movlb");
	private JMenuItem movlw = new JMenuItem("movlw");
	private JMenuItem movwf = new JMenuItem("movwf");
	private JMenuItem mullw = new JMenuItem("mullw");
	private JMenuItem mulwf = new JMenuItem("mulwf");
	private JMenu n = new JMenu("N");
	private JMenuItem nop = new JMenuItem("nop");
	private JMenuItem negf = new JMenuItem("negf");
	private JMenu r = new JMenu("R");
	private JMenuItem rcall = new JMenuItem("rcall");
	private JMenuItem returnItem = new JMenuItem("return");
	private JMenuItem rlcf = new JMenuItem("rlcf");
	private JMenuItem rlfnc = new JMenuItem("rlfnc");
	private JMenu s = new JMenu("S");
	private JMenuItem sublw = new JMenuItem("sublw");

	
	private JMenu aID = new JMenu("A");
	private JMenuItem addwfcIDItem = new JMenuItem("addwfc");
	private JMenuItem addwfIDItem = new JMenuItem("addwf");
	private JMenu bID = new JMenu("B");
	private JMenuItem bcIDItem = new JMenuItem("bc");
	private JMenuItem bcfIDItem = new JMenuItem("bcf");
	private JMenuItem bnIDItem = new JMenuItem("bn");
	private JMenuItem bncIDItem = new JMenuItem("bnc");
	private JMenuItem bnnIDItem = new JMenuItem("bnn");
	private JMenuItem bnovIDItem = new JMenuItem("bnov");
	private JMenuItem bovIDItem = new JMenuItem("bov");
	private JMenuItem bsfIDItem = new JMenuItem("bsf");
	private JMenuItem btfscIDItem = new JMenuItem("btfsc");
	private JMenuItem btfssIDItem = new JMenuItem("btfss");
	private JMenuItem btgIDItem = new JMenuItem("btg");
	private JMenuItem bzIDItem = new JMenuItem("bz");
	private JMenu cID = new JMenu("C");
	private JMenuItem clrfIDItem = new JMenuItem("clrf");
	private JMenuItem comfIDItem = new JMenuItem("comf");
	private JMenuItem cpfseqIDItem = new JMenuItem("cpfseq");
	private JMenuItem cpfsgtIDItem = new JMenuItem("cpfsgt");
	private JMenuItem cpfsltIDItem = new JMenuItem("cpfslt");
	private JMenu dID = new JMenu("D");
	private JMenuItem dcfsnzIDItem	= new JMenuItem("dcfsnz");
	private JMenuItem decfIDItem = new JMenuItem("decf");
	private JMenuItem decfszIDItem	= new JMenuItem("decfsz");
	private JMenu iID = new JMenu("I");
	private JMenuItem incfIDItem = new JMenuItem("incf");
	private JMenuItem incfszIDItem = new JMenuItem("incfsz");
	private JMenuItem infsnzIDItem = new JMenuItem("infsnz");
	private JMenuItem iorwfIDItem = new JMenuItem("iorwf");
	private JMenu mID = new JMenu("M");
	private JMenuItem movfIDItem = new JMenuItem("movf");
	private JMenu nID = new JMenu("N");
	private JMenuItem negfIDItem = new JMenuItem("negf");
	private JMenuItem movwfIDItem = new JMenuItem("movwf");
	private JMenuItem mullwIDItem = new JMenuItem("mullw");
	private JMenu rID = new JMenu("R");
	private JMenuItem rlcfIDItem = new JMenuItem("rlcf");
	private JMenuItem rlfncIDItem = new JMenuItem("rlfnc");
	private JMenu sID = new JMenu("S");
	private JMenuItem sublwIDItem = new JMenuItem("sublw");

	private JMenu generalConcepts = new JMenu("General Concepts");
	private JMenu indirectAddressing = new JMenu("Indirect Addressing");
	private JMenuItem indfItem = new JMenuItem("indf");
	private JMenuItem pluswItem = new JMenuItem("plusw");
	private JMenuItem postdecItem = new JMenuItem("postdec");
	private JMenuItem postincItem = new JMenuItem("postinc");
	private JMenuItem preincItem = new JMenuItem("preinc");
	private JMenu loops = new JMenu("Loops");
	private JMenuItem fastPortAToggleItem = new JMenuItem("Fast PORTA Toggle");
	private JMenuItem slowToggleAllPortsItem = new JMenuItem("Slow Toggle All Ports");
	
	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem testItem = new JMenuItem("Test");
	
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
	
	private HashMap<Integer, Integer> portRegList;

	
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
		
		testItem.addActionListener(new HelpAction("instructionTest", "instructionTest"));
//		helpMenu.add(testItem);///////////////////////////////////////////////////////////
		

		examples.add(instructions);
		instructions.add(basic);
		instructions.add(inDepth);
		basic.add(a);
		basic.add(b);
		basic.add(c);
		basic.add(d);
		basic.add(g);
		basic.add(i);
		basic.add(l);
		basic.add(m);
		basic.add(n);
		basic.add(r);
		basic.add(s);
		a.add(addlw);
		addlw.addActionListener(new ExampleAction("basic", "addlw"));
		a.add(addwf);
		addwf.addActionListener(new ExampleAction("basic", "addwf"));
		a.add(addwfc);
		addwfc.addActionListener(new ExampleAction("basic", "addwfc"));
		a.add(andlw);
		andlw.addActionListener(new ExampleAction("basic","andlw"));
		b.add(bc);
		bc.addActionListener(new ExampleAction("basic","bc"));
		b.add(bcf);
		bcf.addActionListener(new ExampleAction("basic","bcf"));
		b.add(bn);
		bn.addActionListener(new ExampleAction("basic","bn"));
		b.add(bnc);
		bnc.addActionListener(new ExampleAction("basic","bnc"));
		b.add(bnn);
		bnn.addActionListener(new ExampleAction("basic","bnn"));
		b.add(bnov);
		bnov.addActionListener(new ExampleAction("basic","bnov"));
		b.add(bnz);
		bnz.addActionListener(new ExampleAction("basic","bnz"));
		b.add(bov);
		bov.addActionListener(new ExampleAction("basic","bov"));
		b.add(bra);
		bra.addActionListener(new ExampleAction("basic","bra"));
		b.add(bsf);
		bsf.addActionListener(new ExampleAction("basic","bsf"));
		b.add(btfsc);
		btfsc.addActionListener(new ExampleAction("basic","btfsc"));
		b.add(btfss);
		btfss.addActionListener(new ExampleAction("basic","btfss"));
		b.add(btg);
		btg.addActionListener(new ExampleAction("basic","btg"));
		b.add(bz);
		bz.addActionListener(new ExampleAction("basic","bz"));
		c.add(clrf);
		clrf.addActionListener(new ExampleAction("basic","clrf"));
		c.add(comf);
		comf.addActionListener(new ExampleAction("basic","comf"));
		c.add(cpfseq);
		cpfseq.addActionListener(new ExampleAction("basic","cpfseq"));
		c.add(cpfsgt);
		cpfsgt.addActionListener(new ExampleAction("basic","cpfsgt"));
		c.add(cpfslt);
		cpfslt.addActionListener(new ExampleAction("basic","cpfslt"));
		d.add(dcfsnz);
		dcfsnz.addActionListener(new ExampleAction("basic","dcfsnz"));
		d.add(decf);
		decf.addActionListener(new ExampleAction("basic","decf"));
		d.add(decfsz);
		decfsz.addActionListener(new ExampleAction("basic","decfsz"));
		g.add(gotoItem);
		gotoItem.addActionListener(new ExampleAction("basic","goto"));
		i.add(incf);
		incf.addActionListener(new ExampleAction("basic", "incf"));
		i.add(incfsz);
		incfsz.addActionListener(new ExampleAction("basic", "incfsz"));
		i.add(infsnz);
		infsnz.addActionListener(new ExampleAction("basic", "infsnz"));
		i.add(iorwf);
		iorwf.addActionListener(new ExampleAction("basic","iorwf"));
		l.add(lfsr);
		lfsr.addActionListener(new ExampleAction("basic","lfsr"));
		m.add(movf);
		movf.addActionListener(new ExampleAction("basic","movf"));
		m.add(movff);
		movff.addActionListener(new ExampleAction("basic","movff"));
		m.add(movlb);
		movlb.addActionListener(new ExampleAction("basic","movlb"));
		m.add(movlw);
		movlw.addActionListener(new ExampleAction("basic","movlw"));
		m.add(movwf);
		movwf.addActionListener(new ExampleAction("basic", "movwf"));
		m.add(mullw);
		mullw.addActionListener(new ExampleAction("basic", "mullw"));
		m.add(mulwf);
		mulwf.addActionListener(new ExampleAction("basic", "mulwf"));
		n.add(nop);
		nop.addActionListener(new ExampleAction("basic", "nop"));
		n.add(negf);
		negf.addActionListener(new ExampleAction("basic", "negf"));
		r.add(rcall);
		rcall.addActionListener(new ExampleAction("basic", "rcall"));
		r.add(returnItem);
		returnItem.addActionListener(new ExampleAction("basic", "return"));
		r.add(rlcf);
		rlcf.addActionListener(new ExampleAction("basic", "rlcf"));
		r.add(rlfnc);
		rlfnc.addActionListener(new ExampleAction("basic", "rlfnc"));
		s.add(sublw);
		sublw.addActionListener(new ExampleAction("basic", "sublw"));
		
		inDepth.add(aID);
		inDepth.add(bID);
		inDepth.add(cID);
		inDepth.add(dID);
		inDepth.add(iID);
		inDepth.add(mID);
		inDepth.add(nID);
		inDepth.add(rID);
		inDepth.add(sID);
		aID.add(addwfcIDItem);
		addwfcIDItem.addActionListener(new ExampleAction("inDepth", "addwfc"));
		aID.add(addwfIDItem);
		addwfIDItem.addActionListener(new ExampleAction("inDepth", "addwf"));
		bID.add(bcIDItem);
		bcIDItem.addActionListener(new ExampleAction("inDepth", "bc"));
		bID.add(bcfIDItem);
		bcfIDItem.addActionListener(new ExampleAction("inDepth", "bcf"));
		bID.add(bnIDItem);
		bnIDItem.addActionListener(new ExampleAction("inDepth", "bn"));
		bID.add(bncIDItem);
		bncIDItem.addActionListener(new ExampleAction("inDepth", "bnc"));
		bID.add(bnnIDItem);
		bnnIDItem.addActionListener(new ExampleAction("inDepth", "bnn"));
		bID.add(bnovIDItem);
		bnovIDItem.addActionListener(new ExampleAction("inDepth", "bnov"));
		bID.add(bovIDItem);
		bovIDItem.addActionListener(new ExampleAction("inDepth", "bov"));
		bID.add(bsfIDItem);
		bsfIDItem.addActionListener(new ExampleAction("inDepth", "bsf"));
		bID.add(btfscIDItem);
		btfscIDItem.addActionListener(new ExampleAction("inDepth", "btfsc"));
		bID.add(btfssIDItem);
		btfssIDItem.addActionListener(new ExampleAction("inDepth", "btfss"));
		bID.add(btgIDItem);
		btgIDItem.addActionListener(new ExampleAction("inDepth", "btg"));
		bID.add(bzIDItem);
		bzIDItem.addActionListener(new ExampleAction("inDepth", "bz"));
		cID.add(clrfIDItem);
		clrfIDItem.addActionListener(new ExampleAction("inDepth", "clrf"));
		cID.add(comfIDItem);
		comfIDItem.addActionListener(new ExampleAction("inDepth", "comf"));
		cID.add(cpfseqIDItem);
		cpfseqIDItem.addActionListener(new ExampleAction("inDepth", "cpfseq"));
		cID.add(cpfsgtIDItem);
		cpfsgtIDItem.addActionListener(new ExampleAction("inDepth", "cpfsgt"));
		cID.add(cpfsltIDItem);
		cpfsltIDItem.addActionListener(new ExampleAction("inDepth", "cpfslt"));
		dID.add(dcfsnzIDItem);
		dcfsnzIDItem.addActionListener(new ExampleAction("inDepth", "dcfsnz"));
		dID.add(decfIDItem);
		decfIDItem.addActionListener(new ExampleAction("inDepth", "decf"));
		dID.add(decfszIDItem);
		decfszIDItem.addActionListener(new ExampleAction("inDepth", "decfsz"));
		iID.add(incfIDItem);
		incfIDItem.addActionListener(new ExampleAction("inDepth", "incf"));
		iID.add(incfszIDItem);
		incfszIDItem.addActionListener(new ExampleAction("inDepth", "incfsz"));
		iID.add(infsnzIDItem);
		infsnzIDItem.addActionListener(new ExampleAction("inDepth", "infsnz"));
		iID.add(iorwfIDItem);
		iorwfIDItem.addActionListener(new ExampleAction("inDepth", "iorwf"));
		mID.add(movfIDItem);
		movfIDItem.addActionListener(new ExampleAction("inDepth", "movf"));
		mID.add(movwfIDItem);
		movwfIDItem.addActionListener(new ExampleAction("inDepth", "movwf"));
		mID.add(mullwIDItem);
		mullwIDItem.addActionListener(new ExampleAction("inDepth", "mullw"));
		nID.add(negfIDItem);
		negfIDItem.addActionListener(new ExampleAction("inDepth", "negf"));
		rID.add(rlcfIDItem);
		rlcfIDItem.addActionListener(new ExampleAction("inDepth", "rlcf"));
		rID.add(rlfncIDItem);
		rlfncIDItem.addActionListener(new ExampleAction("inDepth", "rlfnc"));
		sID.add(sublwIDItem);
		sublwIDItem.addActionListener(new ExampleAction("inDepth", "sublw"));
		
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
		loops.add(slowToggleAllPortsItem);
		slowToggleAllPortsItem.addActionListener(new ExampleAction("loops", "slowToggleAllPorts"));

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
		colRenderer = new PgmColumnColorRenderer(leftRenderer);
		pgmMemTable.getColumnModel().getColumn(0).setCellRenderer(colRenderer);
		colRenderer = new PgmRtColumnColorRenderer(rightRenderer);
		pgmMemTable.getColumnModel().getColumn(1).setCellRenderer(colRenderer);
		
		//data memory table setup
		String[] colNames1  = {"Address (0x)", "Data"};
		dtm_data = new DataTableModel(colNames1);
		dataMemTable.setModel(dtm_data);
		colRenderer = new PgmColumnColorRenderer(leftRenderer);
		dataMemTable.getColumnModel().getColumn(0).setCellRenderer(colRenderer);
		colRenderer = new ColumnColorRenderer(rightRenderer);
		dataMemTable.getColumnModel().getColumn(1).setCellRenderer(colRenderer);
		
		//port register table setup
		String[] colNames2 = {"Port/Register", "Data"};
		dtm_portReg = new PortRegTableModel(colNames2);
		portRegTable.setModel(dtm_portReg);
		colRenderer = new PgmColumnColorRenderer(leftRenderer);
		portRegTable.getColumnModel().getColumn(0).setCellRenderer(colRenderer);
		colRenderer = new ColumnColorRenderer(rightRenderer);
		portRegTable.getColumnModel().getColumn(1).setCellRenderer(colRenderer);
		
		JTabbedPane accessMemTab = new JTabbedPane();
		accessMemTab.add("Data Memory", dataMemTable);
		accessMemTab.setToolTipText("Address | Contents");
		JTabbedPane portRegTab = new JTabbedPane();
		portRegTab.add("Special Function", portRegTable);
		portRegTab.setToolTipText("Register | Contents");
		
		//make access memory / special function vertical split pane and load components
		JSplitPane splitPaneAccSpFunction = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
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
		
		//this creates an ICEpdf jpanel showing the pic user manual in a separate tab
//		String leftPdf = "./documentation/pic18C39500a.pdf";
//		SwingController controller = new SwingController();
//		SwingViewBuilder factory = new SwingViewBuilder(controller);
//		JPanel viewerComponentPanel = factory.buildViewerPanel();
//		ComponentKeyBinding.install(controller, viewerComponentPanel);
//		controller.getDocumentViewController().setAnnotationCallback(
//			      new org.icepdf.ri.common.MyAnnotationCallback(
//			             controller.getDocumentViewController()));
//		controller.openDocument(leftPdf);
		
		lstPanel = new LstFileWindow();
		JScrollPane lstPanelScroll = new JScrollPane(lstPanel);
		
	
		JSplitPane whole = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		whole.setLeftComponent(lstPanelScroll);
		whole.setRightComponent(rightHalf);
		whole.setResizeWeight(1);
		
		JTabbedPane mainTabs = new JTabbedPane();
		mainTabs.add("Pic18F452", whole);
		
		//adds tab showing pic usr manual, removed for license considerations
//		mainTabs.add("Reference", viewerComponentPanel);
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(mainTabs, BorderLayout.CENTER);

		getContentPane().add(mainPanel);
		initialize();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
		setVisible(true);		
		
		//initialize hashmap for portRegTable  changes during stepping
		//(address of change, row in portRegTable)
		portRegList = new HashMap<Integer, Integer>();
		portRegList.put(0xfd8, 1);
		portRegList.put(0xfe8, 0);
		portRegList.put(0xf80, 4);
		portRegList.put(0xf81, 6);
		portRegList.put(0xf82, 8);
		portRegList.put(0xf83, 10);
		portRegList.put(0xf84, 12);
		portRegList.put(0xfe0, 13);
		portRegList.put(0xfe9, 14);
		portRegList.put(0xfea, 15);
		portRegList.put(0xfe1, 16);
		portRegList.put(0xfe2, 17);
		portRegList.put(0xfd9, 18);
		portRegList.put(0xfda, 19);
		portRegList.put(0xff3, 20);
		portRegList.put(0xff4, 21);
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
		
		public void setValueAt(int value, int row, int col){
			data.get(row).set(col, Integer.toBinaryString(value));
			while(data.get(row).get(col).length() < 8)
				data.get(row).set(col, "0" + data.get(row).get(col));
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
			if(reqCont.loadAction(fileName)){
//				System.out.println(fileName);
				lstFileName = fileName.substring(0, fileName.length() - 3) + "lst";
//				System.out.println("lstFileName is: " + lstFileName);
				lstPanel.loadLstFile();
				lstPanel.highlight("0000");
			}
			else 			
				JOptionPane.showMessageDialog(null, "Stop current program before loading a new program");
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
//			System.out.println("directory is: " + directory);
			
			fileName = "/" + directory  + subDirectory + name +
					"/" + name + ".hex";
			
			if(reqCont.loadAction(fileName)){
//				System.out.println(fileName);
				lstFileName = fileName.substring(0, fileName.length() - 3) + "lst";
				System.out.println("lstFileName is: " + lstFileName);
				lstPanel.loadLstFile();
			}
			else 
				JOptionPane.showMessageDialog(null, "Stop current program before loading a new program");
		}
	}
	
	class HelpAction implements ActionListener{
		private String name = "", directory = "";
		
		public HelpAction(String directory, String name){
			this.name = name;
			this.directory = directory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fileName = "./tests/" + directory + "/" + name + ".hex";
			reqCont.loadAction(fileName);
//			System.out.println(fileName);
			lstFileName = fileName.substring(0, fileName.length() - 3) + "lst";
//			System.out.println("lstFileName is: " + lstFileName);
			lstPanel.loadLstFile();	
			reqCont.testAction();
		}
	}
	
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
			//remove all key value pairs from lineList hashmap
			lineList.clear();
			fileContents = new StringBuilder();
			@SuppressWarnings("unused")
			File file;
			String line = null, key;
			int count = 0;
			BufferedReader reader = null;
			try{
				
				//checks first in the example directory in classpath
				reader = new BufferedReader(new InputStreamReader(
	                    this.getClass().getResourceAsStream(lstFileName)));
//				System.out.println("in loadLstFile");
			}
			catch(Exception e){
				System.out.println("in loadLstFile, " + lstFileName + " not found in example directory");
				
				//if file not found in examples, tries to find it in local file system
				try{
					file = new File(lstFileName);
					reader = new BufferedReader(new FileReader(file));
					System.out.println("in loadLstFile, " + lstFileName + " was found in local file system");
				}
				catch(Exception ex){
					JOptionPane.showMessageDialog(this, "in loadLstFile, .lst file not found in example directory or local file system");
				}
			}
				
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
				try {
//					while((line = reader.readLine()) != null){
					while(((reader != null) && (line = reader.readLine()) != null)){
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
				} catch (IOException e) {
					e.printStackTrace();
				}
				textArea.setText(fileContents.toString());
				font = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
				textArea.setForeground(Color.BLACK);
				textArea.setFont(font);
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		public void highlight(String key){
			try{
				textArea.getHighlighter().removeAllHighlights();
				textArea.getHighlighter().addHighlight(lineList.get(key)[0], lineList.get(key)[1], highlighter);
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
	
	//called by ReplyController.updateMemTables(Object[]), originally initiated by
	//RequestController.stepAction(). Receives object array containing hashset of which 
	//addresses in data memory were written to during the previous instruction and the
	//arraylist of data memory
	public void updateDataMemTable(Object[] dm_changes){
		dataMemTable.clearChanges();
		portRegTable.clearChanges();
		HashSet<Integer> changes = (HashSet<Integer>)dm_changes[0];
		ArrayList<Integer> dm = (ArrayList<Integer>) dm_changes[1];
		for(Integer change : changes){
//			System.out.println("change: " + Integer.toHexString(change));
			//for every change, if it's in the ports/reg list, change the value
			if(portRegList.containsKey(change)){
				dtm_portReg.setValueAt((int)dm.get(change), (int)portRegList.get(change), 1);
				portRegTable.addChange((int)portRegList.get(change));
			}
			dtm_data.setValueAt((int)dm.get(change), (int)change, 1);
			dataMemTable.addChange(change);
		}
		//updates port/reg table even if no changes were written, turns the previous
		//step's highlighting off if no changes were written to portRegTable during 
		//current step.
		portRegTable.tableChanged(new TableModelEvent(dtm_portReg));
	}
		
	//called by ReplyController.updateMemTables(ArrayList), originally initiated by
	//RequestController.stopAction(). Since no updates are made to the pic18.changes
	//hashmap in run mode, this method receives the data memory array and compares 
	//values currently in the jtable with values in the data memory array. If there 
	//are differences, the jtable is updated for that particular row. If that row also
	//belongs in the ports/registers jtable, that jtable's row is updated as well. The 
	//purpose of all this is to show what is different now in the data memory compared 
	//to when run mode was initiated.
	public void updateDataMemTable(ArrayList<Integer> dm) {
		dataMemTable.clearChanges();
		portRegTable.clearChanges();
		for(int i = 0; i < dm.size(); i++){
			
			//if value currently in jtable is different from value in pic data memory
			if(!(Integer.parseInt((String)dtm_data.getValueAt(i, 1), 2) == (dm.get(i)))){
				dtm_data.setValueAt((int)dm.get(i), i, 1);
				dataMemTable.addChange(i);
				if(portRegList.containsKey(i)){
					dtm_portReg.setValueAt((int)dm.get(i), (int)portRegList.get(i), 1);
					portRegTable.addChange((int)portRegList.get(i));
				}	
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
		
		//add ProdL
		ob = new Vector<String>();
		i = 0x0f3;
		ob.add("PRODL");
		value = Integer.toBinaryString(dm.get(i));
		while(value.length() < 8)
			value = "0" + value;
		ob.add(value);
		dtm_portReg.addRow(ob);
		
		//add Prodh
		ob = new Vector<String>();
		i = 0x0f4;
		ob.add("PRODH");
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
