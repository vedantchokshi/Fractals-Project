/**
 * The class Complex defines a Complex number
 * @author Vedant Chokshi
 */
public class Complex {
	private double real, imaginary;

	/**
	 * Constructor for a Complex number which takes in 2 arguments as the real and imaginary values
	 * @param real
	 * @param imaginary
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Getter for real part of the Complex number
	 * @return real number
	 */
	public Double getReal() {
		return real;
	}
	
	/**
	 * Getter for imaginary part of the Complex number
	 * @return imaginary number
	 */
	public Double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Squares the Complex number
	 */
	public void square() {
		double r = (real*real) - (imaginary*imaginary);
		double i = 2*(real)*(imaginary);
		
		real = r;
		imaginary = i;
	}

	/**
	 * Method to return the modulus square of the Complex number
	 * @return modulus square
	 */
	public Double modulusSquared() {
		return real*real + imaginary*imaginary;
	}
	
	/**
	 * Adds another Complex number to the current Complex number
	 * @param d Complex number to add
	 */
	public void add(Complex d) {
		real += d.getReal();
		imaginary += d.getImaginary();
	}	
}
