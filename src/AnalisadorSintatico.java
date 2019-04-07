import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintatico {

    private TabelaDeTokens tabelaDeTokens;
    private TabelaDeSimbolos tabelaDeSimbolos;
    private Token tokenAtual;
    private Integer tokenPosicao = 0;
    private List<String> erros = new ArrayList<String>();

    public AnalisadorSintatico(TabelaDeTokens tabelaDeTokens, TabelaDeSimbolos tabelaDeSimbolos){
        this.tabelaDeTokens = tabelaDeTokens;
        this.tabelaDeSimbolos = tabelaDeSimbolos;
    }

    public void proximoToken(){
        this.tokenAtual = this.tabelaDeTokens.getTabelaDeTokens().get(this.tokenPosicao++);
    }

    public Token lookAHead(){
        return this.tabelaDeTokens.getTabelaDeTokens().get(this.tokenPosicao);
    }

    public void analisar() throws Exception {
        this.proximoToken();
        this.funPrograma();
        System.out.println("Erros:");
        for(String erro : this.erros){
            System.out.println(erro);
        }
    }

    public void addErro(String msg, Token token){
        this.erros.add(msg + " => " + token.toString());
    }

    public void cadeiaVazia(){ return; }

    //<programa> ::= <comando> <programa> | <funcao> <programa> |
    public void funPrograma() throws Exception {
        if(this.tokenAtual.getImagem().equals("(")) {
            Token lookAHeadToken = this.lookAHead();
            //<funcao>
            if (lookAHeadToken.getImagem().equals("fun")) {
                this.funFuncao();
                this.funPrograma();
            }
            //<comando>
            else {
                this.funComando();
                this.funPrograma();
            }
        }
        else{
            this.cadeiaVazia();
        }
    }

    //<funcao> ::= '(' <funcao-interna> ')'
    public void funFuncao(){
        if(this.tokenAtual.getImagem().equals("(")){
            this.proximoToken();
            this.funFuncaoInterna();
            if(this.tokenAtual.getImagem().equals(")")){
                this.proximoToken();
            }
            else {
                this.addErro("Esperado: )", this.tokenAtual);
            }
        }
        else{
            this.addErro("Esperado: (", this.tokenAtual);
        }
    }

    //<funcao-interna> ::= 'fun' id <params> ':' <tipo> <comandos>
    public void funFuncaoInterna(){
        if(this.tokenAtual.getImagem().equals("fun")){
            this.proximoToken();
            if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
                this.proximoToken();
                this.funParams();
                if(this.tokenAtual.getImagem().equals(":")){
                    this.proximoToken();
                    this.funTipo();
                    this.funComandos();
                }
                else {
                    this.addErro("Esperado: :", this.tokenAtual);
                }
            }
            else{
                this.addErro("Esperado: ID", this.tokenAtual);
            }
        }
        else{
            this.addErro("Esperado: fun", this.tokenAtual);
        }
    }

    //<params> ::= <param> <params> |
    public void funParams(){
        //if(this.lookAHead().getImagem().equals("(")){
        if(this.tokenAtual.getImagem().equals("(")){
            this.funParam();
            this.funParams();
        }
        else{
            this.cadeiaVazia();
        }
    }

    //<param> ::= '(' <tipo> id ')'
    public void funParam(){
        if(this.tokenAtual.getImagem().equals("(")){
            this.proximoToken();
            this.funTipo();
            if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
                this.proximoToken();
                if(this.tokenAtual.getImagem().equals(")")){
                    this.proximoToken();
                }
                else{
                    this.addErro("Esperado: )", this.tokenAtual);
                }
            }
            else{
                this.addErro("Esperado: ID", this.tokenAtual);
            }
        }
        else{
            this.addErro("Esperado: (", this.tokenAtual);
        }
    }

    //<tipo> ::= 'int' | 'real' | 'texto' | 'logico' | 'nada'
    public void funTipo(){
        List<String> terminais = List.of("int", "real", "texto", "logico", "nada");
        if(terminais.contains(this.tokenAtual.getImagem())){
            this.proximoToken();
        }
        else{
            this.addErro("Esperado: " + String.join(", ", terminais), this.tokenAtual);
        }
    }

    //<comandos> ::= <comando> <comandos> |
    public void funComandos(){
        if(this.tokenAtual.getImagem().equals("(")){
            this.funComando();
            this.funComandos();
        }
        else{
            this.cadeiaVazia();
        }
    }

    //<comando> ::= '(' <comando-interno> ')'
    public void funComando(){
        if(this.tokenAtual.getImagem().equals("(")){
            this.proximoToken();
            this.funComandoInterno();
            if(this.tokenAtual.getImagem().equals(")")){
                this.proximoToken();
            }
            else{
                this.addErro("Esperado: )", this.tokenAtual);
            }
        }
        else{
            this.addErro("Esperado: (", this.tokenAtual);
        }
    }

    //<comando-interno> ::= <decl> | <atrib> | <invoca> | <se> | <leitura> | <enquanto> | <para> | <retorno> | <mostrar>
    //"int", "real", "texto", "logico", "nada", //<decl>
    //"=", //<atrib>
    //id, // <invoca>
    //"se", //<se>
    //"le", //<leitura>
    //"enquanto", //<enquanto>
    //"para", //<para>
    //"ret", //<retorno>
    //"mostra" //<mostrar>
    public void funComandoInterno(){
        switch (this.tokenAtual.getImagem()){
            case "int":
            case "real":
            case "texto":
            case "logico":
            case "nada":
                this.funDecl();
                break;
            case "=":
                this.funAtrib();
                break;
            case "se":
                this.funSe();
                break;
            case "le":
                this.funLeitura();
                break;
            case "enquanto":
                this.funEnquanto();
                break;
            case "para":
                this.funPara();
                break;
            case "ret":
                this.funRetorno();
                break;
            case "mostra":
                this.funMostrar();
                break;
            default:
                if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
                this.funInvoca();
            }
                else{
                this.addErro("Esperado: int, real, texto, logico, nada, =, se, le, enquanto, para, ret, mostra, ID", this.tokenAtual);
            }
            break;
        }
    }

    //<decl> ::= <tipo> <ids>
    public void funDecl(){
        this.funTipo();
        this.funIds();
    }

    //<ids> ::= id <ids2>
    public void funIds(){
        if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
            this.proximoToken();
            this.funIds2();
        }
        else{
            this.addErro("Esperado: ID", this.tokenAtual);
        }
    }

    //ids2> ::= id <ids2> |
    public void funIds2(){
        if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
            this.proximoToken();
            this.funIds2();
        }
        else{
            this.cadeiaVazia();
        }
    }

    //<atrib> ::= '=' id <expr>
    public void funAtrib(){
        if(this.tokenAtual.getImagem().equals("=")){
            this.proximoToken();
            if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
                this.proximoToken();
                this.funExpr();
            }
            else{
                this.addErro("Esperado: ID", this.tokenAtual);
            }
        }
        else{
            this.addErro("Esperado: =", this.tokenAtual);
        }
    }

    //<expr> ::= <operan> | '(' <op2> <expr> <expr> ')' | '(' <op1> id ')'
    public void funExpr(){
        if( //<operan>
            this.tokenAtual.getClasse().equals(TokenClasseEnum.ID) ||
            this.tokenAtual.getClasse().equals(TokenClasseEnum.CLI) ||
            this.tokenAtual.getClasse().equals(TokenClasseEnum.CLR) ||
            this.tokenAtual.getClasse().equals(TokenClasseEnum.CLL) ||
            this.tokenAtual.getClasse().equals(TokenClasseEnum.CLS)
        ){
            this.funOperan();
        }
        else if(this.tokenAtual.getImagem().equals("(")){
            this.proximoToken();
            if(this.tokenAtual.getImagem().equals("++") || this.tokenAtual.getImagem().equals("--")){ //<op1>
                this.funOp1();
                if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
                    this.proximoToken();
                }
                else{
                    this.addErro("Esperado: ID", this.tokenAtual);
                }
            }
            else{
                List<String> op2Terminais = List.of("&&" , "||" , ">" , ">=" , "<" , "<=" , "==" , "!=" , "." , "+" , "-" , "*" , "/");
                if(op2Terminais.contains(this.tokenAtual.getImagem())){ //<op2>
                    this.funOp2();
                    this.funExpr();
                    this.funExpr();
                    if(this.tokenAtual.getImagem().equals(")")){
                        this.proximoToken();
                    }
                }
                else{
                    this.addErro("Esperado: ++, --, " + String.join(", ", op2Terminais), this.tokenAtual);
                }
            }
        }
        else{
            this.addErro("Esperado: (", this.tokenAtual);
        }
    }

    //<op1> ::= '++' | '--'
    public void funOp1(){
        if(this.tokenAtual.getImagem().equals("++") || this.tokenAtual.getImagem().equals("--")){
            this.proximoToken();
        }
        else{
            this.addErro("Esperado: ++, --", this.tokenAtual);
        }
    }

    //<op2> ::= '&&' | '||' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '.' | '+' | '-' | '*' | '/'
    public void funOp2(){
        List<String> terminais = List.of("&&" , "||" , ">" , ">=" , "<" , "<=" , "==" , "!=" , "." , "+" , "-" , "*" , "/");
        if(terminais.contains(this.tokenAtual.getImagem())){
            this.proximoToken();
        }
        else{
            this.addErro("Esperado: " + String.join(", ", terminais), this.tokenAtual);
        }
    }

    //<operan> ::= id | cli | clr | cll | cls | '(' <invoca> ')'
    public void funOperan(){
        List<TokenClasseEnum> terminais = List.of(TokenClasseEnum.ID, TokenClasseEnum.CLI, TokenClasseEnum.CLR, TokenClasseEnum.CLL, TokenClasseEnum.CLS);
        if(terminais.contains(this.tokenAtual.getClasse())){
            this.proximoToken();
        }
        else{
            if(this.tokenAtual.getImagem().equals("(")){
                this.proximoToken();
                this.funInvoca();
                if(this.tokenAtual.getImagem().equals(")")){
                    this.proximoToken();
                }
                else{
                    this.addErro("Esperado: )", this.tokenAtual);
                }
            }
            else{
                this.addErro("Esperado: ID, CLI, CLR, CLL, CLS, ( ", this.tokenAtual);
            }
        }
    }

    //<invoca> ::= id <args>
    public void funInvoca(){
        if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
            this.proximoToken();
            this.funArgs();
        }else {
            this.addErro("Esperado: ID", this.tokenAtual);
        }
    }

    //<args> ::= <expr> <args> |
    public void funArgs(){
        List<TokenClasseEnum> exprTerminais = List.of(TokenClasseEnum.ID, TokenClasseEnum.CLI, TokenClasseEnum.CLR, TokenClasseEnum.CLL, TokenClasseEnum.CLS);
        if(exprTerminais.contains(this.tokenAtual.getClasse()) || this.tokenAtual.getImagem().equals("(")){
            this.funExpr();
            this.funArgs();
        }
        else{
            this.cadeiaVazia();
        }
    }

    //<se> ::= 'se' <expr> '(' <comandos> ')' <senao>
    public void funSe(){
        if(this.tokenAtual.getImagem().equals("se")){
            this.proximoToken();
            this.funExpr();
            if(this.tokenAtual.getImagem().equals("(")){
                this.proximoToken();
                this.funComandos();
                if(this.tokenAtual.getImagem().equals(")")){
                    this.proximoToken();
                }else{
                    this.addErro("Esperado: )", this.tokenAtual);
                }
            }
            else{
                this.addErro("Esperado: (", this.tokenAtual);
            }
        }
        else{
            this.addErro("Esperado: se", this.tokenAtual);
        }
    }

    //<leitura> ::= 'le' id
    public void funLeitura(){
        if(this.tokenAtual.getImagem().equals("le")){
            this.proximoToken();
            if(this.tokenAtual.getClasse().equals(TokenClasseEnum.ID)){
                this.proximoToken();
            }
            else{
                this.addErro("Esperado: ID", this.tokenAtual);
            }
        }
        else{
            this.addErro("Esperado: le", this.tokenAtual);
        }
    }

    //<enquanto> ::= 'enquanto' <expr> <comandos>
    public void funEnquanto(){
        if(this.tokenAtual.getImagem().equals("enquanto")){
            this.proximoToken();
            this.funExpr();
            this.funComandos();
        }
        else{
            this.addErro("Esperado: enquanto", this.tokenAtual);
        }
    }

    //<para> ::= 'para' <atrib> <expr> <atrib> <comandos>
    public void funPara(){
        if(this.tokenAtual.getImagem().equals("para")){
            this.proximoToken();
            this.funAtrib();
            this.funExpr();
            this.funAtrib();
            this.funComandos();
        }
        else{
            this.addErro("Esperado: para", this.tokenAtual);
        }
    }

    //<retorno> ::= 'ret' <expr>
    public void funRetorno(){
        if(this.tokenAtual.getImagem().equals("ret")){
            this.proximoToken();
            this.funExpr();
        }
        else{
            this.addErro("Esperado: ret", this.tokenAtual);
        }
    }

    //<mostrar> ::= 'mostra '<expr>
    public void funMostrar(){
        if(this.tokenAtual.getImagem().equals("mostra")){
            this.proximoToken();
            this.funExpr();
        }
        else{
            this.addErro("Esperado: mostra", this.tokenAtual);
        }
    }

}
