package br.com.simao.campo_minado_swing.view;

import br.com.simao.campo_minado_swing.model.Tabuleiro;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class TabuleiroConsole {
    private static Scanner entrada;
    private Tabuleiro tabuleiro;

    public TabuleiroConsole(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        entrada = new Scanner(System.in);
        executar();
    }

    private void executar() {
        try {
            boolean continuar = true;
            jogo();

            while (continuar) {
                System.out.println("outra partida (s/n)");
                String op = entrada.nextLine();
                if(op.equalsIgnoreCase("n"))
                    continuar = false;
                else{
                    this.tabuleiro.reiniciar();

                }
                if (!continuar)
                    break;
                    //fixme nao existe sair
            }
        }catch (Exception e){
            //fixme trocar exception
            System.out.println("saindo!!");
        }finally {
            entrada.close();
        }
    }

    private void jogo() {
        try {
            while (!tabuleiro.objetivoAlcancado()) {
                System.out.println(tabuleiro);
                String digitado = capturarValor("Digite (x,y): ");
                Iterator<Integer> xy =  Arrays.stream(digitado.split(",")).map(e -> Integer.parseInt(e.trim())).iterator();
                digitado = capturarValor("1- abrir \n2- (des)marcar");
                switch (digitado){
                    case "1":
                        tabuleiro.abrir(xy.next(), xy.next());
                        break;
                    case "2":
                        tabuleiro.alterarMarcacao(xy.next(), xy.next());
                        break;
                    default:
                        System.out.println("Opção invalida");
                }
            }
            System.out.println("Ganhou!");
        }catch (Exception e){
            System.out.println("PERDEU!");
            System.out.println(tabuleiro);
        }
    }

    private String capturarValor(String texto){
        System.out.print(texto);
        String dgitado = entrada.nextLine();

        //if("sair".equalsIgnoreCase(dgitado))
        //fixme nao exite excessao a ser lançada
        return dgitado;
    }

}
