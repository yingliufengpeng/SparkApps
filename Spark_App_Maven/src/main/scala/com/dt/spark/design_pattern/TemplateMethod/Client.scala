package com.dt.spark.design_pattern.TemplateMethod

import com.dt.spark.design_pattern.BankTemplateMethod.BankTemplateMethod

/**
  * Created by peng.wang on 2016/6/8.
  */
object Client {
    def main(args: Array[String]) {
        new DrawMoney().process

        new BankTemplateMethod {
            /**
              * 抽象方法
              */
            override def transact: Unit = println( "我要存钱！！！" )
        }.process

        new BankTemplateMethod {
            /**
              * 抽象方法
              */
            override def transact: Unit = println( "我要理财，我这里有两千万韩币！！！" )
        }.process
    }
}


class DrawMoney extends BankTemplateMethod {
    /**
      * 抽象方法
      */
    override def transact: Unit = println( "我要取款！！！" )
}