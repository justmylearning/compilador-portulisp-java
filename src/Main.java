import Exceptions.ExceptionTokenClasseNaoReconhecida;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        String codigoFonte = lerArquivo("codigo_fonte.txt");
        TabelaDeTokens tabelaDeTokens = new TabelaDeTokens();
        TabelaDeSimbolos tabelaDeSimbolos = new TabelaDeSimbolos();
        AnalisadorLexico analisadorLexico = new AnalisadorLexico(tabelaDeTokens, tabelaDeSimbolos);
        tabelaDeTokens = analisadorLexico.analisar(codigoFonte);
        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tabelaDeTokens,tabelaDeSimbolos);
        analisadorSintatico.analisar();
    }

    public static String lerArquivo(String filePath){

        File file = new File(filePath);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String content = new String();
        while(sc.hasNextLine()){
            content += sc.nextLine() + '\n';
        }

        return content;
    }
}
