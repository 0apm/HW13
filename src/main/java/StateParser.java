import org.jparsec.Parser;
import org.jparsec.Parsers;

import java.util.List;

/**
 * Created by rm on 12.12.16.
 */

public class StateParser {

    static Parser<String> STATE = Parsers
            .or(    NumberParser.SKIP,
                    NumberParser.READ,
                    NumberParser.WRITE,
                    NumberParser.ASSIGNMENT,
                    NumberParser.COLON)

            .label("S")
            .from(NumberParser.tokenizer,
                    NumberParser.IGNORED.skipMany());

    static Parser<String> PROGRAM = Parsers.or(STATE);
}
