package test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

public class TestT4 {
   
   public static void main(String[] args) {
      ScheduledJob job = new ScheduledJob();
      Date date = new Date();
      Timer jobScheduler = new Timer();
      //jobScheduler.scheduleAtFixedRate(job, 1000, 3000);
      jobScheduler.schedule(job, date, 604800000);

   }
}

class ScheduledJob extends TimerTask {
   
   public void run() {
      System.out.println(new Date());
   }
}