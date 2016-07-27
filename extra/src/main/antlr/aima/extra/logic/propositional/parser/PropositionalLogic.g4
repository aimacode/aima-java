grammar PropositionalLogic;

@header {
package aima.extra.logic.propositional.parser;
}

/*
 * Sentence        -> AtomicSentence : ComplexStence
 * AtomicSentence  -> True : False : P : Q : R : ...
 * ComplexSentence -> (Sentence) | [Sentence]
 *                 :  ~Sentence
 *                 :  Sentence & Sentence
 *                 :  Sentence | Sentence
 *                 :  Sentence => Sentence
 *                 :  Sentence <=> Sentence
 *
 * OPERATOR PRECEDENCE: ~, &, |, =>, <=>
 */

/*
=============
GRAMMAR RULES
=============
*/

parse: sentence+ EOF;

sentence
    : bracketedsentence
    | atomicsentence
    | op=NOT right=sentence
    |<assoc=right> left=sentence op=AND right=sentence
    |<assoc=right> left=sentence op=OR right=sentence
    |<assoc=right> left=sentence op=IMPLICATION right=sentence
    |<assoc=right> left=sentence op=BICONDITIONAL right=sentence
    ;

bracketedsentence
    : '(' sentence ')'
    | '[' sentence ']'
    ;

atomicsentence
    : SYMBOL
    | QUOTED_SYMBOL
    ;

/*
===========
LEXER RULES
===========
*/
    
NOT:              '~';
AND:              '&';
OR:               '|';
IMPLICATION:      '=>';
BICONDITIONAL:    '<=>';

SYMBOL
    :  INITIAL SUBSEQUENT* 
    |  PECULIAR_IDENTIFIER
    ;

QUOTED_SYMBOL
    : '"'  (ESCAPE_SEQUENCE | ~('\\' | '"' ) )* '"'
    | '\'' (ESCAPE_SEQUENCE | ~('\\' | '\'') )* '\''
    ;

COMMENT
    :   '/*' .*? '*/'    -> channel(HIDDEN) // match anything between /* and */
    ;

LINE_COMMENT
    : '//' ~[\r\n]* '\r'? ('\n' | EOF) -> channel(HIDDEN)
    ;

WS  :   [ \t\r\n]+ -> skip
    ; // Define whitespace rule, toss it out

// fragments  
fragment INITIAL : LETTER | SPECIAL_INITIAL;
fragment LETTER : 'a'..'z' | 'A'..'Z';
fragment SPECIAL_INITIAL : '!' | '@' | '#' | '$' | '%' | ':' | '?' | '^' | '_';
fragment SUBSEQUENT : INITIAL | DIGIT | SPECIAL_SUBSEQUENT;
fragment DIGIT : '0'..'9';
fragment SPECIAL_SUBSEQUENT : '.' | '+' | '-' | '@';
fragment PECULIAR_IDENTIFIER : '+' | '-';

fragment ESCAPE_SEQUENCE
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')
    |   UNICODE_ESCAPE
    |   OCTAL_ESCAPE
    ;

fragment UNICODE_ESCAPE
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment OCTAL_ESCAPE
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment HEX_DIGIT
    : ('0'..'9'|'a'..'f'|'A'..'F')
    ;