package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class InicioPanel extends JPanel {
    private JButton jugarButton, tablaButton, ajustesButton;
    private JFrame frame;

    // Dificultad guardada (1=Facil, 2=Normal, 3=Dificil)
    private int dificultad;

    public InicioPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Don´t crash");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titulo.setForeground(Color.BLUE); // o colores llamativos

        jugarButton = new JButton("JUGAR");
        tablaButton = new JButton("TABLA DE POSICIONES");
        ajustesButton = new JButton("AJUSTES");

        cargarDificultad();

        jugarButton.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Ingrese su nombre:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                GamePanel juego = new GamePanel(nombre.trim(), frame, dificultad);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(juego);
                frame.revalidate();
                frame.repaint();
                frame.pack();
                juego.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre para jugar.");
            }
        });

        tablaButton.addActionListener(e -> {
            TablaPosicionesPanel tabla = new TablaPosicionesPanel(frame, this);
            tabla.setDificultad(dificultad);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(tabla);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            tabla.requestFocusInWindow();
        });

        ajustesButton.addActionListener(e -> {
            String difStr = dificultadIntToString(dificultad);
            AjustesPanel ajustes = new AjustesPanel(frame, difStr, this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(ajustes);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            ajustes.requestFocusInWindow();
        });

        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titulo, gbc);

        gbc.gridy++;
        add(jugarButton, gbc);
        gbc.gridy++;
        add(tablaButton, gbc);
        gbc.gridy++;
        add(ajustesButton, gbc);
    }

    private void cargarDificultad() {
        File config = new File("config.txt");
        if (config.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(config))) {
                String linea = br.readLine();
                dificultad = Integer.parseInt(linea.trim());
                if (dificultad < 1 || dificultad > 3) dificultad = 2; // normal default
            } catch (Exception e) {
                dificultad = 2;
            }
        } else {
            dificultad = 2;
        }
    }
    public static int dificultadStringAInt(String dificultad) {
        switch (dificultad.toUpperCase()) {
            case "FACIL": return 1;
            case "NORMAL": return 2;
            case "DIFICIL": return 3;
            default: return 2; // valor por defecto: Normal
        }
    }

    private String dificultadIntToString(int dificultad) {
        switch (dificultad) {
            case 1: return "Fácil";
            case 2: return "Normal";
            case 3: return "Difícil";
            default: return "Normal"; // o algún valor por defecto
        }
    }

    public void guardarDificultad(int nuevaDif) {
        // Guardar en archivo config.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"))) {
            bw.write(String.valueOf(nuevaDif));
            this.dificultad = nuevaDif;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
