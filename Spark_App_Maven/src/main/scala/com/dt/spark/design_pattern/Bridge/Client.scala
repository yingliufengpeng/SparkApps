package com.dt.spark.design_pattern.Bridge

/**
  * Created by 王鹏 on 2016/5/31.
  */
object Client {
    def main(args: Array[String]) {
        /**
          * 我要销售联想的笔记本
          *
          */
        val c : Computer = new LapTop( new Lenovo() )
        c.sale()
        val cc : Computer = new Ipad( new Dell() )
        cc.sale()
    }
}
