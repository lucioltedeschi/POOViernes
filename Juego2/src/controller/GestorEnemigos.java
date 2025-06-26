package controller;

import model.Enemigo;
import model.Jugador;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GestorEnemigos {
    private ArrayList<Enemigo> enemigos = new ArrayList<>();
    private Random rand = new Random();

    public void generarEnemigo(int panelWidth, int anchoCarril, int jugadorWidth, int jugadorHeight) {
        if (anchoCarril == 0) return;
        if (rand.nextInt(40) == 0) {
            int carril = rand.nextInt(panelWidth / anchoCarril);
            int x = carril * anchoCarril;
            enemigos.add(new Enemigo(x, -100, jugadorWidth, jugadorHeight));
        }
    }

    public void moverYEliminar(int velocidad, int panelHeight) {
        Iterator<Enemigo> it = enemigos.iterator();
        while (it.hasNext()) {
            Enemigo e = it.next();
            e.mover(velocidad);
            if (e.getY() > panelHeight) {
                it.remove();
            }
        }
    }

    public boolean hayColision(Jugador jugador) {
        for (Enemigo e : enemigos) {
            if (e.getLimites().intersects(jugador.getLimites())) return true;
        }
        return false;
    }

    public void dibujarEnemigos(Graphics g) {
        for (Enemigo e : enemigos) {
            e.dibujar(g);
        }
    }
}
