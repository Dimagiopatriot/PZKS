class TreeBuilder(exp: Expression) {

    private var output = """"""

    init {
        output = checkExpressionType(exp)
    }

    private fun checkExpressionType(exp: Expression): String {

        when (exp) {
            is Expression.Ident, is Expression.NumCon, is Expression.Unary -> return exp.toString()
            is Expression.Binary -> return buildTree(exp)
            else -> return "Something goes wrong"
        }
    }

    fun buildTree(exp: Expression.Binary): String {
        return """(${LexerAnalyzer.Symbol[exp.opr]})---${checkExpressionType(exp.right)}
            | |
            | |_${checkExpressionType(exp.left)}
        """
    }

    fun printTree() = output.trimMargin()
}