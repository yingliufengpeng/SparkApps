package com.dt.spark.design_pattern.Flyweight

/**
  * Created by peng.wang on 2016/6/2.
  */
object Client {
    def main(args: Array[String]) {
        val chess1 = ChessFlyWeightFactory.getChess( "black" )
        val chess2 = ChessFlyWeightFactory.getChess( "black" )
        println( chess1 )
        println( chess2 )

        println( "增加外部状态的处理" )
        chess1.display( new Coordinate( 10 , 10 ) )
        chess2.display( new Coordinate( 20 , 20 ) )
    }
}
