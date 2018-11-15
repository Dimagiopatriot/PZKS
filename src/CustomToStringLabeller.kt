import edu.uci.ics.jung.visualization.decorators.ToStringLabeller

class CustomToStringLabeller : ToStringLabeller() {

    override fun apply(o: Any?): String? {
        return if (o is Expression.Binary) {
            LexerAnalyzer.Symbol[o.opr]
        } else {
            o.toString()
        }
    }
}