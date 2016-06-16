package com.dt.spark.design_pattern.Strategy

/**
  * Created by peng.wang on 2016/6/8.
  */
object Client {
    def main(args: Array[String]) {
        val ctx = new Context( new OldCustomerFewStrategy )
        ctx.printPrice( 998 )
        while( true ) {}
    }
}
