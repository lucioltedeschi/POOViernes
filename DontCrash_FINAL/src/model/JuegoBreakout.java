package model;

import java.util.ArrayList;

public class JuegoBreakout {
    Pelota pelota;
    Paleta paleta;
    ArrayList<Bloque> bloques;
    final int ANCHO = 300;
    final int ALTO = 400;
    int nivel = 1;
    int vidas;
    boolean juegoTerminado = false;
    boolean gano = false;

    private int cantidadBloques;

    public JuegoBreakout(int dificultad, int ANCHO, int ALTO) {
        // Definir cantidad bloques según dificultad
        switch (dificultad) {
            case 1: cantidadBloques = 10; vidas = 3; break;     // Fácil
            case 2: cantidadBloques = 20; vidas = 2; break;     // Normal
            case 3: cantidadBloques = 40; vidas = 1; break;     // Difícil
            default: cantidadBloques = 20; vidas = 2; break;
        }
        iniciarNivel(ANCHO, ALTO);
    }

    private void iniciarNivel(int anchoPanel, int altoPanel) {
        pelota = new Pelota(anchoPanel / 2 - 10, altoPanel - 100);
        paleta = new Paleta(anchoPanel / 2 - 30, altoPanel - 50);
        bloques = new ArrayList<>();

        int anchoBloque = 50;
        int altoBloque = 20;
        int espacioX = 5;
        int espacioY = 5;
        int inicioX = 10;
        int inicioY = 40;

        // Cuántos bloques caben horizontalmente según el ancho del panel
        int bloquesPorFila = (anchoPanel - inicioX) / (anchoBloque + espacioX);
        if (bloquesPorFila == 0) bloquesPorFila = 1; // Al menos 1 bloque

        // Cuántas filas necesarias para la cantidad total de bloques
        int filas = (int) Math.ceil((double)cantidadBloques / bloquesPorFila);

        int bloquesCreados = 0;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < bloquesPorFila; col++) {
                if (bloquesCreados >= cantidadBloques) break;

                int x = inicioX + col * (anchoBloque + espacioX);
                int y = inicioY + fila * (altoBloque + espacioY);

                bloques.add(new Bloque(x, y));
                bloquesCreados++;
            }
        }
    }


    public void actualizar(int anchoPanel, int altoPanel) {
        pelota.mover();
        // Colisiones con bloques
        for (int i = 0; i < bloques.size(); i++) {
            Bloque b = bloques.get(i);
            if (pelota.getRect().intersects(b.getRect())) {
                bloques.remove(i);
                System.out.println("Bloque eliminado, bloques restantes: " + bloques.size());
                pelota.invertirDireccionY();
                break;
            }
        }

        if (bloques.isEmpty()) {
            juegoTerminado = true;
            gano = true;
        }

        if (pelota.getRect().intersects(paleta.getRect())) {
            pelota.invertirDireccionY();
        }

        if (pelota.getX() <= 0 || pelota.getX() + pelota.getAncho() >= anchoPanel) {
            pelota.invertirDireccionX();
        }


        if (pelota.getY() <= 0) {
            pelota.invertirDireccionY();
        } else if (pelota.getY() + pelota.getAlto() >= altoPanel) {
            vidas--;
            if (vidas <= 0) {
                juegoTerminado = true;
            } else {
                pelota = new Pelota(150, 300);
            }
        }
    }


    public void reiniciarJuego() {
        juegoTerminado = false;
        gano = false;
        vidas = (cantidadBloques == 10) ? 3 : (cantidadBloques == 20) ? 2 : 1;
        iniciarNivel(ANCHO, ALTO);
    }

    public Pelota getPelota() {
        return pelota;
    }

    public void setPelota(Pelota pelota) {
        this.pelota = pelota;
    }

    public Paleta getPaleta() {
        return paleta;
    }

    public void setPaleta(Paleta paleta) {
        this.paleta = paleta;
    }

    public ArrayList<Bloque> getBloques() {
        return bloques;
    }

    public void setBloques(ArrayList<Bloque> bloques) {
        this.bloques = bloques;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public boolean isGano() {
        return gano;
    }

    public void setGano(boolean gano) {
        this.gano = gano;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }

    public int getALTO() {
        return ALTO;
    }

    public int getANCHO() {
        return ANCHO;
    }

    public int getCantidadBloques() {
        return cantidadBloques;
    }

    public void setCantidadBloques(int cantidadBloques) {
        this.cantidadBloques = cantidadBloques;
    }
}
