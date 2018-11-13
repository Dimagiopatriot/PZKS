import java.io.FileInputStream

class SyntaxAnalyzer(private val lex: LexerAnalyzer) {

    private val unOprs = 1L shl LexerAnalyzer.minus // useful Symbol Sets

    private val binOprs = (1L shl LexerAnalyzer.plus or (1L shl LexerAnalyzer.minus)
            or (1L shl LexerAnalyzer.times) or (1L shl LexerAnalyzer.over))

    private val startsExp = (unOprs
            or (1L shl LexerAnalyzer.word) or (1L shl LexerAnalyzer.numeral)
            or (1L shl LexerAnalyzer.open))

    var oprPriority = IntArray(LexerAnalyzer.eofSy)


    fun init() {
        for (i in oprPriority.indices) oprPriority[i] = 0
        oprPriority[LexerAnalyzer.plus] = 1
        oprPriority[LexerAnalyzer.minus] = 1
        oprPriority[LexerAnalyzer.times] = 2
        oprPriority[LexerAnalyzer.over] = 2
    }//init

    init {
        init()
    }

    private fun syIs(sym: Int): Boolean { // test and skip symbol
        if (lex.sy == sym) {
            lex.inSymbol()
            return true
        }
        return false
    }//syIs

    private fun check(sym: Int) {  // check and skip a particular symbol
        if (lex.sy == sym)
            lex.inSymbol()
        else
            error(LexerAnalyzer.Symbol[sym] + " Expected")
    }//check


    fun exp(): Expression? {// exp()
        val e = exp(1)
        check(LexerAnalyzer.eofSy)
        return e
    }


    private fun exp(priority: Int)                             // exp(...)
            : Expression? {
        var e: Expression? = null
        if (priority < 3) {
            e = exp(priority + 1)
            var sym = lex.sy

            while (member(sym, binOprs) && oprPriority[sym] == priority) {
                lex.inSymbol()                                       // e.g. 1+2+3
                e = Expression.Binary(sym, e!!, exp(priority + 1)!!)
                sym = lex.sy
            }
        } else if (member(lex.sy, unOprs)) { // unary op, e.g. -3
            val sym = lex.sy
            lex.inSymbol()
            e = Expression.Unary(sym, exp(priority)!!)
        } else
        // operand
        {
            when (lex.sy) {
                LexerAnalyzer.word -> {
                    e = Expression.Ident(lex.theWord)
                    lex.inSymbol()
                }
                LexerAnalyzer.numeral -> {
                    e = Expression.NumCon(lex.theNum)
                    lex.inSymbol()
                }
                LexerAnalyzer.open -> {                     // e.g. (e)
                    lex.inSymbol()
                    e = exp(1)
                    check(LexerAnalyzer.close)
                }
                else -> error("bad operand")
            }//switch
        }//if

        //e?.syType = lex.sy
        return e
    }//exp(...)


    fun member(n: Int, s: Long): Boolean {   // ? is n a member of the "set" s ?
        return 1L shl n and s != 0L
    }

    fun error(msg: String) {
        lex.error("Syntax: $msg")
    }// error
}

// the following allows Syntax to be tested on its own
fun main(argv: Array<String>) {
    println("--- Testing Syntax ---")
    val syn = SyntaxAnalyzer(LexerAnalyzer(FileInputStream("test1")))
    val e = syn.exp()
    println(e.toString())
    println("--- done ---")
}//main