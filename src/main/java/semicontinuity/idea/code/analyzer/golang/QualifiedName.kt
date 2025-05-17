package semicontinuity.idea.code.analyzer.golang

class QualifiedName(val qualifier: String, val name: String) {
    override fun toString(): String {
        return if (qualifier.isEmpty()) name else "$qualifier.$name"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val that = other as QualifiedName

        if (qualifier != that.qualifier) {
            return false
        }
        return name == that.name
    }

    override fun hashCode(): Int {
        var result = qualifier.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
