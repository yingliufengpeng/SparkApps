package com.dt.spark.design_pattern.Prototype

import java.util.Date

/**
  * 测试原型模式
  * Created by peng.wang on 2016/6/1.
  */
object Client {
    def main(args: Array[String]) {
        val s1 = new Sheep( "少莉" , new Date() )
//        println( s1.getbirthday )
//        println( s1.getname )

        s1.getbirthday.setTime( 333334 )

        println( s1.getbirthday )
        println( s1.getname )
        val s2  = s1.clone().asInstanceOf[ Sheep ]
        println( s2.getbirthday )
        println( s2.getname )

    }
}
