class Commutation(val enterExpression: Expression.Binary) {

    val possibleForms = mutableListOf<Expression>()
    var iteration = 0
    var globalWhileIteration = 0

    init {
        startCommutate()
        println("Варіанти комутації: ")
        possibleForms.forEach { println("$it") }
    }

    fun startCommutate() {
        for (whileIteration in 0..7) {
            possibleForms.add(commutate(enterExpression))
            globalWhileIteration  = whileIteration
        }
    }

    fun commutate(enterExpression: Expression): Expression {
        var buffExp = enterExpression
        if (buffExp is Expression.Binary) {
            var buffExpLeft = buffExp.left
            var buffExpRight = buffExp.right
            while (iteration < globalWhileIteration) {
                if (buffExpLeft is Expression.Binary) {
                    buffExpLeft.left = remakeExp(buffExpLeft)
                    buffExp.left = buffExpLeft
                }
                if (buffExpRight is Expression.Binary) {
                    buffExpRight.right = remakeExp(buffExpRight)
                    buffExp.right = buffExpRight
                }
                iteration++
            }
        }
        return buffExp
    }

    fun remakeExp(exp: Expression): Expression {
        if (exp is Expression.Binary) {
            val startLeftExpression = exp.left
            val startRightExpression = exp.right
            if (exp.opr == 4 || exp.opr == 6) {
                exp.left = startRightExpression
                exp.right = startLeftExpression
            }
        }
        return exp
    }
}