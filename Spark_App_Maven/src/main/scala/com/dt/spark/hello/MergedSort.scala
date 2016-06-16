package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */



object MergedSort {


    def main(args: Array[String]) {
        println( mergedsort( ( x : Int , y : Int ) => x < y )( List( 3 , 7 , 9 , 5 ) ) )
    }

    /**
      *     二分法排序
      * @param less
      * @param input
      * @tparam T
      * @return
      */
    def mergedsort[ T ]( less : ( T , T ) => Boolean ) ( input : List[ T ] ) : List[ T ] = {

        /**
          * 很显然，这是在做归并排序的过程
          * @param xList 左边的自我排序
          * @param yList 右边的自我排序
          * @return 已排序好的列表
          */
        def merge( xList : List[ T ] , yList : List[ T ] ) : List[ T ] =
            ( xList , yList ) match {
                case ( Nil , _ ) => yList
                case ( _ , Nil ) => xList
                case ( x :: xtail , y :: ytail ) =>
                    if( less( x , y ) ) x :: merge( xtail , yList )
                    else y :: merge( xList , ytail )
        }

        val n = input.length / 2
        if( n == 0 ) input
        else {
            val ( x , y ) = input.splitAt( n ) //要把排序的列表input平均分成两个列表
            merge( mergedsort( less )( x ) , mergedsort( less )( y ) )  //先对分后的两个列表进行自我排序，在对排序好的列表就行归排序
        }
    }
}
