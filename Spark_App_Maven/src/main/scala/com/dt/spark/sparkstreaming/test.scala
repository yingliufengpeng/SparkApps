package com.dt.spark.sparkstreaming

import java.sql.DriverManager

import com.dt.spark.sparkstreaming.ConnectionPool_scala
import org.apache.spark.Logging

/**
  * Created by peng.wang on 2016/5/5.
  */
object test extends Logging {

    var m : List[ Int ] = _
    def main(args: Array[String]) {

        try {
//                val conn = DriverManager.getConnection(
//                    "jdbc:mysql://Master:3306/sparkstreaming",
//                    "root",
//                    "123456")

            val conn = ConnectionPool_scala.getConnection
            val sql = " insert into categorytop3( category , item , click_count ) values ( 'category' , 'item' , 61  )"
            val stmt = conn.createStatement()
            stmt.executeUpdate( sql )
            logInfo( "----------Sucess!!-----------" )
            ConnectionPool_scala.returnConnection( conn )



        }catch {
            case e : Exception => e.printStackTrace()
            logInfo( "mmm" )
        }

        if( m.isEmpty ){
            println( "kong")
        }

    }
}
