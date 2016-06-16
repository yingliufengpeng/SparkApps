package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */

abstract class Person2
case class Student2( age : Int ) extends Person2
case class Worker( age : Int , salary : Double ) extends Person2
case object Shared extends Person2


object case_class_object {
    def main(args: Array[String]) {

        def caseOps( person : Person2 ) = person match {
            case Student2( age ) => println( "I am " + age + "years old" )
            case Worker( _ , salary ) => println( "Wow , I got " + salary )
            case Shared => println( "No property" )
        }

        caseOps( Student2( 19 ) )
        caseOps( Shared )

    }
}
