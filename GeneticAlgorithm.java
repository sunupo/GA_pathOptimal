package pathOptimal;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneticAlgorithm {
	private int populationSize;

	/**
	 * 变异率（0-1.0，一般取值小于0.1）
	 */
	private double mutationRate;

	/**
	 * 交叉率（0.0-1.0 ）
	 */
	private double crossoverRate;

	/**
	 * 在一代代之间保留下来的精英个体，不参与交叉变异
	 */
	private int elitismCount;

	/**
	 * 偏好参数
	 */
	private double a=0.7;

	private double maxFitness=-1;
	final double ACC_INDIVIDUAL=0.000001;
	private double avgPopulationFitness=-1;
	final double ACC_POPULATION=0.000001;


	private double[][] distance=null;

	ProcessData data=null;

	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		data=new ProcessData(1,100);
	}

	/**
	 * 初始化种群
	 */
	public Population initPopulation(int chromosomeLength) {
 		Population population = new Population(this.populationSize, chromosomeLength);
		return population;
	}


	/**
	 * 个体是一个序列
	 * 对每一个个体计算适应度
	 * 一个个体的序列就是一个解，
	 *首先计算出发点到第一个配送点的距离
	 * 		再计算其他路径总长度
	 * 		得到总的运输时间
	 *计算到达每一个点的时间，与每一个点的截止时间做对比，求出每一个点的延误时间（假设每一个点的服务时间相同 ）
	 *最后计算总的时间
	 */
	public double calcFitness(Individual individual) {
		int[] chromosome=individual.getChromosome();
//		chromosome[i]的值，就是订单的标号
		;
		double[] demands=data.getDemands();

		float sum=0;//用来求连续几个订单的总需求量之和
		int j=0;//第j辆车

		double[] arrivalTime=new double[individual.getChromosome().length];//每一个订单的到达时间

		List<String> vehicleOrderIdList=new ArrayList<>();
		String orderIds="";//用来记录每一辆车对应的订单id号码
		for (int i = 0; i < chromosome.length; i++) {
//			System.out.println("into add vehicleOrderIdList");

//			if(sum<Constants.capacitys[j]){
			if(sum<200){
				orderIds+=i+"-";
				sum+=demands[chromosome[i]];
			}else{
				j++;
				sum=0;
				vehicleOrderIdList.add(orderIds);
				orderIds="";
			}
		}
//		System.out.println("vehicleOrderIdList.size="+vehicleOrderIdList.size());
		for (int i = 0; i < vehicleOrderIdList.size(); i++) {
//			System.out.println("第"+i+"辆车="+vehicleOrderIdList.get(i));

		}

//		根据j的最终值得到最终使用总的车辆数（j）,标号为（0,1,2，j-1）
//		每一辆车需要配送的订单标号为

//		现在车辆数确定了，每一辆车需要配送的订单号orderId也确定了，就可以计算总的配送时间了
		distance=data.getDistance(data.getxCoords(),data.getyCoords());
		double totalDeliveryTime=0;//每一辆车的总配送时间之和
		for(int i=0;i<vehicleOrderIdList.size();i++){
//			每一辆车的订单标号数组，String类型
			String[] idStrArray=vehicleOrderIdList.get(i)
					.substring(0,vehicleOrderIdList.get(i).length()-1)
					.split("-");
			int[] idArray=new int[idStrArray.length];//每一辆车的订单标号数组，int类型
			for (int k = 0; k <idArray.length ; k++) {
				idArray[k]=Integer.parseInt(idStrArray[k]);
			}
//			计算每一辆车的运输距离eachDis，（最后需要把每一辆车的运输距离求和）
//			计算每个订单的到达时间arrivalTime，
			double eachDis=0,eachDeliveryTime=0;
			switch (idArray.length){
				case 0:
					break;
				case 1:
					eachDis+=Math.sqrt(
							(Constants.SOURCE_X-data.getxCoords()[idArray[0]])*(Constants.SOURCE_X-data.getxCoords()[idArray[0]])
							+(Constants.SOURCE_Y-data.getyCoords()[idArray[0]])*(Constants.SOURCE_Y-data.getyCoords()[idArray[0]])
				);
					arrivalTime[idArray[0]]=eachDis/Constants.AVG_SPEED;
					break;
				default:
					eachDis+=Math.sqrt(
							(Constants.SOURCE_X-data.getxCoords()[idArray[0]])*(Constants.SOURCE_X-data.getxCoords()[idArray[0]])
									+(Constants.SOURCE_Y-data.getyCoords()[idArray[0]])*(Constants.SOURCE_Y-data.getyCoords()[idArray[0]])
					);
					arrivalTime[idArray[0]]=eachDis/Constants.AVG_SPEED;
					for (int k = 0; k <idArray.length-1 ; k++) {

						eachDis+=Constants.calDis(data.getxCoords()[k],data.getyCoords()[k],
								data.getxCoords()[k+1],data.getyCoords()[k+1]);
						arrivalTime[idArray[k+1]]=eachDis/Constants.AVG_SPEED;
					}
					break;
			}
			eachDeliveryTime=eachDis/Constants.AVG_SPEED;
			totalDeliveryTime+=eachDeliveryTime;
//			System.out.println("temp_arrivalTime.size="+arrivalTime.length);
//			System.out.println("temp_data.getDueTimes().length="+data.getDueTimes().length);
		}
//		System.out.println("arrivalTime.size="+arrivalTime.length);
//		System.out.println("data.getDueTimes().length="+data.getDueTimes().length);

//		计算用的延误时间，due date就是预期到达时间，当做订单截止时间计算
		double totalDelayTime=0;//总的延误时间
		for (int i = 0; i < data.getDueTimes().length; i++) {
			double diffvalue=0;
			if(arrivalTime[i] > data.getDueTimes()[i]){
				diffvalue=arrivalTime[i]-data.getDueTimes()[i];
			}else{
				diffvalue=0;
			}
			totalDelayTime+=diffvalue;
		}



		// 计算适应度fitness，总时间越小，适应度越大
		double fitness = 1/((1-a)*totalDeliveryTime+a*totalDelayTime);
//		System.out.print("totalDeliveryTime="+totalDeliveryTime);
//		System.out.print("---totalDelayTime="+totalDelayTime);
		System.out.println(individual.getChromosome().toString()+"  fitness= "+fitness);
//		System.out.println(fitness);


		// Store fitness
		individual.setFitness(fitness);

		return fitness;
	}

	/**
	 *评估整个population
	 * 实际上，循环遍历总体中的individual，计算每个individual的fitness，然后计算population的fitness。
	 * population的fitness可能重要也可能不重要，但这里重要的是确保每个individual都得到评估。
	 * @param population
	 *
	 */
	public void evalPopulation(Population population) {
		double populationFitness = 0;


		for (Individual individual : population.getIndividuals()) {
			populationFitness += calcFitness(individual);
		}

		population.setPopulationFitness(populationFitness);
		System.out.println(populationFitness);

	}

	/**
	 * 检查种群是否满足终止条件
	 * 可以用迭代次数、是否收敛、是否超过分配的时间，是否有一个最优解（最优解不好判断 ）
	 * @param population
	 * @return 如果满足终止条件，为true，否则为false
	 */
	public boolean isTerminationConditionMet(Population population) {
		// TODO: 5/21/2019 编写终止条件
		//1.本次种群最好个体的适应度与上次作比较（1. 最大适应度值和平均适应度值变化不大、趋于稳定;）
		//2.当适应度值变化小于某个值时,进化停止,如果始终达不到这个条件,就达到最大迭代次数时停止进化迭代。
		for (Individual individual : population.getIndividuals()) {
			if (Math.abs(individual.getFitness() -maxFitness)<ACC_INDIVIDUAL ||Math.abs(population.getPopulationFitness()/population.size())<ACC_POPULATION) {
				return true;
			}
		}

		return false;
	}

	/**
 	 * 选择一个parent用来交叉获得下一代
	 * @param population
 	 * @return T
	 */
	public Individual selectParent(Population population) {

 		Individual individuals[] = population.getIndividuals();

		double populationFitness = population.getPopulationFitness();
		double rouletteWheelPosition = Math.random() * populationFitness;


		double spinWheel = 0;
		for (Individual individual : individuals) {
			spinWheel += individual.getFitness();
			if (spinWheel >= rouletteWheelPosition) {
				return individual;
			}
		}
		return individuals[population.size() - 1];
	}

	/**
	 * 考虑交叉率和精英主义
	 *
	 * @param population
 	 * @return new population
	 */
	public Population crossoverPopulation(Population population) {
//		System.out.println("into crossover");
		// 根据上一代种群大小，创建新的种群
		Population newPopulation = new Population(population.size());
		// 根据适应度循环遍历当前种群
//		System.out.println("into populationIndex");
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
//			System.out.println("populationIndex="+populationIndex);
			Individual parent1 = population.getFittest(populationIndex);

//			Map<Integer,Integer> mapper=new HashMap<>();//记录映射,因为交叉之后可能出现重复基因，需要通过mapper修正
//			List<Integer> oldLocation=new ArrayList<>();//记录交叉之前原来基因的位置
//			List<Integer> newLocation=new ArrayList<>();//记录交叉后新基因的位置

			// 判断该个体是否参与交叉crossover
//			System.out.println(" 是否参与交叉");
			if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
//				System.out.println("\ninto 交叉 populationIndex="+populationIndex);
				// 初始化子代个体
				Individual offspring = new Individual(parent1.getChromosomeLength());
				// 根据轮盘赌，选择第二个亲代
				Individual parent2 = selectParent(population);
				// 循环遍历染色体组 genome
//				System.out.println("into 循环遍历染色体组 genome");
				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					Map<Integer,Integer> mapper=new HashMap<>();//记录映射,因为交叉之后可能出现重复基因，需要通过mapper修正
					List<Integer> oldLocation=new ArrayList<>();//记录交叉之前原来基因的位置
					List<Integer> newLocation=new ArrayList<>();//记录交叉后新基因的位置

					if (0.5 > Math.random()) {
						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
						oldLocation.add(geneIndex);
//						oldLocation.add(parent1.getGene(geneIndex));
					} else {
						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
						newLocation.add(geneIndex);
//						newLocation.add(parent2.getGene(geneIndex));
						mapper.put(parent2.getGene(geneIndex),parent1.getGene(geneIndex));
					}
					//对offspring进行修正
//					System.out.println("into 对offspring进行修正");
					for(int i:oldLocation){
						int oldGene=offspring.getGene(i);//是未交叉部分的基因，取出来，判断与交叉部分是否有重复基因
						for (int j:newLocation){
							int newGene=offspring.getGene(j);
							if(oldGene==newGene){
								//说明交叉过后基因重复
								int geneCode=oldGene;
								while(mapper.get(geneCode)!=null){
									geneCode=mapper.get(geneCode);
								}
								offspring.setGene(i,geneCode);
							}
							break;//第i个基因的重复修正了,进入i+1
						}
					}
				}
//				//对offspring进行修正
//				System.out.println("into 对offspring进行修正");
//
//				for(int i:oldLocation){
//					int oldGene=offspring.getGene(i);//是未交叉部分的基因，取出来，判断与交叉部分是否有重复基因
//					for (int j:newLocation){
//						int newGene=offspring.getGene(j);
//						if(oldGene==newGene){
//							//说明交叉过后基因重复
//							int geneCode=oldGene;
//							while(mapper.get(geneCode)!=null){
//								geneCode=mapper.get(geneCode);
//							}
//							offspring.setGene(i,geneCode);
//						}
//						break;//第i个基因的重复修正了,进入i+1
//					}
//				}

				// Add offspring to new population
//				System.out.println("交叉后add=");

				newPopulation.setIndividual(populationIndex, offspring);
			} else {
//				System.out.println("\ninto 不交叉 populationIndex="+populationIndex);

				// Add individual to new population without applying crossover
				newPopulation.setIndividual(populationIndex, parent1);
			}
		}

		newPopulation.shuffle();
		return newPopulation;
	}

	/**
	 * 互换两个基因
	 * @param population
	 * @return newPopulation
	 */
	public Population mutatePopulation(Population population) {
//		System.out.println("into mutation");
		// Initialize new population
		Population newPopulation = new Population(this.populationSize);
		//根据适应度fitness循环遍历种群population
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);

			// 循环遍历个体individual的基因gene
			for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
				// 保留精英个体
				if (populationIndex > this.elitismCount) {
					// 与变异率作比较
					if (this.mutationRate > Math.random()) {
						//应该是把某个个体individual的染色体的任意两个基因互换，这样产生的解才是可行解
						int index1=(int)(Math.random()*individual.getChromosomeLength());
						int index2=(int)(Math.random()*individual.getChromosomeLength());
						int tempGene=-1;
						tempGene=individual.getGene(index1);
						individual.setGene(index1,individual.getGene(index2));
						individual.setGene(index2,tempGene);
						//或者突变一个基因a为另外任何一个存在的基因b，然后，然后把另一个b改为a
					}
				}
			}
			// 无论合体是否变异，加入到新的种群
			newPopulation.setIndividual(populationIndex, individual);
		}
		// 返回个体individual变异mutation后组成的新的种群newPopulation
		newPopulation.shuffle();
		return newPopulation;
	}

}
