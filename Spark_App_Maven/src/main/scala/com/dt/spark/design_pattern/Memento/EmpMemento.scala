package com.dt.spark.design_pattern.Memento

/**
  * 备忘录类
  * Created by peng.wang on 2016/6/3.
  */

class EmpMemento {
    var ename : String = _
    var age : Int = _
    var salary : Double = _

    def this( emp : Emp ) = {
        this
        ename = emp.ename
        age = emp.age
        salary = emp.salary
    }
}
