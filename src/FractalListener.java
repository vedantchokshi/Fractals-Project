import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Listener which controls all the GUI
 * @author Vedant Chokshi
 */
public class FractalListener implements MouseMotionListener, MouseListener, ActionListener, ChangeListener, ComponentListener {
	private DetailsPanel detailsPanel;
	private Fractal mandelbrot, juliaset;
	private int x1, x2, y1, y2, x, y, w, h;
	private int xMin, xMax, yMin, yMax;
	private boolean isNewRect = false, constantJulia = false;
	
	/**
	 * 
	 * @param detailsPanel
	 * @param mandelbrotPanel
	 * @param juliaset
	 */
	public FractalListener(DetailsPanel detailsPanel, Fractal mandelbrotPanel, Fractal juliaset) {
		this.detailsPanel = detailsPanel;
		this.mandelbrot = mandelbrotPanel;
		this.juliaset = juliaset;
	}
	
	/**
	 * Generates Juliasets according to mouse movements
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		setJLabels(e.getX(), e.getY());
		if(constantJulia && !mandelbrot.checkIfThreading()) {
			juliaset.generateJuliaset(new Complex(mandelbrot.translateCoordinates(e.getX(), e.getY()).getReal(), mandelbrot.translateCoordinates(e.getX(), e.getY()).getImaginary()));	
		}		
	}
	
	/**
	 * Generates Juliasets according to the point the mouse clicks on
	 */
	@Override
	public void mouseClicked(MouseEvent e) {	
		if(!constantJulia && !mandelbrot.checkIfThreading()) {
			juliaset.generateJuliaset(new Complex(mandelbrot.translateCoordinates(e.getX(), e.getY()).getReal(), mandelbrot.translateCoordinates(e.getX(), e.getY()).getImaginary()));
		}
	}
	
	/**
	 * If mousedragged with left button, then zoom
	 * If mousedragged with right, then pan
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e) && !mandelbrot.checkIfThreading()) {
			setJLabels(e.getX(), e.getY());
			x2 = e.getX();
			y2 = e.getY();
			
			int width = x1 - x2; 
	    	int height = y1 - y2;

	    	w = Math.abs(width);
	    	h = Math.abs(height);
	    	x = width < 0 ? x1 : x2;
	    	y = height < 0 ? y1 : y2;
			
			mandelbrot.setRectangleDimensions(x, y, w, h);
			mandelbrot.repaint();
			isNewRect = true;
		} else if (!mandelbrot.checkIfThreading()){
			 mandelbrot.setDrawingCoordinates(mandelbrot.getDrawingCoordinates(0) - (x1 - e.getX()), mandelbrot.getDrawingCoordinates(1) - (y1 - e.getY()));

             Complex temp = mandelbrot.translateCoordinates(x1 , y1);
             double[] mouse = new double[] {temp.getReal(), temp.getImaginary()};
             temp = mandelbrot.translateCoordinates(e.getX(), e.getY());
             double[] current = new double[] {temp.getReal(), temp.getImaginary()};
             
             double[] d = new double[] {0.0, 0.0};
             for (int i = 0; i < 2; i++)
             {
                 d[i] = (mouse[i] - current[i]);
             }
             mandelbrot.setComplexCoordinates(new double[] {mandelbrot.getxMin() + d[0], mandelbrot.getxMax() + d[0], mandelbrot.getyMin() - d[1], mandelbrot.getyMax() - d[1]});
             x1 = e.getX();
             y1 = e.getY();
             mandelbrot.repaint();
         
		}
		
	}

	/**
	 * Sets intial point of click so calculations for zoom and pan can be made
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(!mandelbrot.checkIfThreading()) {
			x1 = e.getX();
			xMin = x1;
			y1 = e.getY();
			yMin = y1;
		}   	
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e) && !mandelbrot.checkIfThreading()) {
			if(isNewRect) {
				x2 = e.getX();
				xMax = x2;
		    	y2 = e.getY();
		    	yMax = y2;
				
				mandelbrot.setRectangleDimensions(0, 0, 0, 0);
				
				int temp = Math.min(xMin, xMax);
				xMax = Math.max(xMin, xMax);
				xMin = temp;
				
				temp = Math.min(yMin, yMax);
				yMax = Math.max(yMin, yMax);
				yMin = temp;
				
				mandelbrot.setCoordinates(xMin, xMax, yMin, yMax);
				detailsPanel.setComplexCoordinates(mandelbrot.getComplexCoordinates());
				mandelbrot.generateMandelbrotSet();
				isNewRect = false;
			}
		} else if (!mandelbrot.checkIfThreading()){
			mandelbrot.setDrawingCoordinates(0, 0);
			mandelbrot.generateMandelbrotSet();
			detailsPanel.setComplexCoordinates(mandelbrot.getComplexCoordinates());
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	/**
	 * ActionListener to set coordinates, open saved Juliasets and enable or disable live Juliaset
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//Does action events for Buttons
		if(!mandelbrot.checkIfThreading() && e.getSource() instanceof JButton) {
			switch ((e.getActionCommand())) {
			case "OK":
				double[] coordinates = detailsPanel.getComplexCoordinates();
				if((coordinates[0] < coordinates[1]) && (coordinates[3] < coordinates[2])) {
					mandelbrot.setComplexCoordinates(coordinates);
					mandelbrot.generateMandelbrotSet();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid complex grid coordinates entered. Please re-enter the grid coordinates");
				}
				break;
			case "Reset":
				mandelbrot.setComplexCoordinates(new double[] {-2, 2, -1.6, 1.6});
				mandelbrot.setDrawingCoordinates(0, 0);
				mandelbrot.setMaxIterations(1000);
				detailsPanel.setComplexCoordinates(mandelbrot.getComplexCoordinates());
				detailsPanel.setSliderValue(1000);
				detailsPanel.setIterations();
				mandelbrot.generateMandelbrotSet();
				break;
			case "Save Juliaset":
				try {
					juliaset.save(detailsPanel.getFileName());
					detailsPanel.addModelFiles(detailsPanel.getFileName());
					JOptionPane.showMessageDialog(null, "Juliaset saved");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "No point selected in the Mandelbrot Set. Please select a point.");
				}
				break;
			default:
				break;
			}
		//Does action events for JComboBox
		} else if (!mandelbrot.checkIfThreading() && e.getSource() instanceof JComboBox) {
			String fileName = (String) detailsPanel.getComboBox().getSelectedItem();
			
			SwingUtilities.invokeLater (new Runnable () {
				public void run() {
					JFrame juliaset = new JFrame(fileName);
					try {						
						if(!fileName.equals("")) {
							File folder = new File("Juliaset");
							JFileChooser path = new JFileChooser();
							File image = new File(folder, fileName + ".png");
							path.setSelectedFile(image);
							BufferedImage img = ImageIO.read(path.getSelectedFile());
							JLabel pictureLabel = new JLabel();
							pictureLabel.setIcon(new ImageIcon(img));
							juliaset.add(pictureLabel);
							juliaset.pack();
				        	juliaset.setVisible(true);
						}			        				        	
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
			});	
		//Does action event for JCheckBox
		} else if(!mandelbrot.checkIfThreading() && e.getSource() instanceof JCheckBox) {
			if(e.getActionCommand().equals("Constant Juliaset")) {
				constantJulia = detailsPanel.getConstantJuliaset().isSelected();
			}		
		}		
	}
	
	/**
	 * Iterations changed according to JSlider movement
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider)e.getSource();
		detailsPanel.setIterations();
		mandelbrot.setMaxIterations(slider.getValue());
		mandelbrot.generateMandelbrotSet();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	/**
	 * Resizes mandelbrot if window is resized
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		if (!mandelbrot.checkIfThreading()) {
			mandelbrot.generateMandelbrotSet();
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	/**
	 * Sets Complex and panel coordinates in the details panel
	 * @param x mouseposition x
	 * @param y mouseposition y
	 */
	public void setJLabels(int x, int y) {
		double real = Math.round((mandelbrot.translateCoordinates(x, y).getReal()) * 1000d) / 1000d;
		double imaginary = -Math.round((mandelbrot.translateCoordinates(x, y).getImaginary()) * 1000d) / 1000d;
		
		detailsPanel.setPanelCoordinates("(" + x + ", " + y + ")");
		
		if(imaginary < 0) {
			detailsPanel.setComplexCoordinates(real + " + " + -imaginary + "i");
		} else {
			detailsPanel.setComplexCoordinates(real + " - " + imaginary + "i");
		}
	}
}
