abstract class Expression {   // Defines expression parse-trees

    abstract fun appendSB(targetStr: java.lang.StringBuilder)  // printing - efficiency!

    var syType = -1
    var parent: Expression.Binary? = null
    var priority: Int? = null

    class Ident(private val id: String) : Expression() {         //Ident
        //e.g. x
        override fun appendSB(targetStr: StringBuilder) {
            targetStr.append(id)
        }

    }

    class NumCon(private val n: Number) : Expression() {       //NumCon
        // e.g. 3

        override fun appendSB(targetStr: StringBuilder) {
            targetStr.append(n.toString())
        }
    }

    class Unary(val opr: Int, val expression: Expression) : Expression() {
        override fun appendSB(targetStr: StringBuilder) {
            targetStr.append("(" + LexerAnalyzer.Symbol[opr] + " ")
            expression.appendSB(targetStr)
            targetStr.append(")")
        }
    }

    class Binary(var opr: Int, var left: Expression, var right: Expression) : Expression() {

        init {
            left.parent = this
            right.parent = this
        }

        override fun appendSB(targetStr: StringBuilder) {
            targetStr.append("(")
            left.appendSB(targetStr)
            targetStr.append(" " + LexerAnalyzer.Symbol[opr] + " ")
            right.appendSB(targetStr)
            targetStr.append(")")
        }
    }

    override fun toString(): String {
        val str = StringBuilder() // efficiency!
        appendSB(str)
        return str.toString()
    }//toString

    fun error(msg: String) {
        println("\nError: $msg")
        System.exit(1)
    }//error
}