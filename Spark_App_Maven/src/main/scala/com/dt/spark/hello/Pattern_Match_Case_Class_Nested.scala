package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */

abstract class Item
case class Book( description : String , price : Double ) extends Item
case class Bundle( description : String , price : Double , items : Item* ) extends  Item



object Pattern_Match_Case_Class_Nested {
    def main(args: Array[String]) {

        def caseclass_nested( person : Item ) = person match {
            case Bundle( _ , _ , art @ Book( _ , _ ) , rest @ _* ) => println( art.description + " : " + art.price )
            case _ => println( "Oops!" )
        }

        caseclass_nested( Bundle( "111 Special's " , 30.0 ,
            Book( "Scala for the Spark Developer" , 69.95 ) ,
            Bundle( "Hadoop" , 40.0 ,
                Book( "Hive" , 79.95 ) ,
                Book( "HBase" , 32.95 )
            )
        ))
    }
}
