import kotlin.math.exp

class Commutation(enterExpression: Expression.Binary) {

    val nodesList = mutableListOf<Expression>()

    init {
        startCommutate()
        println("Варіанти комутації: ")
    }

    fun startCommutate() {

    }

    fun decomposeExpressionToNodes(expression: Expression) {
        when (expression) {
            is Expression.Binary -> {
                decomposeExpressionToNodes(expression.left)
                decomposeExpressionToNodes(expression.right)
            }
            else -> {
                expression.parent?.let { parent ->
                    if (parent is Expression.Binary && parent.opr == 4) {
                        expression.priority = 0
                        nodesList.add(expression)
                    }
                }
            }
        }
    }

    fun commutate(enterExpression: Expression): Expression {

        return enterExpression
    }

    fun remakeExp(exp: Expression.Binary): Expression.Binary = with(exp) {
        val startLeftExpression = left
        val startRightExpression = right
        if (opr == 4 || opr == 6) {
            left = startRightExpression
            right = startLeftExpression
        }
        return this
    }
}