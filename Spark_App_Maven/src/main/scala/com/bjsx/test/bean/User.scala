package com.bjsx.test.bean

/**
  * Created by peng.wang on 2016/6/3.
  */
class User {
//    private[this] var _id: Int = _

    def this( id : Int ) = {
        this
        _id = id
    }
    var _id: Int = _
    private[this] var _name: String = _

    def name: String = _name

    def name_=(value: String): Unit = {
      _name = value
    }


    def id: Int = _id

    def id_=(value: Int): Unit = {
      _id = value
    }

    def print = println( "OK" )

}
