package cn.dreampie.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by ice on 14-11-28.
 */
public class JobDemo implements Job {
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    System.out.println("hi" + new Date().getTime());
  }
}

