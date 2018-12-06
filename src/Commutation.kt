class Commutation(enterExpression: Expression.Binary) {

    val possibleForms = mutableListOf<Expression>()
    val enterExpressionClone = Expression.Binary(enterExpression.opr, enterExpression.left, enterExpression.right)

    var iteration = 0
    var globalWhileIteration = 0

    init {
        startCommutate()
        println("Варіанти комутації: ")
        possibleForms.forEach { println("$it") }
    }

    fun startCommutate() {
        possibleForms.add(commutate(enterExpressionClone))
    }

    fun commutate(enterExpression: Expression): Expression {
        var buffExp = enterExpression
        if (enterExpression is Expression.Binary) {
            buffExp = remakeExp(enterExpression)
            buffExp.left = commutate(buffExp.left)
            buffExp.right = commutate(buffExp.right)
            iteration++
        }
        return buffExp
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