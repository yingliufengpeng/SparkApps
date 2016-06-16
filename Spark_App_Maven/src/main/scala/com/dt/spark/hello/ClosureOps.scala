package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */
object ClosureOps {
    def main(args: Array[String]) {

        val data = List( 1 , 2 , 3 , 4 , 5 , 6 )
        var sum = 0
        data.foreach( sum += _ )

        val m = ( x : Int ) => x + 5

        def add( more : Int ) = ( x : Int ) => x + more

        def outer( more : Int )  = {
            def inner( x : Int ) : Int = {
                more + x
            }

            inner _
        }


    }
}
