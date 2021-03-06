import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Arrays;

// Main class
//COMP3301_assignment_2_smoothing filters
//Implementation of mean, gaussian, median, and Kuwahara filters.
//v_1.0 2019/02/10
// Seokho Han 201761541
// Teng Hong Lee 201723459
// Utsav Ashish Koju 201763299
public class SmoothingFilter extends Frame implements ActionListener {
	BufferedImage input;
	ImageCanvas source, target;
	TextField texSigma;
	int width, height;
	private int maskSize = 5;
	// Constructor
	public SmoothingFilter(String name) {
		super("Smoothing Filters");
		// load image
		try {
			input = ImageIO.read(new File(name));
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
		width = input.getWidth();
		height = input.getHeight();
		// prepare the panel for image canvas.
		Panel main = new Panel();
		source = new ImageCanvas(input);
		target = new ImageCanvas(input);
		main.setLayout(new GridLayout(1, 2, 10, 10));
		main.add(source);
		main.add(target);
		// prepare the panel for buttons.
		Panel controls = new Panel();
		Button button = new Button("Add noise");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("5x5 mean");
		button.addActionListener(this);
		controls.add(button);
		controls.add(new Label("Sigma:"));
		texSigma = new TextField("1", 1);
		controls.add(texSigma);
		button = new Button("5x5 Gaussian");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("5x5 median");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("5x5 Kuwahara");
		button.addActionListener(this);
		controls.add(button);
		// add two panels
		add("Center", main);
		add("South", controls);
		addWindowListener(new ExitListener());
		setSize(width*3+100, height+100);//width*2 -> width*3
		setVisible(true);
	}
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	// Action listener for button click events
	public void actionPerformed(ActionEvent e) {
		// example -- add random noise
		if ( ((Button)e.getSource()).getLabel().equals("Add noise") ) {
			Random rand = new Random();
			int dev = 64;
			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(source.image.getRGB(x, y));
					int red = clr.getRed() + (int)(rand.nextGaussian() * dev);
					int green = clr.getGreen() + (int)(rand.nextGaussian() * dev);
					int blue = clr.getBlue() + (int)(rand.nextGaussian() * dev);
					red = red < 0 ? 0 : red > 255 ? 255 : red;
					green = green < 0 ? 0 : green > 255 ? 255 : green;
					blue = blue < 0 ? 0 : blue > 255 ? 255 : blue;
					source.image.setRGB(x, y, (new Color(red, green, blue)).getRGB());
				}
			source.repaint();
		}
		//by Seokho Han
		if ( ((Button)e.getSource()).getLabel().equals("5x5 mean") ) {
			System.out.println("5X5 mean filter!");

			//store original RGbs
			int [][] rPixels = new int[height][width];
			int [][] gPixels = new int[height][width];
			int [][] bPixels = new int[height][width];
			//store filtered RGBs
			int [][] newRedPixels = new int[height][width];
			int [][] newGreenPixels = new int[height][width];
			int [][] newBluePixels = new int[height][width];


			//+++
			//store original RGBs into arrays
			for ( int y=0 ; y<height ; y++ ){
				for ( int x=0 ; x<width ; x++) {
					Color clr = new Color(source.image.getRGB(x, y));
					rPixels[y][x] = clr.getRed();
					gPixels[y][x] = clr.getGreen();
					bPixels[y][x] = clr.getBlue();
				}
			}

			for ( int y=2 ; y<height-2 ; y++ ){
				for ( int x=2 ; x<width-2 ; x++) {
					int sr = 0, sg = 0, sb = 0;
					for (int v = -2; v <= 2; v ++){
						for (int u = -2; u <= 2; u ++){
							sr += rPixels[y+v][x+u];
							sg += gPixels[y+v][x+u];
							sb += bPixels[y+v][x+u];
						}
					}
					target.image.setRGB(x, y, (new Color(sr/25, sg/25, sb/25).getRGB()));
				}
			}target.repaint();


			//+++

//			//=== fixed 3/23/2019 seokho han this works
//			for ( int y=2 ; y<height-2 ; y++ ){
//				for ( int x=2 ; x<width-2 ; x++) {
//					int sr = 0, sg = 0, sb = 0;
//					for (int v = -2; v <= 2; v ++){
//						for (int u = -2; u <= 2; u ++){
//							Color cl = new Color(source.image.getRGB(x+u, y+v));
//							sr += cl.getRed();
//							sg += cl.getGreen();
//							sb += cl.getBlue();
//						}
//					}
//					target.image.setRGB(x, y, (new Color(sr/25, sg/25, sb/25).getRGB()));
//				}
//			}//target.repaint();
//			//===
		}

		//Have to work on these
		//Gaussian FIlter
		//by Seokho Han
		if ( ((Button)e.getSource()).getLabel().equals("5x5 Gaussian") ){
			System.out.println("5x5 Gaussian called!");
			int radius = 2;
			float sigma = radius / 3.0f;
			float sigmaSquareTwo = sigma * sigma * 2.0f;
			//	float sigmaRoot = (float) Math.sqrt(sigmaSquareTwo * Math.PI);
			float twoSigmaSquarePI = (float) (sigmaSquareTwo * Math.PI);


			//store RGbs
			int[][] rPixels = new int[height][width];
			int[][] gPixels = new int[height][width];
			int[][] bPixels = new int[height][width];

			for ( int y=0 ; y<height ; y++ ){
				for ( int x=0 ; x<width ; x++){
					Color clr = new Color(source.image.getRGB(x, y));
					int red, green, blue;
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();

					rPixels[y][x] = clr.getRed();
					gPixels[y][x] = clr.getGreen();
					bPixels[y][x] = clr.getBlue();
				}
			}
			for ( int y = 0 ; y < height ; y++ ){
				for ( int x = 0 ; x < width ; x++){
					float redSum = 0, greenSum = 0, blueSum = 0;
					int red = rPixels[y][x];
					int green = gPixels[y][x];
					int blue = bPixels[y][x];

					for (int u = -radius; u <= radius ; u ++){
						for (int v = -radius; v <= radius; v ++){
							if((u + y) >= 0 && (u + y) < height && (v + x) >= 0 && (v + x) < width){
								int distance = u * u + v * v;
								redSum +=  (float) ((Math.exp(-distance / sigmaSquareTwo)) / twoSigmaSquarePI);
								greenSum += (float) ((Math.exp(-distance / sigmaSquareTwo)) / twoSigmaSquarePI);
								blueSum += (float) ((Math.exp(-distance / sigmaSquareTwo)) / twoSigmaSquarePI);
							}
							else{
								int tempI = u;
								int tempJ = v;
								//if-stat-1
								if (((u + y) < 0) || ((v + x) < 0)){
									while ((u + y) < 0){
										u += 1;
									}
									while ((v + x) < 0){
										v += 1;
									}
								}//end if-stat-1
								//if-stat-2
								if ((u + y) >= height || (v + x) >= width){
									while ((u + y) >= height){
										u -= 1;
									}
									while ((v + x) >= width){
										v -= 1;
									}
								}//end if-stat-2
								int distance = u * u + v * v;
								if (distance == 0){
									//System.out.print("Distance is 0!");
									distance = 8;
								}
								redSum +=  (float) ((Math.exp(-distance / sigmaSquareTwo)) / twoSigmaSquarePI);
								greenSum += (float) ((Math.exp(-distance / sigmaSquareTwo)) / twoSigmaSquarePI);
								blueSum += (float) ((Math.exp(-distance / sigmaSquareTwo)) / twoSigmaSquarePI);
								u = tempI;
								v = tempJ;
							}//end else
						}
					}
					red = (int) (red / redSum);
					green = (int) (green / greenSum);
					blue = (int) (blue / blueSum);
					target.image.setRGB(x, y, (new Color(red,green,blue).getRGB()));
				}
				target.repaint();
			}


		}
		//5x5 median filter
		//Seokho v_1.0
		if ( ((Button)e.getSource()).getLabel().equals("5x5 median") ){
			System.out.println("5x5 median called Testing updates!");
			//store original RGbs
			int [][] rPixels = new int[height][width];
			int [][] gPixels = new int[height][width];
			int [][] bPixels = new int[height][width];
			//If offset is 2, then it travels through 5x5 neighbors
			int offset = 2;

			//store original RGBs into arrays
			for ( int y=0 ; y<height ; y++ ){
				for ( int x=0 ; x<width ; x++) {
					Color clr = new Color(source.image.getRGB(x, y));
					rPixels[y][x] = clr.getRed();
					gPixels[y][x] = clr.getGreen();
					bPixels[y][x] = clr.getBlue();
				}
			}

			//median filter
			for ( int y=offset ; y<height-offset ; y++ ){
				for ( int x=offset ; x<width-offset ; x++) {
					int red, green, blue;
					int[] redArray = new int[25];
					int[] greenArray = new int[25];
					int[] blueArray = new int[25];
					int index = 0;
					//traveling the neighbored valuse from (2, 2)
					for (int u = -offset, i = 0; u <= offset; u++){
						for(int v = -offset; v <= offset; v++, i++){
							redArray[i] = rPixels[y-u][x-v];
							greenArray[i] = gPixels[y-u][x-v];
							blueArray[i] = bPixels[y-u][x-v];
							index++;
						}
					}
					//sort the neighbored RGBs of (x,y) pixel
					Arrays.sort(redArray);
					Arrays.sort(greenArray);
					Arrays.sort(blueArray);
					int i = (index%2 == 0)?index/2-1:index/2;
					//find the median values
					red = redArray[i];
					green = greenArray[i];
					blue = blueArray[i];

					target.image.setRGB(x, y, (new Color(red, green, blue).getRGB()));
				}
			}target.repaint();
		}
		//by Seokho Han v_1.0
		if ( ((Button)e.getSource()).getLabel().equals("5x5 Kuwahara") ){
			System.out.println("5x5 Kuwahara called!");
			//Store each region's sum and variance among 4 neighboring regions
			float sumROne, sumGOne, sumBOne,
			sumRTwo, sumGTwo, sumBTwo,
			sumRThree, sumGThree, sumBThree,
			sumRFour, sumGFour, sumBFour,
			varianceROne, varianceGOne, varianceBOne,
			varianceRTwo, varianceGTwo, varianceBTwo,
			varianceRThree, varianceGThree, varianceBThree,
			varianceRFour, varianceGFour, varianceBFour;

			//store original RGbs
			int [][] rPixels = new int[height][width];
			int [][] gPixels = new int[height][width];
			int [][] bPixels = new int[height][width];
			//If offset is 2, then it travels through 5x5 neighbors
			int offset = 2;

			//store original RGBs into arrays
			for ( int y=0 ; y<height ; y++ ){
				for ( int x=0 ; x<width ; x++) {
					Color clr = new Color(source.image.getRGB(x, y));
					rPixels[y][x] = clr.getRed();
					gPixels[y][x] = clr.getGreen();
					bPixels[y][x] = clr.getBlue();
				}
			}
			//5x5 Kuwahara filter
			for ( int y=offset ; y<height-offset ; y++ ){
				for ( int x=offset ; x<width-offset ; x++) {

					int red = 0, green = 0, blue = 0;
					//sum of first region
					sumROne = 0; sumGOne = 0; sumBOne = 0;
					//sum of second region
					sumRTwo = 0; sumGTwo = 0; sumBTwo = 0;
					//sum of third region
					sumRThree = 0; sumGThree = 0; sumBThree = 0;
					//sum of fourth region
					sumRFour = 0; sumGFour = 0;sumBFour = 0;
					//variance of first region
					varianceROne = 0; varianceGOne = 0; varianceBOne = 0;
					//variance of second region
					varianceRTwo = 0; varianceGTwo = 0; varianceBTwo = 0;
					//variance of third region
					varianceRThree = 0; varianceGThree = 0; varianceBThree = 0;
					//variance of fourth region
					varianceRFour = 0; varianceGFour = 0; varianceBFour = 0;

					//get the sum of regions
					for (int u = -offset; u <= offset; u++){
						for(int v = -offset; v <= offset; v++){
							int regionY = y - u;
							int regionX = x - v;
							//first region (left-top)
							if (regionY <= y && regionX <= x){
								sumROne += rPixels[regionY][regionX];
								sumGOne += gPixels[regionY][regionX];
								sumBOne += bPixels[regionY][regionX];
							}
							//second region (right-top)
							if (regionY <= y && regionX >= x){
								sumRTwo += rPixels[regionY][regionX];
								sumGTwo += gPixels[regionY][regionX];
								sumBTwo += bPixels[regionY][regionX];
							}
							//third region (left-bottom)
							if (regionY >= y && regionX <= x){
								sumRThree += rPixels[regionY][regionX];
								sumGThree += gPixels[regionY][regionX];
								sumBThree += bPixels[regionY][regionX];
							}
							//fourth region (right-bottom)
							if (regionY >= y && regionX >= x){
								sumRFour += rPixels[regionY][regionX];
								sumGFour += gPixels[regionY][regionX];
								sumBFour += bPixels[regionY][regionX];
							}
						}
					}
					//get each region's variance
					for (int u = -offset; u <= offset; u++){
						for(int v = -offset; v <= offset; v++){
							int regionY = y - u;
							int regionX = x - v;
							//first region (left-top)
							if (regionY <= y && regionX <= x){
								varianceROne = ((sumROne/9) - rPixels[regionY][regionX]) * ((sumROne/9) - rPixels[regionY][regionX]);
								varianceGOne = ((sumGOne/9) - gPixels[regionY][regionX]) * ((sumGOne/9) - gPixels[regionY][regionX]);
								varianceBOne = ((sumBOne/9) - bPixels[regionY][regionX]) * ((sumBOne/9) - bPixels[regionY][regionX]);
							}
							//second region (right-top)
							if (regionY <= y && regionX >= x){
								varianceRTwo = ((sumRTwo/9) - rPixels[regionY][regionX]) * ((sumRTwo/9) - rPixels[regionY][regionX]);
								varianceGTwo = ((sumGTwo/9) - gPixels[regionY][regionX]) * ((sumGTwo/9) - gPixels[regionY][regionX]);
								varianceBTwo = ((sumBTwo/9) - bPixels[regionY][regionX]) * ((sumBTwo/9) - bPixels[regionY][regionX]);
							}
							//third region (left-bottom)
							if (regionY >= y && regionX <= x){
								varianceRThree = ((sumRThree/9) - rPixels[regionY][regionX]) * ((sumRThree/9) - rPixels[regionY][regionX]);
								varianceGThree = ((sumGThree/9) - gPixels[regionY][regionX]) * ((sumGThree/9) - gPixels[regionY][regionX]);
								varianceBThree = ((sumBThree/9) - bPixels[regionY][regionX]) * ((sumBThree/9) - bPixels[regionY][regionX]);
							}
							//fourth region (right-bottom)
							if (regionY >= y && regionX >= x){
								varianceRFour = ((sumRFour/9) - rPixels[regionY][regionX]) * ((sumRFour/9) - rPixels[regionY][regionX]);
								varianceGFour = ((sumGFour/9) - gPixels[regionY][regionX]) * ((sumGFour/9) - gPixels[regionY][regionX]);
								varianceBFour = ((sumBFour/9) - bPixels[regionY][regionX]) * ((sumBFour/9) - bPixels[regionY][regionX]);
							}
						}
					}
					//store variances to the arrays
					float[] varianceArrayR = {varianceROne, varianceRTwo, varianceRThree, varianceRFour};
					float[] varianceArrayG = {varianceGOne, varianceGTwo, varianceGThree, varianceGFour};
					float[] varianceArrayB = {varianceBOne, varianceBTwo, varianceBThree, varianceBFour};
					//sort to find the minimum variance
					Arrays.sort(varianceArrayR);
					Arrays.sort(varianceArrayG);
					Arrays.sort(varianceArrayB);
					//get RGBs
					//get the mean of the region that has the minimum variance
					//get red
					if (varianceArrayR[0] == varianceROne){
						red = (int) sumROne/9;
					}
					if (varianceArrayR[0] == varianceRTwo){
						red = (int) sumRTwo/9;
					}
					if (varianceArrayR[0] == varianceRThree){
						red = (int) sumRThree/9;
					}
					if (varianceArrayR[0] == varianceRFour){
						red = (int) sumRFour/9;
					}
					//get green
					if (varianceArrayG[0] == varianceGOne){
						green = (int) sumGOne/9;
					}
					if (varianceArrayG[0] == varianceGTwo){
						green = (int) sumGTwo/9;
					}
					if (varianceArrayG[0] == varianceGThree){
						green = (int) sumGThree/9;
					}
					if (varianceArrayG[0] == varianceGFour){
						green = (int) sumGFour/9;
					}
					//get blue
					if (varianceArrayB[0] == varianceBOne){
						blue = (int) sumBOne/9;
					}
					if (varianceArrayB[0] == varianceBTwo){
						blue = (int) sumBTwo/9;
					}
					if (varianceArrayB[0] == varianceBThree){
						blue = (int) sumBThree/9;
					}
					if (varianceArrayB[0] == varianceBFour){
						blue = (int) sumBFour/9;
					}
					//end getting RGBs for (x,y)pixel of the image
					target.image.setRGB(x, y, (new Color(red, green, blue).getRGB()));
				}
			}
			//redraw the image
			target.repaint();
		}
	}

	public static int getPixel(int alpha, int red, int green, int blue){
		return (alpha<<24) | (red<<16) | (green<<8) | blue;
	}

	public static void main(String[] args) {
		new SmoothingFilter(args.length==1 ? args[0] : "signal_hill.png");
	}
}
