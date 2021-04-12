package temperature_sensor_app;

import java.util.Calendar;

public class DateTimeService
{
   private Calendar calendar;

   public DateTimeService() {
   }

   //method returns date/time as a formatted String object
   public String getDateAndTime()
   {
	 this.calendar = Calendar.getInstance();	 
	 System.out.println("The Raspberry Pi 4 time is: " + this.calendar.getTime().toString());
     return this.calendar.getTime().toString();	
   }	
}