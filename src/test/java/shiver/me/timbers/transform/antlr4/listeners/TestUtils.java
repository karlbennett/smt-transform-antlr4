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
    public static final int TEST_TOKEN_TYPE_ONE = 0;
    public static final Token TEST_TOKEN_ONE = mockToken(TEST_TOKEN_TYPE_ONE, TEST_TOKEN_NAME_ONE);

    public static final String TEST_TOKEN_NAME_TWO = "test token two";
    public static final int TEST_TOKEN_TYPE_TWO = 1;
    public static final Token TEST_TOKEN_TWO = mockToken(TEST_TOKEN_TYPE_TWO, TEST_TOKEN_NAME_TWO);

    public static final String TEST_RULE_NAME_ONE = "test rule one";
    public static final int TEST_RULE_TYPE_ONE = 0;
    public static final ParserRuleContext TEST_RULE_CONTEXT_ONE = mockParserRuleContextWithDefaultToken();

    public static final String TEST_RULE_NAME_TWO = "test rule two";
    public static final int TEST_RULE_TYPE_TWO = 1;
    public static final ParserRuleContext TEST_RULE_CONTEXT_TWO = mockParserRuleContext(TEST_RULE_TYPE_TWO,
            TEST_TOKEN_TWO);

    public static final ErrorNode TEST_ERROR_NODE_ONE = mockErrorNodeWithDefaultToken();
    public static final ErrorNode TEST_ERROR_NODE_TWO = mockErrorNode(TEST_RULE_CONTEXT_TWO, TEST_TOKEN_TWO);

    public static final TerminalNode TEST_TERMINAL_NODE_ONE = TEST_ERROR_NODE_ONE;
    public static final TerminalNode TEST_TERMINAL_NODE_TWO = TEST_ERROR_NODE_TWO;


    public static Token mockToken(int type, String name) {

        final Token token = mock(Token.class);
        when(token.getStartIndex()).thenReturn(2);
        when(token.getStopIndex()).thenReturn(5);
        when(token.getText()).thenReturn(name);
        when(token.getType()).thenReturn(type);
        when(token.toString()).thenReturn(name);

        return token;
    }

    public static Recognizer mockRecognizer() {

        final Recognizer recognizer = mock(Recognizer.class);
        when(recognizer.getTokenNames()).thenReturn(new String[]{TEST_TOKEN_NAME_ONE, TEST_TOKEN_NAME_TWO});
        when(recognizer.getRuleNames()).thenReturn(new String[]{TEST_RULE_NAME_ONE, TEST_RULE_NAME_TWO});

        return recognizer;
    }

    public static TerminalNode mockTerminalNode(RuleContext context, Token token) {

        return mockErrorNode(context, token);
    }

    public static ErrorNode mockErrorNodeWithDefaultToken() {

        return mockErrorNode(TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
    }

    public static ErrorNode mockErrorNode(RuleContext context, Token token) {

        final ParseTree parseTree = mockParseTree(context);

        final ErrorNode terminalNode = mock(ErrorNode.class);
        when(terminalNode.getParent()).thenReturn(parseTree);
        when(terminalNode.getSymbol()).thenReturn(token);

        return terminalNode;
    }

    public static ParseTree mockParseTree(RuleContext context) {

        final ParseTree parseTree = mock(ParseTree.class);
        when(parseTree.getPayload()).thenReturn(context);

        return parseTree;
    }

    public static ParserRuleContext mockParserRuleContextWithDefaultToken() {

        return mockParserRuleContext(TEST_RULE_TYPE_ONE, TEST_TOKEN_ONE);
    }

    public static ParserRuleContext mockParserRuleContext(int ruleIndex, Token token) {

        final ParserRuleContext context = mock(ParserRuleContext.class);
        when(context.getRuleIndex()).thenReturn(ruleIndex);
        when(context.getStart()).thenReturn(token);

        return context;
    }
}
