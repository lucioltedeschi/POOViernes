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
    private int velocidad = 5;
    private boolean leftPressed = false, rightPressed = false;
    private Image fondoOriginal;
    private Image fondoEscalado;
    private int fondoY = 0;

    private int puntaje = 0;
    private String nombreJugador;
    private JFrame frame;

    public GamePanel(String nombreJugador, JFrame frame) {
        this.nombreJugador = nombreJugador;
        this.frame = frame;

        setPreferredSize(new Dimension(400, 500));
        setBackground(Color.white);
        setFocusable(true);
        addKeyListener(this);

        fondoOriginal = new ImageIcon(getClass().getResource("/resources/background.jpg")).getImage();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fondoEscalado = fondoOriginal.getScaledInstance(getWidth(), -1, Image.SCALE_SMOOTH);
                requestFocusInWindow();
            }
        });

        jugador = new Jugador(150, 400, 50, 100);
        gestorEnemigos = new GestorEnemigos();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (getWidth() > 0) {
                    fondoEscalado = fondoOriginal.getScaledInstance(getWidth(), -1, Image.SCALE_SMOOTH);
                }
                requestFocusInWindow();
            }
        });

        timer = new Timer(16, this);
        timer.start();

        // TICKS DE PUNTAJE //

        scoreTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puntaje++;
            }
        });
        scoreTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarFondo(g);
        jugador.dibujar(g);
        gestorEnemigos.dibujarEnemigos(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Puntaje: " + puntaje, 10, 20);
    }

    private void dibujarFondo(Graphics g) {
        if (fondoEscalado == null || fondoEscalado.getHeight(null) <= 0) return;

        int imgHeight = fondoEscalado.getHeight(null);
        int panelHeight = getHeight();

        int y = fondoY % imgHeight;

        for (int i = -1; i <= panelHeight / imgHeight + 1; i++) {
            g.drawImage(fondoEscalado, 0, y + i * imgHeight, null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fondoY += velocidad;

        jugador.mover(leftPressed, rightPressed, getWidth(), velocidad);
        gestorEnemigos.generarEnemigo(getWidth(), getWidth() / 4, 50, 100);
        gestorEnemigos.moverYEliminar(velocidad, getHeight());

        if (gestorEnemigos.hayColision(jugador)) {
            timer.stop();
            scoreTimer.stop();

            int opcion = JOptionPane.showOptionDialog(
                    this,
                    "¡Chocaste! Puntaje: " + puntaje + "\n¿Querés intentar recuperarte en el Breakout?",
                    "Fin del juego de autos",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {"Jugar Breakout", "Terminar"},
                    "Jugar Breakout"
            );

            if (opcion == JOptionPane.YES_OPTION) {
                // Cambiar al panel Breakout
                Panel breakoutPanel = new Panel();

                frame.getContentPane().removeAll();
                frame.getContentPane().add(breakoutPanel);
                frame.revalidate();
                frame.repaint();
                frame.pack();
                frame.setLocationRelativeTo(null);

                breakoutPanel.requestFocusInWindow();

            } else {
                guardarPuntaje(nombreJugador, puntaje);
                JOptionPane.showMessageDialog(this, "Puntaje final: " + puntaje);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new InicioPanel(frame));
                frame.revalidate();
                frame.repaint();
            }
        }


        repaint();
    }

    private void guardarPuntaje(String nombre, int puntaje) {
        File archivo = new File("puntajes.txt");
        String[] nombres = new String[1000];
        int[] puntajesArr = new int[1000];
        int cantidad = 0;

        if (archivo.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(":");
                    if (partes.length == 2) {
                        String n = partes[0].trim();
                        int p = 0;
                        try {
                            p = Integer.parseInt(partes[1].trim());
                        } catch (NumberFormatException ex) {
                            p = 0;
                        }
                        nombres[cantidad] = n;
                        puntajesArr[cantidad] = p;
                        cantidad++;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

        boolean nuevoRecord = false;
        boolean encontrado = false;
        for (int i = 0; i < cantidad; i++) {
            if (nombres[i].equals(nombre)) {
                encontrado = true;
                if (puntaje > puntajesArr[i]) {
                    puntajesArr[i] = puntaje;
                    nuevoRecord = true;
                }
                break;
            }
        }
        if (!encontrado) {
            nombres[cantidad] = nombre;
            puntajesArr[cantidad] = puntaje;
            cantidad++;
            nuevoRecord = true;
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(archivo));
            for (int i = 0; i < cantidad; i++) {
                writer.write(nombres[i] + ": " + puntajesArr[i]);
                writer.newLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }

        if (nuevoRecord) {
            JOptionPane.showMessageDialog(this, "¡Nuevo récord personal de " + puntaje + " puntos!");
        }
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
    public void keyTyped(KeyEvent e) {}
}
