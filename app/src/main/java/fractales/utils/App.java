package fractales;

import fractales.model.*;
import fractales.utils.FractalImage;

import java.util.function.Function;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import org.apache.commons.cli.*;

public class App {

    // console and gui options
  private static final Option CONSOLE_OPT =
	Option.builder("csl")
	.longOpt("console")
	.desc("Launches application in console mode")
	.build();

  private static final Option GUI_OPT =
	Option.builder("gui")
	.longOpt("graphics")
	.desc("Launches application in GUI mode")
	.build();

  // Julia and Mandelbrot options
  private static final Option MAX_ITER_OPT =
	Option.builder("maxIter")
	.longOpt("maxIteration")
	.hasArg()
	.valueSeparator()
	.desc("Sets the maximal value of iterations for divergence computing")
	.build();

  private static final Option STEP_OPT =
	Option.builder("step")
	.longOpt("discreteStep")
	.hasArg()
	.valueSeparator()
	.desc("Sets the discrete step value for the Complex plane")
	.build();

  private static final Option XMIN_OPT =
	Option.builder("xMin")
	.hasArg()
	.valueSeparator()
	.desc("Sets the minimal value along the x-axis")
	.build();

  private static final Option XMAX_OPT =
	Option.builder("xMax")
	.hasArg()
	.valueSeparator()
	.desc("Sets the maximal value along the x-axis")
	.build();

  private static final Option YMIN_OPT =
	Option.builder("yMin")
	.hasArg()
	.valueSeparator()
	.desc("Sets the minimal value along the y-axis")
	.build();

  private static final Option YMAX_OPT =
	Option.builder("yMax")
	.hasArg()
	.valueSeparator()
	.desc("Sets the maximal value along the y-axis")
	.build();

  private static final Option IMG_W_OPT =
	Option.builder("w")
	.longOpt("imageWidth")
	.hasArg()
	.valueSeparator()
	.desc("Sets the width of the image in which to store the fractal")
	.build();

  private static final Option IMG_H_OPT =
	Option.builder("h")
	.longOpt("imageHeight")
	.hasArg()
	.valueSeparator()
	.desc("Sets the height of the image in which to store the fractal")
	.build();

  private static final Option FILENAME_OPT =
	Option.builder("name")
	.longOpt("filename")
	.hasArg()
	.valueSeparator()
	.desc("Sets the name of the while in which to store the image")
	.build();

  private static final Option COLOR_FUN_OPT =
  Option.builder("colorFun")
  .longOpt("colorFunction")
  .hasArg()
  .numberOfArgs(3)
  .valueSeparator(';')
  .desc("The function color")
  .build();

  private static final Option JULIA_OPT =
  Option.builder("julia")
  .desc("Julia generation")
  .build();

  private static final Option MANDELBROT_OPT =
  Option.builder("mandelbrot")
  .desc("Mandelbrot generation")
  .build();

  // Julia options only
  private static final Option COMPLEX_CST_OPT =
	Option.builder("constant")
	.longOpt("complexConstant")
	.hasArg()
  .numberOfArgs(2)
	.valueSeparator(';')
	.desc("Sets the value of the Complex constant for divergence")
	.build();

  private static final Option ITER_FUN_OPT =
  Option.builder("iterFun")
  .longOpt("iterationFunction")
  .hasArg()
  .numberOfArgs(4)
  .valueSeparator(';')
  .desc("The iteration function")
  .build();

  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(CONSOLE_OPT);
    options.addOption(GUI_OPT);
    options.addOption(MAX_ITER_OPT);
    options.addOption(STEP_OPT);
    options.addOption(XMIN_OPT);
    options.addOption(XMAX_OPT);
    options.addOption(YMIN_OPT);
    options.addOption(YMAX_OPT);
    options.addOption(IMG_W_OPT);
    options.addOption(IMG_H_OPT);
    options.addOption(FILENAME_OPT);
    options.addOption(COLOR_FUN_OPT);
    options.addOption(JULIA_OPT);
    options.addOption(MANDELBROT_OPT);
    options.addOption(COMPLEX_CST_OPT);
    options.addOption(ITER_FUN_OPT);

    String set = "";

    Julia.Builder juliaBuilder = new Julia.Builder();
    Mandelbrot.Builder mandelbrotBuilder = new Mandelbrot.Builder();
    // var builder;

    CommandLine commandLine;
    HelpFormatter helper = new HelpFormatter();
    CommandLineParser parser = new DefaultParser();

    try {
      //parsing the commandline
      commandLine = parser.parse(options, args);

      if(commandLine.hasOption("csl") && commandLine.hasOption("gui")) {
        //erreur
        helper.printHelp(" ", options);
    		System.exit(0);
      }
      else if(commandLine.hasOption("csl")) {
        //launching console version
        if(commandLine.hasOption("julia")
            && commandLine.hasOption("mandelbrot")){
              helper.printHelp(" ", options);
              System.exit(0);
        }
        else if(commandLine.hasOption("julia")) {
          set = "julia";

          if(commandLine.hasOption("constant")){
            //parsing constant
            double constantReal =
            Double.parseDouble(commandLine.getOptionValues("constant")[0]);
            double constantIm =
            Double.parseDouble(commandLine.getOptionValues("constant")[1]);

            Complex constant = Complex.of(constantReal,constantIm);
            juliaBuilder.complexConstant(constant);
          }

          if(commandLine.hasOption("iterFun")){
            //parsing iterFun
            //parsing alpha factor
            double alphaReal =
            Double.parseDouble(commandLine.getOptionValues("iterFun")[0]);
            double alphaIm =
            Double.parseDouble(commandLine.getOptionValues("iterFun")[1]);

            Complex alpha = Complex.of(alphaReal,alphaIm);

            //parsing beta factor
            double betaReal =
            Double.parseDouble(commandLine.getOptionValues("iterFun")[2]);
            double betaIm =
            Double.parseDouble(commandLine.getOptionValues("iterFun")[3]);

            Complex beta = Complex.of(betaReal,betaIm);

            juliaBuilder.iterationFunction(alpha,beta);
          }
        }
        else if (commandLine.hasOption("mandelbrot")) {
          set = "mandelbrot";
        }


        if(commandLine.hasOption("maxIter")) {
          int maxIter =
          Integer.parseInt(commandLine.getOptionValue("maxIter"));
          if(set.equals("julia")){
            juliaBuilder.maxIteration(maxIter);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.maxIteration(maxIter);
          }
        }

        if(commandLine.hasOption("step")) {
          double step =
          Double.parseDouble(commandLine.getOptionValue("step"));
          if(set.equals("julia")){
            juliaBuilder.discreteStep(step);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.discreteStep(step);
          }
        }

        if(commandLine.hasOption("xMin")) {
          int xMin =
          Integer.parseInt(commandLine.getOptionValue("xMin"));
          if(set.equals("julia")){
            juliaBuilder.xMin(xMin);
          }
          else if(set.equals("mandelbrot")) {
            mandelbrotBuilder.xMin(xMin);
          }
        }

        if(commandLine.hasOption("xMax")) {
          int xMax =
          Integer.parseInt(commandLine.getOptionValue("xMax"));
          if(set.equals("julia")){
            juliaBuilder.xMax(xMax);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.xMax(xMax);
          }
        }

        if(commandLine.hasOption("yMin")) {
          int yMin =
          Integer.parseInt(commandLine.getOptionValue("yMin"));
          if(set.equals("julia")){
            juliaBuilder.yMin(yMin);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.yMin(yMin);
          }
        }

        if(commandLine.hasOption("yMax")) {
          int yMax =
          Integer.parseInt(commandLine.getOptionValue("yMax"));
          if(set.equals("julia")){
            juliaBuilder.yMax(yMax);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.yMax(yMax);
          }
        }

        if(commandLine.hasOption("h")) {
          int imageHeight =
          Integer.parseInt(commandLine.getOptionValue("h"));
          if(set.equals("julia")){
            juliaBuilder.imageHeight(imageHeight);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.imageHeight(imageHeight);
          }
        }

        if(commandLine.hasOption("w")) {
          int imageWidth =
          Integer.parseInt(commandLine.getOptionValue("w"));
          if(set.equals("julia")){
            juliaBuilder.imageWidth(imageWidth);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.imageWidth(imageWidth);
          }
        }

        if(commandLine.hasOption("name")){
          if(set.equals("julia")){
            juliaBuilder.fileName(commandLine.getOptionValue("name"));
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.fileName(commandLine.getOptionValue("name"));
          }
        }

        if(commandLine.hasOption("colorFun")) {
          double alpha =
          Double.parseDouble(commandLine.getOptionValues("colorFun")[0]);
          double beta =
          Double.parseDouble(commandLine.getOptionValues("colorFun")[1]);
          double gamma =
          Double.parseDouble(commandLine.getOptionValues("colorFun")[2]);
          //initialize color function
          if(set.equals("julia")){
            juliaBuilder.colorFunction((float)alpha, (float)beta, (float)gamma);
          }
          else if(set.equals("mandelbrot")){
            mandelbrotBuilder.colorFunction((float)alpha, (float)beta, (float)gamma);
          }

        }

        Fractal fractal = null;
        if(set.equals("julia"))
        {
          fractal = juliaBuilder.build();
        }
        else if(set.equals("mandelbrot"))
        {
          fractal = mandelbrotBuilder.build();
        }
        FractalImage fi = FractalImage.of(fractal);
        fi.saveFile();
      }
      else if(commandLine.hasOption("gui"))
      {
        //launching gui version
        System.out.println("graphical interface");
      }
      else
      {
        System.out.println("Choose between console (-csl) or gui (-gui)");
      }
    }
    catch(Exception e){
  		System.out.println(e.getMessage());
  		helper.printHelp(" ", options);
  		System.exit(0);
    }
   }
  }