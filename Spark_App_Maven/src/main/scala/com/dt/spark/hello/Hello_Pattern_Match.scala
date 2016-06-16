package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */
object Hello_Pattern_Match {
    def main(args: Array[String]) {

//        val data = 30
//        data match {
//            case 1 => println( "First" )
//            case 2 => println( "Second" )
//            case _ => println( "Not Known Number" )
//        }
//
//        val result = data match {
//            case i if 1 == i => "The First"
//            case number if number % 2 == 0 =>
//                println( "xxxxxxxxxx" )
//                number
//            case _ => "Not Known Number"
//        }
//        println( result )

        "Spark ! ".foreach( c => println(
             c match {
                case ' ' => "space"
                case ch => "Char: " + ch
            }
        ))
    }
}
