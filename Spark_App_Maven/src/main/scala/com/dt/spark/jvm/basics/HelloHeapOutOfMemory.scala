package com.dt.spark.jvm.basics

/**
  * Created by peng.wang on 2016/4/21.
  */
import scala.collection.mutable.ArrayBuffer

class Person

class Student  extends Person

object HelloHeapOutOfMemory {
    def main(args: Array[String]) {
        println( "HelloHeapOutOfMemory" )
        val persons = ArrayBuffer[ Person ]()
        var count = 0
        while( true )
        {
            persons += new Person
            println( "Instance : " + ( count += 1 , count  ) )
        }
    }
}
