jfinal-quartz
============

jfinal  quartz  plugin

use  [search](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22jfinal-quartz%22)

use  it  very easy:

```java
//quartz start plugin
plugins.add(new QuartzPlugin());

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
//run  cron
new QuartzCronJob(new QuartzKey(1, "test", "test"), "*/5 * * * * ?", DemoJob.class)

.addParam("name", "quartz").start();
//run once
new QuartzOnceJob(new QuartzKey(2, "test", "test"), new Date(), DemoJob.class)

.addParam("name", "quartz").start();

```
