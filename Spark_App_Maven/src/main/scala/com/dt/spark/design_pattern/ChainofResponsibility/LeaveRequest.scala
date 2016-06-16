package com.dt.spark.design_pattern.ChainofResponsibility

/**
  * Created by peng.wang on 2016/6/8.
  */
class LeaveRequest {
    var empName = ""
    var leaveDays = 0
    var reason = ""

    def this( empName : String , leaveDays : Int , reason : String ) = {
        this
        this.empName = empName
        this.leaveDays = leaveDays
        this.reason = reason
    }
}
