package br.com.simao.campo_minado_swing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {
    private final int cols;
    private final int rows;
    private final int minas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        if(evento == CampoEvento.EXPLODIR){
            mostrarMinas();
            notificarObservadores(false);
        }else if(objetivoAlcancado()){
            notificarObservadores(true);

        }
    }

    public void forEach(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }

    public void registrarObservador(Consumer<ResultadoEvento> observador){
        observadores.add(observador);
    }

    private void notificarObservadores(boolean resultado){
        observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
    }

    public Tabuleiro(int cols, int rows, int minas) {
        this.cols = cols;
        this.rows = rows;
        this.minas = minas;

        gerarCampos();
        associarVizinhos();
        setarMinas();
    }

    private void setarMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = c -> c.isMinado();
        do {

            int r = (int)(Math.random() * campos.size());
            campos.get(r).minar();
            minasArmadas = campos.stream().filter(minado).count();
        } while (minasArmadas < minas);
    }

    private void associarVizinhos() {
        for (Campo c1: campos) {
            for(Campo c2 : campos){
                c1.addVizinho(c2);
            }
        }
    }

    private void gerarCampos() {
        for (int i = 0; i < rows; i++){
            for(int j = 0;j< cols; j++){
                Campo campo = new Campo(i,j);
                campos.add(campo);
                campo.registrarObservador(this);
            }
        }
    }

    public boolean objetivoAlcancado(){
        return campos.stream().allMatch(c -> c.objetivoAlcancado());
    }

    public void reiniciar(){
        campos.stream().forEach(c -> c.reiniciar());
        setarMinas();
    }

    public void abrir(int row, int col){
        campos.parallelStream().filter(c -> c.getRow() == row && c.getCol() == col)
                .findFirst().ifPresent(c -> c.abrir());

    }

    private void mostrarMinas(){
        campos.stream()
                .filter(c -> c.isMinado())
                .filter(c-> !c.isMarcado())
                .forEach(c -> c.setAberto(true));
    }

    public void alterarMarcacao(int row, int col){
        campos.parallelStream().filter(c -> c.getRow() == row && c.getCol() == col)
                .findFirst().ifPresent(c -> c.alternarMarcacao());
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    //    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        int id =0;
//        sb.append("   ");
//        for (int i = 0; i < cols; i++){
//            sb.append(" ");
//            sb.append(i);
//            sb.append(" ");
//        }
//        sb.append("\n");
//        for (int i=0; i<rows;i++){
//            sb.append(" ");
//            sb.append(i);
//            sb.append(" ");
//            for (int j=0;j<cols; j ++){
//                sb.append(" ");
//                sb.append(campos.get(id));
//                sb.append(" ");
//                id++;
//            }
//            sb.append("\n");
//        }
//        return sb.toString();
//    }
}
