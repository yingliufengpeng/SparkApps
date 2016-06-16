package com.dt.spark.design_pattern.Prototype

import java.io.{ObjectInputStream, ByteArrayInputStream, ObjectOutputStream, ByteArrayOutputStream}
import java.util.Date

import org.eclipse.jetty.util.ByteArrayISO8859Writer

/**
  * 使用序列化和反序列化使用深克隆
  * Created by peng.wang on 2016/6/1.
  */
object Client3 {
    def main(args: Array[String]) {
        val s1 = new Sheep( "少莉" , new Date() )


        println( s1.getbirthday )
        println( s1.getname )
        /**
          * 使用序列化和反序列化实现深复制
          */

        val bos = new ByteArrayOutputStream()
        val oos = new ObjectOutputStream( bos )
        oos.writeObject( s1 )
        val bytes = bos.toByteArray

        val bis = new ByteArrayInputStream( bytes )
        val ois = new ObjectInputStream( bis )

        val s2  = ois.readObject().asInstanceOf[ Sheep ]
        s2.getbirthday.setTime( 333334 )
        println( s2.getbirthday )
        println( s2.getname )

        println( s1.getbirthday )
        println( s1.getname )
    }
}
