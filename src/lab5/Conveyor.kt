package lab5

import Expression
import LexerAnalyzer
import SyntaxAnalyzer
import java.io.ByteArrayInputStream

class Conveyor(stringExp: String) {

    private val mainExpression = SyntaxAnalyzer(LexerAnalyzer(ByteArrayInputStream(stringExp.toByteArray()))).exp()
    private val expressionWrapperList = mutableListOf<TickExpresssionWrapper>()
    private val instructionMap = mutableMapOf<String, List<Expression>>()

    private val tickSize = 4

    fun startConveyor() {
        if (mainExpression is Expression.Binary) {
            extractParentDeep(mainExpression)
            val mapOfExpressionWrapperList = expressionWrapperList.groupBy { it.parentCount }.toSortedMap(reverseOrder())

            var instructionCounter = 0
            mapOfExpressionWrapperList.forEach { map ->
                val expressionList = map.value
                val delayStack = mutableListOf<StackDelayWrapper>()
                var i = 0

                do {
                    if (i < expressionList.size) {
                        delayStack.add(StackDelayWrapper(tickSize, expressionList[i]))
                        i++
                    }

                    instructionMap["Instruction $instructionCounter"] = delayStack.map { it.tickExpressionWrapper.binaryExpression }
                    instructionCounter++

                    delayStack.forEach { element -> element.delayToRemove-- }
                    delayStack.removeAll { it.delayToRemove == 0 }
                } while (delayStack.isNotEmpty())
            }
        }
    }

    fun extractParentDeep(expression: Expression.Binary) {
        var buffExp = expression
        var parentCount = 0
        var parentExp = buffExp.parent
        while (parentExp != null) {
            buffExp = parentExp
            parentExp = buffExp.parent
            parentCount++
        }
        expressionWrapperList.add(TickExpresssionWrapper(parentCount, expression))

        if (expression.left is Expression.Binary) {
            extractParentDeep(expression.left as Expression.Binary)
        }

        if (expression.right is Expression.Binary) {
            extractParentDeep(expression.right as Expression.Binary)
        }
    }
}