package model;

public class Paleta extends Entidad implements Movible {
    public Paleta(int x, int y) {
        super(x, y, 60, 10);
    }

    @Override
    public void mover() {
        // No se usa mover directo; se usan métodos para mover izquierda/derecha
    }

    public void moverIzquierda() {
        x = Math.max(x - 15, 0);
    }

    public void moverDerecha(int limite) {
        x = Math.min(x + 15, limite - ancho);
    }
}
