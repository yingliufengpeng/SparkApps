package com.dt.spark.design_pattern.Memento

/**
  * 元发器类
  * Created by peng.wang on 2016/6/3.
  */
class Emp {
    var ename : String = _
    var age : Int = _
    var salary : Double = _

    def this( name : String , age : Int , salary : Double ) = {
        this
        ename = name
        this.age = age
        this.salary = salary
    }

    /**
      * 首先进行备忘操作，并返回备忘录对象
      */
    def createMemento = new EmpMemento( this )

    /**
      * 进行数据恢复，恢复成指定备忘录的值
      */
    def recovery( mmt : EmpMemento ) = {
        ename = mmt.ename
        age = mmt.age
        salary = mmt.salary
    }

}
