package view;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MenuPrincipalPanel extends JPanel {
    private JFrame frame;
    private JButton jugarBtn, tablaBtn, ajustesBtn;
    private int dificultad;
    private final String CONFIG_FILE = "config.txt";

    public MenuPrincipalPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // espaciado general

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        JLabel logoLabel = new JLabel(logoIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(logoLabel, gbc);

        // Título
        JLabel titulo = new JLabel("");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        gbc.gridy++;
        add(titulo, gbc);

        // Botones
        jugarBtn = new JButton("Jugar");
        tablaBtn = new JButton("Tabla de Posiciones");
        ajustesBtn = new JButton("Ajustes");

        JButton[] botones = {jugarBtn, tablaBtn, ajustesBtn};
        for (JButton btn : botones) {
            btn.setBackground(Color.GRAY);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Fuente, estilo y tamanio
            btn.setPreferredSize(new Dimension(250, 50));  // Tamaño fijo más grande
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);
            btn.setBorderPainted(true);
            btn.setMargin(new Insets(10, 20, 10, 20)); // Padding interno
        }
        // Leer dificultad guardada
        dificultad = cargarDificultad();

        jugarBtn.addActionListener(e -> {
            // Pide nombre al jugar
            String nombre = JOptionPane.showInputDialog(this, "Ingrese su nombre:");
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre para jugar.");
                return;
            }
            nombre = nombre.trim();

            GamePanel gamePanel = new GamePanel(nombre, frame, dificultad);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(gamePanel);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
            gamePanel.requestFocusInWindow();
        });

        tablaBtn.addActionListener(e -> {
            TablaPosicionesPanel tablaPanel = new TablaPosicionesPanel(frame, this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(tablaPanel);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
            tablaPanel.requestFocusInWindow();
        });

        ajustesBtn.addActionListener(e -> {
            AjustesPanel ajustesPanel = new AjustesPanel(frame, dificultadIntAString(dificultad), this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(ajustesPanel);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
            ajustesPanel.requestFocusInWindow();
        });


        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0; gbc.gridy = 0;
        add(titulo, gbc);
        gbc.gridy++;
        add(jugarBtn, gbc);
        gbc.gridy++;
        add(tablaBtn, gbc);
        gbc.gridy++;
        add(ajustesBtn, gbc);
    }

    public int getDificultad() {
        return dificultad;
    }

    // Lee dificultad desde archivo config.txt y devuelve int
    private int cargarDificultad() {
        File f = new File(CONFIG_FILE);
        if (!f.exists()) return 2; // default normal

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String dif = br.readLine();
            if (dif != null) {
                return dificultadStringAInt(dif.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2;
    }

    // Guarda dificultad como texto en config.txt
    public void guardarDificultad(int dificultad) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write(dificultadIntAString(dificultad));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dificultad = dificultad;
    }

    // Convierte string a int
    public static int dificultadStringAInt(String dificultad) {
        switch (dificultad.toUpperCase()) {
            case "FACIL": return 1;
            case "NORMAL": return 2;
            case "DIFICIL": return 3;
            default: return 2; // normal por defecto
        }
    }

    // Convierte int a string
    public static String dificultadIntAString(int dificultad) {
        switch (dificultad) {
            case 1: return "FACIL";
            case 2: return "NORMAL";
            case 3: return "DIFICIL";
            default: return "NORMAL";
        }
    }
}
