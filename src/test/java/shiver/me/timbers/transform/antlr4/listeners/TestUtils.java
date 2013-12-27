package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class TestUtils {

    private TestUtils() {
    }

    public static final String TEST_TOKEN_NAME_ONE = "test token one";
    public static final String TEST_TOKEN_NAME_TWO = "test token two";
    public static final int TEST_TOKEN_TYPE_ONE = 0;
    public static final int TEST_TOKEN_TYPE_TWO = 1;

    public static final String TEST_RULE_NAME_ONE = "test rule one";
    public static final String TEST_RULE_NAME_TWO = "test rule two";
    public static final int TEST_RULE_TYPE_TWO = 1;

    public static Recognizer mockRecognizer() {

        final Recognizer recognizer = mock(Recognizer.class);
        when(recognizer.getTokenNames()).thenReturn(new String[]{TEST_TOKEN_NAME_ONE, TEST_TOKEN_NAME_TWO});
        when(recognizer.getRuleNames()).thenReturn(new String[]{TEST_RULE_NAME_ONE, TEST_RULE_NAME_TWO});

        return recognizer;
    }

    public static TerminalNode mockTerminalNodeWithDefaultToken() {

        return mockErrorNode(TEST_TOKEN_NAME_ONE, TEST_TOKEN_TYPE_ONE);
    }

    public static TerminalNode mockTerminalNode(String tokenName, int tokenType) {

        return mockErrorNode(tokenName, tokenType);
    }

    public static ParseTree mockParseTree() {

        final RuleContext context = mock(RuleContext.class);

        final ParseTree parseTree = mock(ParseTree.class);
        when(parseTree.getPayload()).thenReturn(context);

        return parseTree;
    }

    public static Token mockToken(String tokenString, int tokenType) {

        final Token token = mock(Token.class);
        when(token.getStartIndex()).thenReturn(2);
        when(token.getStopIndex()).thenReturn(5);
        when(token.getText()).thenReturn(tokenString);
        when(token.getType()).thenReturn(tokenType);
        when(token.toString()).thenReturn(tokenString);

        return token;
    }

    public static ErrorNode mockErrorNodeWithDefaultToken() {

        final ParseTree parseTree = mockParseTree();

        final Token token = mockToken(TEST_TOKEN_NAME_ONE, TEST_TOKEN_TYPE_ONE);

        final ErrorNode terminalNode = mock(ErrorNode.class);
        when(terminalNode.getParent()).thenReturn(parseTree);
        when(terminalNode.getSymbol()).thenReturn(token);

        return terminalNode;
    }

    public static ErrorNode mockErrorNode(String tokenName, int tokenType) {

        final ParseTree parseTree = mockParseTree();

        final Token token = mockToken(tokenName, tokenType);

        final ErrorNode terminalNode = mock(ErrorNode.class);
        when(terminalNode.getParent()).thenReturn(parseTree);
        when(terminalNode.getSymbol()).thenReturn(token);

        return terminalNode;
    }

    public static ParserRuleContext mockParserRuleContextWithDefaultToken() {

        return mockParserRuleContext(TEST_TOKEN_NAME_ONE, TEST_TOKEN_TYPE_ONE);
    }

    public static ParserRuleContext mockParserRuleContext(String tokenName, int tokenType) {

        final Token token = mockToken(tokenName, tokenType);

        final ParserRuleContext context = mock(ParserRuleContext.class);
        when(context.getStart()).thenReturn(token);

        return context;
    }

    public static ParserRuleContext mockParserRuleContext(int rule, String tokenName, int tokenType) {

        final Token token = mockToken(tokenName, tokenType);

        final ParserRuleContext context = mock(ParserRuleContext.class);
        when(context.getRuleIndex()).thenReturn(rule);
        when(context.getStart()).thenReturn(token);

        return context;
    }
}
