jfinal-quartz
============

jfinal  quartz  plugin，查看其他插件-> [Maven](http://search.maven.org/#search%7Cga%7C1%7Ccn.dreampie)

maven 引用  ${jfinal-quartz.version}替换为相应的版本如:0.2

```xml
<dependency>
<groupId>cn.dreampie</groupId>
<artifactId>jfinal-quartz</artifactId>
<version>${jfinal-quartz.version}</version>
</dependency>
```

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
