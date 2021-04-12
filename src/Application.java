package temperature_sensor_app;

import javax.swing.*;
import java.util.*; 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class Application extends JFrame implements ActionListener {

	private JButton setSamplingButton;
	private JTextField samplingRateInput;
	private JLabel lastReadingStats, intervalSetConfirmation;
	private JPanel graph;
	private int samplingIntervalInSeconds;
	private String ipAddress;
	private ArrayList<Float> temperatureList;
	private Graph graphObj;
	private static int readingCount = 1;
	
	public Application(String ip) {
	  // call the parent class constructor - sets the frame title
	  super("Simple Swing Example");
	  this.ipAddress = ip;
	  float[] data = {0};
	  
	  // Top panel that contains graph
	  this.graphObj = new Graph(data);
	  this.graph = this.graphObj;
	  // Set starting panel size
	  graph.setPreferredSize(new Dimension(800, 600));
	  // Make the pane scroll-able
      JScrollPane graphScrollPane = new JScrollPane(graph);
      JPanel graphPanel = new JPanel(new BorderLayout());
      graphPanel.add(graphScrollPane);

      // Bottom panel that contains input area and trigger button 
      this.samplingRateInput = new JTextField();
      this.setSamplingButton = new JButton("Set Interval and Start");
      this.intervalSetConfirmation = new JLabel("Please provide sampling interval (in seconds)");
      this.setSamplingButton.addActionListener(this);
      JPanel inputPanel = new JPanel();      
      inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
      inputPanel.add(samplingRateInput);
      inputPanel.add(intervalSetConfirmation);
      inputPanel.add(setSamplingButton);
      
      // Panel that holds current reading stats
      JPanel currentReading = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
      this.lastReadingStats = new JLabel();
      currentReading.add(lastReadingStats);

      // Parent panel that hold the 3 panels above
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
      mainPanel.add(graphPanel);
      mainPanel.add(inputPanel);
      mainPanel.add(currentReading);

      JFrame frame = new JFrame("Gaurav's Application");
      Dimension d = new Dimension(810,680);
	  frame.setPreferredSize(d);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(mainPanel);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		// on button click
	  if (e.getActionCommand().equals("Set Interval and Start")) {
		// get input from the text box
		samplingIntervalInSeconds = Integer.parseInt(this.samplingRateInput.getText());
		// update label to show confirmation message
	    this.intervalSetConfirmation.setText("Sampling interval set to " 
	    		+ samplingIntervalInSeconds + " second(s).");
	    // hide text box and button
	    this.samplingRateInput.setEditable(false);
	    this.samplingRateInput.setVisible(false);
	    this.setSamplingButton.setVisible(false);
	    
	    Client theApp = new Client(this.ipAddress);
	    
	    // Create a new timer to execute every x seconds set by the user
	    Timer timer = new Timer();
	    int begin = 0;
	    int timeInterval = samplingIntervalInSeconds * 1000;
	    
	    // this will hold the historical temperature readings
	    temperatureList = new ArrayList<>();
	    
	    timer.schedule(new TimerTask() {
	       @Override
	       public void run() {
	    	   // call the server to get temperature reading
	    	   float temperature = getTemperatureFromServer(theApp);
	    	   // generate graph with current and historical readings
	    	   generateGraph(temperatureList, temperature);
	       }
	    }, begin, timeInterval);
	    
	  }
	}
	
	public void generateGraph(ArrayList<Float> temperatureList, float temperature) {
		// Insert at index 0
		temperatureList.add(0, temperature);
		// If size becomes 21, remove the last element 
		if (temperatureList.size() > 20) {
			temperatureList.remove(temperatureList.size() - 1);
		}
		
		// convert to float array from ArrayList as it is the expected graph input format
		float[] temperatureArray = new float[temperatureList.size()];
		int i = 0;
		for (float f : temperatureList) {
			temperatureArray[i++] = f;
		}
		
		// Set graph data and refresh
		this.graphObj.data = temperatureArray;
		this.graph.revalidate();
		this.graph.repaint();
	}
	
	public float getTemperatureFromServer(Client theApp) {
		// call the server
		String stats = theApp.getStats();
		// response format is comma separated - datetime,temperate
		String[] statsList = stats.split(",");
		// temperature is received in mili-celsius, divide by 1000 to convert to celsius
		float temperature = Float.parseFloat(statsList[1])/1000;
		// update stats label to display current readings
		this.lastReadingStats.setText("Last reading at: " + statsList[0] + " | Reading number:" 
				+ readingCount + " | Temperature: " + temperature);
		// increment reading counter
		readingCount++;
		
		return temperature;
	}

	public static void main(String args[]) {
		if(args.length==1){
			new Application(args[0]);
		}
    	else
    	{
    		System.out.println("Error: you must provide the address of the server");
    		System.out.println("Usage is:  java Client x.x.x.x  (e.g. java Client 192.168.7.2)");
    		System.out.println("      or:  java Client hostname (e.g. java Client localhost)");
    	}    
    	System.out.println("**. End of Application.");

	}
}

