package com.dt.spark.core

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by peng.wang on 2016/3/15.
  */
object tranformation {
    def main(args: Array[String]) {
        //main方法里面调用的每一个功能都必须是模块化的，每个模块可以是用函数来进行封装！
        val sc = sparkContext( "Tranformation Operations" )   //创建SparkContext
//        mapTranformation( sc )            //map案例
//        filterTranformation( sc )         //filter 案例
//        flatMapTranformation( sc )        //flatMap案例
//        groupByKeyTranformation( sc )     //groupByKey案例
//        reduceByKeyTranformation( sc )    //reduceByKey案例
//        joinTransformation( sc )          //join案例
//        cogroupTransformation( sc )         //cogroup案例
        secondarySortKey( sc )              //secondarySortKey案例
        sc.stop                             //停止SarkContext，熊安徽相关的Driver对象，释放资源


    }

    def sparkContext( name : String ) =
    {
        val conf =  new SparkConf().setAppName( "Tranformaton" ).setMaster( "local" ) //创建SparkConf，初始化的配置
        val sc = new SparkContext( conf ) //创建SparkContext，这是第一个RDD创建的唯一入口，也是Driver的灵魂，是通往集群的唯一通道
        sc
    }

    def mapTranformation( sc : SparkContext ): Unit =
    {
        val nums = sc.parallelize( 1 to 10000000 )  //根据集合创建RDD
        val mapped = nums.map( item => 2 * item )  //map适用于任何类型的元素，且其作用的集合中每一个元素循环遍历并调用其作为参数的函数对每一个遍历的元素进行具体刷的处理
        mapped.collect.foreach( println )       //收集计算结果并通过foreach循环打印
    }

    def filterTranformation( sc : SparkContext ): Unit =
    {
        val nums = sc.parallelize( 1 to 10000000 )  //根据集合创建RDD
        val filtered = nums.filter( item => item % 2 == 0 ) //过滤出我们所需要的偶数类型
                                                            //根据filter中作为参数的函数的Booleean值来判断符合条件的元素，并打印基于这些元素构成的MapPartitionRDD
        filtered.collect.foreach( println )

    }

    def flatMapTranformation( sc : SparkContext ): Unit =
    {
        val bigData = Array( "Scala Spark " , "Java Hadoop" , "Java Tachyon" )  //实例化字符串类型的Array
        val bigDataString = sc.parallelize( bigData ) //创建以字符串为元素类型的ParalleCollectionRDD
        val words = bigDataString.flatMap( line =>  line.split( " " ) )  //产生的结果为：
                                                                                        /*
                                                                                            Scala
                                                                                            Spark
                                                                                            Java
                                                                                            Hadoop
                                                                                            Java
                                                                                            Tachyon
                                                                                        */
        words.collect.foreach( println )
    }


    def groupByKeyTranformation( sc : SparkContext ): Unit =
    {
        //准备数据
        val data = Array( 100 -> "Spark" , 100 -> "Tachyon" , 70 -> "Hadoop" , 80 -> "Kafa" , 80 -> "HBase" )
        val dataRDD = sc.parallelize( data )  //创建RDD
        dataRDD.groupByKey. //按照相同的Key对Value进行分组，分组后的value是一个集合
            collect.foreach( println )
    }

    def reduceByKeyTranformation( sc : SparkContext ): Unit =
    {

        val lines = sc.textFile( "D:/Program Files (x86)/spark/README.md" )

        /*
            对初始化的RDD进行Transformation级别的处理，例如map，filter等高阶函数的编程，来进行具体的数据计算
            对每一行的字符串拆分成单个的单词
         */
        val words = lines.flatMap{ line => line.split( " " ) }

        /*
            对初始的RDD进行Transformation级别的垂，例如map，filter等高阶函数的编程，来进行具体的数据计算
            在单词拆分的基础上对每个单词实例计数为1 ，也就是word => ( word , 1 )
         */
        val pairs = words.map{ word => ( word , 1 ) }

        /*
           对初始的RDD进行Transformation级别的处理，例如map，filter等高阶函数等的编程，来进行具体的数据计算
           在每个单词实例计数为1的基础上统计每个单词在文件中出现的总次数
        */
        val wordCounts = pairs.reduceByKey( _ + _ )

        wordCounts.collect.foreach( w_pair =>  println( w_pair._1 + " : " + w_pair._2 ) )
    }

    def joinTransformation( sc : SparkContext ): Unit =
    {

        //模拟数据库表的结构来模拟实战，相当于对两张表做join操作的过程！
        //写代码的操作确实是非常耗体力的过程
        val studentNames = Array(
            1 -> "Spark" , 2 -> "Tachyon"  , 3 -> "Hadoop"
        )

        val studentScores = Array(
            1 -> 100 , 2 -> 95 , 3 -> 60
        )

        val names = sc.parallelize( studentNames )         //转化为PartitionRDD
        val scores = sc.parallelize( studentScores )        //转化为PartitionRDD

        names.join( scores ).collect.foreach( println )
    }

    def cogroupTransformation( sc : SparkContext ): Unit =
    {
        val nameList = Array( 1 -> "Spark" , 2 -> "Tachyon" , 3 -> "Hadoop" )
        val scoreList = Array( 1 -> 100 , 2 -> 90 , 3 -> 70 , 1 -> 110 , 2 -> 95 , 2 -> 60 )

        val names = sc.parallelize( nameList )
        val scores = sc.parallelize( scoreList )

        names.cogroup( scores ).collect.foreach( println )
    }

    //自定义二次排序
    /*
        二次排序，具体的实现步骤
        第一步：按照Ordered和Serrializable接口实现子弟和工艺的Key
        第二步：将要进行二次排序的文件加载进来的< key , value >类型的RDD
        第四步：去除掉排序的Key，只保留排序的结果
        @author DT大数据梦工厂 王家林  && 王鹏
        新浪微博：http://weibo.com/ilovepains
     */

    class SecondarySortKey( val first : Int , val second : Int ) extends Ordered[ SecondarySortKey ] with Serializable
    {
        override def compare( other : SecondarySortKey ) : Int  =
        {
            if( first - other.first != 0 )
                first - other.first
            else
                second - other.second
        }
    }

    def secondarySortKey( sc : SparkContext ): Unit =
    {
        val lines = sc.textFile( "D:\\scala\\WordCount2\\src\\com\\dt\\spark\\helloSpark.txt" )
        val pairWithSortKey = lines.map( line => {
            var key_value = new SecondarySortKey( line.split( " " )( 0 ).toInt , line.split( " " )( 1 ).toInt )
            //因为map是从一个集合转换到另一个集合，而且，有函数的操作的功能，所以，最后的一个表达式就是集合生成的结果！
            ( key_value , line )
        }  )
        pairWithSortKey.sortByKey( false ).map( line => line._2 ).collect.foreach( println )
    }


}
