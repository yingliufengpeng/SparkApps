package com.dt.spark.java

import java.io.{InputStreamReader, BufferedReader}
import java.net.Socket



/**
  * Created by peng.wang on 2016/4/21.
  */
object SocketClient {
    def main(args: Array[String]) {
        val client = new Socket( "localhost" , 9999 )
        val buffer = new BufferedReader( new InputStreamReader( client.getInputStream ) )

        println( "The content from server is : " + buffer.readLine() )

        buffer.close()
        client.close()
    }
}
