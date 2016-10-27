package org.elixir_lang.code_insight.completion.provider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.math.IntRange;
import org.elixir_lang.psi.ElixirAccessExpression;
import org.elixir_lang.psi.ElixirDotInfixOperator;
import org.elixir_lang.psi.QualifiableAlias;
import org.elixir_lang.psi.call.Call;
import org.elixir_lang.psi.call.qualification.Qualified;
import org.elixir_lang.psi.stub.type.call.Stub;
import org.elixir_lang.reference.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.elixir_lang.psi.impl.ElixirPsiImplUtil.macroChildCalls;
import static org.elixir_lang.psi.impl.ElixirPsiImplUtil.qualifierToModular;
import static org.elixir_lang.psi.operation.Normalized.operatorIndex;
import static org.elixir_lang.psi.operation.infix.Normalized.leftOperand;
import static org.elixir_lang.structure_view.element.CallDefinitionClause.nameArityRange;

public class CallDefinitionClause extends CompletionProvider<CompletionParameters> {
    /*
     * Private Instance Methods
     */

    @NotNull
    private static Iterable<LookupElement> callDefinitionClauseLookupElements(@NotNull Call scope) {
        Call[] childCalls = macroChildCalls(scope);
        List<LookupElement> lookupElementList = null;

        if (childCalls != null && childCalls.length > 0) {
            for (Call childCall : childCalls) {
                if (org.elixir_lang.structure_view.element.CallDefinitionClause.is(childCall)) {
                    Pair<String, IntRange> nameArityRange = nameArityRange(childCall);

                    if (nameArityRange != null) {
                        String name = nameArityRange.first;

                        if (name != null) {
                            if (lookupElementList == null) {
                                lookupElementList = new ArrayList<LookupElement>();
                            }

                            lookupElementList.add(
                                    org.elixir_lang.code_insight.lookup.element.CallDefinitionClause.createWithSmartPointer(
                                            nameArityRange.first,
                                            childCall
                                    )
                            );
                        }
                    }
                }
            }
        }

        if (lookupElementList == null) {
            lookupElementList = Collections.emptyList();
        }

        return lookupElementList;
    }

    @NotNull
    private static Iterable<LookupElement> callDefinitionClauseLookupElements(@NotNull PsiElement scope) {
        Iterable<LookupElement> lookupElementIterable;

        if (scope instanceof Call) {
            lookupElementIterable = callDefinitionClauseLookupElements((Call) scope);
        } else {
            lookupElementIterable = Collections.emptyList();
        }

        return lookupElementIterable;
    }

    /*
     * Protected Instance Methods
     */

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext context,
                                  @NotNull CompletionResultSet resultSet) {
        PsiElement originalPosition = parameters.getOriginalPosition();
        PsiElement originalParent = null;

        if (originalPosition != null) {
            originalParent = originalPosition.getParent();

            if (originalParent != null) {
                PsiElement grandParent = originalParent.getParent();

                if (grandParent instanceof org.elixir_lang.psi.qualification.Qualified) {
                    org.elixir_lang.psi.qualification.Qualified qualifiedGrandParent = (org.elixir_lang.psi.qualification.Qualified) grandParent;
                    PsiElement qualifier = qualifiedGrandParent.qualifier();

                    Call modular = qualifierToModular(qualifier);

                    if (modular != null) {
                        resultSet.withPrefixMatcher("").addAllElements(
                                callDefinitionClauseLookupElements(modular)
                        );
                    }
                }
            }
        }
    }
}
