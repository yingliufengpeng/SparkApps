package com.dt.spark.jvm.basics

import java.nio.ByteBuffer

/**
  * Created by peng.wang on 2016/4/21.
  */
object HelloDirectMemoryOutOfMemoy {
    var count = 1
    final val ONE_GB = 1024 * 1024 * 1024

    def main(args: Array[String]) {

        while( true )
        {
            println( s"$count" )
            val buffer = ByteBuffer.allocate( ONE_GB )
            count += 1
        }
    }
}
