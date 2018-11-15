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

    init {
        graph.root = exp
        checkExpressionType(exp)
    }

    private fun checkExpressionType(exp: Expression) {

        when (exp) {
            is Expression.Binary ->  buildTree(exp)
        }
    }

    fun buildTree(exp: Expression.Binary){
        graph.addChild("$exp${exp.left}", exp, exp.left)
        graph.addChild("$exp${exp.right}", exp, exp.right)

        checkExpressionType(exp.left)
        checkExpressionType(exp.right)
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