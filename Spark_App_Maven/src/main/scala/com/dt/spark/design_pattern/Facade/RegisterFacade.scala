package com.dt.spark.design_pattern.Facade

/**
  * Created by peng.wang on 2016/6/3.
  */
class RegisterFacade {
    def register : Unit = {
        val a = new 海淀工商局
        a.checkName
        val b = new 海淀质检局
        b.orgCodeCertificate
        val c = new 海淀税务局
        c.taxCertificate
        val d = new 中国工商银行
        d.openAccount
    }
}
