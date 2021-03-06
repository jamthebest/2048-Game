package juego_2048;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author javier
 */
public final class Main extends javax.swing.JFrame {
    private int[][] boardMap;
    private final JLabel[][] componentes;
    private final JPanel[][] cuadros;
    private boolean finJuego = false;
    private int resultado = 0;
    private Map<Integer, Color> colores;
    
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        // Inicializando Variables
        colores = new HashMap<>();
        colores.put(0, new Color(205, 193, 180));
        colores.put(2, new Color(238, 228, 218));
        colores.put(4, new Color(237, 224, 200));
        colores.put(8, new Color(242, 177, 121));
        colores.put(16, new Color(245, 149, 99));
        colores.put(32, new Color(246, 124, 95));
        colores.put(64, new Color(246, 94, 59));
        colores.put(128, new Color(237, 207, 114));
        colores.put(256, new Color(237, 204, 97));
        colores.put(512, new Color(237, 200, 80));
        colores.put(1024, new Color(237, 197, 63));
        colores.put(2048, new Color(237, 194, 46));
        colores.put(4096, new Color(184, 133, 172));
        colores.put(8192, new Color(175, 109, 179));
        this.componentes = new JLabel[][]{{cuadro00,cuadro01,cuadro02,cuadro03}, {cuadro10,cuadro11,cuadro12,cuadro13}, {cuadro20,cuadro21,cuadro22,cuadro23}, {cuadro30,cuadro31,cuadro32,cuadro33}};
        this.cuadros = new JPanel[][]{{panel00,panel01,panel02,panel03}, {panel10,panel11,panel12,panel13}, {panel20,panel21,panel22,panel23}, {panel30,panel31,panel32,panel33}};
        boardMap = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                boardMap[i][j] = 0;
            }
        }
        
        // Inicializando procedimiento para detectar que tecla direccional fue precionada
        KeyMonitor monitor = new KeyMonitor(this);
        addKeyListener(monitor);
        
        // Inicializando primer numero a aparecer en panel
        siguienteFicha();
        // Posibilidad para que aparezcan dos campos al iniciar juego (80%)
        if (new Random().nextFloat() <= 0.8) {
            siguienteFicha();
        }
        /*boardMap[0][3] = 4;
        boardMap[1][3] = 2;
        boardMap[3][3] = 2;*/
        this.dibujarPanel();
    }
    
    public boolean obtenerFinJuego() {
        return this.finJuego;
    }
    
    // Verifica si hay algun posible movimiento o si la partida se terminó
    public boolean verificarFin() {
        boolean esFin = true;
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                if (this.boardMap[i][j] == 0) {
                    return false;
                } else {
                    if ((i - 1 >= 0 && this.boardMap[i - 1][j] == this.boardMap[i][j]) || (i + 1 <= 3 && this.boardMap[i + 1][j] == this.boardMap[i][j])) {
                        return false;
                    }
                    if ((j - 1 >= 0 && this.boardMap[i][j - 1] == this.boardMap[i][j]) || (j + 1 <= 3 && this.boardMap[i][j + 1] == this.boardMap[i][j])) {
                        return false;
                    }
                }
            }
        }
        this.finJuego = true;
        return esFin;
    }
    
    // Muestra un mensaje cuando el juego terminó
    public void mostrarFin() {
        JOptionPane.showMessageDialog(null, "No hay más movimientos disponibles", "Fin del Juego", JOptionPane.WARNING_MESSAGE);
    }
    
    // Generación de numeros a aparecer en panel
    public int numeroAleatorio() {
        int value = new Random().nextDouble() < 0.9 ? 2 : 4;
        System.out.println(value);
        return value;
    }
    
    // Obtener siguiente numero con hubicación
    public void siguienteFicha() {
        int value = numeroAleatorio();
        int[] location = hubicacionAleatoria();
        System.out.println(location[0] + ", " + location[1]);
        if (location[0] == -1 || location[1] == -1) {
            finJuego = true;
            System.out.println("Fin del Juego");
            this.mostrarFin();
            return;
        }
        boardMap[location[0]][location[1]] = value;
        dibujarPanel();
    }
    
    // Generación de hubicación aleatoria
    public int[] hubicacionAleatoria() {
        int[] location = {-1, -1};
        if (hayLugarDisponible() == false) {
            return location;
        }
        int x = 0;
        int y = 0;
        do {
            x = new Random().nextInt(4);
            y = new Random().nextInt(4);
        } while(boardMap[x][y] != 0);
        location[0] = x;
        location[1] = y;
        return location;
    }
    
    // Verificar si hay hubicaciones disponibles
    public boolean hayLugarDisponible() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (boardMap[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // Dibuja el panel de acuerdo a los valores en boardMap
    public void dibujarPanel() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                componentes[i][j].setText("");
                Color cuadroColor = colores.get(this.boardMap[i][j]);
                cuadros[i][j].setBackground(cuadroColor);
                if (boardMap[i][j] != 0) {
                    int value = boardMap[i][j];
                    componentes[i][j].setText(String.valueOf(value));
                }
            }
        }
        this.score.setText(String.valueOf(this.resultado));
    }
    
    // Acción de mover a la derecha
    public boolean moverDerecha() {
        boolean seMueve = false;
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j >= 0; j--) {
                if (this.boardMap[i][j] != 0) {
                    int posicionDisponible = j;
                    for (int k = j + 1; k <= 3; k++) {
                        if (this.boardMap[i][k] == 0) {
                            posicionDisponible = k;
                        } else {
                            if (this.sePuedeMover(i, j, k, false)) {
                                posicionDisponible = k;
                            }
                            break;
                        }
                    }
                    if (posicionDisponible != j) {
                        System.out.println("i: " + i + ", j: " + j + ", pos: " + posicionDisponible);
                        seMueve = this.sePuedeMover(i, j, posicionDisponible, true) || seMueve;
                    } else {
                        System.out.println("Nada se movió");
                    }
                }
            }
        }
        return seMueve;
    }
    
    // Acción de mover a la izquierda
    public boolean moverIzquierda() {
        boolean seMueve = false;
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                if (this.boardMap[i][j] != 0) {
                    int posicionDisponible = j;
                    for (int k = j - 1; k >= 0; k--) {
                        if (this.boardMap[i][k] == 0) {
                            posicionDisponible = k;
                        } else {
                            if (this.sePuedeMover(i, j, k, false)) {
                                posicionDisponible = k;
                            }
                            break;
                        }
                    }
                    if (posicionDisponible != j) {
                        System.out.println("i: " + i + ", j: " + j + ", pos: " + posicionDisponible);
                        seMueve = this.sePuedeMover(i, j, posicionDisponible, true) || seMueve;
                    } else {
                        System.out.println("Nada se movió");
                    }
                }
            }
        }
        return seMueve;
    }
    
    // Acción de mover hacia arriba
    public boolean moverArriba() {
        boolean seMueve = false;
        for (int j = 0; j <= 3; j++) {
            for (int i = 0; i <= 3; i++) {
                if (this.boardMap[i][j] != 0) {
                    int posicionDisponible = i;
                    for (int k = i - 1; k >= 0; k--) {
                        if (this.boardMap[k][j] == 0) {
                            posicionDisponible = k;
                        } else {
                            if (this.sePuedeMoverArribaAbajo(i, j, k, false)) {
                                posicionDisponible = k;
                            }
                            break;
                        }
                    }
                    if (posicionDisponible != i) {
                        System.out.println("i: " + i + ", j: " + j + ", pos: " + posicionDisponible);
                        seMueve = this.sePuedeMoverArribaAbajo(i, j, posicionDisponible, true) || seMueve;
                    } else {
                        System.out.println("Nada se movió");
                    }
                }
            }
        }
        return seMueve;
    }
    
    // Acción de mover hacia abajo
    public boolean moverAbajo() {
        boolean seMueve = false;
        for (int j = 3; j >= 0; j--) {
            for (int i = 3; i >= 0; i--) {
                if (this.boardMap[i][j] != 0) {
                    int posicionDisponible = i;
                    for (int k = i + 1; k <= 3; k++) {
                        if (this.boardMap[k][j] == 0) {
                            posicionDisponible = k;
                        } else {
                            if (this.sePuedeMoverArribaAbajo(i, j, k, false)) {
                                posicionDisponible = k;
                            }
                            break;
                        }
                    }
                    if (posicionDisponible != i) {
                        System.out.println("i: " + i + ", j: " + j + ", pos: " + posicionDisponible);
                        seMueve = this.sePuedeMoverArribaAbajo(i, j, posicionDisponible, true) || seMueve;
                    } else {
                        System.out.println("Nada se movió");
                    }
                }
            }
        }
        return seMueve;
    }

    // Verificar si una ficha se puede mover desde la posicion (i, j) hacia (i, posicionDisponible)
    public boolean sePuedeMover(int i, int j, int posicionDisponible, boolean mover) {
        boolean sePuede = false;
        if (this.boardMap[i][posicionDisponible] == 0) {
            if (mover) {
                this.colocarValor(i, j, posicionDisponible);
            }
            sePuede = true;
        } else if (this.boardMap[i][j] == this.boardMap[i][posicionDisponible]) {
            if (mover) {
                this.colocarValor(i, j, posicionDisponible);
            }
            sePuede = true;
        }
        return sePuede;
    }

    // Verificar si una ficha se puede mover desde la posicion (i, j) hacia (i, posicionDisponible)
    public boolean sePuedeMoverArribaAbajo(int i, int j, int posicionDisponible, boolean mover) {
        boolean sePuede = false;
        if (this.boardMap[posicionDisponible][j] == 0) {
            if (mover) {
                this.colocarValorArribaAbajo(i, j, posicionDisponible);
            }
            sePuede = true;
        } else if (this.boardMap[i][j] == this.boardMap[posicionDisponible][j]) {
            if (mover) {
                this.colocarValorArribaAbajo(i, j, posicionDisponible);
            }
            sePuede = true;
        }
        System.out.println("Se mueve ab: " + sePuede);
        return sePuede;
    }
    
    // Si es posible coloca el valor de la posicion (x, primerY) en la posicion (x, segundoY)
    private void colocarValor(int x, int primerY, int segundoY) {
        System.out.println("x: " + x + ", Py: " + primerY + ", Sy: " + segundoY + ", board: " + this.boardMap[x][segundoY] + ", value: " + this.boardMap[x][primerY]);
        if (this.boardMap[x][segundoY] == 0) {
            this.boardMap[x][segundoY] = this.boardMap[x][primerY];
        } else {
            this.resultado += this.boardMap[x][segundoY] + this.boardMap[x][primerY];
            this.boardMap[x][segundoY] += this.boardMap[x][primerY];
        }
        this.boardMap[x][primerY] = 0;
        //this.dibujarPanel();
        /*for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(this.boardMap[i][j] + " ");
            }
            System.out.println("");
        }*/
    }
    
    // Si es posible coloca el valor de la posicion (x, primerY) en la posicion (x, segundoY)
    private void colocarValorArribaAbajo(int x, int primerY, int segundoY) {
        System.out.println("x: " + x + ", Py: " + primerY + ", Sy: " + segundoY + ", board: " + this.boardMap[x][segundoY] + ", value: " + this.boardMap[x][primerY]);
        if (this.boardMap[segundoY][primerY] == 0) {
            this.boardMap[segundoY][primerY] = this.boardMap[x][primerY];
        } else {
            this.resultado += this.boardMap[segundoY][primerY] + this.boardMap[x][primerY];
            this.boardMap[segundoY][primerY] += this.boardMap[x][primerY];
        }
        this.boardMap[x][primerY] = 0;
        /*for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(this.boardMap[i][j] + " ");
            }
            System.out.println("");
        }//*/
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        content = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        panelScore = new javax.swing.JPanel();
        lblScore = new javax.swing.JLabel();
        score = new javax.swing.JLabel();
        tablero = new javax.swing.JPanel();
        panel00 = new javax.swing.JPanel();
        cuadro00 = new javax.swing.JLabel();
        panel01 = new javax.swing.JPanel();
        cuadro01 = new javax.swing.JLabel();
        panel02 = new javax.swing.JPanel();
        cuadro02 = new javax.swing.JLabel();
        panel03 = new javax.swing.JPanel();
        cuadro03 = new javax.swing.JLabel();
        panel10 = new javax.swing.JPanel();
        cuadro10 = new javax.swing.JLabel();
        panel11 = new javax.swing.JPanel();
        cuadro11 = new javax.swing.JLabel();
        panel12 = new javax.swing.JPanel();
        cuadro12 = new javax.swing.JLabel();
        panel13 = new javax.swing.JPanel();
        cuadro13 = new javax.swing.JLabel();
        panel20 = new javax.swing.JPanel();
        cuadro20 = new javax.swing.JLabel();
        panel21 = new javax.swing.JPanel();
        cuadro21 = new javax.swing.JLabel();
        panel22 = new javax.swing.JPanel();
        cuadro22 = new javax.swing.JLabel();
        panel23 = new javax.swing.JPanel();
        cuadro23 = new javax.swing.JLabel();
        panel31 = new javax.swing.JPanel();
        cuadro31 = new javax.swing.JLabel();
        panel32 = new javax.swing.JPanel();
        cuadro32 = new javax.swing.JLabel();
        panel30 = new javax.swing.JPanel();
        cuadro30 = new javax.swing.JLabel();
        panel33 = new javax.swing.JPanel();
        cuadro33 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Juego 2048");
        setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        setForeground(java.awt.Color.orange);

        content.setBackground(new java.awt.Color(236, 231, 214));
        content.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        content.setForeground(new java.awt.Color(240, 240, 240));

        name.setFont(new java.awt.Font("Castellar", 1, 80)); // NOI18N
        name.setForeground(new java.awt.Color(119, 110, 101));
        name.setText("2048");

        panelScore.setBackground(new java.awt.Color(157, 137, 137));

        lblScore.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
        lblScore.setForeground(new java.awt.Color(209, 186, 186));
        lblScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScore.setText("SCORE");

        score.setFont(new java.awt.Font("Snap ITC", 0, 24)); // NOI18N
        score.setForeground(new java.awt.Color(255, 255, 255));
        score.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        score.setText("0");

        javax.swing.GroupLayout panelScoreLayout = new javax.swing.GroupLayout(panelScore);
        panelScore.setLayout(panelScoreLayout);
        panelScoreLayout.setHorizontalGroup(
            panelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblScore, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
            .addGroup(panelScoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(score, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelScoreLayout.setVerticalGroup(
            panelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelScoreLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblScore)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(score)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        score.getAccessibleContext().setAccessibleName("lblScore");

        tablero.setBackground(new java.awt.Color(187, 173, 160));
        tablero.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel00.setBackground(new java.awt.Color(220, 204, 204));
        panel00.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro00.setBackground(new java.awt.Color(205, 193, 180));
        cuadro00.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro00.setForeground(new java.awt.Color(99, 87, 87));
        cuadro00.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel00Layout = new javax.swing.GroupLayout(panel00);
        panel00.setLayout(panel00Layout);
        panel00Layout.setHorizontalGroup(
            panel00Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel00Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro00, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel00Layout.setVerticalGroup(
            panel00Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel00Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro00, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        cuadro00.getAccessibleContext().setAccessibleName("lbl00");

        panel01.setBackground(new java.awt.Color(220, 204, 204));
        panel01.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro01.setBackground(new java.awt.Color(224, 209, 209));
        cuadro01.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro01.setForeground(new java.awt.Color(99, 87, 87));
        cuadro01.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel01Layout = new javax.swing.GroupLayout(panel01);
        panel01.setLayout(panel01Layout);
        panel01Layout.setHorizontalGroup(
            panel01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel01Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro01, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel01Layout.setVerticalGroup(
            panel01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel01Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro01, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel02.setBackground(new java.awt.Color(220, 204, 204));
        panel02.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro02.setBackground(new java.awt.Color(224, 209, 209));
        cuadro02.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro02.setForeground(new java.awt.Color(99, 87, 87));
        cuadro02.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel02Layout = new javax.swing.GroupLayout(panel02);
        panel02.setLayout(panel02Layout);
        panel02Layout.setHorizontalGroup(
            panel02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel02Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro02, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel02Layout.setVerticalGroup(
            panel02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel02Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro02, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel03.setBackground(new java.awt.Color(220, 204, 204));
        panel03.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro03.setBackground(new java.awt.Color(224, 209, 209));
        cuadro03.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro03.setForeground(new java.awt.Color(99, 87, 87));
        cuadro03.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel03Layout = new javax.swing.GroupLayout(panel03);
        panel03.setLayout(panel03Layout);
        panel03Layout.setHorizontalGroup(
            panel03Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel03Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro03, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel03Layout.setVerticalGroup(
            panel03Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel03Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro03, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel10.setBackground(new java.awt.Color(220, 204, 204));
        panel10.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro10.setBackground(new java.awt.Color(224, 209, 209));
        cuadro10.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro10.setForeground(new java.awt.Color(99, 87, 87));
        cuadro10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel10Layout = new javax.swing.GroupLayout(panel10);
        panel10.setLayout(panel10Layout);
        panel10Layout.setHorizontalGroup(
            panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro10, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel10Layout.setVerticalGroup(
            panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro10, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel11.setBackground(new java.awt.Color(220, 204, 204));
        panel11.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro11.setBackground(new java.awt.Color(224, 209, 209));
        cuadro11.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro11.setForeground(new java.awt.Color(99, 87, 87));
        cuadro11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel11Layout = new javax.swing.GroupLayout(panel11);
        panel11.setLayout(panel11Layout);
        panel11Layout.setHorizontalGroup(
            panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro11, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel11Layout.setVerticalGroup(
            panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro11, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel12.setBackground(new java.awt.Color(220, 204, 204));
        panel12.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro12.setBackground(new java.awt.Color(224, 209, 209));
        cuadro12.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro12.setForeground(new java.awt.Color(99, 87, 87));
        cuadro12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel12Layout = new javax.swing.GroupLayout(panel12);
        panel12.setLayout(panel12Layout);
        panel12Layout.setHorizontalGroup(
            panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro12, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel12Layout.setVerticalGroup(
            panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro12, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel13.setBackground(new java.awt.Color(220, 204, 204));
        panel13.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro13.setBackground(new java.awt.Color(224, 209, 209));
        cuadro13.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro13.setForeground(new java.awt.Color(99, 87, 87));
        cuadro13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel13Layout = new javax.swing.GroupLayout(panel13);
        panel13.setLayout(panel13Layout);
        panel13Layout.setHorizontalGroup(
            panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro13, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel13Layout.setVerticalGroup(
            panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro13, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel20.setBackground(new java.awt.Color(220, 204, 204));
        panel20.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro20.setBackground(new java.awt.Color(224, 209, 209));
        cuadro20.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro20.setForeground(new java.awt.Color(99, 87, 87));
        cuadro20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel20Layout = new javax.swing.GroupLayout(panel20);
        panel20.setLayout(panel20Layout);
        panel20Layout.setHorizontalGroup(
            panel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro20, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel20Layout.setVerticalGroup(
            panel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro20, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel21.setBackground(new java.awt.Color(220, 204, 204));
        panel21.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro21.setBackground(new java.awt.Color(224, 209, 209));
        cuadro21.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro21.setForeground(new java.awt.Color(99, 87, 87));
        cuadro21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel21Layout = new javax.swing.GroupLayout(panel21);
        panel21.setLayout(panel21Layout);
        panel21Layout.setHorizontalGroup(
            panel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro21, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel21Layout.setVerticalGroup(
            panel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro21, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel22.setBackground(new java.awt.Color(220, 204, 204));
        panel22.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro22.setBackground(new java.awt.Color(224, 209, 209));
        cuadro22.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro22.setForeground(new java.awt.Color(99, 87, 87));
        cuadro22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel22Layout = new javax.swing.GroupLayout(panel22);
        panel22.setLayout(panel22Layout);
        panel22Layout.setHorizontalGroup(
            panel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro22, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel22Layout.setVerticalGroup(
            panel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro22, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel23.setBackground(new java.awt.Color(220, 204, 204));
        panel23.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro23.setBackground(new java.awt.Color(224, 209, 209));
        cuadro23.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro23.setForeground(new java.awt.Color(99, 87, 87));
        cuadro23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel23Layout = new javax.swing.GroupLayout(panel23);
        panel23.setLayout(panel23Layout);
        panel23Layout.setHorizontalGroup(
            panel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro23, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel23Layout.setVerticalGroup(
            panel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro23, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel31.setBackground(new java.awt.Color(220, 204, 204));
        panel31.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro31.setBackground(new java.awt.Color(224, 209, 209));
        cuadro31.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro31.setForeground(new java.awt.Color(99, 87, 87));
        cuadro31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel31Layout = new javax.swing.GroupLayout(panel31);
        panel31.setLayout(panel31Layout);
        panel31Layout.setHorizontalGroup(
            panel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro31, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel31Layout.setVerticalGroup(
            panel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro31, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel32.setBackground(new java.awt.Color(220, 204, 204));
        panel32.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro32.setBackground(new java.awt.Color(224, 209, 209));
        cuadro32.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro32.setForeground(new java.awt.Color(99, 87, 87));
        cuadro32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel32Layout = new javax.swing.GroupLayout(panel32);
        panel32.setLayout(panel32Layout);
        panel32Layout.setHorizontalGroup(
            panel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro32, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel32Layout.setVerticalGroup(
            panel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro32, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel30.setBackground(new java.awt.Color(220, 204, 204));
        panel30.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro30.setBackground(new java.awt.Color(224, 209, 209));
        cuadro30.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro30.setForeground(new java.awt.Color(99, 87, 87));
        cuadro30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel30Layout = new javax.swing.GroupLayout(panel30);
        panel30.setLayout(panel30Layout);
        panel30Layout.setHorizontalGroup(
            panel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro30, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel30Layout.setVerticalGroup(
            panel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro30, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel33.setBackground(new java.awt.Color(220, 204, 204));
        panel33.setPreferredSize(new java.awt.Dimension(160, 160));

        cuadro33.setBackground(new java.awt.Color(224, 209, 209));
        cuadro33.setFont(new java.awt.Font("Perpetua Titling MT", 1, 48)); // NOI18N
        cuadro33.setForeground(new java.awt.Color(99, 87, 87));
        cuadro33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel33Layout = new javax.swing.GroupLayout(panel33);
        panel33.setLayout(panel33Layout);
        panel33Layout.setHorizontalGroup(
            panel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro33, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel33Layout.setVerticalGroup(
            panel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuadro33, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout tableroLayout = new javax.swing.GroupLayout(tablero);
        tablero.setLayout(tableroLayout);
        tableroLayout.setHorizontalGroup(
            tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tableroLayout.createSequentialGroup()
                        .addComponent(panel00, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tableroLayout.createSequentialGroup()
                        .addComponent(panel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tableroLayout.createSequentialGroup()
                        .addComponent(panel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tableroLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tableroLayout.setVerticalGroup(
            tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel00, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addComponent(name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(tablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel content;
    private javax.swing.JLabel cuadro00;
    private javax.swing.JLabel cuadro01;
    private javax.swing.JLabel cuadro02;
    private javax.swing.JLabel cuadro03;
    private javax.swing.JLabel cuadro10;
    private javax.swing.JLabel cuadro11;
    private javax.swing.JLabel cuadro12;
    private javax.swing.JLabel cuadro13;
    private javax.swing.JLabel cuadro20;
    private javax.swing.JLabel cuadro21;
    private javax.swing.JLabel cuadro22;
    private javax.swing.JLabel cuadro23;
    private javax.swing.JLabel cuadro30;
    private javax.swing.JLabel cuadro31;
    private javax.swing.JLabel cuadro32;
    private javax.swing.JLabel cuadro33;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel name;
    private javax.swing.JPanel panel00;
    private javax.swing.JPanel panel01;
    private javax.swing.JPanel panel02;
    private javax.swing.JPanel panel03;
    private javax.swing.JPanel panel10;
    private javax.swing.JPanel panel11;
    private javax.swing.JPanel panel12;
    private javax.swing.JPanel panel13;
    private javax.swing.JPanel panel20;
    private javax.swing.JPanel panel21;
    private javax.swing.JPanel panel22;
    private javax.swing.JPanel panel23;
    private javax.swing.JPanel panel30;
    private javax.swing.JPanel panel31;
    private javax.swing.JPanel panel32;
    private javax.swing.JPanel panel33;
    private javax.swing.JPanel panelScore;
    private javax.swing.JLabel score;
    private javax.swing.JPanel tablero;
    // End of variables declaration//GEN-END:variables
}

// Clase para detectar la pulsacion de teclas
class KeyMonitor extends KeyAdapter {
    Main display;

    KeyMonitor(Main display) {
        this.display = display;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        boolean seMueve = false;
        switch(event.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (this.display.obtenerFinJuego() == false) {
                    seMueve = this.display.moverArriba();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (this.display.obtenerFinJuego() == false) {
                    seMueve = this.display.moverAbajo();
                }
                break;
            case KeyEvent.VK_LEFT:
                if (this.display.obtenerFinJuego() == false) {
                    seMueve = this.display.moverIzquierda();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (this.display.obtenerFinJuego() == false) {
                    seMueve = this.display.moverDerecha();
                }
                break;
            default:
                System.out.println("Other");
        }
        if (seMueve) {
            this.display.siguienteFicha();
        } else {
            if (this.display.verificarFin()) {
                System.out.println("Fin del Juego");
                this.display.mostrarFin();
            }
        }
    }
}