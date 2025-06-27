package model;

import java.awt.*;

public abstract class Personaje {
    protected int x, y, width, height;

    public Personaje(int x, int y, int width, int height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    public abstract void dibujar(Graphics g);

    public Rectangle getLimites() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
}
