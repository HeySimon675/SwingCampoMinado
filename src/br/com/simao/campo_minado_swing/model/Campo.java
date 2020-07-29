package br.com.simao.campo_minado_swing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Campo {
    private final int row;
    private final int col;

    private boolean minado = false;
    private boolean aberto = false;
    private boolean marcado = false;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    public void registrarObservador(CampoObservador observador){
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvento evento){
        observadores.stream().forEach(observador -> observador.eventoOcorreu(this, evento));
    }
    public Campo(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isMarcado() { return marcado; }

    public boolean isAberto() { return aberto; }

    boolean addVizinho(Campo campo) {
        boolean isDiagonal = ((row != campo.getRow()) && (col != campo.getCol()));
        int delta = Math.abs(row - campo.getRow()) + Math.abs(col - campo.getCol());
        if (isDiagonal) {   //vizinho diagonal
            if (delta == 2) {
                vizinhos.add(campo);
                return true;
            }
        } else if(delta == 1){  //vizinho lateral
            vizinhos.add(campo);
            return true;
        }
        return false;
    }

    public void alternarMarcacao(){
        if (!isAberto()){
            marcado = !marcado;
            if (marcado){
                notificarObservadores(CampoEvento.MARCAR);

            }else{
                notificarObservadores(CampoEvento.DESMARCAR);
            }

        }
    }


    void minar(){
        minado = true;
    }

    public boolean vizinhancaSegura(){
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    public boolean abrir(){
        if(!aberto && !marcado){

            if (minado){    //fim de jogo
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }
            setAberto(true);
            if (vizinhancaSegura()){
                    vizinhos.forEach(v -> v.abrir());
                }
            return true;
        }else
            return false;
    }

    boolean objetivoAlcancado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    public long minasNaVizinhanca(){
        return vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }

    @Override
    public String toString() {
        if (marcado){
            return "x";
        } else if (aberto && minado){
            return "*";
        }else if( aberto && minasNaVizinhanca()>0){
            return Long.toString(minasNaVizinhanca());
        }else if(aberto){
            return " ";
        }else
            return "?";
    }

    public boolean isMinado() {
        return minado;
    }

    void setAberto(boolean option){
        aberto = option;
        if(option)
            notificarObservadores(CampoEvento.ABRIR);
    }
}

