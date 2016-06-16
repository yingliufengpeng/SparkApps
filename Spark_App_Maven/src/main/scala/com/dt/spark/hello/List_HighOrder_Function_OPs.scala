package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */
object List_HighOrder_Function_OPs {

    def hastotallyZeroRow( m : List[ List[ Int ] ] ) = m.exists( row => row.forall( _ == 0 ) )

    def main(args: Array[String]) {
        val m = List(
            List( 1 , 0 , 0 ) ,
            List( 0 , 1 , 0 ) ,
            List( 0 , 0 , 0 )
        )
        println( hastotallyZeroRow( m ) )
    }
}
