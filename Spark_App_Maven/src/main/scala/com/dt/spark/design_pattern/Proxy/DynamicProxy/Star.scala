package com.dt.spark.design_pattern.Proxy.DynamicProxy

/**
  * Created by peng.wang on 2016/6/8.
  */
trait Star {
    /**
      * 面谈
      */
    def confer : Unit

    /**
      * 签合同
      */
    def signContract : Unit

    /**
      * 订票
      */
    def bookTcket : Unit

    /**
      * 唱歌
      */
    def sing : Unit

    /**
      * 收钱
      */
    def collectMoney : Unit
}
