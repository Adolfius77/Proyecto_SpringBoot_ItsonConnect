/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EstudianteDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javax.swing.JOptionPane;
import org.springframework.context.ConfigurableApplicationContext;
import com.fasterxml.jackson.core.type.TypeReference;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import org.springframework.boot.SpringApplication;

/**
 *
 * @author USER
 */
public class DescubrirFrm extends javax.swing.JFrame {

    private EstudianteDTO estudianteActual;
    private static ConfigurableApplicationContext context;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DescubrirFrm.class.getName());
    private List<EstudianteDTO> listaCompletaEstudiantes = new ArrayList<>();

    /**
     * Creates new form DescubrirFrm
     */
    public DescubrirFrm(EstudianteDTO estudianteActual, ConfigurableApplicationContext context) {
        this.estudianteActual = estudianteActual;
        this.context = context;

        initComponents();

        configurarVentana();
        configurarComboBox();
        cargarEstudiantes();

        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenu6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                irAPerfil();
            }
        });
    }

    private void irAPerfil() {
        if (this.estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.dispose();
            return;
        }

        EditarPerfilFrm editFrame = new EditarPerfilFrm(this.estudianteActual);
        editFrame.setVisible(true);
        this.dispose();
    }

    private void configurarComboBox() {
        if (cmbCarreras == null) {
            System.err.println("cmbCarreras es nulo.");
            return;
        }

        // 1. Asegúrate de que esté vacío
        cmbCarreras.removeAllItems();

        // 2. Añade la opción por defecto
        cmbCarreras.addItem("Todas las Carreras");

        // 3. Conecta el listener
        // (Esto ahora funcionará porque filtrarEstudiantes() ya existe)
        cmbCarreras.addActionListener(e -> filtrarEstudiantes());
    }

    private void filtrarEstudiantes() {
        String carreraSeleccionada = (String) cmbCarreras.getSelectedItem();
        List<EstudianteDTO> estudiantesFiltrados;

        if (carreraSeleccionada == null || carreraSeleccionada.equals("Todas las Carreras")) {
            estudiantesFiltrados = new ArrayList<>(this.listaCompletaEstudiantes);
        } else {
            estudiantesFiltrados = this.listaCompletaEstudiantes.stream()
                    .filter(estudiante -> {
                        if (estudiante.getCarrera() == null) {
                            return false;
                        }
                        return estudiante.getCarrera().equals(carreraSeleccionada);
                    })
                    .collect(Collectors.toList());
        }
        mostrarEstudiantes(estudiantesFiltrados);
    }

    private void mostrarEstudiantes(List<EstudianteDTO> estudiantesAMostrar) {

        // 1. Define la acción de eliminación (Callback)
        Consumer<PersonasFrm> funcionDeEliminacion = (tarjetaParaEliminar) -> {
            SwingUtilities.invokeLater(() -> {
                // Elimina de la UI
                panelDinamico.remove(tarjetaParaEliminar);
                panelDinamico.revalidate();
                panelDinamico.repaint();

                // Opcional: También elimina de la lista en memoria
                this.listaCompletaEstudiantes.remove(tarjetaParaEliminar.getEstudianteDTO()); // <-- Necesitarías un getter en PersonasFrm
            });
        };

        // 2. Limpia el panel
        panelDinamico.removeAll();

        // 3. Crea y añade las tarjetas filtradas
        for (EstudianteDTO dto : estudiantesAMostrar) {
            PersonasFrm card = new PersonasFrm(
                    estudianteActual.getId(),
                    dto,
                    funcionDeEliminacion
            );
            panelDinamico.add(card);
        }

        // 4. Refresca la UI
        panelDinamico.revalidate();
        panelDinamico.repaint();
    }

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {
        if (this.estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.dispose();
            return;
        }

        inicioConnectFrm inicioFrm = new inicioConnectFrm(this.estudianteActual);

        inicioFrm.setVisible(true);

        this.dispose();
    }

    public DescubrirFrm() {
        initComponents();
        configurarVentana();

    }

    private void configurarVentana() {
        this.setTitle("Itson Connect - Descubrir");
        setLocationRelativeTo(null);

        panelDinamico.setLayout(new GridLayout(0, 1, 10, 10));
        panelDinamico.setBackground(new Color(234, 231, 225)); // Color de fondo

        scrollpanel.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Preguntar al usuario si quiere salir
                int a = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres salir?", "Salir", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    if (context != null) {
                        SpringApplication.exit(context, () -> 0);
                    }
                    // Cierra la aplicación
                    System.exit(0);
                }
            }
        });
    }

    private void cargarEstudiantes() {
        if (estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error: No se ha iniciado sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.listaCompletaEstudiantes.clear();

        // Usamos un hilo para la llamada a la API
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                // 1. Crear cliente y petición HTTP
                HttpClient client = HttpClient.newHttpClient();
                String url = ConfigCliente.BASE_URL + "/api/estudiantes/descubrir?idActual=" + estudianteActual.getId();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .header("Content-Type", "application/json")
                        .build();

                // 2. Enviar petición y recibir respuesta
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<EstudianteDTO> estudiantes = objectMapper.readValue(response.body(), new TypeReference<List<EstudianteDTO>>() {
                    });

                    // 3. Guarda la lista completa
                    this.listaCompletaEstudiantes = estudiantes;

                    // --- LÓGICA NUEVA: EXTRAER CARRERAS ---
                    // 4. Extrae las carreras únicas de la lista de estudiantes
                    //    Usamos un Set para evitar duplicados automáticamente
                    Set<String> carrerasUnicas = estudiantes.stream()
                            .map(EstudianteDTO::getCarrera) // Obtiene la carrera de cada estudiante
                            .filter(carrera -> carrera != null && !carrera.isEmpty()) // Filtra nulls o vacíos
                            .collect(Collectors.toSet()); // Colecciona en un Set

                    // 5. Actualiza la UI en el hilo de Swing
                    SwingUtilities.invokeLater(() -> {
                        // Muestra TODOS los estudiantes la primera vez
                        mostrarEstudiantes(this.listaCompletaEstudiantes);

                        // Llena el ComboBox con las carreras únicas
                        for (String carrera : carrerasUnicas) {
                            cmbCarreras.addItem(carrera);
                        }
                    });

                } else {
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + response.body(), "Error", JOptionPane.ERROR_MESSAGE)
                    );
                }
            } catch (Exception e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al cargar estudiantes", e);
                SwingUtilities.invokeLater(()
                        -> JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                );
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        textFieldRedondo1 = new presentacion.TextFieldRedondo();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        scrollpanel = new javax.swing.JScrollPane();
        panelDinamico = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbCarreras = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();

        jMenuItem1.setText("jMenuItem1");

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        textFieldRedondo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldRedondo1ActionPerformed(evt);
            }
        });

        jButton1.setText("Buscar");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel1.setText("Descubre");

        panelDinamico.setLayout(new java.awt.GridLayout(1, 0));
        scrollpanel.setViewportView(panelDinamico);

        jLabel2.setFont(new java.awt.Font("SansSerif", 2, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Conectate con otros estudiantes del ITSON que comparten tus mismo intereses");

        jLabel3.setText("Filtrar por:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(63, 63, 63)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(cmbCarreras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(cmbCarreras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrollpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jMenuBar1.setBackground(new java.awt.Color(30, 115, 179));

        jMenu1.setBackground(new java.awt.Color(30, 115, 179));
        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("ITSON Connect");
        jMenu1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(30, 115, 179));
        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Inicio");
        jMenu2.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jMenuBar1.add(jMenu2);

        jMenu5.setBackground(new java.awt.Color(30, 115, 179));
        jMenu5.setForeground(new java.awt.Color(255, 255, 255));
        jMenu5.setText("Matches");
        jMenu5.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu5);

        jMenu6.setBackground(new java.awt.Color(30, 115, 179));
        jMenu6.setForeground(new java.awt.Color(255, 255, 255));
        jMenu6.setText("Perfil");
        jMenu6.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

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

    private void textFieldRedondo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldRedondo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldRedondo1ActionPerformed

    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked
        if (this.estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.dispose();
            return;
        }

        matchesFrm mensajes = new matchesFrm(this.estudianteActual);

        mensajes.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_jMenu5MouseClicked

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
            java.util.logging.Logger.getLogger(DescubrirFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DescubrirFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DescubrirFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DescubrirFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DescubrirFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbCarreras;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel panelDinamico;
    private javax.swing.JScrollPane scrollpanel;
    private presentacion.TextFieldRedondo textFieldRedondo1;
    // End of variables declaration//GEN-END:variables
}
