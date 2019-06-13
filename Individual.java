package pathOptimal;

import java.util.ArrayList;
import java.util.List;

public class Individual {
	private int[] chromosome;
	private double fitness = -1;

	private List<Integer> totalGene=new ArrayList<>();


	/**
	 * 用特定的染色体初始化该染色体
	 * @param chromosome
	 */
	public Individual(int[] chromosome) {
		// Create individual chromosome
		this.chromosome = chromosome;
	}

	/**
	 * 随机产生个体
	 * @param chromosomeLength 基因的长度
	 */
	public Individual(int chromosomeLength) {

		for (int i = 0; i <chromosomeLength ; i++) {
			totalGene.add(i);//订单编号2-101
		}
		this.chromosome = new int[chromosomeLength];

		for (int gene = 0; gene < chromosomeLength; gene++) {
			if(totalGene.size()>0){
				int index=(int)(Math.random()*totalGene.size());//产生0<=index<chromosomeLength
				this.setGene(gene,totalGene.get(index));
				totalGene.remove(index);
			}

		}
	}

	public int[] getChromosome() {
		return this.chromosome;
	}


	public int getChromosomeLength() {
		return this.chromosome.length;
	}

	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}


	public int getGene(int offset) {
		return this.chromosome[offset];
	}


	public void setFitness(double fitness) {
		this.fitness = fitness;
	}


	public double getFitness() {
		return this.fitness;
	}
	
	

	public String toString() {
		String output = "";
		for (int gene = 0; gene < this.chromosome.length; gene++) {
			output += this.chromosome[gene]+"-";
		}
		return output;
	}
}
