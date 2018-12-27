package lab5

import Expression
import LexerAnalyzer
import SyntaxAnalyzer
import java.io.ByteArrayInputStream
import java.util.*

class Conveyor(stringExp: String) {

    private val mainExpression = SyntaxAnalyzer(LexerAnalyzer(ByteArrayInputStream(stringExp.toByteArray()))).exp()
    private val expressionWrapperList = mutableListOf<TickExpressionWrapper>()
    private val layerHolders = mutableListOf<LayerInfoHolder>()
    private lateinit var operationMap: SortedMap<Int, List<LayerInfoHolder>>

    private val tickSizes = mapOf(
            LexerAnalyzer.plus to 1,
            LexerAnalyzer.minus to 2,
            LexerAnalyzer.times to 3,
            LexerAnalyzer.over to 4
    )
    private val maxTickSize = tickSizes.map { it.value }.max()!!

    private val layerNumber = 3

    fun startConveyor() {
        if (mainExpression is Expression.Binary) {
            extractParentDeep(mainExpression)
            val mapOfExpressionWrapperList = expressionWrapperList.groupBy { it.parentCount }.toSortedMap(reverseOrder())

            conveyingV2(mapOfExpressionWrapperList)
            operationMap = layerHolders.groupBy { it.tik }.toSortedMap()
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
                    layerHolders.add(LayerInfoHolder(expression, insideTickCounter, layerNum))
                    insideTickCounter += maxTickSize

                    if (expression == expList.last()) {
                        insideTickCounter += (layerNumber - 1) * maxTickSize
                    }
                }
            }
            outsideTickCounter += maxTickSize
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
        operationMap.forEach { output += "Ticks #${it.key} - ${it.key + maxTickSize - 1} : ${it.value.joinToString(separator = "\n\t\t\t\t  ")} \n" }
        println(output)
        val ticksSize = operationMap.map { it.key }.max()!! + maxTickSize - 1
        println("Time: $ticksSize ticks")
        val acceleration = ((getOperationMnojennya() * layerNumber) + 2).toDouble() / ticksSize.toDouble()
        println("Acceleration: $acceleration")
        println("Efficiency: ${acceleration / layerNumber}")
    }

    fun getOperationMnojennya(): Int {
        val mapByOpr = expressionWrapperList.groupBy { it.binaryExpression.opr }
        var groupator = 0
        mapByOpr.forEach { element -> groupator += element.value.size * tickSizes[element.key]!! }
        return groupator
    }
}