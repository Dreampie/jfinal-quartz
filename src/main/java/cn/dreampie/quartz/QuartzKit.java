package cn.dreampie.quartz;

import cn.dreampie.quartz.job.QuartzJob;
import com.google.common.collect.Lists;
import org.quartz.SchedulerFactory;

import java.util.List;

/**
 * Created by wangrenhui on 14-4-21.
 */
public class QuartzKit {

  private static SchedulerFactory schedulerFactory;

  private static List<QuartzJob> quartzJobs = Lists.newArrayList();

  private QuartzKit() {
  }

  public static QuartzJob getJob(QuartzKey quartzKey) {
    for (QuartzJob quartzJob : quartzJobs) {
      if (quartzJob.getQuartzKey().equals(quartzKey)) {
        return quartzJob;
      }
    }
    return null;
  }

  public static void stopJob(QuartzKey quartzKey) {
    for (QuartzJob quartzJob : quartzJobs) {
      if (quartzJob.getQuartzKey().equals(quartzKey)) {
        quartzJob.stop();
      }
    }
  }

  public static void pauseJob(QuartzKey quartzKey) {
    for (QuartzJob quartzJob : quartzJobs) {
      if (quartzJob.getQuartzKey().equals(quartzKey)) {
        quartzJob.pause();
      }
    }
  }

  public static void resumeJob(QuartzKey quartzKey) {
    for (QuartzJob quartzJob : quartzJobs) {
      if (quartzJob.getQuartzKey().equals(quartzKey)) {
        quartzJob.resume();
      }
    }
  }


  public static SchedulerFactory getSchedulerFactory() {
    return schedulerFactory;
  }

  public static void setSchedulerFactory(SchedulerFactory schedulerFactory) {
    QuartzKit.schedulerFactory = schedulerFactory;
  }

  public static List<QuartzJob> getQuartzJobs() {
    return quartzJobs;
  }

  public static void setQuartzJobs(List<QuartzJob> quartzJobs) {
    QuartzKit.quartzJobs = quartzJobs;
  }

  public static void addQuartzJob(QuartzJob startedJob) {
    QuartzKit.quartzJobs.add(startedJob);
  }

  public static void removeQuartzJob(QuartzJob startedJob) {
    QuartzKit.quartzJobs.remove(startedJob);
  }
}