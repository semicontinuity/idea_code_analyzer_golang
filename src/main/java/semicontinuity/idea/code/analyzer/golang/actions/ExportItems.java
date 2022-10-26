package semicontinuity.idea.code.analyzer.golang.actions;

import java.util.Collection;

import com.goide.psi.GoCallExpr;
import com.goide.psi.GoCompositeLit;
import com.goide.psi.GoFile;
import com.goide.psi.GoFunctionDeclaration;
import com.goide.psi.GoMethodDeclaration;
import com.goide.psi.GoRecursiveVisitor;
import com.goide.psi.GoReferenceExpression;
import com.goide.psi.GoShortVarDeclaration;
import com.goide.psi.GoTypeSpec;
import com.goide.psi.GoVarDefinition;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.ResolveState;
import org.jetbrains.annotations.NotNull;


public class ExportItems extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        var goFile = (GoFile) anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (goFile == null) {
            return;
        }
        var visitor = new GoRecursiveVisitor() {
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
                                System.out.println("literalValue.getTypeReferenceExpression().getText() = " + literalValue.getTypeReferenceExpression().getText());
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

        Collection<? extends GoTypeSpec> types = goFile.getTypes();
        for (GoTypeSpec typeSpec : types) {
            System.out.println("typeSpec.getText() = " + typeSpec.getText());
        }

        visitMethods(goFile, visitor);

        System.out.println();

        visitFunctions(goFile, visitor);
    }

    private static void visitFunctions(GoFile goFile, GoRecursiveVisitor visitor) {
        Collection<? extends GoFunctionDeclaration> functions = goFile.getFunctions();
        for (GoFunctionDeclaration function : functions) {
            System.out.println("function = " + function.getName());

            System.out.println("CALLS:");
            function.accept(visitor);
        }
    }

    private static void visitMethods(GoFile goFile, GoRecursiveVisitor visitor) {
        for (GoMethodDeclaration method : goFile.getMethods()) {
            var receiver = method.getReceiver();
            System.out.println("=================");
            System.out.println("receiver = " + receiver);
            if (receiver != null) {
                System.out.println(receiver.getType().getText() + '.' + method.getName());
            }

            System.out.println("CALLS:");
            method.accept(visitor);
        }
    }
}
