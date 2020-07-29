package br.com.simao.campo_minado_swing.view;

import br.com.simao.campo_minado_swing.model.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {
    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(tabuleiro.getRows(),tabuleiro.getCols()));
        tabuleiro.forEach(c -> add(new BotaoCampo(c)));
        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() ->{
                if(e.isGanhou()){
                    JOptionPane.showMessageDialog(this, "Ganhou!!");
                }else{
                    JOptionPane.showMessageDialog(this, "Perdeu!!");
                }
                tabuleiro.reiniciar();
            } );
        });
    }
}
