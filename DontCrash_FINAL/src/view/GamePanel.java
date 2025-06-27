package view;

import controller.GestorEnemigos;
import model.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Jugador jugador;
    private GestorEnemigos gestorEnemigos;
    private Timer timer, scoreTimer;
    private int velocidad;
    private boolean leftPressed = false, rightPressed = false;

    private Image fondo;
    private int fondoY = 0;

    private int puntaje = 0;
    private int proximoAumento = 10;
    private String nombreJugador;
    private JFrame frame;
    private boolean yaUsoBreakout = false;

    private int dificultad;

    public GamePanel(String nombreJugador, JFrame frame, int dificultad) {
        this.nombreJugador = nombreJugador;
        this.frame = frame;
        this.dificultad = dificultad;

        setPreferredSize(new Dimension(400, 500));
        setBackground(Color.lightGray);
        setFocusable(true);
        fondo = new ImageIcon(getClass().getResource("/resources/background.JPG")).getImage();
        addKeyListener(this);


        // Ajustar velocidad según dificultad
        switch (dificultad) {
            case 1 -> velocidad = 3; // Fácil
            case 3 -> velocidad = 7; // Difícil
            default -> velocidad = 5; // Normal
        }

        jugador = new Jugador(150, 400, 50, 100);
        gestorEnemigos = new GestorEnemigos();

        timer = new Timer(16, this);
        timer.start();

        scoreTimer = new Timer(1000, e -> {
            puntaje++;
            if (puntaje >= proximoAumento) {
                velocidad++;
                proximoAumento += 10;
            }
        });
        scoreTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar fondo adaptado al tamaño del panel
        if (fondo != null) {
            int ancho = getWidth();
            int alto = getHeight();

            // Altura real de la imagen
            int fondoAltoReal = fondo.getHeight(null);

            // Dibujar dos veces para efecto de scroll vertical
            g.drawImage(fondo, 0, fondoY, ancho, alto, null);
            g.drawImage(fondo, 0, fondoY - alto, ancho, alto, null);

            fondoY += velocidad / 2;
            if (fondoY >= alto) fondoY = 0;
        }

        // Dibujar jugador y enemigos
        jugador.dibujar(g);
        gestorEnemigos.dibujarEnemigos(g);

        // Dibujar puntaje
        g.setColor(Color.BLACK);
        g.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g.drawString("Puntaje: " + puntaje, 10, 20);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        jugador.mover(leftPressed, rightPressed, getWidth(), velocidad);
        gestorEnemigos.generarEnemigo(getWidth(), getWidth() / 4, 50, 100);
        gestorEnemigos.moverYEliminar(velocidad, getHeight());

        if (gestorEnemigos.hayColision(jugador)) {
            timer.stop();
            scoreTimer.stop();

            if (!yaUsoBreakout) {
                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "¡Chocaste! Puntaje: " + puntaje + "\n¿Querés intentar recuperarte en el Breakout?",
                        "Fin del juego de autos",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Jugar Breakout", "Terminar"},
                        "Jugar Breakout"
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    BreakoutPanel breakoutPanel = new BreakoutPanel(frame, nombreJugador, puntaje, dificultad);
                    setYaUsoBreakout(true);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(breakoutPanel);
                    frame.revalidate();
                    frame.repaint();
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    breakoutPanel.requestFocusInWindow();
                } else {
                    guardarPuntaje();
                    volverAlMenu();
                }
            } else {
                guardarPuntaje();
                JOptionPane.showMessageDialog(this,
                        "¡Fin del juego!\nYa habías usado tu oportunidad de recuperación.\nPuntaje final: " + puntaje);
                volverAlMenu();
            }
        }

        repaint();
    }

    private void guardarPuntaje() {
        File archivo = new File("puntajes.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            // Guardar en formato: Nombre: puntaje: DIFICULTAD
            String difTexto = switch (dificultad) {
                case 1 -> "FÁCIL";
                case 3 -> "DIFÍCIL";
                default -> "NORMAL";
            };
            writer.write(nombreJugador + ": " + puntaje + ": " + difTexto);
            writer.newLine();
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void setYaUsoBreakout(boolean yaUsoBreakout) {
        this.yaUsoBreakout = yaUsoBreakout;
    }

    public void setPuntajeInicial(int puntajeInicial) {
        this.puntaje = puntajeInicial;
    }

}
