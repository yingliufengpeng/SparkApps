package com.dt.spark.design_pattern.Singleton.Singleton1

/**
  * 测试懒汉式单例模式，因为在scala中Object中是lazy级别的！！！
  * Created by peng.wang on 2016/6/9.
  */
class Singleton

object Singleton {
    val instance = new Singleton
    def getInstance = instance
}
