package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JPanel implements ActionListener, KeyListener {
    private JuegoBreakout juego;
    private Timer timer;
    private String nombreJugador;
    private int puntaje;
    private JFrame frame;

    public Panel(String nombreJugador, int puntaje, JFrame frame) {
        juego = new JuegoBreakout();

        this.nombreJugador = nombreJugador;
        this.puntaje = puntaje;
        this.frame = frame;

        setPreferredSize(new Dimension(400, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(juego.paleta.getX(), juego.paleta.getY(), juego.paleta.getAncho(), juego.paleta.getAlto());

        g.setColor(Color.RED);
        g.fillOval(juego.pelota.getX(), juego.pelota.getY(), juego.pelota.getAncho(), juego.pelota.getAlto());

        g.setColor(Color.BLUE);
        for (Bloque b : juego.bloques) {
            g.fillRect(b.getX(), b.getY(), b.getAncho(), b.getAlto());
        }

        g.setColor(Color.WHITE);
        g.drawString("Vidas: " + juego.vidas, 10, 15);
        g.drawString("Nivel: " + juego.nivel, 240, 15);

        if (juego.juegoTerminado) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            String msg = juego.gano ? "¡Ganaste!" : "¡Perdiste!";
            g.drawString(msg + " REINICIA CON ENTER", 20, juego.ALTO / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!juego.juegoTerminado) {
            juego.actualizar();
            repaint();
        } else {
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (juego.juegoTerminado && e.getKeyCode() == KeyEvent.VK_ENTER) {
            juego.reiniciarJuego();
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                juego.paleta.moverIzquierda();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                juego.paleta.moverDerecha(juego.ANCHO);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}

