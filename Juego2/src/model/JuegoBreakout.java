package model;

import java.awt.*;
import java.util.ArrayList;
class JuegoBreakout {
    Pelota pelota;
    Paleta paleta;
    ArrayList<Bloque> bloques;
    final int ANCHO = 300;
    final int ALTO = 400;
    int nivel = 1;
    int vidas = 1;
    boolean juegoTerminado = false;
    boolean gano = false;


    public JuegoBreakout() {
        iniciarNivel();
    }

    private void iniciarNivel() {
        pelota = new Pelota(150, 300);
        paleta = new Paleta(120, 350);
        bloques = new ArrayList<>();

        int filas = nivel;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < 5; j++) {
                bloques.add(new Bloque(60 * j + 10, 30 + i * 25));
            }
        }
    }

    public void actualizar() {
        pelota.mover();
        Rectangle pelotaRect = pelota.getRect();

        for (int i = 0; i < bloques.size(); i++) {
            Bloque b = bloques.get(i);
            if (pelotaRect.intersects(b.getRect())) {
                bloques.remove(i);
                pelota.invertirDireccionY();
                break;
            }
        }

        if (bloques.isEmpty()) {
            if (nivel < 3) {
                nivel++;
                iniciarNivel();
            } else {
                juegoTerminado = true;
                gano = true;
            }
        }

        if (pelotaRect.intersects(paleta.getRect())) {
            pelota.invertirDireccionY();
        }

        if (pelota.getX() <= 0 || pelota.getX() + pelota.getAncho() >= ANCHO) {
            pelota.invertirDireccionX();
        }

        if (pelota.getY() <= 0) {
            pelota.invertirDireccionY();
        } else if (pelota.getY() + pelota.getAlto() >= ALTO) {
            vidas--;
            if (vidas <= 0) {
                juegoTerminado = true;
            } else {
                pelota = new Pelota(150, 300);
            }
        }
    }

    public void reiniciarJuego() {
        nivel = 1;
        vidas = 1;
        juegoTerminado = false;
        gano = false;
        iniciarNivel();
    }
}
