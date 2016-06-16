package com.dt.spark.design_pattern.Mediator

/**
  * Created by peng.wang on 2016/6/2.
  */
object Client {
    def main(args: Array[String]) {
        val m = new President

        val market = new Market( m )
        val devp = new Development( m )
        val f = new Finacial( m )

        market.selfAction
        market.outAction
    }
}
