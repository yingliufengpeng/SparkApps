package com.dt.spark.design_pattern.Memento

/**
  * Created by peng.wang on 2016/6/3.
  */
object Client {
    def main(args: Array[String]) {
        val taker = new CareTaker
        val emp = new Emp( "peng" , 19 , 200 )
        println( "第一次打印对象：" + emp.ename + "----" + emp.age + "---" + emp.salary )

        taker.memnto = emp.createMemento    //备忘录一次

        emp.age = 38
        emp.ename = "wangpeng"
        emp.salary = 333
        println( "第二次打印对象：" + emp.ename + "----" + emp.age + "---" + emp.salary )

        emp.recovery( taker.memnto )    //恢复备忘录保存的对象
        println( "第二次打印对象：" + emp.ename + "----" + emp.age + "---" + emp.salary )
    }


}
