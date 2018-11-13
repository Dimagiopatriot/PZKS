import java.io.FileInputStream
import java.io.InputStream
import java.lang.NumberFormatException

class LexerAnalyzer(val inp: InputStream) {           // Lexical processor of symbols

    var sy = -1                                      // lexical state variables
    var ch = ' '
    var buffer = ByteArray(1)
    var eof = false
    var theWord = "<?>"
    var theNum: Number = 666

    companion object {
        val word = 0   // symbol codes...
        val numeral = 1 // nums
        val open = 2   // (
        val close = 3   // )
        val plus = 4   // +
        val minus = 5   // -
        val times = 6   // *
        val over = 7   // /
        val eofSy = 8

        val Symbol = arrayOf("<word>", "<numeral>", "(", ")", "+", "-", "*", "/", "<eof>")//Symbol
    }

    init {
        OperatorsChecker(this)
        inSymbol()
    }

    fun eoi(): Boolean {
        return sy == eofSy
    }


    fun inSymbol()                                           // inSymbol
    // get the next symbol from the input stream
    {
        if (sy == eofSy) return
        while (ch == ' ') getch() // skip white space

        if (eof)
            sy = eofSy
        else if (Character.isLetter(ch)) { // words
            val w = StringBuffer()
            while (Character.isLetterOrDigit(ch)) {
                w.append(ch)
                getch()
            }
            theWord = w.toString()
            sy = word
        } else if (Character.isDigit(ch) || ch == '.') {   // numbers
            val numberStr = StringBuffer()
            theNum = 0
            while (Character.isDigit(ch) || ch == '.') {
                numberStr.append(ch)
                getch()
            }

            try {
                theNum = numberStr.toString().toDouble()
            } catch (e: NumberFormatException) {
                error("bad symbol")
            }

            sy = numeral
        } else {// special symbols
            val ch2 = ch.toInt()
            getch()
            when (ch2) {
                '+'.toInt() -> sy = plus
                '-'.toInt() -> sy = minus
                '*'.toInt() -> sy = times
                '/'.toInt() -> sy = over
                '('.toInt() -> sy = open
                ')'.toInt() -> sy = close
                else -> error("bad symbol")
            }
        }
    }//inSymbol


    fun getch()                                                        // getch
    // NB. changes variable ch as a side-effect.
    {
        ch = '.'
        if (sy == eofSy) return
        try {
            var n = 0
            if (inp.available() > 0) n = inp.read(buffer)
            if (n <= 0) eof = true else ch = buffer[0].toChar()
        } catch (e: Exception) {
        }

        if (ch == '\n' || ch == '\t') ch = ' '
    }//getch


    fun skipRest() {
        if (!eof) print("skipping to end of input...")
        var n = 0
        while (!eof) {
            if (n % 80 == 0) println() // break line
            print(ch)
            n++
            getch()
        }
        println()
    }//skipRest


    fun error(msg: String)                                       // error
    {
        println("\nError: $msg")
        skipRest()
        System.exit(1)
    }//error
}

// the following main() allows Lexical to be tested in isolation
fun main(argv: Array<String>) {
    println("--- Testing LexerAnalyzer ---")
    for (i in argv.indices)
    // command line params if any
        print("argv[" + i + "]=" + argv[i] + "\n")
    val lex = LexerAnalyzer(FileInputStream("test1"))
    while (!lex.eoi()) {
        val sy = lex.sy
        print("$sy: ")
        if (sy == LexerAnalyzer.word) System.out.print(lex.theWord)
        if (sy == LexerAnalyzer.numeral) System.out.print(lex.theNum)
        println(",")
        lex.inSymbol()
    }
    println("--- end ---")
}//main