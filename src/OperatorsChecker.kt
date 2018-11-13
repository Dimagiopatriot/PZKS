import java.io.FileInputStream
import java.io.InputStream
import java.util.*

class OperatorsChecker(val lexerAnalyzer: LexerAnalyzer, inp: InputStream = FileInputStream("test1")) {

    private var expression = ""
    private val problemSituations = listOf("*-", "-*", "/-", "-/", "--", "+-", "-+", "++", "*+", "+*")

    init {
        val scanner = Scanner(inp)
        while (scanner.hasNext()) {
            expression += scanner.next()
        }
        checkProblemSituations()
    }

    private fun checkProblemSituations() {
        problemSituations.forEach {
            if (expression.contains(it)) {
                println("Error: Syntax: bad operator")
                println("skipping to end of input...")
                println(expression.substringAfter(it))
                System.exit(1)
            }
        }
    }
}