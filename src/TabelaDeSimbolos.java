import Exceptions.ExceptionSimboloNaoExiste;

import java.util.ArrayList;

public class TabelaDeSimbolos {
    private ArrayList<Simbolo> tabelaDeSimbolos;
    private Integer indice;

    public TabelaDeSimbolos(){
        this.tabelaDeSimbolos = new ArrayList<Simbolo>();
        this.indice = 0;
    }

    public ArrayList<Simbolo> getTabelaDeSimbolos() {
        return tabelaDeSimbolos;
    }

    public void setTabelaDeSimbolos(ArrayList<Simbolo> tabelaDeSimbolos) {
        this.tabelaDeSimbolos = tabelaDeSimbolos;
    }

    public Simbolo adicionarSimbolo(Simbolo novoSimbolo){
        try{

            Simbolo simboloExistente = this.procurarSimboloPorImagem(novoSimbolo.getImagem());
            return simboloExistente;

        }catch (ExceptionSimboloNaoExiste ex){

            novoSimbolo.setIndice(this.getIndice());
            this.tabelaDeSimbolos.add(novoSimbolo);
            this.setIndice( this.getIndice() + 1 );
            return novoSimbolo;

        }
    }

    public Simbolo procurarSimboloPorImagem(String imagemProcurada) throws ExceptionSimboloNaoExiste {
        Simbolo simboloEncontrado = null;
        for(Simbolo simboloItem: this.getTabelaDeSimbolos()){

            if(simboloItem.getImagem().equals(imagemProcurada)){
                simboloEncontrado = simboloItem;
            }

        }

        if(simboloEncontrado == null)
            throw new ExceptionSimboloNaoExiste();

        return simboloEncontrado;
    }

    public Integer getIndice() {
        return indice;
    }

    private void setIndice(Integer indice) {
        this.indice = indice;
    }
}