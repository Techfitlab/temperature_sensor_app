package temperature_sensor_app;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.*;

public class Graph extends JPanel {

		public float[] data;
	    final int PAD = 60;
	    final int TEXTPAD = 10;
	    
	    public Graph(float[] data) {
	    	this.data = data;
	    }

	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D)g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                            RenderingHints.VALUE_ANTIALIAS_ON);
	        int w = getWidth();
	        int h = getHeight();
	        
	        // Draw ordinate (x-axis).
	        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
	        
	        // Draw absicissa (y-axis).
	        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
	        
	        // Draw labels.
	        Font font = g2.getFont();
	        FontRenderContext frc = g2.getFontRenderContext();
	        LineMetrics lm = font.getLineMetrics("0", frc);
	        float sh = lm.getAscent() + lm.getDescent();
	        
	        // Absicissa label.
	        String s = "Recent <------------------------------- Readings -------------------------------> Oldest";
	        float sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
	        float sw = (float)font.getStringBounds(s, frc).getWidth();
	        float sx = (w - sw)/2;
	        g2.drawString(s, sx, sy);
	        
	        // Get temperature stats from sampled data
	        float maxTemperature = getMax();
	        float minTemperature = getMin();
	        float averageTemperature = getAverage();

	        // Horizontal distance between points to plot
	        double xInc = (double)(w - 2*PAD)/(data.length-1);
	        // Vertical distance between points to plot, depending on the max reading
	        double scale = (double)(h - 2*PAD)/(maxTemperature - minTemperature);
	        
	        // Draw data lines
	        g2.setPaint(Color.black.darker());
	        for(int i = 0; i < data.length-1; i++) {
	            double x1 = PAD + i*xInc;
	            double y1 = h - PAD - scale*(data[i] - minTemperature);
	            double x2 = PAD + (i+1)*xInc;
	            double y2 = h - PAD - scale*(data[i+1] - minTemperature);
	            g2.draw(new Line2D.Double(x1, y1, x2, y2));
	        }
	        
	        // Draw minimum line, label and value
	        g2.setPaint(Color.yellow.darker());
	        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
	        s = "MIN";
	        g2.drawString(s, TEXTPAD, (int) (h-PAD));
	        s = Float.toString(minTemperature);
	        g2.drawString(s, w-PAD+TEXTPAD, (int) (h-PAD));
	        
	        // Draw maximum line, label and value
	        g2.setPaint(Color.red.darker());
	        g2.draw(new Line2D.Double(PAD, h-PAD-scale*(maxTemperature - minTemperature), w-PAD, h-PAD-scale*(maxTemperature - minTemperature)));
	        s = "MAX";
	        g2.drawString(s, TEXTPAD, (int) (h-PAD-scale*(maxTemperature - minTemperature)));
	        s = Float.toString(maxTemperature);
	        g2.drawString(s, w-PAD+TEXTPAD, (int) (h-PAD-scale*(maxTemperature - minTemperature)));
	        
	        // Draw average line, label and value
	        g2.setPaint(Color.green.darker());
	        g2.draw(new Line2D.Double(PAD, h-PAD-scale*(averageTemperature - minTemperature), w-PAD, h-PAD-scale*(averageTemperature - minTemperature)));
	        s = "AVE";
	        g2.drawString(s, TEXTPAD, (int) (h-PAD-scale*(averageTemperature - minTemperature)));
	        s = Float.toString(averageTemperature);
	        g2.drawString(s, w-PAD+TEXTPAD, (int) (h-PAD-scale*(averageTemperature - minTemperature)));
	        
	        
	        // Mark data points.
	        g2.setPaint(Color.red);
	        for(int i = 0; i < data.length; i++) {
	            double x = PAD + i*xInc;
	            double y = h - PAD - scale*(data[i] - minTemperature);
	            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
	        }

	    }
	    
	    private float getMax() {
	        float max = data[0];
	        for(int i = 1; i < data.length; i++) {
	            if(data[i] > max)
	                max = data[i];
	        }
	        return max;
	    }
	    
	    private float getMin() {
	        float min = data[0];
	        for(int i = 1; i < data.length; i++) {
	            if(data[i] < min)
	                min = data[i];
	        }
	        return min;
	    }
	    
	    private float getAverage() {
	        float total = 0;
	        for(int i = 0; i < data.length; i++) {
	            total += data[i];
	        }
	        return total/data.length;
	    }

	    public static void main(String[] args) {
	    	float[] data = {
	    	        21, 14, 18, 03, 86, 88, 74, 87, 54, 77
	    	    };
	        JFrame f = new JFrame();
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.add(new Graph(data));
	        f.setSize(800,600);
	        f.setLocation(200,200);
	        f.setVisible(true);
	    }
}
