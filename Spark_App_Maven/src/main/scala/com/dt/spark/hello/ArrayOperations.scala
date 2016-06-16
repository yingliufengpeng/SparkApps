package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */
object ArrayOperations {
    def main(args: Array[String]) {
        val array = Array( 1 , 2 , 3 , 4 , 5 )
        for( i <- 0 until array.length ) {
            println( array( i ) )
        }

//        for( elem <- array ) {
//            println( elem )
//        }

        for( elem <- array ) println( elem )
    }
}
