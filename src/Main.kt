import java.io.FileInputStream

fun main(argv: Array<String>) {

    val syn = SyntaxAnalyzer(LexerAnalyzer(FileInputStream("test1")))
    val e = syn.exp()                          // parse an Expression
    println("----------------")
    println("---- start expression ----")
    println(e.toString())
  /*  if (e is Expression.Binary) {
        val commutation = Commutation(e)
        commutation.commutateExp()
        commutation.printExp()
    }*/

    if (e is Expression.Binary) {
        val scopeUnpacker = ScopePacker(e)
        scopeUnpacker.pack()
    }

   /* val treeBuilder = TreeBuilder(e!!)
    println("----------------")
    println(e.toString())
    println("--- Get Tree ---")
    treeBuilder.showTree()*/

    println("--- done ---")
}//main