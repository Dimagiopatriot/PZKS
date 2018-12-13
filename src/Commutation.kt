import kotlin.math.exp

class Commutation(private val enterExpression: Expression.Binary) {

    val nodesList = mutableSetOf<Expression>()
    var newExpressionInStringFormat = ""

    fun commutateExp() {
        decomposeExpressionToNodes(enterExpression)
        val copy = mutableSetOf<Expression>()
        copy.addAll(nodesList)
        copy.forEach { if (it is Expression.Binary) removeChilds(it) }
        val sortedNodes = nodesList.sortedBy { it.priority }.filter {
            it.parent != null && (it == it.parent?.right || it == it.parent?.left)
        }
        newExpressionInStringFormat += "${sortedNodes[0]}"
        sortedNodes.dropLast(1).forEachIndexed { index, _ ->
            val nextIt = sortedNodes[index + 1]
            nextIt.parent?.let { parent ->
                val parentOfParent = parent.parent
                if (parentOfParent != null && parentOfParent.right == parent) {
                    val parentOfParentOpr = parentOfParent.opr
                    when (listOf(parentOfParentOpr, parent.opr)) {
                        listOf(5, 5), listOf(5, 4) -> newExpressionInStringFormat += " ${LexerAnalyzer.Symbol[5]} $nextIt"
                    }
                } else {
                    newExpressionInStringFormat += " ${LexerAnalyzer.Symbol[parent.opr]} $nextIt"
                }
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
                        var parentOfParent = parent.parent
                        while (true) {
                            if (parentOfParent != null) {
                                if (parentOfParent.opr == 4) {
                                    if (parentOfParent.parent != null && parentOfParent.parent!!.opr == 7) {
                                        parentOfParent.parent!!.priority = 3
                                        nodesList.add(parentOfParent.parent!!)
                                    } else {
                                        parent.priority = 1
                                        nodesList.add(parent)
                                    }
                                    break
                                } else {
                                    parentOfParent.priority = 2
                                    nodesList.add(parentOfParent)
                                    parentOfParent = parentOfParent.parent
                                }
                            } else
                                break
                        }
                    }
                }
            }
        }
    }

    fun removeChilds(parentExp: Expression.Binary) {
        var stairsExp = parentExp
        while (stairsExp.right is Expression.Binary || stairsExp.left is Expression.Binary) {
            nodesList.removeIf { it == stairsExp.right || it == stairsExp.left }
            val goLeft = stairsExp.left
            if (goLeft is Expression.Binary) {
                stairsExp = goLeft
            } else {
                break
            }
        }
    }

    fun printExp() {
        println("After commutation")
        println(newExpressionInStringFormat)
    }
}