package com.dt.spark.design_pattern.Strategy

/**
  * Created by peng.wang on 2016/6/8.
  */
trait Strategy {
    def getPrice( standardPrice : Double ) : Double
}


class NewCustomerFewStrategy extends Strategy {
    override def getPrice(standardPrice: Double): Double = {
        println( "不打折，原价！！！" )
        standardPrice
    }
}

class NewCustomerManyStrategy extends Strategy {
    override def getPrice(standardPrice: Double): Double = {
        println( "打九折！！！" )
        standardPrice * 0.9
    }
}

class OldCustomerFewStrategy extends Strategy {
    override def getPrice(standardPrice: Double): Double = {
        println( "打八五折！！！" )
        standardPrice * 0.85
    }
}

class OldCustomerManyStrategy extends Strategy {
    override def getPrice(standardPrice: Double): Double = {
        println( "打八折！！！" )
        standardPrice * 0.8
    }
}