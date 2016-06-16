package com.dt.spark.java

import java.io.PrintStream
import java.net.{ServerSocket, URL, InetAddress}
import java.util.Scanner

//import org.apache.hadoop.hdfs.server.common.JspHelper.Url



/**
  * Created by peng.wang on 2016/4/20.
  */
object JavaNet {
    def main(args: Array[String]) {
        val local =  InetAddress.getLocalHost
        val remote = InetAddress.getByName( "www.baidu.com" )

        println( "Local ip : " + local.getHostAddress )
        println( "Baidu ip :" + remote.getHostAddress )

        val url = new URL( "http://www.baidu.com" )
//        val inputStream = url.openStream()
//
//        val scanner = new Scanner( inputStream )
//        scanner.useDelimiter( "\n" )
//
//        while( scanner.hasNext )
//        {
//            println( scanner.next )
//         }

//        val urlConnection = url.openConnection()
//        println( "The length : " + urlConnection.getContentLength )
//        println( "The Content type : " + urlConnection.getContentType )

        val server = new ServerSocket( 9999 )





        val flag = true
        var count = 0
        while( flag )
        {

            val client = server.accept()
            count += 1
            println( "======It's ready to accept client's request======" )
            new Thread( new Runnable {
                override def run(): Unit =
                {
                    val output = new PrintStream( client.getOutputStream )

                    output.println( "Lite is short , you need Spark!!! " + count  )
                }
            }).start()
        }

        println( "connect is close!!" )



        server.close()



    }
}
