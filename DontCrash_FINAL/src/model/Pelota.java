package model;

public class Pelota extends Entidad implements Movible {
    private int dx = 3, dy = -3;

    public Pelota(int x, int y) {
        super(x, y, 10, 10);
    }

    public void setVelocidad(int v) {
        int signoX = (dx < 0) ? -1 : 1;
        int signoY = (dy < 0) ? -1 : 1;
        dx = signoX * v;
        dy = signoY * v;
    }

    public void mover() {
        x += dx;
        y += dy;
    }

    public void invertirDireccionX() {
        dx = -dx;
    }

    public void invertirDireccionY() {
        dy = -dy;
    }
}
