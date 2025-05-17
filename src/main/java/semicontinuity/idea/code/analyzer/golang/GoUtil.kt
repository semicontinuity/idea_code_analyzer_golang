package semicontinuity.idea.code.analyzer.golang


import com.goide.psi.GoFile
import com.goide.psi.GoInterfaceType
import com.goide.psi.GoMethodDeclaration
import com.goide.psi.GoMethodSpec
import com.goide.psi.GoNamedSignatureOwner
import com.intellij.psi.util.PsiTreeUtil

// Helper: Compare signatures (you can make this more robust if needed)
fun signaturesMatch(m1: GoNamedSignatureOwner, m2: GoNamedSignatureOwner): Boolean {
    val sig1 = m1.signature
    val sig2 = m2.signature
    return sig1?.text == sig2?.text
}

// Main function: Returns the interface method this method implements, or null
fun findImplementedInterfaceMethod(method: GoMethodDeclaration): GoMethodSpec? {
    val file = method.containingFile as? GoFile ?: return null
    val interfaces = PsiTreeUtil.collectElementsOfType(file, GoInterfaceType::class.java).filter { true }

    for (iface in interfaces) {
        val ifaceType = iface ?: continue
        val ifaceMethods = ifaceType.methods // FIXED

        for (ifaceMethod in ifaceMethods) {
            // Compare names
            if (ifaceMethod.name == method.name &&
                signaturesMatch(method, ifaceMethod)
            ) {
                // You can add more checks for assigning value/pointer to interface as needed
                return ifaceMethod
            }
        }
        // Optionally, recursively check embedded interfaces here if desired
    }
    return null
}