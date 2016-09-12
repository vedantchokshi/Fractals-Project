import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Class Fractal makes Mandelbrot and Juliaset
 * @author Vedant Chokshi
 */
public class Fractal extends JPanel {	
	private double xMin, xMax, yMin, yMax;
	private int maxIterations, maxThreads, rX, rY, rW, rH;	
	private BufferedImage fractal;
	private int[] drawingCoordinates;
	private boolean running = false;

	/**
	 * Constructor for Fractal which takes in the number of iterations
	 * @param iterations number of iterations for the fractal
	 */
	public Fractal(int iterations) {
		this.setBackground(new Color(0, 255, 138));
		xMin = -2;
		xMax = 2;
		yMin = -1.6;
		yMax = 1.6;
		maxIterations = iterations;
		maxThreads = Runtime.getRuntime().availableProcessors();
		setDrawingCoordinates(0, 0);
	}
	
	/**
	 * Converts from screen pixel to Complex coordinate
	 * @param x x point
	 * @param y y point
	 * @return new converted Complex number
	 */
	public Complex translateCoordinates(double x, double y) {
		double real = xMin + ((x/this.getWidth())*(xMax - xMin));
		double imaginary =  -(yMin + ((y/this.getHeight())*(yMax - yMin)));
		return new Complex(real, imaginary);
	}

	public void generateMandelbrotSet() {
		generateMandelbrotSet(this.getWidth(), this.getHeight());
	}
	
	private void generateMandelbrotSet(int x, int y) {
		if(!running && (x!=0||y!=0)) {
			SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() {
					running = true;
					fractal = threadMandelbrot(x, y);
					running = false;
					repaint();
					return null;
				}				
			};
			sw.execute();
		}
	}
	
	/**
	 * Threads mandelbrot set
	 * @param x width
	 * @param y height
	 * @return mandelbrot set
	 */
	private BufferedImage threadMandelbrot(int x, int y) {
		BufferedImage mandelbrot = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		ConcurrentHashMap<Integer, BufferedImage> hm = new ConcurrentHashMap<>();
		CountDownLatch c = new CountDownLatch(maxThreads);
		
		int y1, split;
		y1 = 0;
		split = y/maxThreads;
		//Loops through splitted regions and makes them
		for (int i = 0; i < maxThreads - 1; i++) {
			MandelbrotPartBuilder p = new MandelbrotPartBuilder(y1, y1+split, hm, c);
			p.execute();
			y1+=split;
		}
		MandelbrotPartBuilder p = new MandelbrotPartBuilder(y1, y, hm, c);
		p.execute();
		
		try {
			//Awaits for all the threads to complete then draws the image
			c.await();
			for (int key : hm.keySet()) {
				mandelbrot.createGraphics().drawImage(hm.get(key), 0, key, null);
			}
		} catch (Exception e) {};		
		return mandelbrot;
	}
	
	public void generateJuliaset(Complex d) {
		generateJuliaset(this.getWidth(), this.getHeight(), d);
	}
	
	private void generateJuliaset(int x, int y, Complex d) {
		if(!running && !(x==0||y==0)) {
			SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() {
					running = true;
					fractal = threadJuliaset(x, y, d);
					running = false;
					repaint();
					return null;
				}
				
			};
			sw.execute();
		}
	}
	
	/**
	 * Threads Juliaset
	 * @param x width
	 * @param y heigh
	 * @param d complex number
	 * @return juliaset
	 */
	private BufferedImage threadJuliaset(int x, int y, Complex d) {
		BufferedImage juliaset = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		ConcurrentHashMap<Integer, BufferedImage> hm = new ConcurrentHashMap<>();
		CountDownLatch c = new CountDownLatch(maxThreads);
		
		int y1, split;
		y1 = 0;
		split = y/maxThreads;
		//Loops through splitted regions and makes them
		for (int i = 0; i < maxThreads - 1; i++) {
			JuliaPartBuilder p = new JuliaPartBuilder(y1, y1+split, d, hm, c);
			p.execute();
			y1+=split;
		}
		JuliaPartBuilder p = new JuliaPartBuilder(y1, y, d, hm, c);
		p.execute();
		
		try {
			//Awaits for all the threads to complete then draws the image
			c.await();
			for (int key : hm.keySet()) {
				juliaset.createGraphics().drawImage(hm.get(key), 0, key, null);
			}
		} catch (Exception e) {};		
		return juliaset;
	}
	
	/**
	 * Checks if thread is running
	 * @return
	 */
	public boolean checkIfThreading() {
		return running;
	}
	
	/**
	 * Saves the fractal to a folder Juliaset
	 * @param fileName name of fractal
	 */
	public void save(String fileName) throws Exception{
		File juliaset = new File("Juliaset");
		File outputfile = new File(juliaset, fileName + ".png");
		ImageIO.write(fractal, "png", outputfile);
	}
	
	/**
	 * Gets complex coordinates
	 * @return xMin, xMax, yMin, yMax
	 */
	public double[] getComplexCoordinates() {
		double coordinates[] = {xMin, xMax, yMin, yMax};
		for(int i=0; i < 4; i++) {
			coordinates[i] = Math.round(coordinates[i] * 100000d) / 100000d;
		}
		return coordinates;
	}
	
	/**
	 * Getter for xMin
	 * @return xMin
	 */
	public double getxMin() {
		return xMin;
	}
	
	/**
	 * Getter for xMax
	 * @return xMax
	 */
	public double getxMax() {
		return xMax;
	}

	/**
	 * Getter for yMin
	 * @return yMin
	 */
	public double getyMin() {
		return yMin;
	}

	/**
	 * Getter for yMax
	 * @return yMax
	 */
	public double getyMax() {
		return yMax;
	}

	/**
	 * Getter for drawing coordinates
	 * @param x index of the array
	 * @return drawing point
	 */
	public int getDrawingCoordinates(int x) {
		return drawingCoordinates[x];
	}
	
	/**
	 * 
	 * @param x starting point x
	 * @param y starting point y
	 * @param width width of rectangle
	 * @param height heigh of rectangle
	 */
	public void setRectangleDimensions(int x, int y, int width, int height) {
		rX = x;
		rY = y;
		rW = width;
		rH = height;
	}
	
	/**
	 * Sets complex coordinates
	 * @param coordinates array of xMin, xMax, yMin, yMax
	 */
	public void setComplexCoordinates(double[] coordinates) {
		xMin = coordinates[0];
		xMax = coordinates[1];
		yMin = coordinates[2];
		yMax = coordinates[3];
	}
	
	/**
	 * Sets complex coordinates from pixel coordinates
	 * @param xMin pixel xMin
	 * @param xMax pixel xMax
	 * @param yMin pixel yMin
	 * @param yMax pixel yMax
	 */
	public void setCoordinates(double xMin, double xMax, double yMin, double yMax) {
		Complex tempOne = translateCoordinates(xMin, yMin);
		Complex tempTwo = translateCoordinates(xMax, yMax);
		this.xMin = tempOne.getReal();
		this.xMax = tempTwo.getReal();
		this.yMin = -tempOne.getImaginary();
		this.yMax = -tempTwo.getImaginary();
	}
	
	/**
	 * Sets coordinates from where the fractal is drawn from on the screen
	 * @param x x point
	 * @param y y point
	 */
	public void setDrawingCoordinates(int x, int y) {
		drawingCoordinates = new int[] {x, y};
	}
	
	/**
	 * Sets max iterations of the Fractal
	 * @param maxIterations number of iterations
	 */
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	/**
	 * Paints the fractal and rectangle
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(fractal, drawingCoordinates[0], drawingCoordinates[1], null);
		if (!(rW==0||rH==0)) {
			g.setColor(new Color(180,180,220,80));
			g.fillRect(rX, rY, rW, rH);
			g.setColor(new Color(0,0,0));
			g.drawRect(rX, rY, rW, rH);
		}
	}

	/**
	 * Partbuilder for Mandelbrot Set
	 * @author Vedant Chokshi
	 */
	private class MandelbrotPartBuilder extends SwingWorker<Void, Void> {		
		private int y1, y2;
		private ConcurrentHashMap<Integer, BufferedImage> hm;
		private CountDownLatch c;
		
		public MandelbrotPartBuilder (int y1, int y2, ConcurrentHashMap<Integer, BufferedImage> hm, CountDownLatch c) {
			this.y1 = y1;
			this.y2 = y2;
			this.hm = hm;
			this.c = c;
		}
		
		//Generates mandelbrot set
		@Override
		protected Void doInBackground() {
			BufferedImage mandelbrot = new BufferedImage(getWidth(), y2-y1, BufferedImage.TYPE_INT_RGB);
			
			for(int x=0; x < getWidth(); x++) {			
				for(int y=y1; y < y2; y++) {
					Complex c = translateCoordinates(x, y);
					Complex z = new Complex(c.getReal(), c.getImaginary());
					int iterations = 0;
					
					while (iterations < maxIterations && z.modulusSquared() < 4.0) {
						z.square();
						z.add(c);
						iterations++;
					}
					
					if (iterations == maxIterations) {
						mandelbrot.setRGB(x, y-y1, Color.BLACK.getRGB());
					} else {
						double nSmooth = (iterations + 1 - Math.log(Math.log(Math.sqrt(z.modulusSquared()))) / Math.log(2) ) / 90f;
						mandelbrot.setRGB(x, y-y1, Color.HSBtoRGB((float) (nSmooth - 0.6), 1, 1));
					}
				}
			}
			
			hm.put(y1, mandelbrot);
			c.countDown();
			return null;
		}
		
	}
	
	/**
	 * Partbuilder for Juliaset
	 * @author Vedant
	 */
	private class JuliaPartBuilder extends SwingWorker<Void, Void> {
		private int y1, y2;
		private Complex d;
		private ConcurrentHashMap<Integer, BufferedImage> hm;
		private CountDownLatch c;
		
		public JuliaPartBuilder (int y1, int y2, Complex d, ConcurrentHashMap<Integer, BufferedImage> hm, CountDownLatch c) {
			this.y1 = y1;
			this.y2 = y2;
			this.d = d;
			this.hm = hm;
			this.c = c;
		}
		
		//Generates mandelbrot set
		@Override
		protected Void doInBackground() {
			BufferedImage juliaset = new BufferedImage(getWidth(), y2-y1, BufferedImage.TYPE_INT_RGB);
			
			for(int x=0; x < getWidth(); x++) {			
				for(int y=y1; y < y2; y++) {
					Complex z = translateCoordinates(x, y);
					int iterations = 0;
					
					while (iterations < maxIterations && z.modulusSquared() < 4.0) {
						z.square();
						z.add(d);
						iterations++;
					}
					
					if (iterations == maxIterations) {
						juliaset.setRGB(x, y-y1, Color.BLACK.getRGB());
					} else {
						double nSmooth = (iterations + 1 - Math.log(Math.log(Math.sqrt(z.modulusSquared()))) / Math.log(2) ) / 100f;
						juliaset.setRGB(x, y-y1, Color.HSBtoRGB((float) (nSmooth - 0.6), 1, 1));
					}
				}
			}
			
			hm.put(y1, juliaset);
			c.countDown();
			return null;
		}		
	}
}
