package model;

import java.awt.*;

abstract class Entidad implements Colision {
    protected int x, y, ancho, alto;

    public Entidad(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public Rectangle getRect() {
        return new Rectangle(x, y, ancho, alto);
    }
}