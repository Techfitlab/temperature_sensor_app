package temperature_sensor_app;

import java.io.*;
import java.util.Random;

public class TemperatureService {
    public int getTemperature() {

    	int tempC = 0;
    	String fileName = "/sys/class/thermal/thermal_zone0/temp";
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	tempC = Integer.parseInt(line);
            	System.out.println("Temp: " + tempC);
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        
        return tempC;
    }
    
    public static void main (String[] args) {
    	new TemperatureService().getTemperature();
    }
}