package semicontinuity.idea.code.analyzer.golang

import com.intellij.psi.PsiElement

// qualifier == "" && name != ""   => function
// qualifier != "" && name != ""   => method
// qualifier != "" && name == ""   => struct
class Member(@JvmField val qualifier: String, @JvmField val name: String, val psiElement: PsiElement) {
    override fun toString(): String {
        return if (qualifier.isEmpty()) name else "$qualifier.$name"
    }

    fun toQualifiedName(): QualifiedName {
        return QualifiedName(qualifier, name)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }

        val that = o as Member

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
