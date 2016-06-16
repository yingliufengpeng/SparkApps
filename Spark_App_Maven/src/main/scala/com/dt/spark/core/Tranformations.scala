package com.dt.spark.core

import org.apache.spark.{SparkContext, SparkConf}

/**
  * 最常用最重要的Spark Tranformations案例实战
  * Created by peng.wang on 2016/3/2.
  */
object Tranformations {



    def main(args: Array[String]) {

        val sc = sparkContext("Tranformations")

        //        mapTranformation( sc )
        //        filterTransformation( sc )
        //        flatmMapTranformation( sc )

//        groupByKeyTranformation(sc)
//        reduceByKeyTranformation(sc)
        joinTransformation( sc )
        cogroupTransformation( sc )
        //停止SparkContext的相关资源
        sc.stop

    }

    def sparkContext(name: String) = {
        //创建SparkConf,初始化的配置
        val conf = new SparkConf().setAppName("Tranformations").setMaster("local")
        //创建SparkContext，这是一个RDD创建的唯一入口，也是Driver的灵魂，是通往集群的唯一通道
        val sc = new SparkContext(conf)

        sc
    }

    def mapTranformation(sc: SparkContext): Unit = {
        //根据集合创建RDD
        val nums = sc.parallelize(1 to 100)

        //Map适用于任何元素，且对其作用的集合的每一个元素循环遍历并调用其作为参数的函数对每一个遍历的元素进行具体化的处理
        val mapped = nums.map(item => 2 * item)
        //收集计算结果，并通过foreach打印
        mapped.collect.foreach(println)
    }

    def filterTransformation(sc: SparkContext): Unit = {
        //根据集合创建RDD
        val nums = sc.parallelize(1 to 100)

        val filtered = nums.filter(item => item % 2 == 0)
        filtered.collect.foreach(println)
    }

    def flatmMapTranformation(sc: SparkContext): Unit = {
        val bigData = Array("Scala Spark", "Java Hadoop", "Java Tachyon")
        //创建一字符串为元素类型的ParallelCollectionRDD
        val bigDataString = sc.parallelize(bigData)
        val words = bigDataString.flatMap(line => line.split(" "))
        words.collect.foreach(println)
    }

    def groupByKeyTranformation(sc: SparkContext): Unit = {
        val data = Array((100, "Spark"), (100, "Tachyon"), (79, "Hadoop"), (80, "Kafka"), (88, "HBase"))
        val dataRDD = sc.parallelize(data)
        val grouped = dataRDD.groupByKey()
        grouped.collect.foreach(println)
    }

    def reduceByKeyTranformation(sc: SparkContext): Unit = {
        val lines = sc.textFile("D:/Program Files (x86)/spark/README.md")

        val words = lines.flatMap { line => line.split(" ") }
        val pairs = words.map { word => (word, 1) }
        val wordCounts = pairs.reduceByKey(_ + _)
        wordCounts.foreach(w_pair => println(w_pair._1 + " : " + w_pair._2))
    }

    def joinTransformation(sc: SparkContext): Unit =
    {
        val studentNames = Array(
            ( 1 , "Spark" ) ,
            ( 2 , "Tachyon" ) ,
            ( 3 , "Hadoop" )
        )

        val studentScores = Array(
            ( 1 , 100 ) ,
            ( 2 , 95 ) ,
            ( 3 , 55 )

        )

        val names = sc.parallelize( studentNames )
        val scores = sc.parallelize( studentScores )

        val studentNameAndScore = names.join( scores )
        studentNameAndScore.collect.foreach( println )
    }

    def cogroupTransformation(sc: SparkContext) =
    {
        val nameList = Array(
            1 -> "Spark" ,
            2 -> "Tachyon" ,
            3 -> "Hadoop"

        )

        val scoresList = Array(
            1 -> 100 ,
            2 -> 90 ,
            3 -> 70 ,
            1 -> 110 ,
            2 -> 95 ,
            2 -> 60
        )

        val names = sc.parallelize( nameList )
        val scores = sc.parallelize( scoresList )

        val nameScores = names.cogroup( scores )

        nameScores.collect.foreach( pair => {
            println( "Student ID: " +  pair._1 )
            println( "Name: " + pair._2._1 )
            println( "Score: " + pair._2._2 )

        } )
    }
}


