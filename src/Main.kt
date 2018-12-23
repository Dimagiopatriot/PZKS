import lab5.Conveyor
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
    //treeBuilder.showTree()

    val conveyor = Conveyor(e.toString())
    conveyor.startConveyor()
    conveyor.printTicks()
    println("--- done ---")
}//main