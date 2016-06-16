package com.dt.spark.sparkstreaming

import java.sql.{ Connection , DriverManager , ResultSet , PreparedStatement }
import kafka.serializer.StringDecoder
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.{RowFactory, Row}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

import java.util.concurrent.LinkedBlockingQueue
import scala.collection.mutable.HashSet
import scala.collection.mutable.ArrayBuffer


/**
  * Created by peng.wang on 2016/4/19.
  *     在线处理广告点击流
  *     广告点击的基本数据格式：timestamp、ip、userID、adID、province、city、
  */
object AdClickedStreamingStats {
    def main(args: Array[String]) {
        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
        /*
            第一步：配置SparkConf：
            1，至少有2条线程：因为Spark Streaming应用程序在运行的时候，至少有一条
            线程用用于不断的训话结束数据，并且至少有一条线程用于处理接受数据（否则的话
            无法有线程用于处理数据，随着时间的推移，内存和磁盘都会不堪重负）
            2，对于集群而言，每个Executor一般的肯定不止一个线程，那对于处理Spark Streaming
            的应用程序而言，每个Executor一般分配多少Core比较合适？根据我们过去的经验，5个
            左右的Core是最佳的（一个段子：Core分配为奇数个Core表现最佳，例如3个、5个、7个Core等）
         */
        val conf = new SparkConf().setMaster( "local[4]" ).setAppName( "AdClickedStreamingStats" )

        /*
            第二步：创建SparkStreamingContext：
            1，这个是SparkStreaming应用程序所有功能的起始点和程序调度的核心
            SparkStreamingContext的构建可以基于SparkConf参数，也可基于持久化的SparkStreamingContext
            的内容恢复过来（典型的场景是Driver崩溃后重新启动，由于Spark Streaming具有连续7*24小时不
            间断运行的特征，所有需要在Driver重新启动后继续上一次的状态，此时的状态恢复需要基于曾经的
            Checkpoint）
            2，在一个Spark Straming的应用程序中可以创建若干个SparkStreamingContext对象，使用下一个SparkStreaming
            之前就需把前面正在运行的SparkSteamingContext对象关闭掉，由此，我们获得一个重大的启发，
            SparkStreaming也只是Spark Core上的一个应用程序而已，只不过SparkStreaming框架想要运行的话，
            只需要工程需写业务逻辑处理代码
         */
        val streamContext = new StreamingContext( conf , Durations.seconds( 10 ) )

        /*
            第三步：创建SparkStreaming输入数据来源input Stream：
            1，数据输入来源可以基于File、HDFS、Flume、Kafka、Socket等
            2，在这里我们指定数据来源于网络Socket端口，Spark Streaming连接上该端口并在运行的时候一直监听
            该端口的数据（当然该端口服务首先必须存在），并且在后续会根据业务需要不断的有数据产生（当然对
            与SparkStreaming应用程序乐颜，有无数据其处理流程都是一样的）
            3，如果经常在每间隔5秒钟没有数据的话，会不断的启动空的Job，其实是会造成调度资源的浪费，因为并
            没有数据需要发生计算：
                实例的企业级生产环境的代码在具体提交Job前，会判断时候有数据，如果没有的话，就不在提交Job

            4，在本案例中具体参数含义
                第一个参数是StreamingContext实例
                第二个参数是Zookeeper集群信息（接受Kafka数据的时候会从Zookeeper中获取Offset等元数据信息）
                第三个参数是Consumer Group
                第四个参数是消费的Topic以及并发读取Topic中Partitions的并发数（线程数）
         */


        val adClickedStreaming = KafkaUtils.createDirectStream[ String , String , StringDecoder , StringDecoder]( streamContext ,

            Map( "metadata.broker.list" -> "Master:9092,Worker:9092" ) ,
            Set( "SparkStreamngDirected" )
        )

        /**
          * 因为要对黑名单进行在线过滤，而数据是在RDD中的，所以必然使用transfrom这个函数；
          * 但是在这里我们必须使用transform，原因是读取进来的Kafka数据是Pair< String , String >
          * 类型，另外的一个原因是过滤后的数据要进行进一步处理，所以必须是读进来的Kafka数据的原始
          * 类型DStream< String , String >
          *
          *     再次说明每个Batch Duration中实际上讲输入的数据卆被一个且仅仅被一个RDD封装的，你可以
          *     有多个InputDStream，但是其实在生产Job的时候，这些不同的 InputDStream再Batch Duration
          *     就相当于Spark基于HDFS数据操作的不同文件来源而已罢了
         */

        val filteredadClickedStreaming = adClickedStreaming.transform( rdd => {
                /**
                  *  在线黑名单过滤的思路步骤：
                  *  1，从数据库中获取黑名单转换成RDD，即新的rD的实例封装黑名单数据
                  *  2，然后把代表黑名单的RDD的实例和Batch Duration产生rdd进行join操作，准确的说
                  *        是进行leftOuterJoin操作，也就说使用Batch Duration产生的rdd和代表黑名单
                  *        的RDD的实例进行leftOuterJoin操作，如果两者都有内容的话，就会是true，否
                  *        则的话就是false
                  *
                  * 我们要留下的是leftOuterJon操作结果为false
                  *
                  */


                val blackListNames = new ArrayBuffer[ String ]()
                val jdbcWrapper = JDBCWrapper.getJDBCInstance
                jdbcWrapper.doQuery( " SELECT * FROM blacklisttable " , null , new ExecuteCallBack {
                    override def resultCallBack(result: ResultSet): Unit = {
                        while( result.next() ) {
                            blackListNames += result.getString( 1 )
                        }
                    }
                })

                val blackListTuple = new ArrayBuffer[ Tuple2[ String , Boolean ] ]()
                for( name <- blackListNames ) {
                    blackListTuple += name -> true
                }

                val blackListFromDB  = blackListTuple
                val sc = rdd.context

                /**
                  *  黑名单的表中只有userID，但是如果要进行join操作的话，就必须是Key-Value，所以在
                  *  这里我们需要基于数据表中的数据产生key-value类型的数据集合
                  */

                val blackListRDD = sc.parallelize( blackListFromDB )

                /**
                  *  进行操作的时候肯定是基于userID进行join的，所以必须把传入的rdd进行mapToPar操作
                  *  转化成为符合格式的rdd
                  */

                val rdd2pair = rdd.map( pair => {
                    val userID = pair._2.split( "\t" )( 2 )
                    ( userID , pair )
                })

                val joined = rdd2pair.leftOuterJoin( blackListRDD )

                val result = joined.filter( pair => {
                    if( pair._2._2.isDefined) {
                        true
                    }else {
                        false
                    }
                }).map( pair => {
                    pair._2._1
                })

                result
            })

        /**
          * 广告点击累计动态更新，每个updateStateBykey都会在Batch Duration的时间间隔的基础上进行
          * 更高点击次数的更新，更新之后一般都会持久化到外部存储设备上，在这里，我们存储到MySQL中
          */
        val updateStateBykeyDStream = filteredadClickedStreaming.map( pair => {
            val splited = pair._2.split( "\t" )
            val timestamp = splited( 0 )
            val ip = splited( 1 )
            val userID = splited( 2 )
            val adID = splited( 3 )
            val province = splited( 4 )
            val city = splited( 5 )
            val clickedRecord = timestamp + "_" + adID + province + "_" + city
            ( clickedRecord , 1L )
        } ).updateStateByKey( ( values , state_option : Option[ Long ] ) => {
            /**
              * values：代表的是当前的key在当前的Batch Duration中出现的次数的集合，例如{ 1 , 1 , 1 , 1 , 1 }
              * state_option：代表当前key在以前的Batch Duration中积累下来的结果
              */
            var clickedTotalHistory = 0L
            if( state_option.isDefined ) {
                clickedTotalHistory = state_option.get
            }

            values.map( clickedTotalHistory += _ )

            Some( clickedTotalHistory )

        })
        updateStateBykeyDStream.foreachRDD( rdd => {
            rdd.foreachPartition(
                /**
                  * 在这里我们使用数据连接池的高效读写数据库的方式写入数据库的MySQl；
                  * 由于传入的参数是一个Iterator类型的集合，所以为了更加高效的操作
                  *     我们需要批量处理，例如说一次性插入1000条Record，使用insertBatch或者
                  *     updateBatch类型的操作；
                  * 插入的用户信息可以只包含：timestamp、adID、clickedCount、time
                  *
                  * 这里有一个问题：可能出现两条记录的Key是一样的，此时需要更新累加操作
                  *
                  */

                records => {
                    val AdClickedList = new ArrayBuffer[ AdClicked ]()
                    while( records.hasNext ) {
                        val record = records.next()
                        val splited = record._1.split( "\t" )

                        val adClicked = new AdClicked
                        adClicked.timestamp = splited( 0 )
                        adClicked.adID = splited( 3 )
                        adClicked.province = splited( 4 )
                        adClicked.city = splited( 5 )
                        adClicked.clickedCount = record._2

                        AdClickedList += adClicked
                    }

                    val inserting = new ArrayBuffer[ AdClicked ]()
                    val updating = new ArrayBuffer[ AdClicked ]()
                    val jdbcWrapper = JDBCWrapper.getJDBCInstance

                    /**
                      * adclicked表的字段：timestamp、ip、userID、adID、province、city
                      */
                    for( adclicked <- AdClickedList ) {
                        jdbcWrapper.doQuery( " SELECT count( 1 ) FROM adclickedcount WHERE " +
                            " timestamp = ?  AND adID = ? AND province = ? AND city = ? " ,
                            List( adclicked.timestamp , adclicked.adID , adclicked.province , adclicked.city  ) ,
                            new ExecuteCallBack { override def resultCallBack(result: ResultSet): Unit = {
                                if( result.next() ) {
                                    val count = result.getLong( 1 )
                                    adclicked.clickedCount = count
                                    updating += adclicked
                                }else {
                                    inserting += adclicked
                                }
                            }
                            })
                    }

                    //adclicked 表的字段：timestamp、ip、userID、adID、province、city、clickedCount
                    val insertParametersList = new ArrayBuffer[ List[ Any ] ]()
                    for( insertRecord <- inserting ) {
                        insertParametersList += List(
                            insertRecord.timestamp ,
                            insertRecord.adID ,
                            insertRecord.province ,
                            insertRecord.city ,
                            insertRecord.clickedCount
                        )
                    }
                    jdbcWrapper.doBatch( " INSERT INTO adclickedcount VALUES( ? , ? , ? , ? , ?  ) " , insertParametersList.toList )

                    //adclicked 表的字段：timestamp、ip、userID、adID、province、city、clickedCount
                    val updateParametersList = new ArrayBuffer[ List[ Any ] ]()
                    for( updateRecord <- updating ) {
                        updateParametersList += List(
                            updateRecord.clickedCount,
                            updateRecord.timestamp ,
                            updateRecord.adID ,
                            updateRecord.province ,
                            updateRecord.city
                        )
                    }
                    jdbcWrapper.doBatch( " UPDATE adclickedcount set clickedCount = ? " +
                        " WHERE timestamp = ? AND adID = ? AND province = ? AND city = ? " ,
                        updateParametersList.toList )
                }

            )
        })




        /*
            第四步：接下来就像对于RDD编程一样基于Dstream进行编程！！！原因是DStream是RDD产生的模板（或者是类），在Spark
            Stream发生计算前，其实质是把每个Batch的DStream的操作翻译成为对RDD的操作！！！

            对初始的DStream进行Transformation级别的处理，例如map、filter等高阶函数等的编程，来进行具体的数据计算
            广告点击的基本数据格式：timestamp、ip、userID、adID、province、city
         */
        val pairs = adClickedStreaming.map( tuple => {
            val splited = tuple._2.split("\t")
            val timestamp = splited( 0 )    //yyyy-MM-dd
            val ip = splited( 1 )
            val userID = splited( 2 )
            val adID = splited( 3 )
            val province = splited( 4 )
            val city = splited( 5 )

            val clickedRecord = timestamp + "_" + ip + "_" + userID + "_" + adID + province + "_" + city
            ( clickedRecord , 1L )
        })


        /**
          * 第四步：对初始的DStream进行Transformation级别的处理，例如map、filter等高阶函数等的编程，来进行具体的数据计算
          * 计算每个Batch Duration中每个User广告点击量
         */

        val adClickedUsers = pairs.reduceByKey( _ + _ )


        /**
          * 计算出什么叫有效的点击？
          * 1,复杂化的一般都是采用机器学习训练好的模型直接在线进行过滤
          * 2,简单的？可以通过一个Batch Duration中的点击次数来判断是不是非法广告点击
          *     程序是会可能模拟真是的广告点击行为，所以通过一个Batch来判断是不完整
          *     的，我们需要对例如一天（也可以是每一小时）的数据进行判断！
          * 3,比如在机器学习退而求其次的做法如下：
          *     例如：一段时间内，同一个IP（MAC地址）有多个账户的访问
          *     例如：可以统一一天内一个用户点击广告的次数，如果一天点击同样的广告操作
          *         50次的话，就列入黑名单
          *
          * 黑名单有一个重点的特征：动态生成！！！所以每一个Batch Duration都要考虑是否
          *     有新的黑名单的加入，此时黑名单需要存储起来，存储在DB/Redis中即可
          *
          * 例如邮件系统中的“黑名单”，可以采用Spark Streaming不断的监控每个用户的操作，
          * 如果用户发送邮件的频率超过了给定的阈值，我们就暂时把用户列入“黑名单”，从而
          * 组织用户过渡频繁的发送邮件
          */

        val filteredClickInBatch = adClickedUsers.filter( pair => {
            if( 1 < pair._2 ) {
                //更新黑名单的数据表
                false
            }
            else
                true
        })


        /*
            此处的print并不会直接触发Job的支持，因为现在一切都是Spark Streaming框架的控制之下，对于Spark Stream
            而言具体是否触发真正的Job运行时基于设置Duration来设置时间间隔

            诸位一定要注意的是SparkStreaming应用程序要执行具体的Job，对Dtream就必须有output Stram操作，
            output Stream有很多类型的函数触发，例如print、saveAsTextFile、saveAsHadoopFiles等，最为重
            要的一个方法是foreaechRDD，因为Spark Streaming处理的结果一般会放在Redis、DB、DashBoard等上面
            ，foreachRDD主要就是用来完成这些功能的，而且可以随意的自定义具体数据到底放到哪里
         */


        filteredClickInBatch.foreachRDD( rdd => {
            rdd.foreachPartition(
                /**
                  * 在这里我们使用数据连接池的高效读写数据库的方式写入数据库的MySQl；
                  * 由于传入的参数是一个Iterator类型的集合，所以为了更加高效的操作
                  *     我们需要批量处理，例如说一次性插入1000条Record，使用insertBatch或者
                  *     updateBatch类型的操作；
                  * 插入的用户信息可以只包含：useID、adID、clickedCount、time
                  *
                  * 这里有一个问题：可能出现两条记录的Key是一样的，此时需要更新累加操作
                  *
                  */

                records => {
                    val userAdClickedList = new ArrayBuffer[ UserAdClicked ]()
                    while( records.hasNext ) {
                        val record = records.next()
                        val splited = record._1.split( "\t" )
                        val userClicked = new UserAdClicked
                        userClicked.timestamp = splited( 0 )
                        userClicked.ip = splited( 1 )
                        userClicked.userID = splited( 2 )
                        userClicked.adID = splited( 3 )
                        userClicked.province = splited( 4 )
                        userClicked.city = splited( 5 )

                        userAdClickedList += userClicked
                    }

                    val inserting = new ArrayBuffer[ UserAdClicked ]()
                    val updating = new ArrayBuffer[ UserAdClicked ]()
                    val jdbcWrapper = JDBCWrapper.getJDBCInstance

                    /**
                      * adclicked表的字段：timestamp、ip、userID、adID、province、city
                      */
                    for( clicked <- userAdClickedList ) {
                        jdbcWrapper.doQuery( " SELECT count( 1 ) FROM adclicked WHERE " +
                            " timestamp = ? AND userID = ? AND adID = ? " , List( clicked.timestamp , clicked.userID , clicked.adID  ) ,
                            new ExecuteCallBack { override def resultCallBack(result: ResultSet): Unit = {
                                if( result.next() ) {
                                    val count = result.getLong( 1 )
                                    clicked.clickedCount = count
                                    updating += clicked
                                }else {
                                    inserting += clicked
                                }
                            }
                        })
                    }

                    //adclicked 表的字段：timestamp、ip、userID、adID、province、city、clickedCount
                    val insertParametersList = new ArrayBuffer[ List[ Any ] ]()
                    for( insertRecord <- inserting ) {
                        insertParametersList += List(
                            insertRecord.timestamp ,
                            insertRecord.ip ,
                            insertRecord.userID ,
                            insertRecord.adID ,
                            insertRecord.province ,
                            insertRecord.city ,
                            insertRecord.clickedCount
                        )
                    }
                    jdbcWrapper.doBatch( " INSERT INTO adclicked VALUES( ? , ? , ? , ? , ? , ? , ? ) " , insertParametersList.toList )

                    //adclicked 表的字段：timestamp、ip、userID、adID、province、city、clickedCount
                    val updateParametersList = new ArrayBuffer[ List[ Any ] ]()
                    for( updateRecord <- updating ) {
                        updateParametersList += List(
                            updateRecord.timestamp ,
                            updateRecord.ip ,
                            updateRecord.userID ,
                            updateRecord.adID ,
                            updateRecord.province ,
                            updateRecord.city ,
                            updateRecord.clickedCount
                        )
                    }
                    jdbcWrapper.doBatch( " UPDATE adclicked set clickedCount = ? " +
                        " WHERE timestamp = ? AND ip = ? AND userID = ? AND adID = ? AND province = ? AND city = ? " ,
                        updateParametersList.toList )
                }

            )
        })

        val blackListBasedOnHistory = filteredClickInBatch.filter( pair => {
            val splited = pair._1.split("\t")
            val date = splited(0)
            val userID = splited(2)
            val adID = splited(3)

            /**
              * 接下来根据date、userID、adID等条件去查询用户点击广告的数据表，获得
              * 总的点击次数，这个时候基于点击次数判断是否属于黑名单点击
              */

            val clickeCountTotalToday = 81
            if (clickeCountTotalToday > 50) {
                true
            } else {
                false
            }
        })


        /**
          * 对整个黑名单的整个RDD进行去重操作！！！
          */

        val blackListUser = blackListBasedOnHistory.map( pair => {
            pair._1.split( "\t" )( 1 )
        })

        val blackListUniqueuserIDtBasedOnHistory = blackListUser.transform( rdd => {
            rdd.distinct()
        })

        blackListUniqueuserIDtBasedOnHistory.foreachRDD( rdd => {
            rdd.foreachPartition( records => {
                /**
                  * 在这里我们使用数据连接池的高效读写数据库的方式写入数据库的MySQl；
                  * 由于窜入的参数是一个Iterator类型的集合，所以为了更加高效的操作
                  *     我们需要批量处理，例如说一次性插入1000条Record，使用insertBatch或者
                  *     updateBatch类型的操作；
                  * 插入的用户信息可以只包含：useID
                  * 此时直接插入数据库即可
                  *
                  */

                val blackList = new ArrayBuffer[ String ]()
                while( records.hasNext ) {
                    blackList += records.next()
                }

                val jdbcWrapper = JDBCWrapper.getJDBCInstance
                jdbcWrapper.doBatch( "INSERT INTO blacklisttable VALUES ( ? )" , List( blackList.toList ) )
            })
        })

        /*
            SparkStreaming执行引擎也就是Driver开始运行，Driver启动的时候是位于一条新的线程中，当然其部
            有消息循环体，接受应用应用程序本身后者Executor中的消息

         */

        /**
          * 对广告点击进行TopN的计算，计算出每天每个省份的Top5的排名的广告
          * 因为我们直接对RDD进行操作，所以使用了transform算子
          */
        updateStateBykeyDStream.transform( rdd => {
             val row_rdd = rdd.map( pair => {
                val splited = pair._1.split( "_" )
                val timestamp = splited( 0 )
                val adID = splited( 1 )
                val province = splited( 2 )
                val clickedRecord = timestamp + "_" + adID + province + "_" + province

                ( clickedRecord , pair._2 )
            }).reduceByKey( _ + _ ).map( pair => {
                val splited = pair._1.split( "_" )
                val timestamp = splited( 0 )
                val adID = splited( 1 )
                val province = splited( 2 )

                Row( timestamp , adID , province , pair._2 )
            })

            val structType = DataTypes.createStructType( Array(
                DataTypes.createStructField( "timestamp" , DataTypes.StringType , true ) ,
                DataTypes.createStructField( "adID" , DataTypes.StringType , true ) ,
                DataTypes.createStructField( "province" , DataTypes.StringType , true ) ,
                DataTypes.createStructField( "clickedCount" , DataTypes.StringType , true )
            ))
            val hiveContext = new HiveContext( row_rdd.sparkContext )
            val df = hiveContext.createDataFrame( row_rdd , structType )
            df.registerTempTable( "topNTableSource" )
            val sqlText = " SELECT timestamp , adID , province , clickedCount FROM " +
                " ( SELECT timestamp , adID , province , clickedCount , ROW_NUMBER() " +
                " OVER( PARTITION BY province ORDER BY clickedCount DESC  ) rank " +
                " FORM topNTableSource ) subquery WHERE rank <= 5 "
            val result = hiveContext.sql( sqlText )

            result.rdd
        }).foreachRDD(  rdd => {
            rdd.foreachPartition(row_iter => {
                val adProvinceTopN = new ArrayBuffer[AdProvinceTopN]()
                while (row_iter.hasNext) {
                    val row = row_iter.next()
                    val item = new AdProvinceTopN
                    item.timestamp = row.getAs(0)
                    item.adID = row.getAs(1)
                    item.province = row.getAs(2)
                    item.clickedCount = row.getAs(3)

                    adProvinceTopN += item
                }

                val jdbcWrapper = JDBCWrapper.getJDBCInstance

                val set = new HashSet[String]()
                adProvinceTopN.map(item => set += item.timestamp + "_" + item.province)

                //adclicked 表的字段：timestamp、ip、userID、adID、province、city、clickedCount
                val deleteParametersList = new ArrayBuffer[List[Any]]()
                for (deleteRecord <- set) {
                    val splited = deleteRecord.split("_")
                    deleteParametersList += List(
                        splited(0),
                        splited(1)
                    )
                }
                jdbcWrapper.doBatch( "DELETE FROM adprovincetopn WHERE timestamp = ?  AND province = ? ", deleteParametersList.toList )

                //adclicked 表的字段：timestamp、adID、province、clickedCount
                val insertParametersList = new ArrayBuffer[List[Any]]()
                for (insertRecord <- adProvinceTopN) {
                    insertParametersList += List(
                        insertRecord.timestamp,
                        insertRecord.adID,
                        insertRecord.province,
                        insertRecord.clickedCount
                    )
                }
                jdbcWrapper.doBatch( "INSERT INTO adprovincetopn VALUES( ? , ? , ? , ? ) ", insertParametersList.toList )
            })
        })

        /**
          * 计算过去半个小时内广告点击的趋势
          *     用户广告点击可以只包含：timestamp、ip、userID、province、city
          */
        filteredadClickedStreaming.map( pair => {
            val splited = pair._2.split( "\t" )
            val adID = splited( 3 )
            val time = splited( 0 ) //Todo：后续需要重构代码实现时间戳的转换提取，此处需要提取出该广告的点几分钟的单位
            ( time + "_" + adID , 1L )
        }).reduceByKeyAndWindow( _ + _  , _ - _  , Durations.minutes( 30 ) , Durations.minutes( 5 ) ).foreachRDD( rdd => {
            rdd.foreachPartition( partition => {
                val adTrend = new ArrayBuffer[ AdThrendStat ]()
                while( partition.hasNext ) {
                    val record = partition.next()
                    val splited = record._1.split( "_")
                    val time = splited( 0 )
                    val adID = splited( 1 )
                    val clickedCount = record._2

                    /**
                      *  在插入到数据库的时候具体需要哪些子弹？time、adID、clickedCount
                      *  而我们通过J2EE技术进行趋势绘图的时候肯定是需要年、月、日、时、分这个维度，所有
                      *  我们在这里需要年、月、日这下时间的维度
                      */

                    var adTrendStat = new AdThrendStat
                    adTrendStat.adID = adID
                    adTrendStat.clickedCount = clickedCount
                    adTrendStat.date = time     //Todo:获取年月日
                    adTrendStat.hour = time     //Todo：获取小时
                    adTrendStat.minute = time   //Todo：获取分钟

                    adTrend += adTrendStat
                }

                val inserting = new ArrayBuffer[ AdThrendStat ]()
                val updating = new ArrayBuffer[ AdThrendStat ]()
                val jdbcWrapper = JDBCWrapper.getJDBCInstance

                /**
                  * adclicked表的字段：timestamp、ip、userID、adID、province、city
                  */
                for( clicked <- adTrend ) {
                    val adTrendCountHistory = new AdTrendCountHistory
                    jdbcWrapper.doQuery( " SELECT count( 1 ) FROM adclickedtrend WHERE " +
                        " date = ? AND hour = ? AND minute = ?  AND adID = ? " , List( clicked.date , clicked.hour , clicked.minute , clicked.adID  ) ,
                        new ExecuteCallBack { override def resultCallBack(result: ResultSet): Unit = {
                            if( result.next() ) {
                                val count = result.getLong( 1 )
                                clicked.clickedCount = count
                                adTrendCountHistory.clickedCountHistory = count
                                updating += clicked
                            }else {
                                inserting += clicked
                            }
                        }
                        })
                }

                //adclickedtrend 表的字段：date、hour、minute、adID
                val insertParametersList = new ArrayBuffer[ List[ Any ] ]()
                for( insertRecord <- inserting ) {
                    insertParametersList += List(
                        insertRecord.date ,
                        insertRecord.hour ,
                        insertRecord.minute ,
                        insertRecord.adID ,
                        insertRecord.clickedCount
                    )
                }
                jdbcWrapper.doBatch( " INSERT INTO adclickedtrend VALUES( ? , ? , ? , ?  ) " , insertParametersList.toList )

                //adclickedtrend 表的字段：date、hour、minute、adID
                val updateParametersList = new ArrayBuffer[ List[ Any ] ]()
                for( updateRecord <- updating ) {
                    updateParametersList += List(
                        updateRecord.clickedCount,
                        updateRecord.date ,
                        updateRecord.hour ,
                        updateRecord.minute ,
                        updateRecord.adID
                    )
                }
                jdbcWrapper.doBatch( " UPDATE adclickedtrend set clickedCount = ? " +
                    " WHERE date = ? AND hour = ? AND minute = ?  AND adID = ? " ,
                    updateParametersList.toList )
            })
        })

        streamContext.start

        streamContext.awaitTermination

        streamContext.stop()


    }
}

trait ExecuteCallBack {
    def resultCallBack(result : ResultSet )
}


object JDBCWrapper {

    private  var jdbcInstance : JDBCWrapper = _
    private val dbConnectionPool = new LinkedBlockingQueue[ Connection ]()

    try {
        Class.forName( "com.mysql.jdbc.Driver" )
    } catch {
        case e : ClassNotFoundException => e.printStackTrace()
    }


    def getJDBCInstance : JDBCWrapper = {
        synchronized{
            if (jdbcInstance == null) {
                jdbcInstance = new JDBCWrapper
            }
            jdbcInstance
        }
    }

    def getConnection : Connection = {
        synchronized{
            while ( 0 == dbConnectionPool.size() ) {
                Thread.sleep( 20 )
            }
            dbConnectionPool.poll()
        }
    }

}

class JDBCWrapper {
    for( i <- 1 to 10 ) {
        try {
            val conn = DriverManager.getConnection(
                "jdbc:mysql://Master:3306/sparkstreaming?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8",
                "root",
                "123456")

            JDBCWrapper.dbConnectionPool.put( conn )
        } catch {
            case e : Exception => e.printStackTrace()
        }
    }

    def doQuery( sqlText : String , paramsList : List[ Any ]  , callBack : ExecuteCallBack ): Unit = {
        val conn = JDBCWrapper.getConnection
        var preparedStatement : PreparedStatement = null
        var result : ResultSet = null
        try {
            preparedStatement = conn.prepareStatement( sqlText )

            for( i <- 0 until paramsList.length ) {
                preparedStatement.setObject( i + 1 , paramsList( i ) )
            }
            preparedStatement.addBatch()

            result = preparedStatement.executeQuery()
            callBack.resultCallBack( result )

        }catch {
            case e : Exception => e.printStackTrace()
        }finally {
            if( null != conn ) {
                JDBCWrapper.dbConnectionPool.put( conn )
            }

            if( null != preparedStatement ) {
                preparedStatement.close()
            }
        }
    }

    def doBatch( sqlText : String , paramsList : List[ List[ Any ] ] ): List[ Int ] = {
        val conn = JDBCWrapper.getConnection
        var preparedStatement : PreparedStatement = null
        var result : List[ Int ] = null
        try {
            conn.setAutoCommit( false )
            preparedStatement = conn.prepareStatement( sqlText )
            for( params <- paramsList ) {
                for( i <- 0 until params.length ) {
                    preparedStatement.setObject( i + 1 , params( i ) )
                }
                preparedStatement.addBatch()
            }
            result = preparedStatement.executeBatch().toList
            conn.commit()
        }catch {
            case e : Exception => e.printStackTrace()
        }finally {
            if( null != conn ) {
                JDBCWrapper.dbConnectionPool.put( conn )
            }

            if( null != preparedStatement ) {
                preparedStatement.close()
            }
        }
        result
    }

}

class UserAdClicked {

    private[this] var _timestamp: String = ""
    private[this] var _ip: String = ""
    private[this] var _userID: String = ""
    private[this] var _adID: String = ""
    private[this] var _province: String = ""
    private[this] var _city: String = ""
    private[this] var _clickedCount: Long = _


    def timestamp: String = _timestamp

    def timestamp_=(value: String): Unit = {
      _timestamp = value
    }

    def ip: String = _ip

    def ip_=(value: String): Unit = {
      _ip = value
    }


    def userID: String = _userID

    def userID_=(value: String): Unit = {
      _userID = value
    }


    def adID: String = _adID

    def adID_=(value: String): Unit = {
      _adID = value
    }


    def province: String = _province

    def province_=(value: String): Unit = {
      _province = value
    }


    def city: String = _city

    def city_=(value: String): Unit = {
      _city = value
    }


    def clickedCount: Long = _clickedCount

    def clickedCount_=(value: Long): Unit = {
      _clickedCount = value
    }


}

class AdClicked {
    private[this] var _timestamp: String = ""
    private[this] var _adID: String = ""
    private[this] var _province: String = ""
    private[this] var _city: String = ""
    private[this] var _clickedCount: Long = _

    def timestamp: String = _timestamp

    def timestamp_=(value: String): Unit = {
        _timestamp = value
    }


    def adID: String = _adID

    def adID_=(value: String): Unit = {
        _adID = value
    }


    def province: String = _province

    def province_=(value: String): Unit = {
        _province = value
    }


    def city: String = _city

    def city_=(value: String): Unit = {
        _city = value
    }


    def clickedCount: Long = _clickedCount

    def clickedCount_=(value: Long): Unit = {
        _clickedCount = value
    }

}

class AdProvinceTopN {
    private[this] var _timestamp: String = ""
    private[this] var _adID: String = ""
    private[this] var _province: String = ""
    private[this] var _clickedCount: Long = _


    def timestamp: String = _timestamp

    def timestamp_=(value: String): Unit = {
        _timestamp = value
    }
    def adID: String = _adID

    def adID_=(value: String): Unit = {
        _adID = value
    }

    def province: String = _province

    def province_=(value: String): Unit = {
        _province = value
    }

    def clickedCount: Long = _clickedCount

    def clickedCount_=(value: Long): Unit = {
        _clickedCount = value
    }
}

class AdThrendStat {
    private[this] var _date: String = ""
    private[this] var _minute: String = ""
    private[this] var _hour: String = ""
    private[this] var _adID: String = ""
    private[this] var _clickedCount: Long = _


    def date: String = _date

    def date_=(value: String): Unit = {
        _date = value
    }

    def minute: String = _minute

    def minute_=(value: String): Unit = {
        _minute = value
    }

    def hour: String = _hour

    def hour_=(value: String): Unit = {
        _hour = value
    }

    def adID: String = _adID

    def adID_=(value: String): Unit = {
        _adID = value
    }

    def clickedCount: Long = _clickedCount

    def clickedCount_=(value: Long): Unit = {
        _clickedCount = value
    }

}

class AdTrendCountHistory {
    private[this] var _clickedCountHistory: Long = _

    def clickedCountHistory: Long = _clickedCountHistory

    def clickedCountHistory_=(value: Long): Unit = {
      _clickedCountHistory = value
    }
}