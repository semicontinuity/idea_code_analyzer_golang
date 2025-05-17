package semicontinuity.idea.code.analyzer.golang

import com.goide.psi.GoBuiltinCallExpr
import com.goide.psi.GoCallExpr
import com.goide.psi.GoCompositeLit
import com.goide.psi.GoFile
import com.goide.psi.GoFunctionDeclaration
import com.goide.psi.GoInterfaceType
import com.goide.psi.GoMethodDeclaration
import com.goide.psi.GoPointerType
import com.goide.psi.GoReceiver
import com.goide.psi.GoRecursiveVisitor
import com.goide.psi.GoRecvStatement
import com.goide.psi.GoReferenceExpression
import com.goide.psi.GoShortVarDeclaration
import com.goide.psi.GoSpecType
import com.goide.psi.GoStructType
import com.goide.psi.GoTypeDeclaration
import com.goide.psi.GoTypeReferenceExpression
import com.goide.psi.GoUnaryExpr
import com.goide.psi.GoVarDefinition
import com.goide.psi.GoVarSpec
import com.intellij.pom.PomTargetPsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.tree.LeafPsiElement
import semicontinuity.idea.code.analyzer.util.Context
import java.util.function.BiConsumer
import java.util.function.Consumer

class GoFileScanner(
    private val goFile: GoFile,
    private val context: Context,
    private val vertexSink: Consumer<Member>,
    private val edgeSink: BiConsumer<Member, Member>
) {
    // Could not find a way to check programmatically. Plain function calls are all represented by LeafPsiElement
    private val builtins = setOf(
        "append",
        "copy",
        "delete",
        "len",
        "cap",
        "make",
        "max",
        "min",
        "new",
        "complex",
        "real",
        "imag",
        "clear",
        "close",
        "panic",
        "recover",
        "print",
        "println",
        "uint8",
        "uint16",
        "uint32",
        "uint64",
        "int8",
        "int16",
        "int32",
        "int64",
        "float32",
        "float64",
        "complex64",
        "complex128",
        "string",
        "int",
        "uint",
        "uintptr",
        "byte",
        "rune",
        "any",
        "comparable"
    )

    fun registerEntities() {
        fillStructMethods()
        fillFunctionNames()
    }

    fun scanCalls() {
        visitMethods()
        visitFunctions()
    }

    private fun fillStructMethods() {
        val types = goFile.types
        for (typeSpec in types) {
            val structName = typeSpec.identifier.text
            if (structName.endsWith("TestSuite")) continue

            vertexSink.accept(Member(structName, "", typeSpec))
            val allMethods = typeSpec.allMethods
            for (method in allMethods) {
                vertexSink.accept(Member(structName, method.name!!, method))
            }
        }
    }

    private fun fillFunctionNames() {
        val functions = goFile.functions
        for (function in functions) {
            if (!function.name!!.startsWith("Test")) {
                vertexSink.accept(Member("", function.name!!, function))
            }
        }
    }

    private fun visitFunctions() {
        goFile.functions.forEach { function: GoFunctionDeclaration? ->
            context.log.accept("    Processing calls inside function " + function!!.qualifiedName)
            if (!function.name!!.startsWith("Test")) {
                function.accept(
                    fillLinksVisitor(
                        Member(
                            "",
                            function.name!!, function
                        )
                    )
                )
            }
            println()
        }
    }

    private fun visitMethods() {
        // receiver.getType().getText()
        // method.getName()
        goFile.methods.forEach(
            Consumer<GoMethodDeclaration> { method: GoMethodDeclaration ->
                context.log.accept("    Processing calls inside method " + method.qualifiedName)
                val qualifier = typeName(method.receiver!!)
                if (!qualifier.endsWith("TestSuite")) {
                    method.accept(
                        fillLinksVisitor(
                            Member(
                                qualifier,
                                method.name!!,
                                method
                            )
                        )
                    )
                }
            }
        )
    }

    private fun fillLinksVisitor(from: Member): GoRecursiveVisitor {
        return object : GoRecursiveVisitor() {
            override fun visitCallExpr(callExpr: GoCallExpr) {
                super.visitCallExpr(callExpr)
                context.log.accept("      > GoCallExpr = " + callExpr.text)
                processCallExpr(callExpr, from)
                context.log.accept("      < GoCallExpr = " + callExpr.text)
            }

            override fun visitBuiltinCallExpr(expr: GoBuiltinCallExpr) {
                context.log.accept("      BuiltinCallExpr = ")
                super.visitBuiltinCallExpr(expr)
                println("      BuiltinCallExpr = $expr")
            }

            override fun visitTypeReferenceExpression(o: GoTypeReferenceExpression) {
                super.visitTypeReferenceExpression(o)
                processTypeReferenceExpression(o, from)
            }

            override fun visitInterfaceType(o: GoInterfaceType) {
                super.visitInterfaceType(o)
            }

            override fun visitStructType(o: GoStructType) {
                super.visitStructType(o)
            }

            override fun visitSpecType(o: GoSpecType) {
                super.visitSpecType(o)
            }

            override fun visitTypeDeclaration(o: GoTypeDeclaration) {
                super.visitTypeDeclaration(o)
            }
        }
    }

    private fun processTypeReferenceExpression(expr: GoTypeReferenceExpression, from: Member) {
        val structName = expr.text
        val goType = expr.resolveType(null)
        println("goType = $goType")
        if (goType != null) {
            val containingFile = goType.containingFile
            if (containingFile is GoFile) {
                val packageName = containingFile.packageName
                if (packageName == goFile.packageName) {
                    println("Referring type from the same package")
                    if (from.qualifier != structName) {
                        edgeSink.accept(from, Member(structName, "", goType))
                    }
                }
            }
        }
    }

    private fun processCallExpr(callExpr: GoCallExpr, from: Member) {
        val expression = callExpr.expression
        if (expression is GoReferenceExpression) {
            val referenceExpression = expression
            val firstChild = referenceExpression.firstChild

            if (firstChild is GoReferenceExpression) {
                val resolved = firstChild.resolve(ResolveState.initial())
                if (resolved is GoVarDefinition) {
                    val varDefinitionParent = resolved.parent

                    if (varDefinitionParent is GoShortVarDeclaration) {
                        val literal = varDefinitionParent.lastChild
                        if (literal is GoCompositeLit) {
                            val structName = literal.typeReferenceExpression!!.text

                            val to = Member(structName, referenceExpression.identifier.text, resolved)
                            edgeSink.accept(from, to)
                            context.logEdge(from, to)
                        } else if (literal is GoUnaryExpr) {
                            val structName = literal.expression!!
                                .getGoType(ResolveState.initial())!!
                                .text
                            val to = Member(structName, referenceExpression.identifier.text, resolved)
                            edgeSink.accept(from, to)
                            context.logEdge(from, to)
                        } else if (literal is GoCallExpr) {
                            processCallExpr(literal, from)
                        } else if (literal is GoBuiltinCallExpr) {
                            context.log.accept("      GoBuiltinCallExpr = " + literal.getText())
                        } else {
                            context.log.accept("      >> literal is not yet supported: " + literal.javaClass.name)
                        }
                    } else if (varDefinitionParent is GoRecvStatement) {
                        val recvStatement = varDefinitionParent
                        context.log.accept("      GoRecvStatement " + recvStatement.recvExpression!!.text)
                        context.log.accept("      GoRecvStatement " + recvStatement.varAssign)
                    } else if (varDefinitionParent is GoVarSpec) {
                        val varSpec = varDefinitionParent

                        //                        context.log.accept("      GoVarSpec " + varSpec.getText());
                        val type = varSpec.type
                        //                        context.log.accept("      GoVarSpec type " + type);
                        if (type != null) {
//                            context.log.accept("      GoVarSpec type " + type.getText());
                            val to = Member(type.text, referenceExpression.identifier.text, resolved)
                            context.logEdge(from, to)
                            edgeSink.accept(from, to)
                        } else {
                            context.log.accept("      GoVarSpec: type is null ? " + varSpec.text)
                        }
                        //                        context.log.accept("      name " + referenceExpression.getIdentifier().getText());
//                        context.log.accept("      GoVarSpec " + varSpec.getAssign());
//                        context.log.accept("      GoVarSpec " + varSpec.getDefinitionList());
//                        context.log.accept("      GoVarSpec " + varSpec.getVarDefinitionList());
//                        context.log.accept("      GoVarSpec " + varSpec.getExpressionList());
                    } else {
                        context.log.accept("      >> varDefinitionParent is not yet supported: " + varDefinitionParent.javaClass.name)
                    }
                } else if (resolved is GoReceiver) {
                    val methodBody = referenceExpression.reference.resolve(ResolveState.initial())
                    val to = Member(
                        typeName(resolved), referenceExpression.identifier.text,
                        methodBody!!
                    )
                    edgeSink.accept(from, to)
                    context.logEdge(from, to)
                } else if (resolved is PomTargetPsiElement) {
                    context.log.accept("      targetPsiElement: " + resolved.target)
                } else if (resolved == null) {
                    context.log.accept("      >> resolved is null")
                } else {
                    context.log.accept("      >> resolved is not yet supported: " + resolved.javaClass.name + ' ' + resolved.text)
                }
            } else if (firstChild is GoCallExpr) {
                val goCallExpr = firstChild
                context.log.accept("      firstChild GoCallExpr")
                //                GoExpression expression1 = goCallExpr.getExpression();
                println("goCallExpr = " + goCallExpr.text)
                processCallExpr(goCallExpr, from)
            } else if (firstChild is LeafPsiElement) {
//                LeafPsiElement leafPsiElement = (LeafPsiElement) firstChild;

                // plain function call?

                val funcName = firstChild.text
                if (!builtins.contains(funcName)) {
                    val to = Member("", funcName, firstChild)
                    edgeSink.accept(from, to)
                    context.logEdge(from, to)
                }

                //                context.log.accept("ref is " + leafPsiElement.getReference());
//                context.log.accept("ref is " + leafPsiElement.getResolveScope());

//                VirtualFile virtualFile = PsiUtilCore.getVirtualFile(leafPsiElement);
//                context.log.accept("virtualFile is " + virtualFile);
//                context.log.accept("virtualFile is " + virtualFile.getName());
//                context.log.accept("virtualFile is " + virtualFile.exists());
//                context.log.accept("virtualFile is " + virtualFile.getFileType());
            } else {
                context.log.accept("firstChild is " + firstChild.javaClass)
                context.log.accept("referenceExpression = " + referenceExpression.text)
            }
        } else {
            context.log.accept("      expr " + expression.javaClass.name + ' ' + expression.text)
        }
    }

    companion object {
        private fun typeName(receiver: GoReceiver): String {
            val type = receiver.type
            return if (type is GoPointerType) {
                type.type!!.text
            } else {
                type!!.text
            }
        }
    }
}
