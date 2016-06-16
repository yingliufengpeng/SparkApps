package com.dt.spark.design_pattern.State

/**
  * Created by peng.wang on 2016/6/8.
  */

/**
  * 如果是银行系统，这个Context类就是账号。根据金额不同，
  * 切换不同的状态
  * @param state
  */
class HomeContext( private var state : State ) {
    def setState( state : State ) = {
        this.state = state
        println( "修改状态！！！" )
        state.handle
    }


}
