package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */
object MapOperations {
    def main(args: Array[String]) {
        val ages = Map( "Rocky" -> 27 , "Spark" -> 5 )

//        for( ( k , v ) <- ages ) {
//            println( "Key is " + k + "Value is " + v )
//        }

        for( ( k , _ ) <- ages ) {  // placeholder，占位符的运用
            println( "Key is " + k  )
        }
    }
}
