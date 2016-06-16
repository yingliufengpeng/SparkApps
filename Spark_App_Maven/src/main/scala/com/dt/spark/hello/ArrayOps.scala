package com.dt.spark.hello

import scala.collection.mutable.ArrayBuffer

/**
  * Created by peng.wang on 2016/5/18.
  */
object ArrayOps {
    def main(args: Array[String]) {
        val nums = new Array[ Int ]( 10 )
        val a = new Array[ String ]( 10 )
        val s = Array( "Hello" , "World" )

        s( 0 ) = "GoodBye"
        val b = ArrayBuffer[ Int ]()
        b += 1
        b += ( 1 , 2 , 3 , 5 )
        b ++= Array( 8 , 13 , 22 )
        b.trimEnd( 5 )
    }
}
