package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Vector;

public class CactusTesting extends CactusTraining{
	private String result;
	
	public static Vector<CactusTesting> getTestingData(){
		Vector<CactusTesting> vTest = new Vector<>();
		
		FileManager fm = new FileManager();
		
		try {
			BufferedReader cactusTesting = fm.readFile("cactusgrowth_test.csv");
			String str = "";
			cactusTesting.readLine();
			while((str = cactusTesting.readLine()) != null){
				str = str.replace(",", ".");
				String[] split = str.split(";");
				Double airHumidity = Double.parseDouble(split[0]);
				DecimalFormat df = new DecimalFormat("0.0");
				Double temperature = Double.parseDouble(split[1]);
				Double ph = Double.parseDouble(split[2]);
				String result = split[3];
				vTest.add(new CactusTesting(airHumidity, temperature, ph, result));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vTest;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public CactusTesting(Double airHumidity, Double temperature, Double ph, String result) {
		super(airHumidity, temperature, ph);
		this.result = result;
	}
	
	
	
}
