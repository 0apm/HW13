import org.jparsec.*;
import org.jparsec.pattern.Patterns;

import javax.swing.plaf.nimbus.State;

import static org.jparsec.Scanners.isChar;

/**
 * Created by rm on 12.12.16.
 */
public class NumberParser {

    public final static Parser<String> NUMBER = Terminals.IntegerLiteral.PARSER.label("NUM");//Scanners.INTEGER.map(Integer::valueOf).label("NUMBER");

    static final Parser<String> IDENTIFIER = Terminals.Identifier.PARSER.label("VAR");

    private static final Terminals TERMS = Terminals
            .operators("+", "-", "*", "/", "%", "==", "!=", ">", ">=", "<", "<=", "&&", "||", "**", "(", ")", ":=", ";")
            .words(Scanners.IDENTIFIER)
            .keywords("skip", "write", "read", "while", "do", "if", "then", "else", "fi", "od")
            .build();

    public final static Parser<?> IGNORED = Parsers.or(Scanners.JAVA_BLOCK_COMMENT, Scanners.JAVA_LINE_COMMENT, Scanners.WHITESPACES);

//
//    Terminals operators = Terminals.operators(","); // only one operator supported so far
//    Parser<?> integerTokenizer = Terminals.IntegerLiteral.TOKENIZER;
//    Parser<String> integerSyntacticParser = Terminals.IntegerLiteral.PARSER;
//    Parser<?> ignored = Parsers.or(Scanners.JAVA_BLOCK_COMMENT, Scanners.WHITESPACES);
//    Parser<?> tokenizer = Parsers.or(operators.tokenizer(), integerTokenizer); // tokenizes the operators and integer
//    Parser<List<String>> integers = integerSyntacticParser.sepBy(operators.token(","))
//            .from(tokenizer, ignored.skipMany());


    public static Parser<?> tokenizer = Parsers.or(TERMS.tokenizer(), Terminals.IntegerLiteral.TOKENIZER, Terminals.Identifier.TOKENIZER);


    public static Parser<?> term(String name) {
        return TERMS.token(name);
    }

    static Parser<String> SKIP = term("skip").label("KW").cast();

    static Parser<String> EXPRESSION = Parsers.or(NUMBER, IDENTIFIER).label("EXP");

    static Parser<String> READ = Parsers.sequence(term("read").cast(), IDENTIFIER).label("READ");

    static Parser<String> WRITE = Parsers.sequence(term("write").cast(), EXPRESSION).label("WRITE");

    static Parser<String> ASSIGNMENT = Parsers.sequence(IDENTIFIER, term(":=").cast(), EXPRESSION).label(":=");

//    static Parser<String> _WHILE = Parsers.sequence(
//            term("while").cast(),
//            EXPRESSION,
//            term("do").cast(),
//            StateParser.STATE,
//            term("od").cast()).label("WHILE");


    static Parser.Reference<String> ref = Parser.newReference();
    static Parser<String> COLON = Parsers.sequence(StateParser.STATE, term(";").cast(), StateParser.STATE, ref.lazy()).label(";");

//    static Parser<String> IFTELSLE;
//
//    static {
//        IFTELSLE = Parsers.sequence(
//                term("if").cast(),
//                EXPRESSION,
//                term("then").cast(),
//                StateParser.STATE,
//                term("else").cast(),
//                StateParser.STATE,
//                term("fi").cast());
//    }


    static Parser<String> parse(Parser<String> atom) {
        Parser.Reference<String> ref = Parser.newReference();
        Parser<String> unit = ref.lazy().between(term("("), term(")")).or(atom);
        Parser<String> parser = new OperatorTable<String>()
                .build(unit);
        ref.set(parser);
        return parser;
    }


}