package com.bjsx.test

import java.lang.reflect.ParameterizedType

import com.bjsx.test.bean.User

/**
  * Created by peng.wang on 2016/6/4.
  */
object Demo04 {

    def test01(map : Map[ String , User ], list : List[ String ] ) : Unit = {
        println( "Demo04.test01" )
    }

    def test02 : Map[ User , String ] = {
        println( "Demo04.test02" )
        null
    }

    def main(args: Array[String]) {
        //获得指定方法参数的泛型信息
        val m = Demo04.getClass.getMethod( "test01" , classOf[ Map[ _ , _ ] ] , classOf[ List[ _ ] ] )
        val t = m.getGenericParameterTypes
        for( paramType <- t ) {
            println( "#" + paramType )
            if( paramType.isInstanceOf[ ParameterizedType] ) {
                val genericTypes = paramType.asInstanceOf[ ParameterizedType ].getActualTypeArguments
                for( genericType <- genericTypes ) {
                    println( "泛型类型" + genericType )
                }
            }
        }

        //获得指定方法返回值泛型类型
        val m2 = Demo04.getClass.getMethod( "test02"  )
        val returnType = m2.getGenericReturnType
        if( returnType.isInstanceOf[ ParameterizedType ] ){
            val genericTypes = returnType.asInstanceOf[ ParameterizedType ].getActualTypeArguments
            for( genericType <- genericTypes ) {
                println( "返回值，泛型类型:" + genericType.getTypeName )
            }
        }
    }
}
