package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

public class FileManager {
	final String FILEPATH = new File("").getAbsolutePath();
	
	BufferedReader readFile(String file) throws FileNotFoundException{
			BufferedReader br = new BufferedReader(new FileReader(FILEPATH+"\\src\\"+file));
			return br;
	}
	
	
	
	
}
