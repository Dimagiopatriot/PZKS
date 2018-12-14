class ScopeUnpacker {

    val operationPriorityMap = mapOf(
            4 to 1, // `+` priority is 1
            5 to 1, // `-` priority is 1
            6 to 2, // `*` priority is 2
            7 to 1  // `/` priority is 2
    )

    var mainExpression: Expression.Binary? = null
        set(value) {
            field = Expression.Binary(value!!.opr, value.left, value.right)
        }


    fun openScopes(expression: Expression.Binary) {
        val expressionOprPriority = operationPriorityMap[expression.opr]
        val buffExpRightSide = expression.right
        val buffExpLeftSide = expression.left

        val buffExpOpr = expression.opr

        if (buffExpRightSide is Expression.Binary) {
            val rightSideOprPriority = operationPriorityMap[buffExpRightSide.opr]
            if (rightSideOprPriority!! < expressionOprPriority!! && buffExpRightSide.opr != 7) {

                expression.opr = buffExpRightSide.opr
                expression.right = Expression.Binary(buffExpOpr, buffExpLeftSide, buffExpRightSide.right)
                expression.left = Expression.Binary(buffExpOpr, buffExpLeftSide, buffExpRightSide.left)
            }
        }
        if (buffExpLeftSide is Expression.Binary) {
            val leftSideOprPriority = operationPriorityMap[buffExpLeftSide.opr]
            if (leftSideOprPriority!! < expressionOprPriority!! && buffExpLeftSide.opr != 7) {

                expression.opr = buffExpLeftSide.opr
                expression.right = Expression.Binary(buffExpOpr, buffExpLeftSide.right, buffExpRightSide)
                expression.left = Expression.Binary(buffExpOpr, buffExpLeftSide.left, buffExpRightSide)
            }
        }
        if (expression.right is Expression.Binary) openScopes(expression.right as Expression.Binary)
        if (expression.left is Expression.Binary) openScopes(expression.left as Expression.Binary)
    }

    fun printExp() {
        println("------ Scopes --------")
        println(mainExpression)
    }
}