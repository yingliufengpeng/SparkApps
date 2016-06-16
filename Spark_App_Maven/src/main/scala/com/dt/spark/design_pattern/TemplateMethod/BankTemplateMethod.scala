package com.dt.spark.design_pattern.BankTemplateMethod

/**
  * Created by peng.wang on 2016/6/8.
  */
abstract class BankTemplateMethod {
    /**
      * 具体方法
      */
    def takeNumber = println( "取号排队！！！" )

    /**
      * 抽象方法
      */
    def transact : Unit

    /**
      *
      */
    def evaluate = println( "反馈评分！！！" )

    final def process = {
        takeNumber
        transact
        evaluate
    }

}
