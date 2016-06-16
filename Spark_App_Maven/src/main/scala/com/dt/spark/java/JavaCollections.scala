package com.dt.spark.java

import java.util

/**
  * Created by peng.wang on 2016/4/21.
  */
object JavaCollections {
    def main(args: Array[String]) {
        val list = new util.ArrayList[ String ]()
        val collection = new util.ArrayList[ String ]()
        list.add( "spark" )
        list.add( "Hadoop" )
        list.add( "Kafka" )
        list.add( "Tachyon" )

        collection.add( "Hive" )
        collection.add( "MySQL ")

        list.addAll( collection )

        println( list )

        val data = list.toArray()

//        data.map( println( _ ) )

        val iterator = data.iterator

        while( iterator.hasNext )
        {
            println( iterator.next() )
        }

    }
}
