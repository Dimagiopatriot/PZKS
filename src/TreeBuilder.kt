import com.google.common.base.Function
import edu.uci.ics.jung.algorithms.layout.TreeLayout
import edu.uci.ics.jung.graph.DelegateTree
import edu.uci.ics.jung.visualization.BasicVisualizationServer
import edu.uci.ics.jung.visualization.renderers.Renderer
import java.awt.Color
import java.awt.Dimension
import java.awt.Paint
import javax.swing.JFrame


class TreeBuilder(exp: Expression) {

    val graph = DelegateTree<Expression, String>()
    var isRootSet = false
    lateinit var resultExpression: Expression.Binary

    init {
        checkExpressionType(exp){ resultExpression = rebuildExpression(it) }
        if (!isRootSet) {
            graph.root = resultExpression
            isRootSet = true
        }
        buildTree(resultExpression)
    }

    private fun checkExpressionType(exp: Expression, method: (exp: Expression.Binary) -> Unit) {
        when (exp) {
            is Expression.Binary -> method(exp)
            //else -> if (!isRootSet) graph.root = exp
        }
    }

    fun rebuildExpression(exp: Expression.Binary): Expression.Binary {
        lateinit var buffExp: Expression.Binary
        when(exp.opr) {
            4,6 -> buffExp = paralellizeTree(exp)
            5 -> buffExp = paralellizeTree(exp,4)
            7 -> buffExp = paralellizeTree(exp, 6)
        }

        checkExpressionType(buffExp.left){ rebuildExpression(it) }
        checkExpressionType(buffExp.right){ rebuildExpression(it) }
        return buffExp
    }

    fun buildTree(exp: Expression.Binary) {

        graph.addChild("$exp${exp.left}", exp, exp.left)
        graph.addChild("$exp${exp.right}", exp, exp.right)

        checkExpressionType(exp.left){ buildTree(it) }
        checkExpressionType(exp.right){ buildTree(it) }
    }

    fun paralellizeTree(exp: Expression.Binary, rightOpr: Int = exp.opr): Expression.Binary = with(exp) {
        val startLeftExpression = left
        val startRightExpression = right
        if (startLeftExpression is Expression.Binary && (startLeftExpression.opr == opr)) {
            val newRightExp = Expression.Binary(rightOpr, startLeftExpression.right, startRightExpression)
            val newLeftExp = startLeftExpression.left
            return Expression.Binary(startLeftExpression.opr, newLeftExp, newRightExp)
        }
        return this
    }

    fun showTree() {
        val layout = TreeLayout<Expression, String>(graph)

        val visualisationServer = BasicVisualizationServer<Expression, String>(layout)
        visualisationServer.preferredSize = Dimension(350, 350)
        visualisationServer.renderContext.vertexLabelTransformer = CustomToStringLabeller()
        visualisationServer.renderContext.vertexFillPaintTransformer = Function<Expression, Paint> { Color.WHITE }
        visualisationServer.renderer.vertexLabelRenderer.position = Renderer.VertexLabel.Position.CNTR

        val frame = JFrame("Our tree")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.add(visualisationServer)
        frame.pack()
        frame.isVisible = true
    }
}