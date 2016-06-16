package com.dt.spark.design_pattern.Mediator

/**
  * 同时类的接口
  * Created by peng.wang on 2016/6/2.
  */
trait Department {
    def selfAction : Unit //做本部门的事情
    def outAction : Unit  //向总经理发出申请
}

class Development( mediator: Mediator ) extends  Department {
    mediator.register( "Development" , this )

    override def selfAction: Unit = println( "专心科研，开发项目！！！" )

    //做本部门的事情
    override def outAction: Unit = {
        println( "汇报工作！没钱了，需要资金支持！！" )

    }
}

class Finacial( mediator: Mediator ) extends  Department {
    mediator.register( "Finacial" , this )

    override def selfAction: Unit = println( "数钱！！！" )

    //做本部门的事情
    override def outAction: Unit = {
        println( "汇报工作！钱太多了，怎么花！！" )

    }
}


class Market( mediator: Mediator ) extends  Department {
    mediator.register( "Market" , this )

    override def selfAction: Unit = println( "跑去接项目！！！" )

    //做本部门的事情
    override def outAction: Unit = {
        println( "汇报工作！项目承接的进度，需要资金支持！！" )
        mediator.command( "Finacial" )
    }
}
