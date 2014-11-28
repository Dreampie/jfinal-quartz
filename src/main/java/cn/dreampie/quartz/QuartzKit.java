package cn.dreampie.quartz;

import org.quartz.*;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by wangrenhui on 14-4-21.
 */
public class QuartzKit {

  private static SchedulerFactory factory;

  private QuartzKit() {
  }

  /**
   * 定时开始任务
   *
   * @param startTime start time
   * @param quartzKey quartzKey
   * @param jobClass  class
   */
  public static void startJobOnce(QuartzKey quartzKey, Date startTime, Class<? extends Job> jobClass) {
    long id = quartzKey.getId();
    String name = quartzKey.getName();
    String group = quartzKey.getGroup();

    try {
      if (factory != null) {
        Scheduler sched = factory.getScheduler();
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
      }
    } catch (Exception e) {
      throw new RuntimeException("Can't start once job.", e);
    }
  }

  /**
   * 定时开始任务
   *
   * @param quartzKey quartzKey
   * @param cronExp   cronExp
   * @param jobClass  class
   */
  public static void startJobCron(QuartzKey quartzKey, String cronExp, Class<? extends Job> jobClass) {
    long id = quartzKey.getId();
    String name = quartzKey.getName();
    String group = quartzKey.getGroup();

    try {
      if (factory != null) {
        Scheduler sched = factory.getScheduler();
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
      }
    } catch (Exception e) {
      throw new RuntimeException("Can't start cron job.", e);
    }
  }

  /**
   * 停止任务
   *
   * @param quartzKey quartzKey
   */
  public static void stopJob(QuartzKey quartzKey) {
    long id = quartzKey.getId();
    String name = quartzKey.getName();
    String group = quartzKey.getGroup();
    try {
      if (factory != null) {
        Scheduler scheduler = factory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + name + "_" + id, "group_" + group + "_" + id);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
          scheduler.pauseTrigger(triggerKey);
          scheduler.unscheduleJob(triggerKey);
          scheduler.deleteJob(trigger.getJobKey());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Can't stop job.", e);
    }
  }

  public static boolean pauseJob(QuartzKey quartzKey) {
    long id = quartzKey.getId();
    String name = quartzKey.getName();
    String group = quartzKey.getGroup();
    try {
      if (factory != null) {
        Scheduler scheduler = factory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + name + "_" + id, "group_" + group + "_" + id);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
          scheduler.pauseTrigger(triggerKey);
          return true;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Can't pause job.", e);
    }
    return false;
  }

  public static boolean resumeJob(QuartzKey quartzKey) {
    long id = quartzKey.getId();
    String name = quartzKey.getName();
    String group = quartzKey.getGroup();
    try {
      if (factory != null) {
        Scheduler scheduler = factory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + name + "_" + id, "group_" + group + "_" + id);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
          scheduler.resumeJob(trigger.getJobKey());
          return true;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Can't resume job.", e);
    }
    return false;
  }

  public static boolean checkJob(QuartzKey quartzKey) {
    long id = quartzKey.getId();
    String name = quartzKey.getName();
    String group = quartzKey.getGroup();
    if (factory != null) {
      try {
        Scheduler scheduler = factory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + name + "_" + id, "group_" + group + "_" + id);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
          return true;
        }
      } catch (Exception e) {
        throw new RuntimeException("Can't check job.", e);
      }
    }
    return false;
  }


  public static void updateJobOnce(QuartzKey quartzKey, Date startTime, Class<? extends Job> jobClass) {
    stopJob(quartzKey);
    startJobOnce(quartzKey, startTime, jobClass);
  }

  public static void updateJobCron(QuartzKey quartzKey, String cronExp, Class<? extends Job> jobClass) {
    stopJob(quartzKey);
    startJobCron(quartzKey, cronExp, jobClass);
  }

  public static SchedulerFactory getFactory() {
    return factory;
  }

  public static void setFactory(SchedulerFactory factory) {
    QuartzKit.factory = factory;
  }
}