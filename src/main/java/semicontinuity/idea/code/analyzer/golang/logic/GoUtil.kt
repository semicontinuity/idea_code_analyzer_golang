package semicontinuity.idea.code.analyzer.golang.logic

import com.goide.GoFileType
import com.goide.psi.GoFile
import com.goide.psi.GoInterfaceType
import com.goide.psi.GoMethodDeclaration
import com.goide.psi.GoMethodSpec
import com.goide.psi.GoNamedSignatureOwner
import com.goide.psi.GoSignature
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
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
