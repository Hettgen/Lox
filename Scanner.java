import java.util.ArrayList;
import java.util.List;

public class Scanner {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    
    Scanner(String source){
        this.source = source;
    }

    
    List<Token> scanTokens(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private void scanToken(){
        char c = advance();
        switch(c){
            // Cases for single char tokens (Except slash)
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '[': addToken(LEFT_BRACE); break;
            case ']': addToken(RIGHT_BRACE); break;
            case ',': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;

            // Tokens greater than 1 character
            case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '>': addToken(match("=") ? GREATER_EQUAL : GREATER);
            case '<': addToken(match('=') ? LESS_EQUAL : EQUAL);

            // Case for slash (/)
            case '/': 
                if(match('/')) 
                    while(peek() != '\n' && !isAtEnd()) advance();
                else
                    addToken(SLASH);
                break;

            default:
                Lox.error(line, "Unexpected Character.");
                break;
        }
    }

    private char advance(){
        return source.charAt(current++);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected){
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    
}