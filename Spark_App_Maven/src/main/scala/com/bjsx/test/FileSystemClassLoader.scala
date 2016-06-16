package com.bjsx.test

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, FileInputStream}

import scala.collection.mutable.ArrayBuffer

/**
  * 自定义文件系统类加载器
  * Created by peng.wang on 2016/6/4.
  */
class FileSystemClassLoader extends ClassLoader{
    //com.bjsxt.test.User.java  ---> d;/myjava/com/bjsxt/test.User.class
    val rootDir = "d:/myjava"

    override def findClass(name: String): Class[_] = {
        var c = findLoadedClass( name )

        /**
          * 应该要先查询有没有加载过这个类。如果已经加载，则直接返回加载好的类。
          * 如果没有，则加载新的类
          */
        if( c != null ) c
        else {
            val parent = this.getParent
            try {
                c = parent.loadClass( name )
            }catch {
                case e : Exception =>
            }

            if( c != null ) return c
            else {
                val classData = getClassData( name )
                if( classData == null ) {

                }else {
                    c = defineClass( name , classData , 0 , classData.length )
                }
            }
            c
        }
    }

    //com.bjsxt.test.User.java  ---> d;/myjava/com/bjsxt/test.User.class
    def getClassData( classname : String ) : Array[ Byte ] = {
        val path = rootDir + "/" + classname.replace( '.' , '/' ) + ".class"
        var is : InputStream = null
        val baos = new ByteArrayOutputStream()

        /**
          * 用来判断输入流的数据是否已经输入完成
          * @param buffer
          * @return
          */
        def f( buffer : Array[ Byte ] ): Boolean ={
            val temp = is.read( buffer )
            temp > 0
        }
        try {
            is = new FileInputStream( path )
            val buffer = new Array[ Byte ]( 10240 )
            var count = 0
            var temp = 0
            while( { temp = is.read( buffer ) ; temp > 0 } )  {
                baos.write( buffer , 0 , temp )
            }
            baos.toByteArray
        }catch {
            case e : Exception => e.printStackTrace()
                null
        }finally {
            is.close()
            baos.close()
        }
    }
}
