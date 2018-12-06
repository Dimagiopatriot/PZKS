import java.io.FileInputStream

fun main(argv: Array<String>) {

    val syn = SyntaxAnalyzer(LexerAnalyzer(FileInputStream("test1")))
    val e = syn.exp()                          // parse an Expression
    val treeBuilder = TreeBuilder(e!!)
    println("----------------")
    println(e.toString())
    println("--- Get Tree ---")
    treeBuilder.showTree()

    println("--- done ---")
}//main