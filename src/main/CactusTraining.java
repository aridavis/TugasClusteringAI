package main;
import java.io.BufferedReader;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Vector;

public class CactusTraining {
	private Double airHumidity;
	private Double temperature;
	private Double ph;
	public Double getAirHumidity() {
		return airHumidity;
	}
	public void setAirHumidity(Double airHumidity) {
		this.airHumidity = airHumidity;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getPh() {
		return ph;
	}
	public void setPh(Double ph) {
		this.ph = ph;
	}
	public CactusTraining(Double airHumidity, Double temperature, Double ph) {
		super();
		this.airHumidity = airHumidity;
		this.temperature = temperature;
		this.ph = ph;
	}

	public static Vector<CactusTraining> getTrainingData(){
		Vector<CactusTraining> vTrain = new Vector<>();
		
		FileManager fm = new FileManager();
		
		try {
			BufferedReader cactusTraining = fm.readFile("cactusgrowth_train.csv");
			String str = "";
			cactusTraining.readLine();
			int i = 1;
			while((str = cactusTraining.readLine()) != null){
				str = str.replace(",", ".");
				String[] split = str.split(";");
				Double airHumidity = Double.parseDouble(split[0]);
				DecimalFormat df = new DecimalFormat("0.0");
				Double temperature = Double.parseDouble(df.format((Double.parseDouble(split[1])-32) * 5 / 9));
				Double ph = Double.parseDouble(split[2]);	
				vTrain.add(new CactusTraining(airHumidity, temperature, ph));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vTrain;
	}
	
}
