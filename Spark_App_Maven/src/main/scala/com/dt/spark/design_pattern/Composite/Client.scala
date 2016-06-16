package com.dt.spark.design_pattern.Composite

/**
  * Created by peng.wang on 2016/6/3.
  */
object Client {
    def main(args: Array[String]) {
        val f1 = new Folder( "myFolder" )
        val f2 = new ImageFile( "big Pic" )
        val f3 = new TextFile( "Hello.txt" )
        f1.addFile( f2 )
        f1.addFile( f3 )

        val f4 = new Folder( "your folder!!!" )
        f4.addFile( f2 )
        f4.addFile( f3 )

        f1.addFile( f4 )

//        f2.killVirus
        f1.killVirus
    }
}
