package cn.dreampie.quartz;

import cn.dreampie.PropertiesKit;
import cn.dreampie.quartz.job.QuartzCronJob;
import cn.dreampie.quartz.job.QuartzJob;
import com.jfinal.plugin.IPlugin;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by wangrenhui on 14-4-21.
 */
public class QuartzPlugin implements IPlugin {

  private Logger logger = LoggerFactory.getLogger(getClass());
  public static String dsName = "main";
  public static boolean dsAlone = false;
  /**
   * 默认配置文件*
   */
  private String config = "quartz/quartz.properties";

  private String jobs = "quartz/jobs.properties";


  public QuartzPlugin() {

  }

  public QuartzPlugin(String dsName) {
    this.dsName = dsName;
  }


  public boolean start() {
    try {
      //加载配置文件
      Properties properties = PropertiesKit.me().loadPropertyFile(config);
      //实例化
      QuartzKit.setSchedulerFactory(new StdSchedulerFactory(properties));
      //获取Scheduler
      Scheduler sched = QuartzKit.getSchedulerFactory().getScheduler();
      //内存,数据库的任务
      sched.start();
      //属性文件中的任务
      startPropertiesJobs();
      return true;
    } catch (Exception e) {
      throw new RuntimeException("Can't start quartz plugin.", e);
    }
  }


  public boolean stop() {
    try {
      QuartzKit.getSchedulerFactory().getScheduler().shutdown();
      QuartzKit.setSchedulerFactory(null);
      return true;
    } catch (Exception e) {
      throw new RuntimeException("Can't stop quartz plugin.", e);
    }
  }


  public void startPropertiesJobs() {
    if (PropertiesKit.exist(jobs)) {
      Properties properties = PropertiesKit.me().loadPropertyFile(jobs);
      Enumeration enums = properties.keys();

      while (enums.hasMoreElements()) {
        String key = enums.nextElement() + "";
        if (!key.startsWith("job")) {
          continue;
        }

        String[] keyArr = key.split("\\.");


        String jobClassKey = key.replace(keyArr[2], "class");
        String idKey = key.replace(keyArr[2], "id");
        String groupKey = key.replace(keyArr[2], "group");
        String cronKey = key.replace(keyArr[2], "cron");
        String enable = key.replace(keyArr[2], "enable");

        //判断任务是否启用
        if (!Boolean.valueOf(properties.getProperty(enable))) {
          continue;
        }

        Integer id = Integer.parseInt(properties.getProperty(idKey));
        String group = properties.getProperty(groupKey);

        QuartzKey quartzKey = new QuartzKey(id, keyArr[1], group == null ? keyArr[1] : group);

        QuartzJob quartzJob = QuartzKit.getJob(quartzKey);
        if (quartzJob != null) {
          logger.info("This  job  has started," + quartzKey);
          continue;
        }

        String jobCron = properties.getProperty(cronKey);
        String jobClassName = properties.getProperty(jobClassKey);
        Class clazz;
        try {
          clazz = Class.forName(jobClassName);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
        //启动任务
        new QuartzCronJob(quartzKey, jobCron, clazz).start();
      }
    }
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public String getJobs() {
    return jobs;
  }

  public void setJobs(String jobs) {
    this.jobs = jobs;
  }

  public static boolean isDsAlone() {
    return dsAlone;
  }

  public static void setDsAlone(boolean dsAlone) {
    QuartzPlugin.dsAlone = dsAlone;
  }
}
