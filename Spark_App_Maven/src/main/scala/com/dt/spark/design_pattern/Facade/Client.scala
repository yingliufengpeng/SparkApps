package com.dt.spark.design_pattern.Facade

/**
  * Created by peng.wang on 2016/6/3.
  */
object Client {
    def main(args: Array[String]): Unit = {
        new RegisterFacade().register
    }
}
