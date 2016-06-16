package com.dt.spark.hello



import java.awt.event.ActionEvent

import java.awt.event.ActionListener
import javax.swing.{JButton, JFrame}



/**
  * Created by peng.wang on 2016/5/19.
  */
object higher_order_funcions {



    def main(args: Array[String]) {

        val frame = new JFrame(  "SAM Testing"  )
        val jButton = new JButton( "Counter" )

        implicit def convertedAction( action : ActionEvent => Unit ) = {
            new ActionListener {
                override def actionPerformed(e: ActionEvent): Unit = action( e )
            }
        }

//        jButton.addActionListener( event : ActionEvent  => {
//            data += 1
//            println( data )
//        })

        frame.setContentPane( jButton )
        frame.pack()
        frame.setVisible( true )


    }
}
