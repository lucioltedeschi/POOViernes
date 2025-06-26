package model;

import javax.swing.*;
import java.awt.*;

public class Enemigo extends Personaje {
    private Image imagen;

    public Enemigo(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.imagen = new ImageIcon("src/resources/enemieModel.png").getImage();
    }

    public void mover(int velocidad) {
        y += velocidad;
    }

    @Override
    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, width, height, null);
    }
}
