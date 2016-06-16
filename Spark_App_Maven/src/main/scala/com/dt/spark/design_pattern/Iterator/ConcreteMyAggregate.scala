package com.dt.spark.design_pattern.Iterator

import scala.collection.mutable.ArrayBuffer

/**
  * 自定义的聚合类
  * Created by peng.wang on 2016/6/1.
  */
class ConcreteMyAggregate {
    val list = ArrayBuffer[ AnyRef ]()

    def addObject( obj : AnyRef ): Unit = {
        list += obj
    }

    def delObject( obj : AnyRef ): Unit = {
        list -= obj
    }

    /**
      * 获得迭代器的对象
      * @return
      */
    def createIterator : MyIterator = new ConcreteIterator

    private class ConcreteIterator extends MyIterator {

        var cursor = 0  //定义游标用于记录遍历时的位置

        override def first: Unit = {
            cursor = 0
        }


        //将游标指向第一个元素
        override def next: Unit = {
            if( cursor < list.length ) cursor += 1
        }

        /**
          * 获取当前游标指向的对象
          *
          * @return
          */
        override def getCurrentObj: AnyRef = list( cursor )

        //
        override def hasNext: Boolean = if( cursor < list.size ) true else false
}
}
