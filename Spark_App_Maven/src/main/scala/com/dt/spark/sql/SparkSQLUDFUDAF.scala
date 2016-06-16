package com.dt.spark.sql

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by peng.wang on 2016/4/17.
  * 通过案例实战Spark SQL下的UDF和UDAF的具体使用
  * UDF：User Defined Function，用户自定义的函数，函数的输入时一条具体的数据记录，实际上讲
  * 即将就是普通的Scala的函数，
  * UDAF：User Defiened Aggregation Function，用户自定义的聚合函数，函数本身作用于数据集合，
  * 能够聚合操作基础上进行自定义操作
  *
  * 实质上讲，例如说UDF会被Spark SQL中的Catalyst封装成为Expression，最终胡通过eval方法来计算
  * 输入的数据Row（此处的Row和DataFrame中Row没有任何关系）
  *
  */
object SparkSQLUDFUDAF {
    def main(args: Array[String]) {

        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
        val conf = new SparkConf()
        conf.setAppName( "SparkSQLUDFUDAF" )
        conf.setMaster( "local[4]" )

        val sc = new SparkContext( conf )
        val sqlContext = new SQLContext( sc )

        //模拟实际使用的数据
        val bigData = Array(
            "Spark" ,
            "Spark" ,
            "Spark" ,
            "hadoop",
            "hadoop",
            "Spark" ,
            "Spark" ,
            "hadoop",
            "Spark" ,
            "hadoop",
            "Spark" ,
            "Spark"
        )

        /*
            基于提供的数据创建DataFrame
         */
        val bigDataRDD = sc.parallelize( bigData )

        val bigDataRDDRow = bigDataRDD.map( item => Row( item ) )

        val structType = StructType(
            Array( StructField( "word" , StringType , true ) )
        )

        val bigDataDF = sqlContext.createDataFrame( bigDataRDDRow , structType )

        bigDataDF.registerTempTable( "bigDataTable" )  //注册成为临时表

        /*
            通过SQLContext注册UDF，在Scala 2.10.x版本UDF函数最好可以接受22个输入参数
         */
        sqlContext.udf.register( "computeLength" , ( input : String ) => input.length )

        //直接在SQL语句中使用UDF，就像使用SQL自动的内部函数一样
        sqlContext.sql( "select word , computeLength( word ) as length from bigDataTable " ).show

        sqlContext.udf.register( "wordcount" , new MyUDAF )

        sqlContext.sql( "select word , wordcount( word ) as count , computeLength( word ) as length " +
            "from  bigDataTable group by word " ).show


        while( true )
        {

        }
    }
}


/*
   按照模板实现UDAF
 */
class MyUDAF extends UserDefinedAggregateFunction
{
    //该方法指定具体输入数据的类型
    override def inputSchema: StructType = StructType( Array( StructField( "input" , StringType , true ) ) )

    //在进行聚合操作的时候所需要处理的数据的结果类型
    override def bufferSchema: StructType = StructType( Array( StructField( "input" , IntegerType , true ) ) )

    //指定UDAF函数计算的返回结果类型
    override def dataType: DataType = IntegerType

    override def deterministic: Boolean = true

    /*
        在Aggregate之前，每组数据初始化结果
     */
    override def initialize(buffer: MutableAggregationBuffer): Unit = ( buffer( 0 ) = 0 )


    /*
        在进行聚合的时候，每当有新的值进来，对分组后的聚合如何进行计算
        本地聚合的操作，相当于Hadoop MapReduce的Combiner
     */
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit =
    {
        buffer( 0 ) = buffer.getAs[ Int ]( 0 ) + 1
    }

    /*
        最后在分布式节点进行Local Reduce完成后需要进行全局级别的Merge操作
     */
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit =
    {
        buffer1( 0 ) = buffer1.getAs[ Int ]( 0 ) + buffer2.getAs[ Int ]( 0 )
    }

    /*
        返回UDAF租后的计算结果
     */
    override def evaluate(buffer: Row): Any = buffer.getAs[ Int ]( 0 )


}
