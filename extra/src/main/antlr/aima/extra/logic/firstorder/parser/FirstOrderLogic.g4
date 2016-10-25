grammar FirstOrderLogic;
@header {
package aima.extra.logic.firstorder.parser;
}

@parser::members {
private aima.core.logic.basic.firstorder.domain.FOLDomain domain;

public void setDomain(aima.core.logic.basic.firstorder.domain.FOLDomain domain) {
	this.domain = domain;
}

public aima.core.logic.basic.firstorder.domain.FOLDomain getDomain() {
	if (domain == null) {
		domain = new aima.core.logic.basic.firstorder.domain.FOLDomain();
	}
	return this.domain;
}

public void addConstant(String constant) {
	getDomain().addConstant(constant);
}

public void addPredicate(String predicate) {
	getDomain().addPredicate(predicate);
}

public void addFunction(String function) {
	getDomain().addFunction(function);
}
}
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
 * Constant        -> Starts with upper Case
 * 
 * Variable        -> Starts with lower Case
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
    |<assoc=right> left=sentence op=AND right=sentence
    |<assoc=right> left=sentence op=OR right=sentence
    |<assoc=right> left=sentence op=IMPLICATION right=sentence
    |<assoc=right> left=sentence op=BICONDITIONAL right=sentence
    | QUANTIFIER variable (',' variable)* right=sentence
    ;

bracketedsentence
    : '(' sentence ')'
    | '[' sentence ']'
    ;

atomicsentence
    : predicate '(' term (',' term)* ')'
    | left=term EQUALS right=term
    ;
    
predicate
    : UPPERCASE_SYMBOL
    | LOWERCASE_SYMBOL
    | QUOTED_SYMBOL
    ;
    
term
	: function '(' term (',' term)* ')'
	| constant
	| variable
	;
	
function
    : UPPERCASE_SYMBOL
    | LOWERCASE_SYMBOL
    | QUOTED_SYMBOL
    ;
    
constant 
    : UPPERCASE_SYMBOL
    | NUMBER
    | QUOTED_SYMBOL
    ;
    
variable
    : LOWERCASE_SYMBOL
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

QUANTIFIER
	: FORALL
	| EXISTS
	;
    
LOWERCASE_SYMBOL
    :  LOWER_CASE_LETTER SUBSEQUENT* 
    |  PECULIAR_IDENTIFIER
    ;
    
UPPERCASE_SYMBOL
    :  UPPER_CASE_LETTER SUBSEQUENT* 
    |  PECULIAR_IDENTIFIER
    ;
    
QUOTED_SYMBOL
    : '"'  (ESCAPE_SEQUENCE | ~('\\' | '"' ) )* '"'
    | '\'' (ESCAPE_SEQUENCE | ~('\\' | '\'') )* '\''
    ;

NUMBER
	: DIGIT+
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

fragment FORALL : 'FORALL';
fragment EXISTS:  'EXISTS';
 
fragment INITIAL : LOWER_CASE_LETTER | UPPER_CASE_LETTER | SPECIAL_INITIAL;
fragment LOWER_CASE_LETTER : 'a'..'z';
fragment UPPER_CASE_LETTER : 'A'..'Z';
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