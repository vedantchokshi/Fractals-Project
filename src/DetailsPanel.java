import java.awt.*;
import java.io.*;
import javax.swing.*;

/**
 * Class to create the JPanel which displays all the details
 * @author Vedant Chokshi
 */
public class DetailsPanel extends JPanel {
	private int rowNum = 0;
	private GridBagConstraints cns;
	private JTextField complexCoordinates, panelCoordinates, xMin, xMax, yMin, yMax, fileName;
	private JComboBox<String> comboBox;
	private DefaultComboBoxModel<String> modelFiles;
	private JSlider slider;
	private JButton okButton, resetButton, saveButton;
	private JLabel iterations;
	private JCheckBox constantJuliaset;

	/**
	 * Creates a details panel with GridBagLayout
	 */
	public DetailsPanel() {		
		this.setLayout(new GridBagLayout());
		
		cns = new GridBagConstraints(); 
        	cns.insets = new Insets(10, 0, 0, 0);
        
		//Adding elements to the GridBagLayout
		setGridBagConstraints(0, 1, 0.007);
		add(new JLabel("Panel Coordinates:"), cns);

		setGridBagConstraints(1, 0, 0.007);
		panelCoordinates = new JTextField();
		panelCoordinates.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		panelCoordinates.setEditable(false);
		add(panelCoordinates, cns);
		
		rowNum++;
		setGridBagConstraints(0, 0.5, 0.007);
		add(new JLabel("Complex Coordinates:"), cns);

		setGridBagConstraints(1, 0.5, 0.007);
		complexCoordinates = new JTextField();
		complexCoordinates.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		complexCoordinates.setEditable(false);
		add(complexCoordinates, cns);
		
		rowNum++;
		setGridBagConstraints(0, 0.2, 0.007);
		iterations = new JLabel("Max Iterations: 1000");
		add(iterations, cns); 

		setGridBagConstraints(1, 0.8, 0.007);
		slider = new JSlider(0, 10000, 1000);
		slider.setMajorTickSpacing(1000);
		slider.setMinorTickSpacing(500);
		slider.setPaintTicks(true);
		add(slider, cns);
		
		JPanel gridXCoordinates = new JPanel();
		gridXCoordinates.setLayout(new GridLayout(1, 5));
		gridXCoordinates.add(new JLabel("xMin:"));
		
		xMin = new JTextField(3);
		xMin.setText("-2");
		gridXCoordinates.add(xMin);
		
		gridXCoordinates.add(new JLabel(""));
		gridXCoordinates.add(new JLabel("xMax:"));
		
		xMax = new JTextField(3);
		xMax.setText("2");
		gridXCoordinates.add(xMax);
		
		rowNum++;
		setGridBagConstraints(0, 0.5, 0.007);
		cns.gridwidth = 2;
		add(gridXCoordinates, cns);
		
		JPanel gridYCoordinates = new JPanel();
		gridYCoordinates.setLayout(new GridLayout(1, 5));
		
		gridYCoordinates.add(new JLabel("yMin:"));
		
		yMin = new JTextField(3);
		yMin.setText("-1.6");
		gridYCoordinates.add(yMin);
		
		gridYCoordinates.add(new JLabel(""));
		gridYCoordinates.add(new JLabel("yMax:"));
		
		yMax = new JTextField(3);
		yMax.setText("1.6");
		gridYCoordinates.add(yMax);
		
		rowNum++;
		setGridBagConstraints(0, 0.5, 0.007);
		cns.gridwidth = 2;
		add(gridYCoordinates, cns);
		
		rowNum++;
		setGridBagConstraints(0, 1, 0.007);	
		okButton = new JButton("OK");
		okButton.setBackground(Color.LIGHT_GRAY);
		add(okButton, cns);
		
		rowNum++;
		setGridBagConstraints(0, 1, 0.007);	
		resetButton = new JButton("Reset");
		resetButton.setBackground(Color.LIGHT_GRAY);
		add(resetButton, cns);
		
		rowNum++;
		setGridBagConstraints(0, 1, 0.010);
		fileName = new JTextField();
		fileName.setText("Enter the filename of Juliaset");
		add(fileName, cns);
		
		rowNum++;
		setGridBagConstraints(0, 1, 0.007);	
		saveButton = new JButton("Save Juliaset");
		saveButton.setBackground(Color.LIGHT_GRAY);
		add(saveButton, cns);
		
		rowNum++;
		setGridBagConstraints(0, 1, 0.010);
		comboBox = new JComboBox<>();
		File dir = new File("Juliaset");
		if(!dir.exists()){
			dir.mkdir();
		}
		File[] directoryListing = dir.listFiles();
		String[] files = new String[directoryListing.length];
		modelFiles = new DefaultComboBoxModel<String>();
		modelFiles.addElement("");
		if (directoryListing != null) {
			for(int i=0; i < directoryListing.length; i++) {
				String[] x = directoryListing[i].getName().split(".png");
				files[i] = x[0];
				modelFiles.addElement(files[i]);
			}			
			comboBox.setModel(modelFiles);
		}
		comboBox.setBackground(Color.WHITE);
		add(comboBox, cns);
		
		rowNum++;
		setGridBagConstraints(0, 0, 0.007);	
		constantJuliaset = new JCheckBox("Constant Juliaset");
		add(constantJuliaset, cns);
		
		//Fills up empty space so elements don't become too large when resized
		rowNum++;
		setGridBagConstraints(0, 1, 0.800);	
		JPanel fillUpSpace = new JPanel();
		add(fillUpSpace, cns);
	}
	
	/**
	 * Add items to JComboBox
	 * @param fileName name of file that is wished to be added to the JComboBox
	 */
	public void addModelFiles(String fileName) {
		if(modelFiles.getIndexOf(fileName) == -1 ) {
			modelFiles.addElement(fileName);
		} 
	}
	
	/**
	 * Checks if a valid input is entered in the JTextField
	 * @param numToCheck number to check
	 * @param overwrite JTextField corresponding to the number
	 * @return valid number, allowing the program to run without exceptions
	 */
	private double checkDoubles(double numToCheck, JTextField overwrite) {
		/*
		 * While incorrect is true, an input dialog keeps popping up until a valid number is entered. 
		 * This sets incorrect to false, exiting the while loop.
		 */
		boolean incorrect = true;
		while(incorrect){
			try {
				numToCheck = Double.parseDouble(overwrite.getText());
				incorrect = false;
			} catch (NumberFormatException e) {
				overwrite.setText(JOptionPane.showInputDialog(overwrite.getText() + " is an invalid input. Please re-enter"));
			}
		}
		return numToCheck;
	}
	
	/**
	 * Gets an array of the Complex coordinates in the JTextFields
	 * @return array of the Complex coordinates in the JTextFields
	 */
	public double[] getComplexCoordinates() {
		double xMin = 0, xMax = 0, yMin = 0, yMax = 0;
		
		xMin = checkDoubles(xMin, this.xMin);
		xMax = checkDoubles(xMax, this.xMax);
		yMin = checkDoubles(yMin, this.yMin);
		yMax = checkDoubles(yMax, this.yMax);
		
		double[] coordinates = {xMin, xMax, yMax, yMin};
		return coordinates;
	}
	
	/**
	 * Gets okButton
	 * @return okButton
	 */
	public JButton getOkButton() {
		return okButton;
	}

	/**
	 * Gets resetButton
	 * @return resetButton
	 */
	public JButton getResetButton() {
		return resetButton;
	}
	
	/**
	 * Gets saveButton
	 * @return saveButton
	 */
	public JButton getSaveButton() {
		return saveButton;
	}
	
	/**
	 * Gets fileName
	 * @return fileName
	 */
	public String getFileName() {
		return fileName.getText();
	}
	
	/**
	 * Gets comboBox which contains all the saved Juliasets
	 * @return comboBox
	 */
	public JComboBox<String> getComboBox() {
		return comboBox;
	}
	
	/**
	 * Gets JSlider used to manage iterations
	 * @return slider
	 */
	public JSlider getSlider() {
		return slider;
	}

	/**
	 * Gets JCheckBox which checks if the user wants constant Juliaset or not
	 * @return constantJuliaset
	 */
	public JCheckBox getConstantJuliaset() {
		return constantJuliaset;
	}

	/**
	 * Sets value of slider
	 * @param value is the value that the slider will now be set to
	 */
	public void setSliderValue(int value) {
		slider.setValue(value);
	}

	/**
	 * Method to set GridBagConstratints
	 * @param gridx Column number
	 * @param weightx Weight of x
	 * @param weighty Weight of y
	 */
	private void setGridBagConstraints(int gridx,  double weightx, double weighty) {
		cns.gridx = gridx;
        cns.gridy = rowNum;
        cns.weightx = weightx;
        cns.weighty = weighty;
        cns.anchor = GridBagConstraints.LAST_LINE_START;
		cns.fill = GridBagConstraints.BOTH;
	}
	
	/**
	 * Sets JTextField coordinates
	 * @param coordinates array which contains xMin, xMax, yMin, yMax
	 */
	public void setComplexCoordinates(double[] coordinates) {
		xMin.setText(Double.toString(coordinates[0]));
		xMax.setText(Double.toString(coordinates[1]));
		yMax.setText(Double.toString(-coordinates[2]));
		yMin.setText(Double.toString(-coordinates[3]));
	}
	
	/**
	 * Sets complexCoordinates
	 * @param text complex coordinates
	 */
	public void setComplexCoordinates(String text) {
		complexCoordinates.setText(text);
	}
	
	/**
	 * Sets panelCoordinates
	 * @param text panel coordinates
	 */
	public void setPanelCoordinates(String text) {
		panelCoordinates.setText(text);
	}
	
	/**
	 * Sets number of iterations on the JLabel
	 */
	public void setIterations() {
		iterations.setText("Max iterations: " + slider.getValue());
	}
}
