package com.dt.spark.design_pattern.Iterator

/**
  * Created by peng.wang on 2016/6/1.
  */
object Client {
    def main(args: Array[String]) {
        val cmt = new ConcreteMyAggregate()
        cmt.addObject( "aa" )
        cmt.addObject( "bb" )
        cmt.addObject( "cc" )

        val myIterator = cmt.createIterator

        while( myIterator.hasNext ) {
            println( myIterator.getCurrentObj )
            myIterator.next
        }

    }
}
