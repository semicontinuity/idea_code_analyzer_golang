package semicontinuity.idea.code.analyzer.golang

import com.goide.psi.GoFile
import com.goide.psi.GoInterfaceType
import com.goide.psi.GoMethodDeclaration
import com.goide.psi.GoMethodSpec
import com.goide.psi.GoNamedSignatureOwner
import com.goide.psi.GoSignature
import com.intellij.psi.util.PsiTreeUtil

fun getSignatureStructure(signature: GoSignature?): String? {
    if (signature == null) return null

    // Parameter types
    val params = signature.parameters.parameterDeclarationList.map { decl ->
        // Get type text from the declaration (covers all names in group)
        decl.type?.text ?: ""
    } ?: emptyList()

    // Result types (can be grouped similarly)
    val results = signature.result?.parameters?.parameterDeclarationList?.map { decl ->
        decl.type?.text ?: ""
    } ?: emptyList()

    // Use explicit separator to avoid whitespace differences
    return params.joinToString(",") + "->" + results.joinToString(",")
}

fun signaturesMatch(m1: GoNamedSignatureOwner, m2: GoNamedSignatureOwner): Boolean {
    return getSignatureStructure(m1.signature) == getSignatureStructure(m2.signature)
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
            val b = ifaceMethod.name == method.name
            val signaturesMatch = signaturesMatch(method, ifaceMethod)
            if (b && signaturesMatch) {
                // You can add more checks for assigning value/pointer to interface as needed
                return ifaceMethod
            }
        }
        // Optionally, recursively check embedded interfaces here if desired
    }
    return null
}