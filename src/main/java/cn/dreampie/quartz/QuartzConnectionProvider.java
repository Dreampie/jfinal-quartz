package cn.dreampie.quartz;

import cn.dreampie.PropertiesKit;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.druid.DruidPlugin;
import org.quartz.utils.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by wangrenhui on 14-4-21.
 */
public class QuartzConnectionProvider implements ConnectionProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private Config config;

  @Override
  public Connection getConnection() throws SQLException {
    return config.getConnection();
  }

  @Override
  public void initialize() throws SQLException {
    config = DbKit.getConfig(QuartzPlugin.dsName);
    if (config == null) {
      throw new RuntimeException("quartz datasource  not found");
    }
  }

  @Override
  public void shutdown() throws SQLException {
    if (QuartzPlugin.dsAlone)
      config.getConnection().close();
    else
      logger.info("quartz datasource is not alone,so you cant close it");
  }

}