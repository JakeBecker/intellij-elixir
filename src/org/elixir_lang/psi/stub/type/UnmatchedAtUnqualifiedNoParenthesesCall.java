package org.elixir_lang.psi.stub.type;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import org.elixir_lang.psi.ElixirUnmatchedAtUnqualifiedNoParenthesesCall;
import org.elixir_lang.psi.impl.ElixirUnmatchedAtUnqualifiedNoParenthesesCallImpl;
import org.elixir_lang.psi.stub.type.call.Stub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UnmatchedAtUnqualifiedNoParenthesesCall extends Stub<org.elixir_lang.psi.stub.UnmatchedAtUnqualifiedNoParenthesesCall, ElixirUnmatchedAtUnqualifiedNoParenthesesCall> {
    /*
     * Constructors
     */

    public UnmatchedAtUnqualifiedNoParenthesesCall(@NotNull String debugName) {
        super(debugName);
    }

    /*
     * Instance Methods
     */

    @Override
    public ElixirUnmatchedAtUnqualifiedNoParenthesesCall createPsi(@NotNull org.elixir_lang.psi.stub.UnmatchedAtUnqualifiedNoParenthesesCall stub) {
        return new ElixirUnmatchedAtUnqualifiedNoParenthesesCallImpl(stub, this);
    }

    @Override
    public org.elixir_lang.psi.stub.UnmatchedAtUnqualifiedNoParenthesesCall createStub(@NotNull ElixirUnmatchedAtUnqualifiedNoParenthesesCall psi, StubElement parentStub) {
        return new org.elixir_lang.psi.stub.UnmatchedAtUnqualifiedNoParenthesesCall(
                parentStub,
                this,
                psi.resolvedModuleName(),
                psi.resolvedFunctionName(),
                psi.resolvedFinalArity(),
                psi.hasDoBlockOrKeyword(),
                StringUtil.notNullize(psi.getName(), "?"),
                psi.canonicalNameSet()
        );
    }

    @NotNull
    @Override
    public org.elixir_lang.psi.stub.UnmatchedAtUnqualifiedNoParenthesesCall deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new org.elixir_lang.psi.stub.UnmatchedAtUnqualifiedNoParenthesesCall(
                parentStub,
                this,
                dataStream.readName(),
                dataStream.readName(),
                dataStream.readInt(),
                dataStream.readBoolean(),
                dataStream.readName(),
                readNameSet(dataStream)
        );
    }
}
