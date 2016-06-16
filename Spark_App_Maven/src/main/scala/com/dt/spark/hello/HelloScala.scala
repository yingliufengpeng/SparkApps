package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */

object HelloScala {

//    println( "Please input some words blow..." )
//    var line = ""
//    do {
//        line = readLine()
//        println( "Read: " + line )
//    }while( line != "" )


    /**
      * 循环不断的读入数据，并不断的输出在控制台上！！！
      */
    def doWhile {
        var line = ""
            do {
                line = readLine()
                println( "Read: " + line )
            }while( line != "" )
    }

    /**
      * 此函数为了求出最大公约数
      * @param x 第一个参数
      * @param y 第二个参数
      * @return  最大的公约数
      */

    def looper( x : Long , y : Long ) : Long = {
        var a = x
        var b = y
        while( a != 0 ) {
            val temp = a
            a = b % a
            b = temp
        }
        b
    }

    def main(args: Array[String]) : Unit = {
        println( "This is a Spark!" )

//        doWhile

//        println( looper( 8 , 4 ) )

        /**
          * 不是太精简的语法结构
          */
//        var file = "scala.txt"
//        if( !args.isEmpty ) file = args( 0 )

        /**
          * 精简的语法结构
          */
//        val file = if( !args.isEmpty ) args( 0 ) else "scala.xml"
//        println( file )

//        for( i <- 1 to 10 ) {
//            println( "Number is : " + i )
//        }

//        val files = ( new java.io.File( "." ) ).listFiles()
//        for( file <- files ) {
//            println( file )
//        }

        val n = 99
        try {
            val half = if( n % 2 == 0 ) n / 2 else throw
                new RuntimeException( "N must be event!" )
        }catch {
            case e : Exception => println( "The exception is :" + e.getMessage )
        }finally {

        }
    }
}
