package temperature_sensor_app;

import java.io.*;

public class LED {
private static String LED0_PATH = "/sys/class/leds/led0";
    
    public void flash (String command) {
        try {
            if (command.equalsIgnoreCase("On") || command.equalsIgnoreCase("Off")) {
                BufferedWriter bw = new BufferedWriter ( 
                        new FileWriter (LED0_PATH+"/trigger"));
                bw.write("none");
                bw.close();
                bw = new BufferedWriter ( 
                        new FileWriter (LED0_PATH+"/brightness"));
                bw.write(command.equalsIgnoreCase("On")? "1":"0");
                bw.close();
            }
            else {
                System.out.println("Invalid command");
            }
        }
        catch(IOException e){
            System.out.println("Rasberry Pi LEDs");
        }
    }
}
