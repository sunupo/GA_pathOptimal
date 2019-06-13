package pathOptimal;

import java.util.*;

import java.awt.Color;
import java.awt.BasicStroke;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class Drive extends ApplicationFrame{

	public Drive( String applicationTitle, String chartTitle )
	{
		super(applicationTitle);
		JFreeChart s=null;
		JFreeChart xylineChart = ChartFactory.createXYLineChart(
				chartTitle ,
				"generation" ,
				"bestFitnes" ,
				createDataset(generationBestFitness) ,
				PlotOrientation.VERTICAL ,
				true , true , false);

		ChartPanel chartPanel = new ChartPanel( xylineChart );
		chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		final XYPlot plot = xylineChart.getXYPlot( );
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		renderer.setSeriesPaint( 0 , Color.RED );
//		renderer.setSeriesPaint( 1 , Color.GREEN );
//		renderer.setSeriesPaint( 2 , Color.YELLOW );
		renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
//		renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
//		renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
		plot.setRenderer( renderer );
		setContentPane( chartPanel );
	}

	public static Individual optimalSolution=new Individual(100);
	public static Map<Integer,Double> generationBestFitness=new HashMap<>();
	public static void main(String[] args) {
		Calendar calendar=Calendar.getInstance();
		long start=calendar.getTime().getTime();
 		GeneticAlgorithm ga = new GeneticAlgorithm(
 				100, 0.001, 0.95, 0);
 		Population population = ga.initPopulation(100);
		for (int i = 0; i < population.getIndividuals().length; i++) {
			System.out.println( population.getIndividuals()[i].toString());
		}

		population.shuffle();
 		ga.evalPopulation(population);
 		int generation = 1;
 		int count=1;
 		Individual bestIndividual=population.getFittest(0);
 		int bestIndividualGeneration=1;
 		generationBestFitness.put(generation,population.getFittest(0).getFitness());
//		while (ga.isTerminationConditionMet(population) == false && generation<20) {
/**
 *连续10代，最优个体适应度趋于稳定
 */
		while (Math.abs(bestIndividual.getFitness()-population.getFittest(0).getFitness())>0.0001 || count<5) {
//			System.out.println("本代best个体: "+generation+"=" + population.getFittest(0).toString());

			System.out.println("本代最优个体"+population.getFittest(0).getChromosome().toString()+
					"fitness="+population.getFittest(0).getFitness());
			if(population.getFittest(0).getFitness()>optimalSolution.getFitness()){
				optimalSolution=population.getFittest(0);
				bestIndividual=optimalSolution;
				bestIndividualGeneration=generation;
			}
			if(Math.abs(bestIndividual.getFitness()-population.getFittest(0).getFitness())<=0.0001){
				count++;
			}else
				count=1;

			population = ga.crossoverPopulation(population);
			population = ga.mutatePopulation(population);
			ga.evalPopulation(population);
			generation++;
			generationBestFitness.put(generation,population.getFittest(0).getFitness());

		}

		System.out.println("在第 " + bestIndividualGeneration + " 代出现最优个体");
		System.out.println("最好个体的适应度" + optimalSolution.getFitness());
		System.out.println("最优解: " + optimalSolution.toString());

		System.out.println("在第 " + (generation-10) + " 代开始收敛");
		System.out.println("收敛时当前种群最优个体的染色体为" + population.getFittest(0).toString()
		+"\n适应度为"+population.getFittest(0).getFitness());
		calendar=Calendar.getInstance();
		long end=calendar.getTime().getTime();
		System.out.println("时间:="+(end-start)+"ms" );
		System.out.println("代数generation，与当前带最优个体适应度关系：\n"+generationBestFitness);

		System.out.println(generationBestFitness.keySet());
		System.out.println(generationBestFitness.values());

		Drive chart = new Drive("Fitness graph", "The change curve of fitness of the optimal individual" +
				" of the current population with the number of iterations");
		chart.pack( );
		chart.setLocation(200,200);
//        RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );

	}
	private XYDataset createDataset( Map<Integer,Double> generationBestFitness)
	{
		final XYSeries mainSeries = new XYSeries( "mainSeries" );
		Set<Integer> key=generationBestFitness.keySet();
		Collection<Double> values=generationBestFitness.values();

		Iterator keyIter=key.iterator(),valuesIter=values.iterator();
 		while(keyIter.hasNext()){
			mainSeries.add( Double.parseDouble(keyIter.next()+""),(Double) valuesIter.next());
		}

//		mainSeries.add( 1.0 , 1.0 );
//		mainSeries.add( 2.0 , 4.0 );
//		mainSeries.add( 3.0 , 3.0 );
//		final XYSeries chrome = new XYSeries( "Chrome" );
//		chrome.add( 1.0 , 4.0 );
//		chrome.add( 2.0 , 5.0 );
//		chrome.add( 3.0 , 6.0 );
//		final XYSeries iexplorer = new XYSeries( "InternetExplorer" );
//		iexplorer.add( 3.0 , 4.0 );
//		iexplorer.add( 4.0 , 5.0 );
//		iexplorer.add( 5.0 , 4.0 );
		final XYSeriesCollection dataset = new XYSeriesCollection( );
		dataset.addSeries( mainSeries );
//		dataset.addSeries( chrome );
//		dataset.addSeries( iexplorer );
		return dataset;
	}
}
