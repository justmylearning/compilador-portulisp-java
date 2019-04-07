public class Token {
    private String imagem;
    private TokenClasseEnum classe;
    private Integer linha, coluna, indiceTabelaDeSimbolos;

    public Token(){
        this.imagem = "";
    }

    public Token(String imagem, TokenClasseEnum classe, Integer linha, Integer coluna, Integer indiceTabelaDeSimbolos) {
        this.imagem = imagem;
        this.classe = classe;
        this.linha = linha;
        this.coluna = coluna;
        this.indiceTabelaDeSimbolos = indiceTabelaDeSimbolos;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public TokenClasseEnum getClasse() {
        return classe;
    }

    public void setClasse(TokenClasseEnum classe) {
        this.classe = classe;
    }

    public Integer getLinha() {
        return linha;
    }

    public void setLinha(Integer linha) {
        this.linha = linha;
    }

    public Integer getColuna() {
        return coluna;
    }

    public void setColuna(Integer coluna) {
        this.coluna = coluna;
    }

    public Integer getIndiceTabelaDeSimbolos() {
        return indiceTabelaDeSimbolos;
    }

    public void setIndiceTabelaDeSimbolos(Integer indiceTabelaDeSimbolos) {
        this.indiceTabelaDeSimbolos = indiceTabelaDeSimbolos;
    }

    @Override
    public String toString() {
        return "Token{" +
                "imagem='" + imagem + '\'' +
                ", classe=" + classe +
                ", linha=" + linha +
                ", coluna=" + coluna +
                ", indiceTabelaDeSimbolos=" + indiceTabelaDeSimbolos +
                '}'+'\n';
    }
}