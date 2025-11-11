/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package presentacion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CarreraDTO;
import dto.PaginaDTO;
import java.awt.Color;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class DialogCarreras extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DialogCarreras.class.getName());

   
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

   
    private final Consumer<String> onCarreraSeleccionada; 

    // Estado de paginación
    private int paginaActual = 0;
    private int totalPaginas = 1; 
    private final int tamanoPagina = 10;
    private String filtroNombre = "";
    

    private DefaultTableModel tableModel;

    public DialogCarreras(java.awt.Frame parent, Consumer<String> onCarreraSeleccionada) {
        super(parent, true); 
        this.onCarreraSeleccionada = onCarreraSeleccionada; 
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();

        initComponents(); 

        configurarTabla();
        cargarCarreras();

        this.setLocationRelativeTo(parent);
    }

    private void configurarTabla() {
     
        this.tableModel = (DefaultTableModel) tablaCarreras.getModel();
        
      
        if (tableModel.getColumnCount() == 0) {
             tableModel.addColumn("ID");
             tableModel.addColumn("Nombre");
        }

        tablaCarreras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    
        tablaCarreras.getColumnModel().getColumn(0).setMinWidth(0);
        tablaCarreras.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCarreras.getColumnModel().getColumn(0).setWidth(0);

        
        tablaCarreras.getColumnModel().getColumn(1).setPreferredWidth(350);

       
        tablaCarreras.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    
                    btnSeleccionar.doClick(); 
                }
            }
        });
    }

    private void cargarCarreras() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{null, "Cargando..."});

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                String filtroCodificado = URLEncoder.encode(this.filtroNombre, StandardCharsets.UTF_8);
                String url = String.format("%s/api/carreras?page=%d&size=%d&nombre=%s",
                        ConfigCliente.BASE_URL,
                        this.paginaActual,
                        this.tamanoPagina,
                        filtroCodificado
                );

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    TypeReference<PaginaDTO<CarreraDTO>> typeRef = new TypeReference<>() {
                    };
                    PaginaDTO<CarreraDTO> pagina = objectMapper.readValue(response.body(), typeRef);

                    SwingUtilities.invokeLater(() -> {
                        actualizarTabla(pagina.getContent());
                        this.totalPaginas = pagina.getTotalPages();
                        this.paginaActual = pagina.getNumber();
                        actualizarControlesPaginacion();
                    });

                } else {
                    SwingUtilities.invokeLater(() -> {
                        tableModel.setRowCount(0);
                        tableModel.addRow(new Object[]{null, "Error al cargar carreras."});
                        JOptionPane.showMessageDialog(this, "Error: " + response.body(), "Error de API", JOptionPane.ERROR_MESSAGE);
                        actualizarControlesPaginacion();
                    });
                }
            } catch (Exception e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al cargar carreras", e);
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[]{null, "Error de conexión."});
                    JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    actualizarControlesPaginacion();
                });
            }
        });
    }

    private void actualizarTabla(List<CarreraDTO> carreras) {
        tableModel.setRowCount(0);

        if (carreras.isEmpty()) {
            tableModel.addRow(new Object[]{null, "No se encontraron resultados."});
        } else {
            for (CarreraDTO carrera : carreras) {
                tableModel.addRow(new Object[]{
                    carrera.getId(),
                    carrera.getNombre()
                });
            }
        }
    }

    private void actualizarControlesPaginacion() {
        if (totalPaginas == 0) {
            lblNumPagina.setText("Página 0 de 0");
        } else {
            lblNumPagina.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
        }

        btnAnterior.setEnabled(paginaActual > 0);
        btnSIguiente.setEnabled(paginaActual < totalPaginas - 1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblBuscador = new presentacion.TextFieldRedondo();
        btnBuscar = new javax.swing.JToggleButton();
        panelRedondo1 = new presentacion.PanelRedondo();
        btnAnterior = new presentacion.botonCircular();
        btnSIguiente = new presentacion.botonCircular();
        lblNumPagina = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaCarreras = new javax.swing.JTable();
        btnCancelar = new javax.swing.JButton();
        btnSeleccionar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
        );

        jButton1.setText("jButton1");

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblBuscador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lblBuscadorKeyReleased(evt);
            }
        });

        btnBuscar.setText("buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnAnterior.setBackground(new java.awt.Color(30, 115, 179));
        btnAnterior.setForeground(new java.awt.Color(255, 255, 255));
        btnAnterior.setText("Anterior");
        btnAnterior.setBorderColor(new java.awt.Color(255, 255, 255));
        btnAnterior.setColor(new java.awt.Color(30, 115, 179));
        btnAnterior.setColorClick(new java.awt.Color(102, 255, 255));
        btnAnterior.setColorOver(new java.awt.Color(30, 115, 179));
        btnAnterior.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });

        btnSIguiente.setBackground(new java.awt.Color(30, 115, 179));
        btnSIguiente.setForeground(new java.awt.Color(255, 255, 255));
        btnSIguiente.setText("Siguiente");
        btnSIguiente.setBorderColor(new java.awt.Color(255, 255, 255));
        btnSIguiente.setColor(new java.awt.Color(30, 115, 179));
        btnSIguiente.setColorClick(new java.awt.Color(0, 255, 255));
        btnSIguiente.setColorOver(new java.awt.Color(30, 115, 179));
        btnSIguiente.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnSIguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSIguienteActionPerformed(evt);
            }
        });

        lblNumPagina.setText("numero pagina");

        javax.swing.GroupLayout panelRedondo1Layout = new javax.swing.GroupLayout(panelRedondo1);
        panelRedondo1.setLayout(panelRedondo1Layout);
        panelRedondo1Layout.setHorizontalGroup(
            panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondo1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(lblNumPagina)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnSIguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        panelRedondo1Layout.setVerticalGroup(
            panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondo1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSIguiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumPagina))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tablaCarreras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "nombre carrera"
            }
        ));
        jScrollPane4.setViewportView(tablaCarreras);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panelRedondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(15, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(15, 15, 15))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(btnSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRedondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnSeleccionar))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        if (paginaActual > 0) {
            paginaActual--;
            cargarCarreras();
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnSIguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSIguienteActionPerformed
        if (paginaActual < totalPaginas - 1) {
            paginaActual++;
            cargarCarreras();
        }
    }//GEN-LAST:event_btnSIguienteActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.filtroNombre = lblBuscador.getText().trim();
        this.paginaActual = 0;
        cargarCarreras();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void lblBuscadorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblBuscadorKeyReleased

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            btnBuscar.doClick();
        }
    }//GEN-LAST:event_lblBuscadorKeyReleased

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        int filaSeleccionada = tablaCarreras.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una carrera de la tabla.", "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreCarrera = (String) tableModel.getValueAt(filaSeleccionada, 1);

        if (nombreCarrera == null || nombreCarrera.equals("Cargando...") || nombreCarrera.equals("No se encontraron resultados.")) {
            JOptionPane.showMessageDialog(this, "Por favor, espera a que carguen los datos o selecciona una carrera válida.", "Selección inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.onCarreraSeleccionada.accept(nombreCarrera);

        this.dispose();
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogCarreras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogCarreras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogCarreras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogCarreras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogCarreras dialog = new DialogCarreras(new javax.swing.JFrame(), (carrera) -> {
                    System.out.println("Prueba: Carrera seleccionada: " + carrera);
                });
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private presentacion.botonCircular btnAnterior;
    private javax.swing.JToggleButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private presentacion.botonCircular btnSIguiente;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private presentacion.TextFieldRedondo lblBuscador;
    private javax.swing.JLabel lblNumPagina;
    private presentacion.PanelRedondo panelRedondo1;
    private javax.swing.JTable tablaCarreras;
    // End of variables declaration//GEN-END:variables
}
