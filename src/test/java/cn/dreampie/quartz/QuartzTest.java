package cn.dreampie.quartz;

import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

import static org.junit.Assert.*;

public class QuartzTest {

  @org.junit.Before
  public void setUp() throws Exception {
    QuartzPlugin quartzPlugin = new QuartzPlugin();
    quartzPlugin.start();
  }

  @Test
  public void testQuartz() throws Exception {

    QuartzKey quartzKey = new QuartzKey(1, "test", "test");

    QuartzKit.startJobCron(quartzKey, "*/5 * * * * ?", JobDemo.class);
//    Thread.sleep(10000);
//    QuartzKit.stopJob(quartzKey);
  }

}