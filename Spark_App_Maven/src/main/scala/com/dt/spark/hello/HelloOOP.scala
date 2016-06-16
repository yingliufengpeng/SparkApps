package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */

class Person {
    private var age = 0
    def increment = age += 1
    def current = age
}

class Student {

    private[this] var _age: Int = 0
    def age: Int = _age
    def age_=(value: Int): Unit = {
      _age = value
    }

    def isYounger( other : Student ) = age < other.age
}

object HelloOOP {
    def main(args: Array[String]) {
        val person = new Person
        person.increment
        println( person.current )

        val student = new Student
        student.age = 3
        println( student.age )
    }
}
