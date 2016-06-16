package com.dt.spark.core

/**
  * Created by peng.wang on 2016/3/3.
  */
class SecondarySortKey( val first : Int , val second : Int  ) extends Ordered[ SecondarySortKey ] with Serializable
{
    override def compare( other : SecondarySortKey ) : Int =
    {
        if( this.first - other.first != 0 )
        {
            this.first - other.first
        }
        else
        {
            this.second - other.second
        }
    }
}
