package com.dt.spark.sparkstreaming

import java.sql.DriverManager

import java.sql.Connection
import org.apache.spark.Logging
//import scala.collection.immutable.Queue
import scala.collection.mutable.ArrayBuffer



/**
  * Created by peng.wang on 2016/5/5.
  */
object ConnectionPool_scala extends Logging{


    private var connectionQueue = ArrayBuffer[ Connection ]()

    try{
        Class.forName("com.mysql.jdbc.Driver")
        logInfo( "------------加载成功！！！-------------" )

    }catch {
        case e : Exception => e.printStackTrace()
    }


    def getConnection : Connection =
        this.synchronized{ //可以把括号换成大括号，那么就不要里面的大括号了
            //特别注意，这是一个语句，最好用{}括起来
                try {
                    if (connectionQueue.isEmpty) {
                        for (i <- 0 until 5) {
                            val conn = DriverManager.getConnection(
                                "jdbc:mysql://Master:3306/sparkstreaming?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8",
//                                "jdbc:mysql://Master:3306/sparkstreaming",
                                "root",
                                "123456")

//                            connectionQueue = connectionQueue :+ conn
                            connectionQueue += conn
                        }
                    }

                } catch {
                    case e: Exception => e.printStackTrace()
                }

                val head = connectionQueue.head
                connectionQueue -= head
                head
        }






    def returnConnection( conn : Connection ) = {
//        connectionQueue = connectionQueue :+ conn
        connectionQueue += conn
    }
}



