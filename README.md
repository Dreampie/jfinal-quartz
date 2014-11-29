jfinal-quartz
============

jfinal  quartz  plugin

use  it  very easy:

```java

//write job
public class DemoJob implements Job {
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    //get param  from  job
    Map data = jobExecutionContext.getJobDetail().getJobDataMap();
    System.out.println("hi,"+data.get("name")+"," + new Date().getTime());
  }
}

//start it

//quartzKey   must  different  every task
//addParam  to  add param in job
//run  once
new QuartzCronJob(new QuartzKey(1, "test", "test"), "*/5 * * * * ?", JobDemo.class).addParam("name", "quartz").start();
//run cron
new QuartzOnceJob(new QuartzKey(2, "test", "test"), new Date(), DemoJob.class).addParam("name", "quartz").start();

```
