package com.dt.spark.design_pattern.State

/**
  * Created by peng.wang on 2016/6/8.
  */
object Client {
    def main(args: Array[String]) {
        val ctx = new HomeContext( null )
        ctx.setState( new BookedState )
    }
}
