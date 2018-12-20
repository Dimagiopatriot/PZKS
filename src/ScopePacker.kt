class ScopePacker(val expression: Expression.Binary) {
    fun pack() = packScopes(expression, expression)

    private fun packScopes(expression: Expression.Binary, wholeExpression: Expression.Binary) {
        fun handleLeftLeft() {
            val newLeft = (expression.left as Expression.Binary).left
            val leftRight = (expression.left as Expression.Binary).right
            val rightRight = (expression.right as Expression.Binary).right

            val newExpression = Expression.Binary(
                    opr = (expression.left as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = leftRight, right = rightRight)
            )

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        fun handleLeftRight() {
            val newLeft = (expression.left as Expression.Binary).left
            val leftRight = (expression.left as Expression.Binary).right
            val rightRight = (expression.right as Expression.Binary).left

            val newExpression = Expression.Binary(
                    opr = (expression.left as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = leftRight, right = rightRight)
            )

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        fun handleRightLeft() {
            val newLeft = (expression.left as Expression.Binary).right
            val leftRight = (expression.left as Expression.Binary).left
            val rightRight = (expression.right as Expression.Binary).right

            val newExpression = Expression.Binary(
                    opr = (expression.left as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = leftRight, right = rightRight)
            )

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        fun handleLeftBinaryLeft() {
            val newLeft = (expression.left as Expression.Binary).left
            val newRightLeft = (expression.left as Expression.Binary).right
            val newRightRight = Expression.NumCon(1.0)

            val newExpression = Expression.Binary(
                    opr = (expression.left as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = newRightLeft, right = newRightRight))

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        fun handleLeftBinaryRight() {
            val newLeft = (expression.left as Expression.Binary).right
            val newRightLeft = (expression.left as Expression.Binary).left
            val newRightRight = Expression.NumCon(1.0)

            val newExpression = Expression.Binary(
                    opr = (expression.left as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = newRightLeft, right = newRightRight))

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        fun handleRightBinaryLeft() {
            val newLeft = (expression.right as Expression.Binary).left
            val newRightLeft = Expression.NumCon(1.0)
            val newRightRight = (expression.right as Expression.Binary).right

            val newExpression = Expression.Binary(
                    opr = (expression.right as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = newRightLeft, right = newRightRight))

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        fun handleRightBinaryRight() {
            val newLeft = (expression.right as Expression.Binary).right
            val newRightLeft = Expression.NumCon(1.0)
            val newRightRight = (expression.right as Expression.Binary).left

            val newExpression = Expression.Binary(
                    opr = (expression.right as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = newRightLeft, right = newRightRight))

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        fun handleRightRight() {
            val newLeft = (expression.left as Expression.Binary).right
            val leftRight = (expression.left as Expression.Binary).left
            val rightRight = (expression.right as Expression.Binary).left

            val newExpression = Expression.Binary(
                    opr = (expression.left as Expression.Binary).opr, left = newLeft,
                    right = Expression.Binary(opr = expression.opr, left = leftRight, right = rightRight)
            )

            expression.left = newExpression.left
            expression.right = newExpression.right
            expression.opr = newExpression.opr

            println("Scopes: $wholeExpression")
        }

        if (canBePacked(expression)) {
            if (expression.left is Expression.Binary && expression.right is Expression.Binary) {
                when {
                    hasSameLeftComponent(
                            expression.left as Expression.Binary,
                            expression.right as Expression.Binary
                    ) -> handleLeftLeft()
                    hasSameLeftRightComponent(
                            expression.left as Expression.Binary,
                            expression.right as Expression.Binary
                    ) -> handleLeftRight()
                    hasSameRightLeftComponent(
                            expression.left as Expression.Binary,
                            expression.right as Expression.Binary
                    ) -> handleRightLeft()
                    hasSameRightComponent(
                            expression.left as Expression.Binary,
                            expression.right as Expression.Binary
                    ) -> handleRightRight()
                }
            } else if (expression.left is Expression.Binary) {
                when {
                    hasSameLeft(expression.left as Expression.Binary, expression.right) -> handleLeftBinaryLeft()
                    hasSameRight(expression.left as Expression.Binary, expression.right) -> handleLeftBinaryRight()
                }
            } else if (expression.right is Expression.Binary) {
                when {
                    hasSameLeft(expression.left, expression.right as Expression.Binary) -> handleRightBinaryLeft()
                    hasSameRight(expression.left, expression.right as Expression.Binary) -> handleRightBinaryRight()
                }
            }
        }

        if (expression.left is Expression.Binary) {
            packScopes(expression.left as Expression.Binary, wholeExpression)
        }

        if (expression.right is Expression.Binary) {
            packScopes(expression.right as Expression.Binary, wholeExpression)
        }
    }

    private fun canBePacked(expression: Expression.Binary) =
            expression.opr != 7 &&
                    (expression.left is Expression.Binary && expression.right is Expression.Binary && (expression.left as Expression.Binary).opr == (expression.right as Expression.Binary).opr) ||
                    (expression.left is Expression.Binary && (expression.right is Expression.Ident || expression.right is Expression.NumCon) && ((expression.left as Expression.Binary).left == expression.right || (expression.left as Expression.Binary).right == expression.right)) ||
                    (expression.right is Expression.Binary && (expression.left is Expression.Ident || expression.left is Expression.NumCon) && ((expression.right as Expression.Binary).left == expression.left || (expression.right as Expression.Binary).right == expression.left))

    private fun hasSameLeftComponent(left: Expression.Binary, right: Expression.Binary) =
            left.left == right.left

    private fun hasSameRightComponent(left: Expression.Binary, right: Expression.Binary) =
            left.right == right.right

    private fun hasSameRightLeftComponent(left: Expression.Binary, right: Expression.Binary) =
            left.right == right.left

    private fun hasSameLeftRightComponent(left: Expression.Binary, right: Expression.Binary) =
            left.left == right.right

    private fun hasSameLeft(left: Expression.Binary, right: Expression) =
            left.left == right

    private fun hasSameRight(left: Expression.Binary, right: Expression) =
            left.right == right

    private fun hasSameLeft(left: Expression, right: Expression.Binary) =
            left == right.left

    private fun hasSameRight(left: Expression, right: Expression.Binary) =
            left == right.right
}