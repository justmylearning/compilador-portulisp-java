import Exceptions.ExceptionTokenClasseNaoReconhecida;

public class AnalisadorLexico {

    private TabelaDeTokens tabelaDeTokens;
    private TabelaDeSimbolos tabelaDeSimbolos;
    private Cursor cursor;

    public AnalisadorLexico(TabelaDeTokens tabelaDeTokens, TabelaDeSimbolos tabelaDeSimbolos){
        this.tabelaDeTokens = tabelaDeTokens;
        this.tabelaDeSimbolos = tabelaDeSimbolos;
        this.cursor = new Cursor();
    }

    public TabelaDeTokens analisar(String codigoFonte) throws ExceptionTokenClasseNaoReconhecida {
        this.separarTokens(codigoFonte);
        this.classificarTokens();
        this.tabelaDeTokens.adicionarToken(new Token("", TokenClasseEnum.EOF, 0, 0, 0));
        System.out.println(this.tabelaDeTokens);
        return this.tabelaDeTokens;
    }

    private void separarTokens(String codigoFonte){
        boolean tokenIsString = false;
        Token tokenIdentificado = new Token();
        for(char caracter: codigoFonte.toCharArray()){
            this.cursor.incrementarColuna();//Andando com cursor

            if(caracter == '\n'){
                this.cursor.incrementarLinha();
                this.cursor.setColuna(0);
                continue;
            }

            if(caracter == ' ' && !tokenIsString){
                if(tokenIdentificado.getImagem() != ""){
                    this.adicionarTokenNaTabelaTokens(tokenIdentificado);
                    tokenIdentificado = new Token();
                }
                continue;
            }

            if(caracter == '"'){
                tokenIsString = !tokenIsString;
                tokenIdentificado.setImagem( tokenIdentificado.getImagem() + caracter );
                continue;
            }

            if(tokenIsString){
                tokenIdentificado.setImagem( tokenIdentificado.getImagem() + caracter );
                continue;
            }

            if(this.caracterIsDelimitador(caracter)){
                tokenIdentificado.setImagem( tokenIdentificado.getImagem() + caracter );
                this.adicionarTokenNaTabelaTokens(tokenIdentificado);
                tokenIdentificado = new Token();
                continue;
            }

            tokenIdentificado.setImagem(tokenIdentificado.getImagem() + caracter);

        }
    }

    private boolean caracterIsDelimitador(char caracter){
        return caracter ==  ')' || caracter ==  '(';
    }

    private void adicionarTokenNaTabelaTokens(Token token){
        Integer coluna = this.cursor.getColuna() - ( token.getImagem().length() );
        if(coluna >= 0)
            token.setColuna( this.cursor.getColuna() - ( token.getImagem().length() ) );
        else
            token.setColuna( 0 );
        token.setLinha(this.cursor.getLinha());

        this.tabelaDeTokens.adicionarToken(token);
    }

    public void classificarTokens() throws ExceptionTokenClasseNaoReconhecida {
        ClassificadorDeTokens cf = new ClassificadorDeTokens();
        for(Token tokenItem : this.tabelaDeTokens.getTabelaDeTokens()){
            cf.classificar(tokenItem);
//            if(tokenItem.getClasse() == TokenClasseEnum.ID){
//                Simbolo simbolo = new Simbolo(tokenItem.getImagem());
//                simbolo = this.tabelaDeSimbolos.adicionarSimbolo(simbolo);
//                tokenItem.setIndiceTabelaDeSimbolos(simbolo.getIndice());
//            }
        }
    }

}
