package lab5

import Expression
import LexerAnalyzer
import SyntaxAnalyzer
import java.io.ByteArrayInputStream
import java.util.*

class Conveyor(stringExp: String) {

    private val mainExpression = SyntaxAnalyzer(LexerAnalyzer(ByteArrayInputStream(stringExp.toByteArray()))).exp()
    private val expressionWrapperList = mutableListOf<TickExpressionWrapper>()
    private val instructionMap = mutableMapOf<String, List<Expression>>()
    private val layerHolders = mutableListOf<LayerInfoHolder>()
    private lateinit var tickMap: SortedMap<Int, List<LayerInfoHolder>>

    private val tickSize = 3
    private val layerNumber = 3

    fun startConveyor() {
        if (mainExpression is Expression.Binary) {
            extractParentDeep(mainExpression)
            val mapOfExpressionWrapperList = expressionWrapperList.groupBy { it.parentCount }.toSortedMap(reverseOrder())

            conveyingV2(mapOfExpressionWrapperList)
            tickMap = layerHolders.groupBy { it.tik }.toSortedMap()

            //conveyingV1(mapOfExpressionWrapperList)
        }
    }

    fun conveyingV2(mapOfExpressionWrapperList: Map<Int, List<TickExpressionWrapper>>) {
        var outsideTickCounter = 1
        var insideTickCounter: Int

        for (layerNum in 1..layerNumber) {
            insideTickCounter = outsideTickCounter
            mapOfExpressionWrapperList.forEach {
                val expList = it.value.map { tickExpressionWrapper -> tickExpressionWrapper.binaryExpression }
                expList.forEach { expression ->
                    for (i in 1..tickSize) {
                        layerHolders.add(LayerInfoHolder(expression, insideTickCounter, layerNum))
                        insideTickCounter++
                    }

                    if (expression == expList.last()) {
                        insideTickCounter += layerNumber - 1
                    }
                }
            }
            outsideTickCounter++
        }
    }

    fun conveyingV1(mapOfExpressionWrapperList: Map<Int, List<TickExpressionWrapper>>) {
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

    fun extractParentDeep(expression: Expression.Binary) {
        var buffExp = expression
        var parentCount = 0
        var parentExp = buffExp.parent
        while (parentExp != null) {
            buffExp = parentExp
            parentExp = buffExp.parent
            parentCount++
        }
        expressionWrapperList.add(TickExpressionWrapper(parentCount, expression))

        if (expression.left is Expression.Binary) {
            extractParentDeep(expression.left as Expression.Binary)
        }

        if (expression.right is Expression.Binary) {
            extractParentDeep(expression.right as Expression.Binary)
        }
    }

    fun printTicks() {
        var output = ""
        tickMap.forEach { output += "Tick #${it.key} : ${it.value.joinToString(separator = "\n\t\t  ")} \n" }
        println(output)
        println("Time: ${tickMap.size} ticks")
        val acceleration = ((expressionWrapperList.size * tickSize * layerNumber) + 2).toDouble() / tickMap.size.toDouble()
        println("Acceleration: $acceleration")
        println("Efficiency: ${acceleration / layerNumber}")
    }

    fun printInstructions() {
        var output = ""
        instructionMap.forEach { output += "${it.key} : ${it.value.joinToString(separator = "\n\t\t\t\t")} \n" }
        println(output)
    }
}