package com.example.calculator

import kotlin.math.floor

class Calculations {
    fun calculate(screenText: String):String {
        val regexNum=Regex("""((-?\d+(\.\d+)?)|(\(-?\d+(\.\d+)?\)))""")
        val multOrDiv=Regex("""$regexNum[X÷]$regexNum""")
        val plusOrMin=Regex("""$regexNum[+−]$regexNum""")
        var text=screenText.replace(",",".").replace("(","").replace(")","")
        var expression:String
        var ans:Double
        while (text.contains(multOrDiv)){
            expression= multOrDiv.find(text,0)!!.value
            ans = if(expression.contains('÷')){
                if(expression.substringAfter('÷').toDouble()==0.0){
                    return "0"
                }
                expression.substringBefore('÷').toDouble()/expression.substringAfter('÷').toDouble()
            }else{
                expression.substringBefore('X').toDouble()*expression.substringAfter('X').toDouble()
            }
            text=text.replaceFirst(expression,ans.toBigDecimal().toPlainString())
        }
        while (text.contains(plusOrMin)){
            expression= plusOrMin.find(text,0)!!.value
            ans = if(expression.contains('+')){
                expression.substringBefore('+').toDouble()+expression.substringAfter('+').toDouble()
            }else{
                expression.substringBefore('−').toDouble()-expression.substringAfter('−').toDouble()
            }
            text=text.replaceFirst(expression,ans.toBigDecimal().toPlainString())
        }
        text=removeZeroFraction(text)
        return text.toBigDecimal().toPlainString().replace('.',',')
    }

    private fun removeZeroFraction(text:String) : String{
        if(text.toDouble()== floor(text.toDouble())) {
            return text.substringBefore('.')
        }
        return text
    }
}