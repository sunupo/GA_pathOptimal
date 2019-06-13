package pathOptimal;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class Population {
	private Individual[] population;
	private double populationFitness = -1;//种群适应度，可以通过把所有个体的适应度求和取得

	/**构造方法1
	 * 初始化种群大小，还需要设置种群每一个个体
	 * @param populationSize
	 */
	public Population(int populationSize) {
		this.population = new Individual[populationSize];
	}

	/**
	 * 构造方法2
	 * 初始化种群
	 *
	 * @param populationSize 种群规模
	 *
	 * @param chromosomeLength 个体染色体长度
	 *
	 */
	public Population(int populationSize, int chromosomeLength) {

		this.population = new Individual[populationSize];

		// 通过种群规模，创建每一个随机产生的个体
		for (int individualCount = 0; individualCount < populationSize; individualCount++) {

			Individual individual = new Individual(chromosomeLength);
			// 把随机产生的个体添加到种群
			this.population[individualCount] = individual;
		}
	}


	public Individual[] getIndividuals() {
		return this.population;
	}


	/**
	 * 个体按照各自的适应度排序，然后通过offset选择出按照适应度排序的个体
	 * （可以再下一代中保留精英个体）
	 * @param offset
	 *           你想要的个体的偏移量，按适合度排序。0到population.size-1
	 * @return
	 */
	public Individual getFittest(int offset) {
		// Order population by fitness
		Arrays.sort(this.population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
			}
		});

		return this.population[offset];
	}


	public void setPopulationFitness(double fitness) {
		this.populationFitness = fitness;
	}


	public double getPopulationFitness() {
		return this.populationFitness;
	}


	public int size() {
		return this.population.length;
	}


	public Individual setIndividual(int offset, Individual individual) {
		return population[offset] = individual;
	}

	public Individual getIndividual(int offset) {
		return population[offset];
	}


	/**
	 * shuffle操作，可选
	 *
	 */
	public void shuffle() {
//		System.out.println("shuffle=");
		Random rnd = new Random();
		for (int i = population.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Individual a = population[index];
			population[index] = population[i];
			population[i] = a;
		}
	}
}