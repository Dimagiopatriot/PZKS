import java.io.FileInputStream

fun main(argv: Array<String>) {

    val syn = SyntaxAnalyzer(LexerAnalyzer(FileInputStream("test1")))
    val e = syn.exp()                          // parse an Expression
    println("----------------")
    println("---- start expression ----")
    println(e.toString())
    val treeBuilder = TreeBuilder(e!!)
    println("----------------")
    println(e.toString())
    println("--- Get Tree ---")
    treeBuilder.showTree()
    if (e is Expression.Binary) {
        Commutation(e)
    }

    println("--- done ---")
}//main