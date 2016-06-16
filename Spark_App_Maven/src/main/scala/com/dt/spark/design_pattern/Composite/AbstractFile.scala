package com.dt.spark.design_pattern.Composite

import java.util

import scala.collection.mutable.ArrayBuffer

/**
  * Created by peng.wang on 2016/6/3.
  */
trait AbstractFile {
    def killVirus : Unit
}

class ImageFile( name : String ) extends AbstractFile {

    override def killVirus: Unit = {
        println( "---图像文件" + name + "进行查杀" )
    }
}

class TextFile( name : String ) extends AbstractFile {

    override def killVirus: Unit = {
        println( "---文本文件" + name + "进行查杀" )
    }
}

class VideoFile( name : String ) extends AbstractFile {

    override def killVirus: Unit = {
        println( "---视频文件" + name + "进行查杀" )
    }
}

class Folder( name : String ) extends AbstractFile {
    /**
      * 定义容器，用来存放容器构建下的子节点
      */
    val list = new ArrayBuffer[ AbstractFile ]()

    def addFile( file : AbstractFile ) = list += file
    def removeFile( file : AbstractFile ) = list -= file
    def getChild( index : Int ) : AbstractFile = list( index )


    override def killVirus: Unit = {
        println( "---文件夹" + name + "进行查杀" )
        for( file <- list ) {
            file.killVirus
        }
    }
}