package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */
object Hello_List {

    def compute( data : Int , dataSet : List[ Int ] ) : List[ Int ] = dataSet match {
        case List() => List( data )
        case head :: tail => if( data <= head ) data :: dataSet
            else head :: compute( data , tail )
    }

    /**
      * 从逻辑的上考虑的，就是我们想对一个列表进行排序，实现的步骤是，先拿出第一个元素，然后
      * 让剩余的数据进行递归操作排序，剩余的数据排序完后，我们把第一元素和排序后的数据进行插入
      * 排序。整体上的逻辑考虑就是如此！！
      * @param list 要排序的数据列表
      * @return     已排序的数据列表
      */
    def sortList( list : List[ Int ] ) : List[ Int ] = list match {
        case List() => List()
        case head :: tail => compute( head , sortList( tail ) )
    }

    def main(args: Array[String]): Unit = {
        println( sortList( List( 1 , 7 , 2 ,5 , 8 )) )

    }
}
