package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InicioPanel extends JPanel {
    private JTextField nombreField;
    private JButton jugarButton;
    private JFrame frame;

    public InicioPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Raptor vs Tesla");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel nombreLabel = new JLabel("Ingrese su nombre:");
        nombreField = new JTextField(15);
        jugarButton = new JButton("Jugar");

        jugarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText().trim();
                if (!nombre.equals("")) {
                    GamePanel panelJuego = new GamePanel(nombre, frame);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(panelJuego);
                    frame.revalidate();
                    frame.repaint();
                    frame.pack();
                    frame.setLocationRelativeTo(null); // Centra ventana
                    panelJuego.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(InicioPanel.this, "Debe ingresar un nombre.");
                }
            }
        });

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        add(titulo, gbc);
        gbc.gridy++;
        add(nombreLabel, gbc);
        gbc.gridy++;
        add(nombreField, gbc);
        gbc.gridy++;
        add(jugarButton, gbc);
    }
}
