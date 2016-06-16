package com.dt.spark.design_pattern.ChainofResponsibility

/**
  * Created by peng.wang on 2016/6/8.
  */
abstract class Leader {
    protected var name = ""
    protected var nextLeader : Leader = _

    def this( name : String ) = {
        this
        this.name = name
    }

    def setNextLeader( nextLeader : Leader ) = this.nextLeader = nextLeader

    def handleRequest( request: LeaveRequest ) : Unit
}
