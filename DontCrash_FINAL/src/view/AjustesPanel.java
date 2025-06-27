package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AjustesPanel extends JPanel {
    private JFrame frame;
    private JComboBox<String> comboDificultad;
    private JButton guardarBtn, volverBtn;
    private JPanel panelAnterior;  // Guardamos el panel anterior para volver
    private String dificultadActual;

    public AjustesPanel(JFrame frame, String dificultad, JPanel panelAnterior) {
        this.frame = frame;
        this.panelAnterior = panelAnterior;
        this.dificultadActual = dificultad;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(Color.lightGray);


        JLabel titulo = new JLabel("Ajustes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel label = new JLabel("Seleccione dificultad:");

        comboDificultad = new JComboBox<>(new String[] {"FACIL", "NORMAL", "DIFICIL"});
        comboDificultad.setSelectedItem(dificultadActual);

        guardarBtn = new JButton("Guardar");
        volverBtn = new JButton("Volver");

        guardarBtn.addActionListener(e -> {
            String nuevaDif = (String) comboDificultad.getSelectedItem();
            int difInt = MenuPrincipalPanel.dificultadStringAInt(nuevaDif);

            // Intentamos castear el panelAnterior para llamar guardarDificultad
            try {
                if (panelAnterior instanceof MenuPrincipalPanel) {
                    ((MenuPrincipalPanel) panelAnterior).guardarDificultad(difInt);
                } else if (panelAnterior instanceof InicioPanel) {
                    ((InicioPanel) panelAnterior).guardarDificultad(difInt);
                } else {
                    // Si hay otro panel que implemente guardarDificultad, agregar aquí
                    JOptionPane.showMessageDialog(this, "No se puede guardar dificultad en el panel actual.");
                    return;
                }
                JOptionPane.showMessageDialog(this, "Dificultad guardada: " + nuevaDif);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar dificultad: " + ex.getMessage());
            }
        });

        volverBtn.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(panelAnterior);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
        });


        // ESTILOS //

        JButton[] botones = {guardarBtn, volverBtn};

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

        comboDificultad.setBackground(Color.GRAY);
        comboDificultad.setForeground(Color.WHITE);
        comboDificultad.setFont(new Font("Segoe UI", Font.BOLD, 18));
        comboDificultad.setPreferredSize(new Dimension(250, 50));
        comboDificultad.setOpaque(true);
        comboDificultad.setFocusable(false);
        comboDificultad.setMaximumRowCount(3); // Opcional: muestra hasta 3 elementos al desplegar


        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(label, gbc);
        gbc.gridx = 1;
        add(comboDificultad, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(guardarBtn, gbc);
        gbc.gridx = 1;
        add(volverBtn, gbc);
    }
}
