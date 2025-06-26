package model;

class Paleta extends Entidad implements Movible {
    public Paleta(int x, int y) {
        super(x, y, 60, 10);
    }

    @Override
    public void mover() {
        // Podés dejarlo vacío o hacer que por defecto se mueva a la derecha, según necesites
    }

    public void moverIzquierda() {
        x = Math.max(x - 15, 0);
    }

    public void moverDerecha(int limite) {
        x = Math.min(x + 15, limite - ancho);
    }
}
