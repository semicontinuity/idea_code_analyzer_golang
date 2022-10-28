package semicontinuity.idea.code.analyzer.golang.actions;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiConsumer;

import com.goide.psi.GoCallExpr;
import com.goide.psi.GoCompositeLit;
import com.goide.psi.GoFile;
import com.goide.psi.GoFunctionDeclaration;
import com.goide.psi.GoNamedSignatureOwner;
import com.goide.psi.GoPointerType;
import com.goide.psi.GoReceiver;
import com.goide.psi.GoRecursiveVisitor;
import com.goide.psi.GoReferenceExpression;
import com.goide.psi.GoShortVarDeclaration;
import com.goide.psi.GoTypeSpec;
import com.goide.psi.GoUnaryExpr;
import com.goide.psi.GoVarDefinition;
import com.intellij.psi.ResolveState;
import org.jetbrains.annotations.NotNull;

public class GoFileScanner {
    private final GoFile goFile;
    private final Structure structure;
    private final BiConsumer<QualifiedName, QualifiedName> callsFiller;

    public GoFileScanner(GoFile goFile, Structure structure) {
        this.goFile = goFile;
        this.structure = structure;
        this.callsFiller = (from, to) -> {
            if (structure.contains(from) && structure.contains(to)) {
                System.out.println("Adding call " + from + "->" + to);
                structure.calls.computeIfAbsent(from, (k) -> new HashSet<>()).add(to);
            } else {
                System.out.println("Out of scope: " + from + " or " + to);
            }
        };
    }

    void registerEntities() {
        fillStructMethods();
        fillFunctionNames();
    }

    void scanCalls() {
        visitMethods();
        visitFunctions();
    }

    private void fillStructMethods() {
        Collection<? extends GoTypeSpec> types = goFile.getTypes();
        for (GoTypeSpec typeSpec : types) {
            var structName = typeSpec.getIdentifier().getText();
            var allMethods = typeSpec.getAllMethods();
            for (GoNamedSignatureOwner method : allMethods) {
                System.out.println("Adding method = " + structName + "." + method.getName());
                structure.structMethods
                        .computeIfAbsent(structName, (k) -> new HashSet<>())
                        .add(method.getName());
            }
        }
    }

    private void fillFunctionNames() {
        Collection<? extends GoFunctionDeclaration> functions = goFile.getFunctions();
        for (GoFunctionDeclaration function : functions) {
            System.out.println("Adding function = " + function.getName());
            structure.functionNames.add(function.getName());
        }
    }

    private void visitFunctions() {
        goFile.getFunctions().forEach(function -> {
            System.out.println("Processing calls inside function " + function.getQualifiedName());
            function.accept(fillLinksVisitor(new QualifiedName("", function.getName()), callsFiller));
        });
    }

    private void visitMethods() {
        // receiver.getType().getText()
        // method.getName()
        goFile.getMethods().forEach(
                method -> {
                    System.out.println("Processing calls inside method " + method.getQualifiedName());
                    method.accept(
                            fillLinksVisitor(
                                    new QualifiedName(
                                            typeName(method.getReceiver()),
                                            method.getName()
                                    ),
                                    callsFiller
                            )
                    );
                }
        );
    }

    @NotNull
    private static GoRecursiveVisitor fillLinksVisitor(final QualifiedName from,
                                                       final BiConsumer<QualifiedName, QualifiedName> sink) {
        return new GoRecursiveVisitor() {
            @Override
            public void visitCallExpr(@NotNull GoCallExpr callExpr) {
                super.visitCallExpr(callExpr);

                var expression = callExpr.getExpression();
//                System.out.println("expression.getText() = " + expression.getText());
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
                                    sink.accept(from, new QualifiedName(structName, referenceExpression.getIdentifier().getText()));
                                } else if (literal instanceof GoUnaryExpr) {
                                    var unaryExpr = (GoUnaryExpr) literal;
                                    var structName = unaryExpr.getExpression().getGoType(ResolveState.initial()).getText();
                                    sink.accept(from, new QualifiedName(structName, referenceExpression.getIdentifier().getText()));
                                }
                            }
                        } else if (resolved instanceof GoReceiver) {
                            var receiver = ((GoReceiver) resolved);
                            sink.accept(from, new QualifiedName(typeName(receiver),
                                    referenceExpression.getIdentifier().getText()));
                        }
                    } else if (firstChild instanceof GoCallExpr) {
                        System.out.println("GoCallExpr");
                    }
                } else {
//                    System.out.println("expression.getText() = " + expression.getText());
                }
            }
        };
    }

    private static String typeName(GoReceiver receiver) {
        var type = receiver.getType();
        if (type instanceof GoPointerType) {
            return ((GoPointerType) type).getType().getText();
        } else {
            return type.getText();
        }
    }

    @NotNull
    private static GoRecursiveVisitor fillLinksVisitor0(final QualifiedName from,
                                                        final BiConsumer<QualifiedName, QualifiedName> sink) {
        return new GoRecursiveVisitor() {
            @Override
            public void visitCallExpr(@NotNull GoCallExpr callExpr) {
                super.visitCallExpr(callExpr);

                System.out.println(callExpr.getText());
                var expression = callExpr.getExpression();
                System.out.println("expression.getText() = " + expression.getText());
                System.out.println("expression.getValue() = " + expression.getValue());
                if (expression instanceof GoReferenceExpression) {
                    var referenceExpression = (GoReferenceExpression) expression;

                    System.out.println("referenceExpression.getText() = " + referenceExpression.getText());

                    var qualifier = referenceExpression.getQualifier();
                    System.out.println("referenceExpression.getQualifier().getText() = " + qualifier.getText());
                    System.out.println("referenceExpression.getRawQualifier().getText() = " + referenceExpression.getRawQualifier().getText());
                    System.out.println("referenceExpression.getQualifier().getReference().resolve().getText() = " + qualifier.getReference().resolve().getText());

                    var firstChild = referenceExpression.getFirstChild();
                    System.out.println("referenceExpression.getFirstChild().getClass() = " + firstChild.getClass());

                    var firstChild1 = ((GoReferenceExpression) firstChild);
                    var element = firstChild1.resolve();
                    System.out.println("element.getText() = " + element.getText());

                    System.out.println("element = " + element.getOriginalElement().getText());
                    System.out.println("element = " + element.getNavigationElement().getText());

                    var resolved = firstChild1.resolve(ResolveState.initial());
                    System.out.println("resolved.getOriginalElement().getText() = " + resolved.getOriginalElement().getText());
                    System.out.println("resolved = " + resolved.getText());

                    if (resolved instanceof GoVarDefinition) {
                        var varDefinition = (GoVarDefinition) resolved;
                        var varDefinitionParent = varDefinition.getParent();

                        if (varDefinitionParent instanceof GoShortVarDeclaration) {
                            var shortVarDeclaration = (GoShortVarDeclaration) varDefinitionParent;
                            var literal = shortVarDeclaration.getLastChild();
                            if (literal instanceof GoCompositeLit) {
                                var literalValue = (GoCompositeLit) literal;
                                var structName = literalValue.getTypeReferenceExpression().getText();


                            }
                        }
//                        var reference = varDefinition.getReference();
//                        System.out.println("varDefinition.getReference() = " + reference.getClass());
//                        if (reference instanceof GoVarReference) {
//                            var goVarReference = (GoVarReference) reference;
//                            System.out.println("goVarReference = " + goVarReference.getCanonicalText());
//                        }
                    }

//                    var reference = resolved.getReference();
//                    if (reference != null) {
//                        System.out.println("reference.getElement().getText() = " + reference.getElement().getText());
//                    }
                    System.out.println("referenceExpression.getIdentifier().getText() = " + referenceExpression.getIdentifier().getText());
                    System.out.println("referenceExpression.getIdentifier().getReference() = " + referenceExpression.getIdentifier().getReference());
                    System.out.println("referenceExpression.getReference().getCanonicalText() = " + referenceExpression.getReference().getCanonicalText());
                    System.out.println("referenceExpression.getReference = " + referenceExpression.getReference().getValue());
                }
//                System.out.println(expression.getText());
//                System.out.println(expression.getGoType(ResolveState.initial()).getText());
//                System.out.println(expression.getGoUnderlyingType(ResolveState.initial()).getText());
//                System.out.println(expression.getValue());
            }
        };
    }
}
