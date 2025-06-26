package model;

class Pelota extends Entidad implements Movible {
    private int dx = 5, dy = -5;

    public Pelota(int x, int y) {
        super(x, y, 10, 10);
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