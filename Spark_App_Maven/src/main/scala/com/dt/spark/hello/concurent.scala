package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */
object concurent {
    def main(args: Array[String]) {

        def runtwice ( body : => Unit ): Unit = {
            body
            body
        }

        runtwice( println( "OK" ) )
    }
}
