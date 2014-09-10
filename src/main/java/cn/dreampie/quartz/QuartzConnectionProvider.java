package cn.dreampie.quartz;

import cn.dreampie.PropertiesUtils;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.plugin.druid.DruidPlugin;
import org.quartz.utils.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by wangrenhui on 14-4-21.
 */
public class QuartzConnectionProvider implements ConnectionProvider {

  private static final String DB_CONFIG = "application.properties";
  private DruidPlugin druidPlugin;

  @Override
  public Connection getConnection() throws SQLException {
    return druidPlugin.getDataSource().getConnection();
  }

  @Override
  public void initialize() throws SQLException {
    PropertiesUtils.me().loadPropertyFile(DB_CONFIG);
    druidPlugin = new DruidPlugin(
        PropertiesUtils.me().getProperty("db.default.url"),
        PropertiesUtils.me().getProperty("db.default.user"),
        PropertiesUtils.me().getProperty("db.default.password"),
        PropertiesUtils.me().getProperty("db.default.driver"));
    // StatFilter提供JDBC层的统计信息
    druidPlugin.addFilter(new StatFilter());
    // WallFilter的功能是防御SQL注入攻击
    WallFilter wallFilter = new WallFilter();
    wallFilter.setDbType("mysql");
    druidPlugin.addFilter(wallFilter);
    druidPlugin.start();
  }

  @Override
  public void shutdown() throws SQLException {
    druidPlugin.stop();
  }

}