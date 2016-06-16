package com.dt.spark.design_pattern.Singleton.Enum

/**
  * Created by peng.wang on 2016/6/9.
  */
object Client {
    def isEnum( enum: Enum.Value ) = {
        if( Enum.isFinished( enum ) ) println( "OK" )
    }
    def main(args: Array[String]) {
        val e = Enum.FAILED
        isEnum( e )
    }
}
