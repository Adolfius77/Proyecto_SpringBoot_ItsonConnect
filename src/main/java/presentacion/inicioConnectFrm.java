/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EstudianteDTO;
import dto.MatchDTO;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author USER
 */
public class inicioConnectFrm extends javax.swing.JFrame {

    /**
     * Creates new form inicioConnectFrm
     */
    private EstudianteDTO estudianteLogueado;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Timer refreshTimer;

    public inicioConnectFrm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public inicioConnectFrm(EstudianteDTO estudiante) {
        this.estudianteLogueado = estudiante;
        initComponents();
        setLocationRelativeTo(null);
        if (this.estudianteLogueado != null) {
            nombreEstudiante.setText(this.estudianteLogueado.getNombre() + " " + this.estudianteLogueado.getApPaterno());
            lblNombreBienvenida.setText(this.estudianteLogueado.getNombre());
        }
        panelMatches1.setLayout(new java.awt.GridLayout(0, 1, 0, 15));
        panelMatches1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelMatches1.setBackground(new Color(248, 249, 250));

        cargarMatchesRecientes();
        cargarFotoEnPanel();
        refreshTimer = new Timer(5000, e -> cargarMatchesRecientes());
        refreshTimer.start();

    }

    private void cargarFotoEnPanel() {
        if (estudianteLogueado.getFotoBase64() != null && !estudianteLogueado.getFotoBase64().isEmpty()) {
            try {
                
                byte[] fotoBytes = java.util.Base64.getDecoder().decode(estudianteLogueado.getFotoBase64());
                
                
                ((presentacion.PanelFotoCircular)panelFotoPerfil).setImagenBytes(fotoBytes);
                
            } catch (Exception e) {
                System.out.println("Error al cargar la foto: " + e.getMessage());
            }
        }
    }

    private void irAPerfil() {
        if (this.estudianteLogueado == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.dispose();
            return;
        }

        EditarPerfilFrm editFrame = new EditarPerfilFrm(this.estudianteLogueado);
        editFrame.setVisible(true);
        this.dispose();
    }

    private void cargarMatchesRecientes() {
        if (estudianteLogueado == null) {
            return;
        }

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                String url = ConfigCliente.BASE_URL + "/api/estudiantes/descubrir?idActual=" + estudianteLogueado.getId() + "&limit=5";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    List<EstudianteDTO> nuevosUsuarios = objectMapper.readValue(response.body(), new TypeReference<List<EstudianteDTO>>() {
                    });

                    SwingUtilities.invokeLater(() -> actualizarPanelDescubrir(nuevosUsuarios));
                }
            } catch (Exception e) {
                System.err.println("Error al cargar usuarios para descubrir: " + e.getMessage());
            }
        });

    }

    private void actualizarPanelDescubrir(List<EstudianteDTO> usuarios) {
        panelMatches1.removeAll();

        if (usuarios.isEmpty()) {
            JLabel lblVacio = new JLabel("¡No hay nuevos usuarios por ahora!");
            lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
            panelMatches1.add(lblVacio);
        } else {
            for (EstudianteDTO usuario : usuarios) {
                PersonasNuevasFrm tarjeta = new PersonasNuevasFrm(estudianteLogueado, usuario);
                panelMatches1.add(tarjeta);
            }
        }
        panelMatches1.revalidate();
        panelMatches1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        botonCircular6 = new presentacion.botonCircular();
        btnMensajes = new presentacion.botonCircular();
        panelMatches2 = new presentacion.PanelRedondo();
        btnOpciones = new presentacion.botonCircular();
        botonCircular9 = new presentacion.botonCircular();
        botonCircular3 = new presentacion.botonCircular();
        botonCircular7 = new presentacion.botonCircular();
        botonCircular8 = new presentacion.botonCircular();
        jComboBox1 = new javax.swing.JComboBox<>();
        botonCircular2 = new presentacion.botonCircular();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        panelFotoUsuario = new presentacion.PanelFotoCircular();
        panelFotoUsuario1 = new presentacion.PanelFotoCircular();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnBuscarEstudiantes = new presentacion.botonCircular();
        btnInicio = new presentacion.botonCircular();
        btnMatches = new presentacion.botonCircular();
        btnPerfil = new presentacion.botonCircular();
        nombreEstudiante = new javax.swing.JLabel();
        btnCerrarSesion = new presentacion.botonCircular();
        panelFotoPerfil = new presentacion.PanelFotoCircular();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        lblNombreBienvenida = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelMatches1 = new presentacion.PanelRedondo();
        panelRedondo1 = new presentacion.PanelRedondo();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        botonCircular1 = new presentacion.botonCircular();
        jLabel9 = new javax.swing.JLabel();

        jLabel1.setText("YA LO LOGRASTE");

        botonCircular6.setBackground(new java.awt.Color(102, 204, 255));
        botonCircular6.setForeground(new java.awt.Color(255, 255, 255));
        botonCircular6.setText("Buscar estudiantes");
        botonCircular6.setBorderColor(new java.awt.Color(255, 255, 255));
        botonCircular6.setColor(new java.awt.Color(102, 204, 255));
        botonCircular6.setColorClick(new java.awt.Color(102, 204, 255));
        botonCircular6.setColorOver(new java.awt.Color(51, 204, 255));
        botonCircular6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        btnMensajes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mensajero.png"))); // NOI18N
        btnMensajes.setText("Mensajes");
        btnMensajes.setBorderColor(new java.awt.Color(255, 255, 255));
        btnMensajes.setColorClick(new java.awt.Color(102, 204, 255));
        btnMensajes.setColorOver(new java.awt.Color(102, 204, 255));
        btnMensajes.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnMensajes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMensajesActionPerformed(evt);
            }
        });

        panelMatches2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelMatches2Layout = new javax.swing.GroupLayout(panelMatches2);
        panelMatches2.setLayout(panelMatches2Layout);
        panelMatches2Layout.setHorizontalGroup(
            panelMatches2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );
        panelMatches2Layout.setVerticalGroup(
            panelMatches2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );

        btnOpciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editar (2).png"))); // NOI18N
        btnOpciones.setText("Perfil");
        btnOpciones.setBorderColor(new java.awt.Color(255, 255, 255));
        btnOpciones.setColorClick(new java.awt.Color(102, 204, 255));
        btnOpciones.setColorOver(new java.awt.Color(102, 204, 255));
        btnOpciones.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnOpciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpcionesActionPerformed(evt);
            }
        });

        botonCircular9.setBackground(new java.awt.Color(102, 204, 255));
        botonCircular9.setForeground(new java.awt.Color(255, 255, 255));
        botonCircular9.setText("Opciones");
        botonCircular9.setBorderColor(new java.awt.Color(255, 255, 255));
        botonCircular9.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        botonCircular9.setRadius(50);

        botonCircular3.setText("botonCircular3");

        botonCircular7.setBackground(new java.awt.Color(102, 204, 255));
        botonCircular7.setForeground(new java.awt.Color(255, 255, 255));
        botonCircular7.setText("Editar Perfil");
        botonCircular7.setBorderColor(new java.awt.Color(255, 255, 255));
        botonCircular7.setColorClick(new java.awt.Color(102, 204, 255));
        botonCircular7.setColorOver(new java.awt.Color(102, 204, 255));
        botonCircular7.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        botonCircular7.setRadius(50);

        botonCircular8.setBackground(new java.awt.Color(102, 204, 255));
        botonCircular8.setForeground(new java.awt.Color(255, 255, 255));
        botonCircular8.setText("Buscar Estudiante");
        botonCircular8.setBorderColor(new java.awt.Color(255, 255, 255));
        botonCircular8.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        botonCircular8.setRadius(50);
        botonCircular8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCircular8ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Opciones", "Cerrar Sesion" }));

        botonCircular2.setBackground(new java.awt.Color(102, 204, 255));
        botonCircular2.setForeground(new java.awt.Color(255, 255, 255));
        botonCircular2.setText("Buscar Estudiante");
        botonCircular2.setBorderColor(new java.awt.Color(255, 255, 255));
        botonCircular2.setColor(new java.awt.Color(102, 204, 255));
        botonCircular2.setColorClick(new java.awt.Color(102, 204, 255));
        botonCircular2.setColorOver(new java.awt.Color(0, 153, 204));
        botonCircular2.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        botonCircular2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCircular2ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("jRadioButton1");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/usuario (2).png"))); // NOI18N

        panelFotoUsuario.setMinimumSize(new java.awt.Dimension(60, 60));
        panelFotoUsuario.setPreferredSize(new java.awt.Dimension(60, 60));
        panelFotoUsuario.setRequestFocusEnabled(false);

        javax.swing.GroupLayout panelFotoUsuarioLayout = new javax.swing.GroupLayout(panelFotoUsuario);
        panelFotoUsuario.setLayout(panelFotoUsuarioLayout);
        panelFotoUsuarioLayout.setHorizontalGroup(
            panelFotoUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panelFotoUsuarioLayout.setVerticalGroup(
            panelFotoUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        panelFotoUsuario1.setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout panelFotoUsuario1Layout = new javax.swing.GroupLayout(panelFotoUsuario1);
        panelFotoUsuario1.setLayout(panelFotoUsuario1Layout);
        panelFotoUsuario1Layout.setHorizontalGroup(
            panelFotoUsuario1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        panelFotoUsuario1Layout.setVerticalGroup(
            panelFotoUsuario1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(247, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnBuscarEstudiantes.setBackground(new java.awt.Color(102, 204, 255));
        btnBuscarEstudiantes.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarEstudiantes.setText("Buscar estudiantes");
        btnBuscarEstudiantes.setBorderColor(new java.awt.Color(255, 255, 255));
        btnBuscarEstudiantes.setColor(new java.awt.Color(102, 204, 255));
        btnBuscarEstudiantes.setColorClick(new java.awt.Color(102, 204, 255));
        btnBuscarEstudiantes.setColorOver(new java.awt.Color(51, 204, 255));
        btnBuscarEstudiantes.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnBuscarEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEstudiantesActionPerformed(evt);
            }
        });

        btnInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hogar.png"))); // NOI18N
        btnInicio.setText("Inicio");
        btnInicio.setBorderColor(new java.awt.Color(204, 255, 255));
        btnInicio.setColorClick(new java.awt.Color(102, 204, 255));
        btnInicio.setColorOver(new java.awt.Color(102, 204, 255));
        btnInicio.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

        btnMatches.setIcon(new javax.swing.ImageIcon(getClass().getResource("/amigos-felices.png"))); // NOI18N
        btnMatches.setText("Matches");
        btnMatches.setBorderColor(new java.awt.Color(204, 255, 255));
        btnMatches.setColorClick(new java.awt.Color(102, 204, 255));
        btnMatches.setColorOver(new java.awt.Color(102, 204, 255));
        btnMatches.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnMatches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMatchesActionPerformed(evt);
            }
        });

        btnPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editar (2).png"))); // NOI18N
        btnPerfil.setText("Perfil");
        btnPerfil.setBorderColor(new java.awt.Color(204, 255, 255));
        btnPerfil.setColorClick(new java.awt.Color(102, 204, 255));
        btnPerfil.setColorOver(new java.awt.Color(102, 204, 255));
        btnPerfil.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });

        nombreEstudiante.setBackground(new java.awt.Color(0, 0, 0));
        nombreEstudiante.setFont(new java.awt.Font("SansSerif", 3, 16)); // NOI18N
        nombreEstudiante.setText("Nombre estudiante");

        btnCerrarSesion.setText("Cerrar sesion");
        btnCerrarSesion.setBorderColor(new java.awt.Color(204, 255, 255));
        btnCerrarSesion.setColorClick(new java.awt.Color(102, 204, 255));
        btnCerrarSesion.setColorOver(new java.awt.Color(102, 204, 255));
        btnCerrarSesion.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        panelFotoPerfil.setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout panelFotoPerfilLayout = new javax.swing.GroupLayout(panelFotoPerfil);
        panelFotoPerfil.setLayout(panelFotoPerfilLayout);
        panelFotoPerfilLayout.setHorizontalGroup(
            panelFotoPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );
        panelFotoPerfilLayout.setVerticalGroup(
            panelFotoPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnBuscarEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nombreEstudiante))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMatches, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(10, Short.MAX_VALUE))
            .addComponent(jSeparator2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(panelFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(nombreEstudiante)))
                .addGap(32, 32, 32)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMatches, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(btnBuscarEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jLabel4.setBackground(new java.awt.Color(51, 51, 51));
        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("!Bienvendio de vuelta!");

        lblNombreBienvenida.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        lblNombreBienvenida.setForeground(new java.awt.Color(0, 0, 0));
        lblNombreBienvenida.setText("jLabel5");

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Descrubre nuevas Personas");

        panelMatches1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelMatches1Layout = new javax.swing.GroupLayout(panelMatches1);
        panelMatches1.setLayout(panelMatches1Layout);
        panelMatches1Layout.setHorizontalGroup(
            panelMatches1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 768, Short.MAX_VALUE)
        );
        panelMatches1Layout.setVerticalGroup(
            panelMatches1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panelMatches1);

        panelRedondo1.setBackground(new java.awt.Color(101, 188, 220));

        jLabel6.setFont(new java.awt.Font("SansSerif", 3, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Acciones Rapidas");

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Actualiza tu informacion ");

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Facilmente");

        botonCircular1.setBackground(new java.awt.Color(102, 204, 255));
        botonCircular1.setForeground(new java.awt.Color(255, 255, 255));
        botonCircular1.setText("Editar Perfil");
        botonCircular1.setBorderColor(new java.awt.Color(255, 255, 255));
        botonCircular1.setColor(new java.awt.Color(102, 204, 255));
        botonCircular1.setColorClick(new java.awt.Color(102, 204, 255));
        botonCircular1.setColorOver(new java.awt.Color(0, 153, 204));
        botonCircular1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        botonCircular1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCircular1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRedondo1Layout = new javax.swing.GroupLayout(panelRedondo1);
        panelRedondo1.setLayout(panelRedondo1Layout);
        panelRedondo1Layout.setHorizontalGroup(
            panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondo1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonCircular1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        panelRedondo1Layout.setVerticalGroup(
            panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondo1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(botonCircular1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Descubre y conectate con otros estudiantes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNombreBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelRedondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblNombreBienvenida))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(102, 102, 102)
                        .addComponent(jLabel7))
                    .addComponent(panelRedondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
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

    private void btnMensajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMensajesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMensajesActionPerformed

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        irAPerfil();

    }//GEN-LAST:event_btnPerfilActionPerformed

    private void botonCircular8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCircular8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonCircular8ActionPerformed

    private void btnMatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatchesActionPerformed
        if (this.estudianteLogueado == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.dispose();
            return;
        }
        matchesFrm matchesVentana = new matchesFrm(this.estudianteLogueado);
        matchesVentana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMatchesActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnBuscarEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEstudiantesActionPerformed
        if (this.estudianteLogueado == null) {

            JOptionPane.showMessageDialog(this, "Error, no se ha iniciado sesion.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DescubrirFrm descu = new DescubrirFrm(this.estudianteLogueado, null);
        descu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBuscarEstudiantesActionPerformed

    private void btnOpcionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpcionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOpcionesActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas cerrar sesion?",
                "Cerrar Sesión",
                javax.swing.JOptionPane.YES_NO_OPTION
        );
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            this.estudianteLogueado = null;

            Main mainFrame = new Main();
            mainFrame.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void botonCircular1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCircular1ActionPerformed
        irAPerfil();
    }//GEN-LAST:event_botonCircular1ActionPerformed

    private void botonCircular2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCircular2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonCircular2ActionPerformed

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
            java.util.logging.Logger.getLogger(inicioConnectFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(inicioConnectFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(inicioConnectFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(inicioConnectFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new inicioConnectFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private presentacion.botonCircular botonCircular1;
    private presentacion.botonCircular botonCircular2;
    private presentacion.botonCircular botonCircular3;
    private presentacion.botonCircular botonCircular6;
    private presentacion.botonCircular botonCircular7;
    private presentacion.botonCircular botonCircular8;
    private presentacion.botonCircular botonCircular9;
    private presentacion.botonCircular btnBuscarEstudiantes;
    private presentacion.botonCircular btnCerrarSesion;
    private presentacion.botonCircular btnInicio;
    private presentacion.botonCircular btnMatches;
    private presentacion.botonCircular btnMensajes;
    private presentacion.botonCircular btnOpciones;
    private presentacion.botonCircular btnPerfil;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblNombreBienvenida;
    private javax.swing.JLabel nombreEstudiante;
    private javax.swing.JPanel panelFotoPerfil;
    private javax.swing.JPanel panelFotoUsuario;
    private javax.swing.JPanel panelFotoUsuario1;
    private presentacion.PanelRedondo panelMatches1;
    private presentacion.PanelRedondo panelMatches2;
    private presentacion.PanelRedondo panelRedondo1;
    // End of variables declaration//GEN-END:variables
}
