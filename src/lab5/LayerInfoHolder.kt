package lab5

import Expression

data class LayerInfoHolder(var expression: Expression, var tik: Int, var layerNumber: Int) {

    override fun toString(): String {
        return "\t$expression\t|\tlayer number #: $layerNumber"
    }
}