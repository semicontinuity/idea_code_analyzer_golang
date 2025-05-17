package semicontinuity.idea.code.analyzer.golang.logic

import com.goide.psi.GoInterfaceType
import com.goide.psi.GoMethodDeclaration
import com.goide.psi.GoMethodSpec

class GoInterfaceSearch(private val interfaces: Collection<GoInterfaceType>) {

    // Returns the interface method this method implements, or null
    fun findImplementedInterfaceMethod(
        method: GoMethodDeclaration
    ): GoMethodSpec? {
        for (iface in interfaces) {
            for (ifaceMethod in iface.methods) {
                if (ifaceMethod.name == method.name && signaturesMatch(method, ifaceMethod)) {
                    return ifaceMethod
                }
            }
            // Optionally, recursively check embedded interfaces here if desired
        }
        return null
    }
}