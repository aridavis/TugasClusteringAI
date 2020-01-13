package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;


public class MainPage {
	Scanner scan = new Scanner(System.in);
	FileManager fm = new FileManager();
	Integer maxIteration = 1000000;
	Integer correctCount = 0;
	Vector<CactusTesting> vTest = new Vector<>();
	Vector<CactusTraining> vTrain = new Vector<>();
	Vector<CactusTraining> vCentroid = new Vector<>();
	Vector<CactusTraining> vTempCentroid = new Vector<>();
	Vector<Vector<CactusTraining>> vCandidate = new Vector<>();
	CactusTraining[][] map = new CactusTraining[10][10000];
	Double[][] distance = new Double[1000][10000];
	Integer centroidSize = 2;


	Double getEuclidean(CactusTraining centroid, CactusTraining target){
		Double air = Math.pow(centroid.getAirHumidity() - target.getAirHumidity(), 2);
		Double ph = Math.pow(centroid.getPh() - target.getPh(), 2);
		Double temp = Math.pow(centroid.getTemperature() - target.getTemperature(), 2);

		Double result = Math.sqrt(air+ph+temp);
		return result;
	}

	private void initCentroid(){
		int size = vTrain.size()/centroidSize;
		for(int i = 0; i<centroidSize;i++){
			vCentroid.add(vTrain.get(i*size + size/2));
			vTempCentroid.add(vTrain.get(0));
			vCandidate.add(new Vector<CactusTraining>());
		}
	}
	
	private Boolean isSameCentroid(){
		Boolean same = true;
		for(int i =0;i<centroidSize;i++){
			CactusTraining data1 = vTempCentroid.get(i);
			CactusTraining data2 = vCentroid.get(i);
			DecimalFormat df = new DecimalFormat("0.000000000");
			
			Double air1 = Double.parseDouble(df.format(data1.getAirHumidity()));
			Double air2 = Double.parseDouble(df.format(data2.getAirHumidity()));
			Double ph1 = Double.parseDouble(df.format(data1.getPh()));
			Double ph2 = Double.parseDouble(df.format(data2.getPh()));
			Double temp1 = Double.parseDouble(df.format(data1.getTemperature()));
			Double temp2 = Double.parseDouble(df.format(data2.getTemperature()));
			
			if(!(air1.equals(air2) && ph1.equals(ph2) && temp1.equals(temp2))){
				same= false;
				break;
			}
		}
		return same;
	}

	void printAllCluster() {
		for(int i = 0 ;i<centroidSize;i++) {
			System.out.println(vCandidate.get(i).size());
		}
	}
	
	private Integer findNearestCluster(int x) {
		Double min = 2000000.0;
		int idx = 0;
		for(int i = 0;i<centroidSize;i++) {
			if(distance[i][x] < min) {
				min = distance[i][x];
				idx = i;
			}
		}
		return idx;
	}

	private void updateCentroid(int idx) {
		Double ph = 0.0;
		Double air = 0.0;
		Double temperature = 0.0;
		for(int i = 0 ;i<vCandidate.get(idx).size();i++) {
			ph += vCandidate.get(idx).get(i).getPh();
			air += vCandidate.get(idx).get(i).getAirHumidity();
			temperature += vCandidate.get(idx).get(i).getTemperature();
		}
		ph /= vCandidate.get(idx).size();
		air /= vCandidate.get(idx).size();
		temperature /= vCandidate.get(idx).size();
		vCentroid.set(idx, new CactusTraining(air, temperature, ph));
	}

	private void updateTempCentroid() {
		for(int i = 0;i<centroidSize;i++){
			vTempCentroid.set(i, vCentroid.get(i));
		}
	}
	
	private void trainData(){
		int x = 0;
		while(!isSameCentroid()){
			updateTempCentroid();
			for(int i =0 ;i<centroidSize;i++) {
				for(int j = 0;j<vTrain.size();j++) {
					distance[i][j] = getEuclidean(vCentroid.get(i), vTrain.get(j));
				}
			}

			for(int i =0;i<centroidSize;i++) {
				vCandidate.set(i, new Vector<CactusTraining>());
			}
			
			for(int i = 0;i<vTrain.size();i++) {
				int idx = findNearestCluster(i);
				vCandidate.get(idx).add(vTrain.get(i));
			}

			for(int i = 0;i<centroidSize;i++) {
				updateCentroid(i);
			}
			x++;
		}
	}

	String testingData(CactusTesting testData) {
		Double airHumidity = testData.getAirHumidity();
		Double temperature = testData.getTemperature();
		Double ph = testData.getPh();
		
		CactusTraining c = new CactusTraining(airHumidity, temperature, ph);
		Double min = 9999.0;
		int idx = 0;
		for(int i = 0;i<centroidSize;i++){
			if(min > getEuclidean(vCentroid.get(i), c)) {
				min = getEuclidean(vCentroid.get(i), c);
				idx = i;
			}
		}
		String prediction = idx == 0 ? "Success" : "Fail";
		if(testData.getResult().equals(prediction)) {
			correctCount++;
		}
		return prediction;
	}
	
	String testingData(CactusTraining testData) {
		Double airHumidity = testData.getAirHumidity();
		Double temperature = testData.getTemperature();
		Double ph = testData.getPh();
		
		CactusTraining c = new CactusTraining(airHumidity, temperature, ph);
		Double min = 9999.0;
		int idx = 0;
		for(int i = 0;i<centroidSize;i++){
			if(min > getEuclidean(vCentroid.get(i), c)) {
				min = getEuclidean(vCentroid.get(i), c);
				idx = i;
			}
		}
		String prediction = idx == 1 ? "Success" : "Fail";
		return prediction;
	}
	
	public MainPage() {
		vTrain = CactusTraining.getTrainingData();
		vTest = CactusTesting.getTestingData();
		
		initCentroid();
		trainData();
		for (CactusTesting test : vTest) {
			testingData(test);
		}
		enter();
		System.out.println("Training Finished");
		System.out.println("=================");
		System.out.println("Accuracy : " + correctCount * 100/vTest.size() + "%");
		
		int input = 0;
		while(input != 4) {
			enter();
			System.out.println("Cactus Data Clustering");
			System.out.println("Accuracy : " + correctCount * 100/vTest.size() + "%");
			
			System.out.println("======================");
			System.out.println("1. Test A New Data");
			System.out.println("2. Clustering Rules");
			System.out.println("3. About");
			System.out.println("4. Exit");
			System.out.print("Choose : ");
			input = scan.nextInt();
			scan.nextLine();
			enter();
			if(input == 1) {
				enter();
				Double airHumidity;
				Double temperature;
				Double ph;
				String result;
				
				System.out.println("Rules");
				System.out.println("=====");
				System.out.println("•	Every value of the cluster result based on the following detail:");
				System.out.println("\to	Success, for every condition in the cluster which have the following criteria:");
				System.out.println("\t\t-	Higher temperature level");
				System.out.println("\t\t-	Lower air humidity");
				System.out.println("\t\t-	Lower ground’s pH");
				System.out.println("\to	Fail, for every condition in the cluster which have the following criteria:");
				System.out.println("\t\t-	Lower temperature level");
				System.out.println("\t\t-	Higher air humidity");
				System.out.println("\t\t-	Higher ground’s pH");
				System.out.println("===============================");
				System.out.println();
				System.out.print("Enter Air Humidity : ");
				airHumidity = scan.nextDouble();
				scan.nextLine();
				System.out.print("Enter Temperature (C) : ");
				temperature = scan.nextDouble();
				scan.nextLine();
				System.out.print("Enter PH : ");
				ph = scan.nextDouble();
				scan.nextLine();
				System.out.println("Testing Result : " + testingData(new CactusTraining(airHumidity, temperature, ph)));
				System.out.println("Press Enter to Continue");
				scan.nextLine();
			}
			else if(input == 2) {
				printClusteringRules();
				scan.nextLine();
			}
			else if(input == 3) {
				printAbout();
				scan.nextLine();
			}
		}
		
	}

	private void printAbout() {
		System.out.println("About");
		System.out.println("=====");
		System.out.println("This is a simple clustering application with K-Means Algorithm");
		System.out.println("The data used is Cactus Growth Data");
		System.out.println("The data is an artificial data which has ever used in Data Mining subject");
		System.out.println("========================================================");
		System.out.println("Lecturer : D1994	Drs. Antonius Herusutopo, B.E., M.Sc.");
		System.out.println("This project is made by : ");
		System.out.println("1. Ari Davis (2201765220)");
		System.out.println("2. Alicia (2201791912)");
		System.out.println("3. Andree Benaya Abyatar (2201754362)");
		System.out.println("4. Junaedi Dede (2201737910)");
		System.out.println("5. Mario Edgar Pranata (2201731775)");
	}

	private void printClusteringRules() {
		System.out.println("Rules");
		System.out.println("=====");
		System.out.println("•	Every value of the cluster result based on the following detail:");
		System.out.println("\to	Success, for every condition in the cluster which have the following criteria:");
		System.out.println("\t\t-	Higher temperature level");
		System.out.println("\t\t-	Lower air humidity");
		System.out.println("\t\t-	Lower ground’s pH");
		System.out.println("\to	Fail, for every condition in the cluster which have the following criteria:");
		System.out.println("\t\t-	Lower temperature level");
		System.out.println("\t\t-	Higher air humidity");
		System.out.println("\t\t-	Higher ground’s pH");
	}

	private void enter() {
		for(int i = 0;i < 30;i++) {
			System.out.println();
		}
	}
	public static void main(String[] args) {
		new MainPage();
	}
}
