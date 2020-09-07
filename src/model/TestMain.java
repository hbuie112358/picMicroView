package model;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class TestMain {
	
	private ArrayList<TestInstruction> testInstructions;
	private Pic18F452 pic18;
	

	public TestMain(Pic18F452 pic18) {
		this.pic18 = pic18;	
		testInstructions = new ArrayList<TestInstruction>();
	}
	
	void execute(){
		pic18.setStepState();
		int nopCount = 1, address, value;
		String addressString = "", valueString = "", instructionString = "";
		while(nopCount < 4){
			address = 0;
			value = 0;
			pic18.run();	
			for(Integer iter : pic18.changes){
				address = (int)iter;
				value = pic18.getDataMem().gpMem[address].getContents();
			}
			testInstructions.add(new TestInstruction(pic18.getInstruction(), address, value));
			if(pic18.getInstruction() == 0)
				nopCount++;
			else nopCount = 0;
		}
		for(int i = 0; i < testInstructions.size(); i++){
			instructionString = Integer.toHexString(testInstructions.get(i).instruction);
			while(instructionString.length() < 4)
				instructionString = "0" + instructionString;
			valueString = Integer.toHexString(testInstructions.get(i).value);
			while(valueString.length() < 2)
				valueString = "0" + valueString;
			addressString = Integer.toHexString(testInstructions.get(i).address);
			while(addressString.length() < 3)
				addressString = "0" + addressString;
			
			System.out.println("instruction " + instructionString +
					" wrote 0x" + valueString + " to address 0x" + 
					addressString);
		}
		int selection = JOptionPane.showConfirmDialog(null, 
				"Do you want to commit these results to the default file?",
				 "Commit?", JOptionPane.YES_NO_OPTION);
		if(selection == JOptionPane.YES_OPTION){
			try{
				FileOutputStream fout = new FileOutputStream("./tests/instructionTest/insTestDefault");
				ObjectOutputStream oout = new ObjectOutputStream(fout);
				try{
					oout.writeObject(testInstructions);
				}
				finally{
					oout.close();
					JOptionPane.showMessageDialog(null, "Finished writing insTestDefault to file", 
							"Commit complete", 2);
					System.out.println("Finished writing insTestDefault to file");
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}

		}
		else{
			JOptionPane.showMessageDialog(null, "Default test file not changed", 
					"Declined commit", 2);
		}

	}
}
