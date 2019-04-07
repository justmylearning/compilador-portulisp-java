public class Cursor {
    private Integer linha;
    private Integer coluna;

    public Cursor(){
        this.linha = 0;
        this.coluna = 0;
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

    public void incrementarColuna(){
        this.coluna++;
    }

    public void incrementarLinha(){
        this.linha++;
    }
}
