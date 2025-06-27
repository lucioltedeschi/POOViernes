package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BreakoutPanel extends JPanel implements ActionListener, KeyListener {
    private JuegoBreakout juego;
    private Timer timer;
    private String nombreJugador;
    private int puntajeAnterior;
    private JFrame frame;
    private int dificultad;

    private boolean izquierdaPresionada = false;
    private boolean derechaPresionada = false;


    public BreakoutPanel(JFrame frame, String nombreJugador, int puntajeAnterior, int dificultad) {
        this.frame = frame;
        this.nombreJugador = nombreJugador;
        this.puntajeAnterior = puntajeAnterior;
        this.dificultad = dificultad;

        setPreferredSize(new Dimension(400, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        int ancho = 400;
        int alto = 500;

        juego = new JuegoBreakout(dificultad, ancho,alto);

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
        g.fillRect(juego.getPaleta().getX(), juego.getPaleta().getY(), juego.getPaleta().getAncho(), juego.getPaleta().getAlto());

        g.setColor(Color.RED);
        g.fillOval(juego.getPelota().getX(), juego.getPelota().getY(), juego.getPelota().getAncho(), juego.getPelota().getAlto());

        g.setColor(Color.BLUE);
        for (Bloque b : juego.getBloques()) {
            g.fillRect(b.getX(), b.getY(), b.getAncho(), b.getAlto());
        }

        g.setColor(Color.WHITE);
        g.drawString("Vidas: " + juego.getVidas(), 10, 15);
        g.drawString("Nivel: " + juego.getNivel(), 240, 15);

        if (juego.isJuegoTerminado()) {
            if (juego.isGano()) {
                // Volver a GamePanel pero con yaUsoBreakout=true para no ofrecer breakout otra vez
                GamePanel gamePanel = new GamePanel(nombreJugador, frame, dificultad);
                gamePanel.setPuntajeInicial(puntajeAnterior);
                gamePanel.setYaUsoBreakout(true);

                frame.getContentPane().removeAll();
                frame.getContentPane().add(gamePanel);
                frame.revalidate();
                frame.repaint();
                frame.pack();
                frame.setLocationRelativeTo(null);
                gamePanel.requestFocusInWindow();
            } else {
                // Perdió en breakout: fin definitivo, sin opción a volver a jugarlo
                JOptionPane.showMessageDialog(this, "¡Perdiste el Breakout! Puntaje final: " + puntajeAnterior);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new MenuPrincipalPanel(frame));
                frame.revalidate();
                frame.repaint();
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!juego.isJuegoTerminado()) {
            juego.actualizar(getWidth(), getHeight());
        }
        if (izquierdaPresionada) {
            juego.getPaleta().moverIzquierda();
        }
        if (derechaPresionada) {
            juego.getPaleta().moverDerecha(getWidth());
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (juego.isJuegoTerminado() && e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (juego.isGano()) {
                GamePanel gamePanel = new GamePanel(nombreJugador, frame, dificultad);
                gamePanel.setPuntajeInicial(puntajeAnterior);

                frame.getContentPane().removeAll();
                frame.getContentPane().add(gamePanel);
                frame.revalidate();
                frame.repaint();
                frame.pack();
                frame.setLocationRelativeTo(null);
                gamePanel.requestFocusInWindow();
            } else {
                guardarPuntaje(); // <- Este faltaba
                JOptionPane.showMessageDialog(this, "¡Perdiste el Breakout! Puntaje final: " + puntajeAnterior);
                volverAlMenu();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                izquierdaPresionada = true;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                derechaPresionada = true;
            }
        }
    }

    private void guardarPuntaje() {
        File archivo = new File("puntajes.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            String difTexto = switch (dificultad) {
                case 1 -> "FÁCIL";
                case 3 -> "DIFÍCIL";
                default -> "NORMAL";
            };
            writer.write(nombreJugador + ": " + puntajeAnterior + ": " + difTexto);
            writer.newLine();
            System.out.println("Puntaje guardado: " + nombreJugador + " - " + puntajeAnterior + " - " + difTexto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void volverAlMenu() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new MenuPrincipalPanel(frame));
        frame.revalidate();
        frame.repaint();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            izquierdaPresionada = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            derechaPresionada = false;
        }
    }
    @Override public void keyTyped(KeyEvent e) {}
}
