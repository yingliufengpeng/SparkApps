package com.dt.spark.design_pattern.Memento

import scala.collection.mutable.ArrayBuffer

/**
  * 负责人 类
  * 负责管理备忘录的对象
  * Created by peng.wang on 2016/6/3.
  */
class CareTaker {
    var memnto : EmpMemento = _
    val list = ArrayBuffer[ EmpMemento ]()
}
