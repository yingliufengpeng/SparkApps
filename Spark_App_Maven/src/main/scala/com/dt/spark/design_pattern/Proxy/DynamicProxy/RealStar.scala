package com.dt.spark.design_pattern.Proxy.DynamicProxy

/**
  * Created by peng.wang on 2016/6/8.
  */
class RealStar extends Star{
    /**
      * 面谈
      */
    override def confer: Unit = println( "真实角色面谈" )

    /**
      * 唱歌
      */
    override def sing: Unit = println( "真实角色唱歌" )

    /**
      * 签合同
      */
    override def signContract: Unit = println( "真实角色签合同" )

    /**
      * 收钱
      */
    override def collectMoney: Unit = println( "真实角色收钱" )

    /**
      * 订票
      */
    override def bookTcket: Unit = println( "真实角色订票" )
}
