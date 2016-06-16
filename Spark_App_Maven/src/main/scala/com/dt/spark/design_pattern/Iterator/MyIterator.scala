package com.dt.spark.design_pattern.Iterator

/**
  * 自定义的迭代器接口
  * Created by peng.wang on 2016/6/1.
  */
trait MyIterator {
    def first : Unit  //将游标指向第一个元素
    def next : Unit   //
    def hasNext : Boolean

    /**
      * 获取当前游标指向的对象
      * @return
      */
    def getCurrentObj : AnyRef
}
