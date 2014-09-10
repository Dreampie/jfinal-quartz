package cn.dreampie.quartz;

import org.quartz.*;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by wangrenhui on 14-4-21.
 */
public class QuartzFactory {

  public SchedulerFactory sf;
  private static QuartzFactory quartzFactory = new QuartzFactory();

  private QuartzFactory() {
  }

  public static QuartzFactory me() {
    return quartzFactory;
  }

  /**
   * 定时开始任务
   *
   * @param startTime start time
   * @param id        quartz id
   * @param name      name
   * @param group     group
   * @param jobClass  class
   */
  public void startJobOnce(Date startTime, int id, String name, String group, Class<? extends Job> jobClass) {
    try {
      Scheduler sched = sf.getScheduler();
      // define the job and tie it to our HelloJob class
      JobDetail job = newJob(jobClass)
          .withIdentity("job_" + name + "_" + id, "group_" + group + "_" + id)
          .requestRecovery()
          .build();
      job.getJobDataMap().put(group + "_" + name, id);
      // 定时执行
      Trigger trigger = newTrigger()
          .withIdentity("trigger_" + name + "_" + id, "group_" + group + "_" + id)
          .startAt(startTime)
          .build();


      sched.scheduleJob(job, trigger);
      sched.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 定时开始任务
   *
   * @param id        quartz id
   * @param name      name
   * @param group     group
   * @param cronExp   cronExp
   * @param jobClass  class
   */
  public void startJobCron(int id, String name, String group, String cronExp, Class<? extends Job> jobClass) {
    try {
      Scheduler sched = sf.getScheduler();
      // define the job and tie it to our HelloJob class
      JobDetail job = newJob(jobClass)
          .withIdentity("job_" + name + "_" + id, "group_" + group + "_" + id)
          .requestRecovery()
          .build();
      job.getJobDataMap().put(group + "_" + name, id);
      // 执行表达式
      CronTrigger trigger = newTrigger()
          .withIdentity("trigger_" + name + "_" + id, "group_" + group + "_" + id)
          .withSchedule(cronSchedule(cronExp)).build();


      sched.scheduleJob(job, trigger);
      sched.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 停止任务
   *
   * @param name  name
   * @param group group
   * @param id    id
   */
  public void stopJob(String name, String group, int id) {
    try {
      if (sf != null) {
        Scheduler scheduler = sf.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + name + "_" + id, "group_" + group + "_" + id);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
          scheduler.pauseTrigger(triggerKey);
          scheduler.unscheduleJob(triggerKey);
          scheduler.deleteJob(trigger.getJobKey());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}