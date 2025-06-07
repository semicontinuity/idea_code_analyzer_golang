package semicontinuity.idea.code.analyzer.golang.logic

import com.goide.GoFileType
import com.goide.psi.GoFile
import com.goide.psi.GoInterfaceType
import com.goide.psi.GoMethodDeclaration
import com.goide.psi.GoMethodSpec
import com.goide.psi.GoNamedSignatureOwner
import com.goide.psi.GoPointerType
import com.goide.psi.GoReferenceExpression
import com.goide.psi.GoSignature
import com.goide.psi.GoStructType
import com.goide.psi.GoType
import com.goide.psi.GoTypeReferenceExpression
import com.goide.psi.GoTypeSpec
import com.goide.psi.impl.GoTypeImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil

fun findGoFilesWithPackage(project: Project, packageName: String): List<GoFile> {
    val scope = GlobalSearchScope.projectScope(project)
    val psiManager = PsiManager.getInstance(project)
    val goFiles = mutableListOf<GoFile>()

    FileTypeIndex.getFiles(GoFileType.INSTANCE, scope).forEach { virtualFile ->
        val psiFile = psiManager.findFile(virtualFile)
        if (psiFile is GoFile) {
            if (psiFile.packageName == packageName) {
                goFiles.add(psiFile)
            }
        }
    }

    return goFiles
}

fun findGoFiles(psiDirectory: PsiDirectory): List<GoFile> = psiDirectory.files.filterIsInstance<GoFile>()

fun goInterfaceTypes(goFiles: List<GoFile>): List<GoInterfaceType> {
    val intefaces: List<GoInterfaceType> = goFiles.flatMap { file ->
        PsiTreeUtil.collectElementsOfType(file, GoInterfaceType::class.java)
            .toList()
    }
    return intefaces
}

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

fun findStructFromMethodDeclaration(methodDeclaration: GoMethodDeclaration): PsiElement? {
    // Get the receiver from the method
    val receiver = methodDeclaration.receiver ?: return null

    // Get the type from the receiver
    var receiverType: GoType? = receiver.type ?: return null

    // Handle pointer types
    if (receiverType is GoPointerType) {
        receiverType = receiverType.type
    }

    // If it's a type reference expression
    when (receiverType) {
        is GoTypeReferenceExpression -> {
            val resolve = receiverType.resolve()

            // If the resolution is a type spec, that's what we want
            if (resolve is GoTypeSpec) {
                val type = resolve.specType.type
                if (type is GoStructType) {
                    return resolve // Return the type spec that defines the struct
                }
            }
        }
        is GoTypeImpl -> {
            // For GoTypeImpl, try to extract the reference or identifier

            // Option 1: Check if there's a reference inside the type text
            val typeText = receiverType.text

            // Try to find any references within the type
            val references = PsiTreeUtil.findChildrenOfType(receiverType, GoReferenceExpression::class.java)
            for (reference in references) {
                val resolved = reference.resolve()
                if (resolved is GoTypeSpec) {
                    val type = resolved.specType.type
                    if (type is GoStructType) {
                        return resolved
                    }
                }
            }

            // Option 2: Look for a direct struct type
            val directStruct = PsiTreeUtil.getChildOfType(receiverType, GoStructType::class.java)
            if (directStruct != null) {
                return directStruct
            }

            // Option 3: Search by name in the current file
            if (!typeText.isNullOrEmpty()) {
                // Get the containing file
                val file = methodDeclaration.containingFile
                if (file != null) {
                    // Look for type specs with matching name
                    val typeSpecs = PsiTreeUtil.findChildrenOfType(file, GoTypeSpec::class.java)
                        .filter { it.name == typeText }

                    // If found, check if it's a struct
                    for (typeSpec in typeSpecs) {
                        val type = typeSpec.specType.type
                        if (type is GoStructType) {
                            return typeSpec
                        }
                    }
                }
            }
        }
    }

    return null
}
