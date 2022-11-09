package me.tapeline.quailj.runtime.std;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import me.tapeline.quailj.utils.ErrorFormatter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FuncExec extends QBuiltinFunc {

    public FuncExec(Runtime runtime) {
        super(
                "exec",
                Arrays.asList(
                        new FuncArgument(
                                "code",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        String code = args.get("code").toString();
        QObject result = null;
        try {
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.scan();
            Parser parser = new Parser(code, tokens);
            Node root = parser.parse();
            Runtime inner = new Runtime(code, root, runtime.io, false);
            result = inner.run(root, inner.memory);
        } catch (RuntimeStriker striker) {
            if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                System.err.println(ErrorFormatter.formatError(code, striker.error));
                System.exit(1);
            } else if (striker.type == RuntimeStriker.Type.RETURN) {
                result = striker.returnValue;
            }
        } catch (ParserException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
        return QObject.nullSafe(result);
    }

}
