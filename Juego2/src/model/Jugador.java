package model;

import javax.swing.*;
import java.awt.*;

public class Jugador extends Personaje {
    private Image imagen;

    public Jugador(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.imagen = new ImageIcon("src/resources/playerModel.png").getImage();
    }

    public void mover(boolean izquierda, boolean derecha, int panelWidth, int velocidad) {
        if (izquierda && x > 0) x -= velocidad;
        if (derecha && x < panelWidth - width) x += velocidad;
    }

    @Override
    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, width, height, null);
    }
}
