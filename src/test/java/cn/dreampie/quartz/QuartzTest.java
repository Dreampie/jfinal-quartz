package cn.dreampie.quartz;

import cn.dreampie.quartz.job.QuartzOnceJob;
import org.junit.Test;

import java.util.Date;

public class QuartzTest {

  @org.junit.Before
  public void setUp() throws Exception {
    QuartzPlugin quartzPlugin = new QuartzPlugin();
    quartzPlugin.start();
  }

  @Test
  public void testQuartz() throws Exception {

    QuartzKey quartzKey = new QuartzKey(1, "test", "test");

    System.out.println(quartzKey.equals(new QuartzKey(1, "test", "test")));

//    new QuartzCronJob(quartzKey, "*/5 * * * * ?", JobDemo.class).addParam("name", "quartz").start();

    new QuartzOnceJob(quartzKey, new Date(), DemoJob.class).addParam("name", "quartz").start();
    new QuartzOnceJob(quartzKey, new Date(), DemoJob.class).addParam("name", "quartz").start();
//    Thread.sleep(10000);
//    QuartzKit.stopJob(quartzKey);
  }

}