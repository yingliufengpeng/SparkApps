package com.dt.spark.sql

import java.sql.DriverManager

/**
  * Created by peng.wang on 2016/4/17.
  */

/*
 * 实战演示Java通过JDBC访问Thrift Server，进而访问Spark SQL，进而访问Hive，这是企业级及开发中最为常见的方式
 * @author DT大数据梦工厂
 * 新浪微博：http://weibo.com/ilovepains
 *
 */

object SparkSQLJDBC2ThriftServer {
    def main(args: Array[String]) {
        val sql = "select * from  scores "

        Class.forName( "org.apache.hive.jdbc.HiveDriver" )

//        $SPARK_HOME/sbin/start-thriftserver.sh  --master spark://Master:7077 --hiveconf hive.server2.transport.mode  --hiveconf hive.server2.thrift.http.path=cliservice
        /*
         * 注意：在连接执之前，需要启动Spark中的thrift服务
         * 即：$SPARK_HOME/sbin/start-thriftserver.sh
         * 			--hiveconf hive.server2.transport.mode
         * 			--hiveconf hive.server2.thrift.http.path=cliservice
         */

//        val conn = DriverManager.getConnection(
//            "jdbc:hive2://Master:10000"
//                + "/hive?hive.server2.transport.mode=http;hive.server2.thrift.http.path=cliservice" ,
//            "root" , "123456"
//        )

        val conn = DriverManager.getConnection(
            "jdbc:hive2://Master:10000"
                + "/hive?hive.server2.thrift.http.path=cliservice" ,
            "root" , "123456"
        )

        val preparedStatement = conn.prepareStatement( sql )
        preparedStatement.setInt( 1 , 30 )
        val resultSet = preparedStatement.executeQuery

        while( resultSet.next() )
        {
            println( resultSet.getString( 1 ) )
        }
    }
}
