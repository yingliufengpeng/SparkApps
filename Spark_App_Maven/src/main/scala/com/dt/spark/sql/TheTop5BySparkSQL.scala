package com.dt.spark.sql

/**
  * Created by peng.wang on 2016/4/19.
  * 项目：找出搜索平台上用户明天搜索排名前5名的产品，The hottest！
  * 元数据：Date、UserID、Item、City、Device
  * 总体的思路：混合使用Spark SQL和Spark Core的内容
  *     第一步：原始的ETL，过滤数据后产生目标，使用RDD的filter等进行操作
  *     第二步：过滤后的数据进行指定条件的查询，查询条件可能非常的复杂（进行广播），
  *         使用RDD的
  *     第三步：由于商品分种类的，我们在得出最终结果之前，首先会与商品进行UV操作（
  *         当然，你也可以对用户访问的商品的PV进行分析），此时们要对商品进行UV计算
  *         的话，就必须构建K-V的RDD，（date#Item , userID）以方便进行groupByKey操作
  *         在调用了groupByKey之后，对user进行去重，并计算出每一天每一种商品的UV，最
  *         终计算出来的结果数据类型date#Item , userID）
  *     第四步：使用开窗函数row_number统计出每日商品UV前5名的内容
  *         row_number() OVER ( PARTITION BY date ORDER BY UV DESC ) rank
  *         此时会产生以date、item、uv为Row 的DataFrame
  *     第五步：DataFrame转成RDD根据日期进行分组，并分析出每天排名前5位的热搜Item
  *     第六步：进行Key-Value交换，然后，进行调用sortByKey进行点击热度排名
  *     第七步：再次进行Key-Value，得出目标数据为date#Item , userID）的格式
  *     第八步：通过RDD直接操纵Mysql等把结果放入生产系统中的DB中，在通过Java EE等
  *         Server技术可视化结果以供市场营销人员、仓库调度系统、快递系统、管理决策人员
  *         使用创造价值；当然也可以放在HIve中，Java EE等技术通过JDBC等连接访问HIve；当
  *         然也可以就放在Spark SQL中，通过Thrift技术供Java EE使用等；当，如果是像双十
  *         一等时候，一般首先放在Redis中，这样可以实现类似秒杀系统的响应速度
  *
  */
object TheTop5BySparkSQL {
    def main(args: Array[String]) {

    }
}
