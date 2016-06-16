package com.dt.spark.jvm.basics

/**
  * Created by peng.wang on 2016/4/21.
  */
object HelloStackOverFlow {


    def main(args: Array[String]) {
        println( "HelloStackOverFlow" )
        val helloStackOverFlow = new HelloStackOverFlow

        helloStackOverFlow.count
    }
}

class HelloStackOverFlow
{
    var counter = 0

    def count: Unit =
    {
        println( "Stack......" )
        counter += 1
        count
    }
}

