package com.dt.spark.design_pattern.ChainofResponsibility

/**
  * Created by peng.wang on 2016/6/8.
  */
class GeneralManager(name : String ) extends Leader( name ) {
    override def handleRequest(request: LeaveRequest): Unit = {
        if( request.leaveDays < 30 ) {
            println( "员工" + request.empName + "请假，天数 " + request.leaveDays + " ，理由 " + request.reason  )
            println( "总经理：" + name + "，审批通过" )
        }else {
            println( "莫非" + request.empName + " 想辞职，居然请假 " + request.leaveDays + "天" )
        }
    }
}
