package com.dt.spark.design_pattern.Proxy.StaticProxy

/**
  * Created by peng.wang on 2016/6/8.
  */
class ProxyStar( var realStar : Star ) extends Star{
    /**
      * 面谈
      */
    override def confer: Unit = println( "代理经纪人面谈" )

    /**
      * 唱歌
      */
    override def sing(): Unit = {
        realStar.sing()
    }

    /**
      * 签合同
      */
    override def signContract: Unit = println( "代理经纪人签合同" )

    /**
      * 收钱
      */
    override def collectMoney: Unit = println( "代理经纪人收钱" )

    /**
      * 订票
      */
    override def bookTcket: Unit = println( "代理经纪人订票" )
}
