package cn.dreampie.quartz;

import cn.dreampie.PropertiesUtils;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.IPlugin;
import org.joda.time.DateTime;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by wangrenhui on 14-4-21.
 */
public class QuartzPlugin implements IPlugin {

  private Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 默认配置文件*
   */
  private String config = "/quartz/quartz.properties";

  private String jobs = "/quartz/jobs.properties";

  private static List<String> startedJob = Lists.newArrayList();

  public QuartzPlugin() {

  }

  public QuartzPlugin(String config) {
    this.config = config;
  }

  @Override
  public boolean start() {
    try {
      //加载配置文件
      Properties properties = PropertiesUtils.me().loadPropertyFile(config);
      //实例化
      QuartzFactory.me().sf = new StdSchedulerFactory(properties);
      //获取Scheduler
      Scheduler sched = QuartzFactory.me().sf.getScheduler();
      //内存,数据库的任务
      sched.start();
      //属性文件中的任务
      startPropertiesJobs();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean stop() {
    try {
      QuartzFactory.me().sf.getScheduler().shutdown();
      QuartzFactory.me().sf = null;
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }


  public void startPropertiesJobs() {
    if (new File(PathKit.getRootClassPath() + jobs).exists()) {
      Properties properties = PropertiesUtils.me().loadPropertyFile(jobs);
      Enumeration enums = properties.keys();

      while (enums.hasMoreElements()) {
        String key = enums.nextElement() + "";
        if (!key.startsWith("job")) {
          continue;
        }

        String[] keyArr = key.split("\\.");
        if (startedJob.contains(keyArr[1])) {
          continue;
        }

        String jobClassKey = key.replace(keyArr[2], "class");
        String idKey = key.replace(keyArr[2], "id");
        String cronKey = key.replace(keyArr[2], "cron");
        String enable = key.replace(keyArr[2], "enable");

        //判断任务是否启用
        if (!Boolean.valueOf(properties.getProperty(enable))) {
          continue;
        }

        Integer id = Integer.parseInt(properties.getProperty(idKey));
        String jobCronExp = properties.getProperty(cronKey);
        String jobClassName = properties.getProperty(jobClassKey);
        Class clazz;
        try {
          clazz = Class.forName(jobClassName);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
        QuartzFactory.me().startJobCron(id, keyArr[1], keyArr[1], jobCronExp, clazz);
        startedJob.add(keyArr[1]);
      }
    }
  }

}
