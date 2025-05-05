package semicontinuity.idea.code.analyzer.golang

import com.goide.psi.GoCompositeLit
import com.goide.psi.GoFile
import com.goide.psi.GoFunctionDeclaration
import com.goide.psi.GoMethodDeclaration
import com.goide.psi.GoMethodSpec
import com.goide.psi.GoNamedSignatureOwner
import com.goide.psi.GoPointerType
import com.goide.psi.GoReceiver
import com.goide.psi.GoSpecType
import com.goide.psi.GoType
import com.goide.psi.GoTypeSpec
import com.goide.psi.GoUnaryExpr
import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil

fun qualifier(e: LeafPsiElement): String {
    return (packageName(e.containingFile) + ':').also { println("LeafPsiElement $it") }
}

fun qualifier(e: GoType): String {
    return (packageName(e.containingFile) + ':' + e.text).also { println("GoType $it") }
}

fun qualifier(e: GoTypeSpec): String {
    return (packageName(e.containingFile) + ':' + e.identifier.text).also { println("GoTypeSpec $it") }
}

fun qualifier(e: GoNamedSignatureOwner) = when (e) {
    is GoFunctionDeclaration -> qualifier(e)
    is GoMethodDeclaration -> qualifier(e)
    is GoMethodSpec -> qualifier(e)
    else -> "".also { println("GoNamedSignatureOwner") }
}

fun qualifier(e: GoFunctionDeclaration): String {
    return (packageName(e.containingFile) + ':').also { println("GoFunctionDeclaration $it") }
}

fun qualifier(e: GoMethodDeclaration): String {
    return (packageName(e.containingFile) + ':').also {
        val t: GoType? = e.receiver?.type
        println("GoMethodDeclaration type $t")
        println("GoMethodDeclaration type ${t?.reference}")
    }
}


fun qualifier(e: GoMethodSpec): String {

    val t = PsiTreeUtil.getStubOrPsiParentOfType(
        e,
        GoSpecType::class.java
    )

    return (packageName(e.containingFile) + ':' + t?.identifier?.text).also {

        println("GoMethodSpec $it ${e.text}")
        println("GoMethodSpec +  $it ${e.name}")
        println("GoMethodSpec +  $it ${e.identifier?.text}")
        println("GoMethodSpec +  $it ${e.qualifiedName}")
    }
}

fun qualifier(unaryExpr: GoUnaryExpr): String {
    return (packageName(unaryExpr.containingFile) + ':' + unaryExpr.expression!!.getGoType(ResolveState.initial())!!.text).also {
        println(
            "GoUnaryExpr $it"
        )
    }
}

fun qualifier(literalValue: GoCompositeLit): String {
    return (packageName(literalValue.containingFile) + ':' + literalValue.typeReferenceExpression!!.text).also {
        println(
            "GoCompositeLit $it"
        )
    }
}

fun qualifier(receiver: GoReceiver): String {
    return (packageName(receiver.containingFile) + ':' + typeName(receiver)).also { println("receiver $it") }
}

fun typeName(receiver: GoReceiver): String {
    val type = receiver.type
    return if (type is GoPointerType) {
        type.type!!.text
    } else {
        type!!.text
    }
}

private fun packageName(file: PsiFile): String {
    if (file is GoFile) {
        val packageClause = file.getPackage()
        if (packageClause != null) {
            return packageClause.name!!
        }
    }
    return "" // Handle cases where package name isn't found
}
