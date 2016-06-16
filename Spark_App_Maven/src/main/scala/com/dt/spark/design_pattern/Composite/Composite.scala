package com.dt.spark.design_pattern.Composite

import sun.misc.CompoundEnumeration

/**
  * 抽奖组件
  * Created by peng.wang on 2016/6/3.
  */
trait Component {
    def operation : Unit
}

/**
  * 叶子节点
  */
trait Leaf extends Component {

}

/**
  * 容器节点
  */
trait Composite extends Component {
    def add( c : Component ) : Unit
    def remove( c : Component ) : Unit
    def getChild( index : Int ) : Component
}

