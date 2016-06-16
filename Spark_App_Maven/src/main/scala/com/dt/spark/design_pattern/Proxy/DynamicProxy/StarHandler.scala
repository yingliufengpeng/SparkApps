package com.dt.spark.design_pattern.Proxy.DynamicProxy

import java.lang.reflect.{Method, InvocationHandler}

/**
  * Created by peng.wang on 2016/6/8.
  */
class StarHandler( realStar: Star ) extends InvocationHandler{

    override def invoke(proxy: scala.Any, method: Method, args: Array[AnyRef]): AnyRef = {
        println( args )
        if( method.getName.equals( "sing" ) )  method.invoke( realStar  )
        null
    }
}
