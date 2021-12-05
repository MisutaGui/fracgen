package fractales;

import java.util.function.Function;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;

/**
 * This class encapsulates a Julia set
 */
public class JuliaSet {

    // escape radius and maximal number of iterations
    // of the function
    private static final double RADIUS = 2.0;
    private final int maxIteration;

    // working rectangle of the Complex plane
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;

    // discrete step
    private final double discreteStep;

    // image height, width and name
    private final int imageHeight;
    private final int imageWidth;
    private final String fileName = "JuliaSetTest";

    // iteration function and constant complex
    private final Function <Complex,Complex> iterationFunction; 
    private final Complex complexConstant;   
    
    // Constructor from Julia Set builder
    private JuliaSet(JuliaSetBuilder builder){
	this.complexConstant = builder.complexConstant;
	this.maxIteration = builder.maxIteration;
	this.discreteStep = builder.discreteStep;
	this.xMin = builder.xMin;
	this.xMax = builder.xMax;
	this.yMin = builder.yMin;
	this.yMax = builder.yMax;
	this.iterationFunction = builder.iterationFunction;
	if(builder.imageHeight <= 0 || builder.imageWidth <= 0){
	    // scales each point of the discrete plane to a pixel of the image
	    this.imageHeight =
		(int) ((Math.abs(yMin) + Math.abs(yMax)) / discreteStep + 1.0);
	    this.imageWidth =
		(int) ((Math.abs(xMin) + Math.abs(xMax)) / discreteStep + 1.0);
	} else {
	    this.imageHeight = builder.imageHeight;
	    this.imageWidth = builder.imageWidth;
	}
    }
    
    /**
     * Builder class for a JuliaSet
     */
    public static class JuliaSetBuilder {

	//required parameters
	private final Complex complexConstant;
	    
	// optionnal parameters for the Julia Set
	private int maxIteration = 1000;
	private double discreteStep = 0.00075;
	private double xMin = -1;
	private double xMax = 1;
	private double yMin = -1;
	private double yMax = 1;
	private Function <Complex,Complex> iterationFunction =
	    (z) -> z.multiply(z).add(complexConstant);
	private int imageHeight = 0;
	private int imageWidth = 0;
	private String fileName = "JuliaSet.png";

	/**
	 * Instantiates a JuliaSetBuilder
	 * @param complex A complex constant
	 */
	public JuliaSetBuilder(Complex complexConstant){
	    this.complexConstant = complex;
	}

	/**
	 * Sets the maximum iteration value for the iteration function
	 * @param maxIteration The maximum iteration value
	 * @return This JuliaSetBuilder instance
	 */
	public JuliaSetBuilder maxIteration(int maxIteration){
	    this.maxIteration = maxIteration;
	    return this;
	}

	/**
	 * Sets the value of the discrete step
	 * @param discreteStep The value of the discrete step
	 * @return This JuliaSetBuilder instance
	 */
	public JuliaSetBuilder discreteStep(double discreteStep){
	    this.discreteStep = discreteStep;
	    return this;
	}

	/**
	 * Sets the value for the minimum real value of a complex number
	 * @param xMin The value for the minimum real value of a complex number
	 * @return This JuliaSetBuilder instance
	 */
	public JuliaSetBuilder xMin(double xMin){
	    this.xMin = xMin;
	    return this;
	}

	/**
	 * Sets the value for the maximum real value of a complex number
	 * @param xMin The value for the maximum real value of a complex number
	 * @return This JuliaSetBuilder instance
	 */
	public JuliaSetBuilder xMax(double xMax){
	    this.xMas = xMax;
	    return this;
	}

	/**
	 * Sets the value for the minimum imaginary value of a complex number
	 * @param xMin The value for the minimum imaginary value of a complex number
	 * @return This JuliaSetBuilder instance
	 */
	public JuliaSetBuilder yMin(double yMin){
	    this.yMin = yMin;
	    return this;
	}

	/**
	 * Sets the value for the maximum imaginary value of a complex number
	 * @param xMin The value for the maximum imaginary value of a complex number
	 * @return This JuliaSetBuilder instance
	 */
	public JuliaSetBuilder yMax(double yMax){
	    this.yMax = yMax;
	    return this;
	}

	/**
	 */
	public JuliaSetBuilder iterationFunction
	    (Function<Complex, Complex> iterationFunction){
	    this.iterationFunction = iterationFunction;
	    return this;
	}

	/**
	 */
	public JuliaSetBuilder imageHeight(int imageHeight){
	    this.imageHeight = imageHeight;
	    return this;
	}

	/**
	 */
	public JuliaSetBuilder imageWidth(int imageWidth){
	    this.imageWidth = imageWidth;
	    return this;
	}

	/**
	 */
	public JuliaSetBuilder fileName(String fileName){
	    this.fileName = fileName;
	    return this;
	}
    }

    /**
     * Computes the index of divergence of the complex z0
     * @param z0 The initial complex of the iteration
     * @return the index of divergence of z0
     */
    private int computeDivergence(Complex z0){
	int iteration = 0;
	Complex zn = z0;
	while(iteration < maxIteration - 1 && zn.modulus() <= RADIUS){
	    zn = function.apply(zn);
	    iteration++;
	}
	return iteration;
    }

    /**
     * Computes the index of divergence of each complex in the 
     * rectangle delimeted by xMin xMax yMin yMax of the complex plane
     * and strores the resulting indices in a 2D array
     * @return A 2D array containing the index of divergence of the
     *         corresponding complex number
     */
    private int[][] arrayDivergence(){
    	// compute divergence for each complex points
    	int[][] arrayDivergence = new int[width][height];
    	for(int i = 0; i < width -1; i++){
    	    for(int j = 0; j < height -1; j++){
		Complex complex = Complex.of(x1 + (step * i), y2 - (step * j));
		arrayDivergence[i][j] = computeDivergence(complex);
	    }
    	}
	return arrayDivergence;
    }

    /**
     * Stores into a file of name filename a PNG image of the JuliaSet defined
     * by this instance
     */
    public void drawImage(){
	var img = new BufferedImage(width, height, BufferedImage. TYPE_INT_RGB);
	int[][] arrayDivergence = arrayDivergence();
	for(int i = 0; i < width -1; i++){
	    for(int j = 0; j < height -1; j++){
		int div = arrayDivergence[i][j];
		int rgb;
		if(div == 999){
		    rgb = 0;
		} else {
		    // rgb=Color.HSBtoRGB((float)div/maxIter, 0.7f, (float)div/maxIter);
		    rgb = Color.HSBtoRGB((float)div*20.0f/(float)maxIter,1.0f,1.0f);
		}
		img.setRGB(i,j,rgb);
	    }
	}
	File file = new File(fileName + ".png");
  	try{
	    ImageIO.write(img, "PNG", file);
	} catch (Exception e){
	    e.printStackTrace();
	}
    }
}
