import java.util.ArrayList;

public class TabelaDeTokens {
    private ArrayList<Token> tabelaDeTokens;

    public TabelaDeTokens(){
        this.tabelaDeTokens = new ArrayList<Token>();
    }

    public ArrayList<Token> getTabelaDeTokens() {
        return tabelaDeTokens;
    }

    public void setTabelaDeTokens(ArrayList<Token> tabelaDeTokens) {
        this.tabelaDeTokens = tabelaDeTokens;
    }

    public void adicionarToken(Token novoToken){
        this.tabelaDeTokens.add(novoToken);
    }

    @Override
    public String toString() {
        return "TabelaDeTokens{" +
                "tabelaDeTokens=\n" + tabelaDeTokens +
                '}';
    }
}