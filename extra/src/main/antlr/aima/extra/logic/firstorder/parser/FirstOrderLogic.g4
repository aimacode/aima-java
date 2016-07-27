grammar FirstOrderLogic;

/*
 * Sentence        -> AtomicSentence : ComplexStence
 * AtomicSentence  -> Predicate | Predicate(Term,...) | Term = Term
 * ComplexSentence -> (Sentence) | [Sentence]
 *                 :  ~Sentence
 *                 :  Sentence & Sentence
 *                 :  Sentence | Sentence
 *                 :  Sentence => Sentence
 *                 :  Sentence <=> Sentence
 *                 :  Quantifier Variable,... Sentence
 * 
 * Term            -> Function(Term,...)
 *                 :  Constant
 *                 :  Variable
 * 
 * Quantifier      -> FORALL
 *                 :  EXISTS
 * 
 * Constant        ->
 * 
 * Variable
 * 
 * Predicate        
 * 
 * Functions
 * 
 * OPERATOR PRECEDENCE: ~, =, &, |, =>, <=>
 */

/**
 * @author Anurag Rai
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
    |<assoc=right> left=sentence op=EQUALS right=sentence
    |<assoc=right> left=sentence op=AND right=sentence
    |<assoc=right> left=sentence op=OR right=sentence
    |<assoc=right> left=sentence op=IMPLICATION right=sentence
    |<assoc=right> left=sentence op=BICONDITIONAL right=sentence
    | quantifier variable+ sentence
    ;

bracketedsentence
    : '(' sentence ')'
    | '[' sentence ']'
    ;

atomicsentence
    : predicate
    | predicate '(' term (',' term)* ')'
    | term EQUALS term
    ;

term
	: function '(' term (',' term)* ')'
	| constant
	| variable
	;
	
quantifier
	: FORALL
	| EXISTS
	;

constant 
    : SYMBOL
    | QUOTED_SYMBOL
    ;
    
variable
    : SYMBOL
    | QUOTED_SYMBOL
    ;
    
predicate
    : SYMBOL
    | QUOTED_SYMBOL
    ;

function
    : SYMBOL
    | QUOTED_SYMBOL
    ;
	

/*
===========
LEXER RULES
===========
*/
    
NOT:              '~';
EQUALS:           '=';
AND:              '&';
OR:               '|';
IMPLICATION:      '=>';
BICONDITIONAL:    '<=>';

FORALL:	          'FORALL';
EXISTS:	          'EXISTS';

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