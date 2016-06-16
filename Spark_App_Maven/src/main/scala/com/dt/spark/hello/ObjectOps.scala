package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */

object University {
    private var studentNo = 0
    private def newStudenNo = {
        studentNo += 1
        studentNo
    }
}

class University {
    val id = University.newStudenNo
    private var number = 0
    def aClass( number : Int ) = { this.number += number}
}


object ObjectOps {
    def main(args: Array[String]) {

    }
}
