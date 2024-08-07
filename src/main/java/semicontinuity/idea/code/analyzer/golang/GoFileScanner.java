package semicontinuity.idea.code.analyzer.golang;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.goide.psi.GoBuiltinCallExpr;
import com.goide.psi.GoCallExpr;
import com.goide.psi.GoCompositeLit;
import com.goide.psi.GoFile;
import com.goide.psi.GoFunctionDeclaration;
import com.goide.psi.GoNamedSignatureOwner;
import com.goide.psi.GoPointerType;
import com.goide.psi.GoReceiver;
import com.goide.psi.GoRecursiveVisitor;
import com.goide.psi.GoRecvStatement;
import com.goide.psi.GoReferenceExpression;
import com.goide.psi.GoShortVarDeclaration;
import com.goide.psi.GoType;
import com.goide.psi.GoTypeSpec;
import com.goide.psi.GoUnaryExpr;
import com.goide.psi.GoVarDefinition;
import com.goide.psi.GoVarSpec;
import com.intellij.pom.PomTargetPsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;
import semicontinuity.idea.code.analyzer.util.Context;

public class GoFileScanner {
    // Could not find a way to check programmatically. Plain function calls are all represented by LeafPsiElement
    private final Set<String> builtins = Set.of(
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
    );

    private final GoFile goFile;
    private final Context context;

    private final Consumer<Node> nodeSink;
    private final BiConsumer<Node, Node> edgeSink;

    public GoFileScanner(
            GoFile goFile,
            Context context,
            Consumer<Node> nodeSink,
            BiConsumer<Node, Node> edgeSink) {
        this.goFile = goFile;
        this.context = context;
        this.edgeSink = edgeSink;
        this.nodeSink = nodeSink;
    }

    public void registerEntities() {
        fillStructMethods();
        fillFunctionNames();
    }

    public void scanCalls() {
        visitMethods();
        visitFunctions();
    }

    private void fillStructMethods() {
        Collection<? extends GoTypeSpec> types = goFile.getTypes();
        for (GoTypeSpec typeSpec : types) {
            var structName = typeSpec.getIdentifier().getText();
            var allMethods = typeSpec.getAllMethods();
            for (GoNamedSignatureOwner method : allMethods) {
                nodeSink.accept(new Node(structName, method.getName(), method));
            }
        }
    }

    private void fillFunctionNames() {
        Collection<? extends GoFunctionDeclaration> functions = goFile.getFunctions();
        for (GoFunctionDeclaration function : functions) {
            if (!function.getName().startsWith("Test")) {
                nodeSink.accept(new Node("", function.getName(), function));
            }
        }
    }

    private void visitFunctions() {
        goFile.getFunctions().forEach(function -> {
            context.log.accept("    Processing calls inside function " + function.getQualifiedName());
            if (!function.getName().startsWith("Test")) {
                function.accept(fillLinksVisitor(new Node("", function.getName(), function)));
            }
            System.out.println();
        });
    }

    private void visitMethods() {
        // receiver.getType().getText()
        // method.getName()
        goFile.getMethods().forEach(
                method -> {
                    context.log.accept("    Processing calls inside method " + method.getQualifiedName());
                    method.accept(
                            fillLinksVisitor(
                                    new Node(
                                            typeName(method.getReceiver()),
                                            method.getName(),
                                            method
                                    )
                            )
                    );
                }
        );
    }

    @NotNull
    private GoRecursiveVisitor fillLinksVisitor(final Node from) {
        return new GoRecursiveVisitor() {
            @Override
            public void visitCallExpr(@NotNull GoCallExpr callExpr) {
                super.visitCallExpr(callExpr);
                context.log.accept("      > GoCallExpr = " + callExpr.getText());
                processCallExpr(callExpr, from);
                context.log.accept("      < GoCallExpr = " + callExpr.getText());
            }

            @Override
            public void visitBuiltinCallExpr(@NotNull GoBuiltinCallExpr expr) {
                context.log.accept("      BuiltinCallExpr = ");
                super.visitBuiltinCallExpr(expr);
                System.out.println("      BuiltinCallExpr = " + expr);
            }
        };
    }

    private void processCallExpr(@NotNull GoCallExpr callExpr, Node from) {
        var expression = callExpr.getExpression();
        if (expression instanceof GoReferenceExpression) {
            var referenceExpression = (GoReferenceExpression) expression;
            var firstChild = referenceExpression.getFirstChild();

            if (firstChild instanceof GoReferenceExpression) {
                var refExpr = ((GoReferenceExpression) firstChild);
                var resolved = refExpr.resolve(ResolveState.initial());
                if (resolved instanceof GoVarDefinition) {
                    var varDefinition = (GoVarDefinition) resolved;
                    var varDefinitionParent = varDefinition.getParent();

                    if (varDefinitionParent instanceof GoShortVarDeclaration) {
                        var shortVarDeclaration = (GoShortVarDeclaration) varDefinitionParent;
                        var literal = shortVarDeclaration.getLastChild();
                        if (literal instanceof GoCompositeLit) {
                            var literalValue = (GoCompositeLit) literal;
                            var structName = literalValue.getTypeReferenceExpression().getText();

                            Node to = new Node(structName, referenceExpression.getIdentifier().getText(), resolved);
                            edgeSink.accept(from, to);
                            context.logEdge(from, to);
                        } else if (literal instanceof GoUnaryExpr) {
                            var unaryExpr = (GoUnaryExpr) literal;
                            var structName = unaryExpr.getExpression().getGoType(ResolveState.initial()).getText();
                            Node to = new Node(structName, referenceExpression.getIdentifier().getText(), resolved);
                            edgeSink.accept(from, to);
                            context.logEdge(from, to);
                        } else if (literal instanceof GoCallExpr) {
                            processCallExpr((GoCallExpr) literal, from);
                        } else if (literal instanceof GoBuiltinCallExpr) {
                            context.log.accept("      GoBuiltinCallExpr = " + literal.getText());
                        } else {
                            context.log.accept("      >> literal is not yet supported: " + literal.getClass().getName());
                        }
                    } else if (varDefinitionParent instanceof GoRecvStatement) {
                        GoRecvStatement recvStatement = (GoRecvStatement) varDefinitionParent;
                        context.log.accept("      GoRecvStatement " + recvStatement.getRecvExpression().getText());
                        context.log.accept("      GoRecvStatement " + recvStatement.getVarAssign());
                    } else if (varDefinitionParent instanceof GoVarSpec) {
                        GoVarSpec varSpec = (GoVarSpec) varDefinitionParent;
//                        context.log.accept("      GoVarSpec " + varSpec.getText());

                        GoType type = varSpec.getType();
//                        context.log.accept("      GoVarSpec type " + type);
                        if (type != null) {
//                            context.log.accept("      GoVarSpec type " + type.getText());
                            Node to = new Node(type.getText(), referenceExpression.getIdentifier().getText(), resolved);
                            context.logEdge(from, to);
                            edgeSink.accept(from, to);
                        } else {
                            context.log.accept("      GoVarSpec: type is null ? " + varSpec.getText());
                        }
//                        context.log.accept("      name " + referenceExpression.getIdentifier().getText());
//                        context.log.accept("      GoVarSpec " + varSpec.getAssign());
//                        context.log.accept("      GoVarSpec " + varSpec.getDefinitionList());
//                        context.log.accept("      GoVarSpec " + varSpec.getVarDefinitionList());
//                        context.log.accept("      GoVarSpec " + varSpec.getExpressionList());
                    } else {
                        context.log.accept("      >> varDefinitionParent is not yet supported: " + varDefinitionParent.getClass().getName());
                    }
                } else if (resolved instanceof GoReceiver) {
                    var receiver = ((GoReceiver) resolved);
                    var methodBody = referenceExpression.getReference().resolve(ResolveState.initial());
                    Node to = new Node(typeName(receiver), referenceExpression.getIdentifier().getText(), methodBody);
                    edgeSink.accept(from, to);
                    context.logEdge(from, to);
                } else if (resolved instanceof PomTargetPsiElement) {
                    PomTargetPsiElement targetPsiElement = (PomTargetPsiElement) resolved;
                    context.log.accept("      targetPsiElement: " + targetPsiElement.getTarget());
                } else if (resolved == null) {
                    context.log.accept("      >> resolved is null");
                } else {
                    context.log.accept("      >> resolved is not yet supported: " + resolved.getClass().getName() + ' ' + resolved.getText());
                }
            } else if (firstChild instanceof GoCallExpr) {
                GoCallExpr goCallExpr = (GoCallExpr) firstChild;
                context.log.accept("      firstChild GoCallExpr");
//                GoExpression expression1 = goCallExpr.getExpression();
                System.out.println("goCallExpr = " + goCallExpr.getText());
                processCallExpr(goCallExpr, from);
            } else if (firstChild instanceof LeafPsiElement) {
//                LeafPsiElement leafPsiElement = (LeafPsiElement) firstChild;

                // plain function call?
                String funcName = firstChild.getText();
                if (!builtins.contains(funcName)) {
                    Node to = new Node("", funcName, firstChild);
                    edgeSink.accept(from, to);
                    context.logEdge(from, to);
                }
//                context.log.accept("ref is " + leafPsiElement.getReference());
//                context.log.accept("ref is " + leafPsiElement.getResolveScope());

//                VirtualFile virtualFile = PsiUtilCore.getVirtualFile(leafPsiElement);
//                context.log.accept("virtualFile is " + virtualFile);
//                context.log.accept("virtualFile is " + virtualFile.getName());
//                context.log.accept("virtualFile is " + virtualFile.exists());
//                context.log.accept("virtualFile is " + virtualFile.getFileType());
            } else {
                context.log.accept("firstChild is " + firstChild.getClass());
                context.log.accept("referenceExpression = " + referenceExpression.getText());
            }
        } else {
            context.log.accept("      expr " + expression.getClass().getName() + ' ' + expression.getText());
        }
    }

    private static String typeName(GoReceiver receiver) {
        var type = receiver.getType();
        if (type instanceof GoPointerType) {
            return ((GoPointerType) type).getType().getText();
        } else {
            return type.getText();
        }
    }
}
