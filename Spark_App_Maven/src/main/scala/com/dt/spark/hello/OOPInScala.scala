package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */

//class Teacher {
//    var name : String = _
//    private var age = 27
//    private[ this ] val gender = "male"
//
//    def this( name : String ) {
//        this
//        this.name = name
//    }
//
//    def sayHello: Unit = {
//        println( this.name + " : " + this.age + " : " + this.gender )
//    }
//}

class Teacher private ( val name : String , val age : Int ) {
    println( "This is the primary constructor!!!" )
    var gender : String = _
    println( "gender: " + gender )

    def this( name : String , age : Int , gender : String ) {
        this( name , age )
        this.gender = gender
    }

    def this( ) = this( "Spark" , 33 , "male" )



    def sayHello: Unit = {
        println( this.name + " : " + this.age + " : " + this.gender )
    }
}


object OOPInScala {
    def main(args: Array[String]) {
//        val p = new Teacher
//        p.name = "Spark"
//        p.sayHello

//        val p = new Teacher( "Spark" , 5 )
//        p.sayHello

//        val p = new Teacher( "Spark" , 5 , "female" )
//        p.sayHello

        val p = new Teacher
        p.sayHello
    }
}
