package semicontinuity.idea.code.analyzer.golang;

import com.intellij.psi.PsiElement;

public class Member {
    private final String qualifier;
    private final String name;
    private final PsiElement psiElement;

    public Member(String qualifier, String name, PsiElement psiElement) {
        this.qualifier = qualifier;
        this.name = name;
        this.psiElement = psiElement;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getName() {
        return name;
    }

    public PsiElement getPsiElement() {
        return psiElement;
    }

    @Override
    public String toString() {
        return qualifier.isEmpty() ? name : qualifier + '.' + name;
    }

    public QualifiedName toQualifiedName() {
        return new QualifiedName(qualifier, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Member that = (Member) o;

        if (!qualifier.equals(that.qualifier)) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = qualifier.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
