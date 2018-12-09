import kotlin.math.exp

class Commutation(private val enterExpression: Expression.Binary) {

    val nodesList = mutableSetOf<Expression>()
    var newExpressionInStringFormat = ""

    fun commutateExp() {
        decomposeExpressionToNodes(enterExpression)
        val sortedNodes = nodesList.sortedBy { it.priority }
        sortedNodes.forEach {
            newExpressionInStringFormat += if (it == sortedNodes.last()){
                "$it "
            } else {
                "$it ${LexerAnalyzer.Symbol[it.parent!!.opr]} "
            }
        }
    }

    fun decomposeExpressionToNodes(expression: Expression) {
        when (expression) {
            is Expression.Binary -> {
                decomposeExpressionToNodes(expression.left)
                decomposeExpressionToNodes(expression.right)
            }
            else -> {
                expression.parent?.let { parent ->
                    if (parent.opr == 4 &&
                            (parent.right is Expression.Binary || parent.left is Expression.Binary)) {
                        expression.priority = 0
                        nodesList.add(expression)
                    }
                    if (parent.right !is Expression.Binary && parent.left !is Expression.Binary) {
                        val parentOfParent = parent.parent
                        if (parentOfParent != null) {
                            if (parentOfParent.opr == 4) {
                                parent.priority = 1
                                nodesList.add(parent)
                            } else {
                                parentOfParent.priority = 2
                                nodesList.add(parentOfParent)
                            }
                        }
                    }
                }
            }
        }
    }

    fun printExp() {
        println("After commutation")
        println(newExpressionInStringFormat)
    }
}