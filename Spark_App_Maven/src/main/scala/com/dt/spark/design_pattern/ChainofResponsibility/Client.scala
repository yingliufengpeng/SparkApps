package com.dt.spark.design_pattern.ChainofResponsibility

/**
  * Created by peng.wang on 2016/6/8.
  */
object Client {
    def main(args: Array[String]) {
        val director = new Director( "张三" )
        val manager = new Manager( "李四" )
        val viceGeneralMananger = new ViceGeneralManager( "王五" )
        val generalManager = new GeneralManager( "赵六" )

        /**
          * 组织责任链对象的关系
          */
        director.setNextLeader( manager )
        manager.setNextLeader( viceGeneralMananger )
        viceGeneralMananger.setNextLeader( generalManager )

        /**
          * 开始请假操作
          */

        val request = new LeaveRequest( "Tom" , 13 , "回家探亲" )
        director.handleRequest( request )

    }
}
