package com.dt.spark.hello

import spire.math.UInt

/**
  * Created by peng.wang on 2016/5/18.
  */
object For_Function_Advanced {
    def main(args: Array[String]) {
//        for( i <- 1 to 2 ; j <- 1 to 2 ) println( ( 100 * i + j ) + "  " )
//        println()
//
//        for( i <- 1 to 2 ; j <- 1 to 2 if i != j ) println( ( 100 * i + j ) + " " )
//        println()

        def addA( x : Int ) = x + 100
        val add = ( x : Int ) => x + 200

        /**
          * addB可以看成是一个函数的名字，也可称为地址
          * ( Int => Int ) 可以看成函数的输入为Int，输出为Int
          */
//        val addB : ( Int => Int ) = ( x => x + 300 )
//
//        println( "The result from a function is : " + addA( 2 ) )
//        println( "The result from a val is : " + add( 2 ) )
//        println( "The result from a valB is : " + addB( 2 ) )

//        def fac( n : Int ) : Int = if( n <= 0 ) 1 else fac( n - 1 )
//
//        println( "The result from a fac is : " + fac( 10 ) )

//        def combine( content : String , left : String = "[ " , right : String = " ]" ) =
//            left + content + right
//
//        println( "The result from a combine is : " + combine( "I love Spark!" ) )

        def connected( args : Int* ) = {
            var result = 0
            for( arg <- args ) result += arg
            result
        }

        println( "The result from a connected is " + connected( 1 , 2 , 3 , 4 , 5 ) )



    }
}
