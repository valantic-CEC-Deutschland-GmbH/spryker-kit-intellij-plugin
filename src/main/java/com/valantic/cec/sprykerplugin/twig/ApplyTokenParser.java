package com.valantic.cec.sprykerplugin.twig;

import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;

import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

public class ApplyTokenParser implements TokenParser {
    @Override
    public String getTag() {
        return "apply";
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) {
        TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip the "set" token
        stream.next();

        // use the built in expression parser to parse the variable name
        Expression<?> filterName = parser.getExpressionParser().parseExpression();
        // TODO implement applyTag
//        parser.getExpressionParser().parseFilterInvocationExpression();
//        stream.expect(Token.Type.PUNCTUATION, "=");
//
//        // use the built in expression parser to parse the variable value
//        Expression<?> value = parser.getExpressionParser().parseExpression();
//
//        // expect to see "%}"
//        stream.expect(Token.Type.EXECUTE_END);
//
//        // NodeSet is composed of a name and a value
//        return new SetNode(lineNumber, name, value);
        return null;
    }
}
