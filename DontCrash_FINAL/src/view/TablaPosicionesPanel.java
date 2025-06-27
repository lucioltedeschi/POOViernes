package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class TablaPosicionesPanel extends JPanel {
    private JFrame frame;
    private JTable tabla;
    private JPanel panelAnterior;
    private DefaultTableModel model;
    private int dificultadActual = 1; // 1 = Fácil, 2 = Normal, 3 = Difícil

    public TablaPosicionesPanel(JFrame frame, JPanel panelAnterior) {
        this.frame = frame;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.lightGray);


        // Panel superior con botones de dificultad
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnFacil = new JButton("Fácil");
        JButton btnNormal = new JButton("Normal");
        JButton btnDificil = new JButton("Difícil");

        ActionListener dificultadListener = e -> {
            String cmd = ((JButton) e.getSource()).getText();
            switch (cmd) {
                case "Fácil": dificultadActual = 1; break;
                case "Normal": dificultadActual = 2; break;
                case "Difícil": dificultadActual = 3; break;
            }
            cargarTabla();
        };

        btnFacil.addActionListener(dificultadListener);
        btnNormal.addActionListener(dificultadListener);
        btnDificil.addActionListener(dificultadListener);

        topPanel.add(new JLabel("Seleccione dificultad:"));
        topPanel.setBackground(Color.lightGray);
        topPanel.add(btnFacil);
        topPanel.add(btnNormal);
        topPanel.add(btnDificil);
        add(topPanel, BorderLayout.NORTH);

        // Tabla de puntajes
        model = new DefaultTableModel(new String[]{"Nombre", "Puntaje"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(model);

        // Estilo visual
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 16));            // Fuente
        tabla.setRowHeight(28);                                         // Altura de filas
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));  // Encabezado
        tabla.getTableHeader().setBackground(Color.DARK_GRAY);          // Fondo encabezado
        tabla.getTableHeader().setForeground(Color.WHITE);              // Texto encabezado
        tabla.setBackground(Color.LIGHT_GRAY);                          // Fondo tabla
        tabla.setForeground(Color.BLACK);                               // Color texto
        tabla.setGridColor(Color.GRAY);                                 // Líneas de separación
        tabla.setFillsViewportHeight(true);                             // Ajuste automático

        // alineado //
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tabla.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        DefaultTableCellRenderer nombreRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent
                    (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        setBackground(new Color(255, 255, 255, 20)); // blanco semi-transparente
                    } else {
                        setBackground(new Color(240, 240, 240)); // gris claro
                    }
                    setForeground(Color.BLACK);
                }
                return this;
            }
        };

        DefaultTableCellRenderer puntajeRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        setBackground(new Color(255, 255, 255, 20)); // blanco semi-transparente
                    } else {
                        setBackground(new Color(240, 240, 240)); // gris claro
                    }
                    setForeground(Color.BLACK);
                }
                return this;
            }
        };

        tabla.getColumnModel().getColumn(0).setCellRenderer(nombreRenderer);
        tabla.getColumnModel().getColumn(1).setCellRenderer(puntajeRenderer);

        // Botón para volver
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(panelAnterior, BorderLayout.NORTH);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
        });

        // ESTILOS //

        JButton[] botones = {btnVolver, btnFacil, btnNormal, btnDificil};

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
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnVolver);
        bottomPanel.setBackground(Color.lightGray);
        add(bottomPanel, BorderLayout.SOUTH);



        // Cargar tabla inicial (Fácil)
        cargarTabla();
    }

    private void cargarTabla() {
        model.setRowCount(0);
        File archivo = new File("puntajes.txt");
        if (!archivo.exists()) return;

        List<Puntaje> puntajes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 3) {
                    String nombre = partes[0].trim();
                    int puntaje;
                    try {
                        puntaje = Integer.parseInt(partes[1].trim());
                    } catch (NumberFormatException ex) {
                        continue;
                    }
                    int dificultad = dificultadStringAInt(partes[2].trim());
                    if (dificultad == dificultadActual) {
                        puntajes.add(new Puntaje(nombre, puntaje));
                    }
                }
            }

            puntajes.sort((p1, p2) -> Integer.compare(p2.puntaje, p1.puntaje));
            int max = Math.min(10, puntajes.size());
            for (int i = 0; i < max; i++) {
                Puntaje p = puntajes.get(i);
                model.addRow(new Object[]{p.nombre, p.puntaje});
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDificultad(int dificultad) {
        this.dificultadActual = dificultad;
        cargarTabla();
    }

    private int dificultadStringAInt(String str) {
        switch (str.toUpperCase()) {
            case "FÁCIL":
            case "FACIL":
                return 1;
            case "NORMAL":
                return 2;
            case "DIFÍCIL":
            case "DIFICIL":
                return 3;
            default:
                return 2;
        }
    }

    private static class Puntaje {
        String nombre;
        int puntaje;

        Puntaje(String nombre, int puntaje) {
            this.nombre = nombre;
            this.puntaje = puntaje;
        }
    }
}
