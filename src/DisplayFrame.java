import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Creates the GUI
 * @author Vedant
 */
public class DisplayFrame extends JFrame {
	private Fractal set, juliaset;
	private DetailsPanel panel;
	private FractalListener listener;
	private GridBagConstraints cns; 
	
	public DisplayFrame(String name) {
		super(name);
	}
	
	/**
	 * Adds the Details Panel, Mandelbrotset, and Juliaset to the Frame
	 * Adds listeners to the components in the Details Panel 
	 */
	public void init() {
		this.setSize(3200, 1200);
		
		JPanel container = new JPanel();
		TitledBorder setBorder = BorderFactory.createTitledBorder("Fractals");
		container.setBorder(setBorder);
		this.setContentPane(container);
		container.setLayout(new GridBagLayout());
		
		set = new Fractal(1000);
		panel = new DetailsPanel();	
		juliaset = new Fractal(100);
		
		listener = new FractalListener(panel, set, juliaset);
		set.addMouseMotionListener(listener);
		set.addMouseListener(listener);
		set.addComponentListener(listener);
		panel.getOkButton().addActionListener(listener);
		panel.getResetButton().addActionListener(listener);
		panel.getSaveButton().addActionListener(listener);
		panel.getComboBox().addActionListener(listener);
		panel.getSlider().addChangeListener(listener);
		panel.getConstantJuliaset().addActionListener(listener);
		
		cns = new GridBagConstraints();
        		
        setGridBagConstraints(0, 0, 0.02, 1);
        TitledBorder panelBorder = BorderFactory.createTitledBorder("Details");
        panel.setBorder(panelBorder);
        container.add(panel, cns);
        
        setGridBagConstraints(1, 0, 0.49, 1);
        container.add(set, cns);
        set.generateMandelbrotSet();
        
        setGridBagConstraints(3, 0, 0.49, 1);
        container.add(juliaset, cns);
        
        this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Method to set GridBagConstratints
	 * @param gridx Column number
	 * @param gridy Row number
	 * @param weightx Weight of x
	 * @param weighty Weight of y
	 */
	private void setGridBagConstraints(int gridx, int gridy, double weightx, double weighty) {
		cns.gridx = gridx;
        cns.gridy = gridy;
        cns.weightx = weightx;
        cns.weighty = weighty;
        cns.anchor = GridBagConstraints.LAST_LINE_START;
		cns.fill = GridBagConstraints.BOTH;
	}
}
