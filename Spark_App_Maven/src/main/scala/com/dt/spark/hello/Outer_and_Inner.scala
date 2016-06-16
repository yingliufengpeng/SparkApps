package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */

class Outer( val name : String ) {  outer =>
    class Inner( val name : String ) {
        def foo( b : Inner ) = println( "Outer: " + outer.name +
            " Inner: " + b.name )
    }
}

object Outer_and_Inner {
    def main(args: Array[String]) {
        val outer1 = new Outer( "Spark" )
        val outer2 = new Outer( "Hadoop" )
        val inner1 = new outer1.Inner( "Scala" )
        val inner2 = new outer2.Inner( "Hadoop" )
        inner1.foo( inner1 )
        inner2.foo( inner2 )
    }
}
