import Exceptions.ExceptionTokenClasseNaoReconhecida;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ClassificadorDeTokens {

    private ArrayList<Token> tabelaDeTokensConhecidos;

    public ClassificadorDeTokens(){
        this.tabelaDeTokensConhecidos = new ArrayList<Token>();
        //PR
        this.tabelaDeTokensConhecidos.add( new Token("fun", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("enquanto", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("se", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("senao", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("ret", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("int", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("real", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("texto", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("logico", TokenClasseEnum.PR, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("nada", TokenClasseEnum.PR, -1, -1, -1) );
        //DE
        this.tabelaDeTokensConhecidos.add( new Token("(", TokenClasseEnum.DE, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token(")", TokenClasseEnum.DE, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("[", TokenClasseEnum.DE, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("]", TokenClasseEnum.DE, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token(":", TokenClasseEnum.DE, -1, -1, -1) );
        //OP
        this.tabelaDeTokensConhecidos.add( new Token("+", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("-", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("*", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("/", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("=", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("&&", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("||", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token(".", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("==", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("!=", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token(">", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("<", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token(">=", TokenClasseEnum.OP, -1, -1, -1) );
        this.tabelaDeTokensConhecidos.add( new Token("<=", TokenClasseEnum.OP, -1, -1, -1) );
        //CLI
        this.tabelaDeTokensConhecidos.add( new Token("<REGEX>^\\d(\\d)*$", TokenClasseEnum.CLI, -1, -1, -1) );
        //CLR
        this.tabelaDeTokensConhecidos.add( new Token("<REGEX>^\\d(\\d)*\\.\\d*$", TokenClasseEnum.CLR, -1, -1, -1) );
        //ID
        this.tabelaDeTokensConhecidos.add( new Token("<REGEX>^[a-zA-Z]([a-zA-Z]|[0-9])*", TokenClasseEnum.ID, -1, -1, -1) );
        //CLL
        this.tabelaDeTokensConhecidos.add( new Token("<REGEX>(V+F)", TokenClasseEnum.CLL, -1, -1, -1) );
        //CLS
        this.tabelaDeTokensConhecidos.add( new Token("<REGEX>^\".*\"$", TokenClasseEnum.CLS, -1, -1, -1) );
    }

    public Token classificar(Token token) throws ExceptionTokenClasseNaoReconhecida {
        for( Token tokenConhecido : this.getTabelaDeTokensConhecidos() ){

            //Checa se a classe do token conhecido possui regex
            if(this.tokenImagemIsRegex(tokenConhecido)){
                String regexQuery = tokenConhecido.getImagem().replace("<REGEX>", "");
                if( Pattern.compile(regexQuery).matcher(token.getImagem()).matches() ){
                    token.setClasse(tokenConhecido.getClasse());
                    return token;
                }
            }
            else if(token.getImagem().toLowerCase().equals(tokenConhecido.getImagem().toLowerCase())){
                token.setClasse(tokenConhecido.getClasse());
                return token;
            }

        }


        System.out.println("-> Classe de Token nao reconhecida: ");
        System.out.println("-> imagem: " + token.getImagem());
        System.out.println("-> linha: " + token.getLinha());
        System.out.println("-> coluna: " + token.getColuna());
        throw new ExceptionTokenClasseNaoReconhecida();
    }

    public ArrayList<Token> getTabelaDeTokensConhecidos() {
        return tabelaDeTokensConhecidos;
    }

    private void setTabelaDeTokensConhecidos(ArrayList<Token> tabelaDeTokensConhecidos) {
        this.tabelaDeTokensConhecidos = tabelaDeTokensConhecidos;
    }

    public boolean tokenImagemIsRegex(Token token){
        if( token.getImagem().toLowerCase().contains("<regex>") )
            return true;
        else
            return false;
    }
}