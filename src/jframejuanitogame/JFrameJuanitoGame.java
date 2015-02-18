/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframejuanitogame;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cesar
 */
public class JFrameJuanitoGame extends JFrame implements Runnable, KeyListener{
    
    private int iPuntos = 0;          // Entero de puntos con valor 100
    private int iDireccion;            // Entero de la direccion del objeto
    private int iVidas;
    private int iVel = 2;
    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basPrincipal;         // Objeto principal
    private Base basMalo;         // Objeto malo
    private LinkedList<Base> lklChimpy; // Coleccion de chimpys
    private LinkedList<Base> lklDiddy; // Coleccion de diddys
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip adcSonidoChimpy;   // Objeto sonido de Chimpy
    private AudioClip adcSonidoDiddy;   // Objeto sonido de Chimpy
    private boolean bPausa = true;     // Se declara el booleano para pausar
    private boolean bGame = true;       //Boolean que finaliza el juego


    public JFrameJuanitoGame(){
        //Define el título de la ventana
        setTitle("JuanitoGame");
        //Define la operación que se llevará acabo cuando la ventana sea cerrada.
        // Al cerrar, el programa terminará su ejecución
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Define el tamaño inicial de la ventana
        // hago el applet de un tamaño 500,500
        setSize(800,500);


        URL urlImagenPrincipal = this.getClass().getResource("juanito.gif");

        int iAzarVidas = (int) (Math.random() * 3) + 4;
        iVidas = iAzarVidas;
        // se crea el objeto para principal 
        basPrincipal = new Base(0, 0, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // se posiciona a principal  en la esquina superior izquierda del Applet 
        basPrincipal.setX(getWidth() / 2);
        basPrincipal.setY(getHeight() / 2);

        // defino la imagen del malo
        URL urlImagenMalo = this.getClass().getResource("chimpy.gif");
        URL urlImagenMalo2 = this.getClass().getResource("diddy.gif");

        // se crea lista de objetos chimpy y diddy
        lklChimpy = new LinkedList();
        lklDiddy = new LinkedList();

        // creo random de 5 a 8
        int iAzar = (int) (Math.random() * 4) + 5;

        // genero cada chango y lo añado a la lista
        for (int iI = 0; iI < 5; iI ++) {
            int iAzar2 = (int) (Math.random() * 7);
            // se posiciona chango 
            int iPosA = (int) (Math.random() * (3 * getWidth() / 4));
            int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO + iPosA ;   
            int iPosY = iAzar2 * getHeight() / iMAXALTO;
            // se crea el objeto para chango 
            Base basChimpy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
            lklChimpy.add(basChimpy);
        }

        // genero cada chango y lo añado a la lista
        for (int iI = 0; iI < 5; iI ++) {
            int iAzar2 = (int) (Math.random() * 7);
            // se posiciona chango 
            int iPosX = (int) (Math.random() * (3 * getHeight() / -4));
            int iPosY = iAzar2 * getHeight() / iMAXALTO;
            // se crea el objeto para chango 
            Base basDiddy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo2));
            lklDiddy.add(basDiddy);
        }

        addKeyListener(this);

        URL urlSonidoChimpy = this.getClass().getResource("monkey1.wav");
      //  adcSonidoChimpy = getAudioClip (urlSonidoChimpy);

        URL urlSonidoDiddy = this.getClass().getResource("monkey2.wav");
       // adcSonidoDiddy = getAudioClip (urlSonidoDiddy);

        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
    
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (bGame) {
            if (bPausa){
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
        }

        JOptionPane.showMessageDialog(null, "Juego Terminado", "GAME OVER", 1);
        System.exit(0);
    }
    
    
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        if (iVidas < 1) {
            bGame = false;
        }

        // actualizo al fantasma a que vaya para la derecha
        for (Base basChimpy : lklChimpy) {
            basChimpy.setX(basChimpy.getX() - iVel);
        }

        // actualizo al fantasma a que vaya para la derecha
        for (Base basDiddy : lklDiddy) {
            basDiddy.setX(basDiddy.getX() + iVel);
        }

        switch(iDireccion) {

            case 1: {
                basPrincipal.setY(basPrincipal.getY() - getHeight() / iMAXALTO);
                iDireccion = 0;
                break; //se mueve hacia arriba
            }
            case 2: {
                basPrincipal.setY(basPrincipal.getY() + getHeight() / iMAXALTO);
                iDireccion = 0;
                break; //se mueve hacia abajo
            }
            case 3: {
                basPrincipal.setX(basPrincipal.getX() - getWidth() / iMAXANCHO);
                iDireccion = 0;
                break; //se mueve hacia la izquierda
            }
            case 4: {
                basPrincipal.setX(basPrincipal.getX() + getWidth() / iMAXANCHO);
                iDireccion = 0;
                break; //se mueve hacia la derecha
            }
        }


    }

    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        //Colision del Planeta con el Applet dependiendo a donde se mueve.
        if(basPrincipal.getY() < 0) { // esta pasando el limite en Y
            basPrincipal.setY(0);
        }       
        // si se esta saliendo del applet//  
        if(basPrincipal.getY() + basPrincipal.getAlto() > getHeight()) {
            basPrincipal.setY(getHeight() - basPrincipal.getAlto());
        }
        if(basPrincipal.getX() < 0) { // esta pasando el limite en X
            basPrincipal.setX(0);
        }
        // si se esta saliendo del applet//   
        if(basPrincipal.getX() + basPrincipal.getAncho() > getWidth()) {          
            basPrincipal.setX(getWidth() - basPrincipal.getAncho());
        }

        // checo colision entre ambos Planetas
        for (Base basChimpy : lklChimpy) {
            if(basPrincipal.intersecta(basChimpy)) {
                iPuntos += 10;
                int iAzar2 = (int) (Math.random() * 7);
                // se posiciona chango 
                int iPosA = (int) (Math.random() * (3 * getWidth() / 4));
                int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO + iPosA;   

                basChimpy.setX((iMAXANCHO - 1) * getWidth() / iMAXANCHO + iPosA);
                basChimpy.setY(iAzar2 * getHeight() / iMAXALTO);
                adcSonidoChimpy.play();

            }
            if(basChimpy.getX() < 0) {   
                int iAzar2 = (int) (Math.random() * 7);
                // se posiciona chango 
                basChimpy.setX((int) (Math.random() * (3 *getWidth() / 4)) + 
                        getWidth());
                basChimpy.setY(iAzar2 * getHeight() / iMAXALTO);
            }
        }

        // checo colision entre ambos Planetas
        for (Base basDiddy : lklDiddy) {
            if(basPrincipal.intersecta(basDiddy)) {
                iVidas --;
                int iAzar2 = (int) (Math.random() * 7);
                // se posiciona chango 
                basDiddy.setX((int) (Math.random() * (3 *getWidth() / -4)));
                basDiddy.setY(iAzar2 * getHeight() / iMAXALTO);
                adcSonidoDiddy.play();
                int iAzar3 = (int) (Math.random() * 3) + 4;
                iVel ++;


            }
            if(basDiddy.getX() + basDiddy.getAncho() > getWidth()) {   
                int iAzar2 = (int) (Math.random() * 7);
                // se posiciona chango 
                basDiddy.setX((int) (Math.random() * (3 *getWidth() / -4)));
                basDiddy.setY(iAzar2 * getHeight() / iMAXALTO);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_UP) { 
            iDireccion = 1;
        } 
        //Presiono flecha abajo
        else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
            iDireccion = 2;
        }
        //Presiono flecha izquierda
        else if (ke.getKeyCode() == KeyEvent.VK_LEFT) { 
            iDireccion = 3;
        } 
        //Presiono flecha derecha
        else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) { 
            iDireccion = 4;
        }
        if (ke.getKeyCode() == 'P') { 
            if (bPausa) {
                bPausa = false;
            }
            else {
                bPausa = true;
            }
        } 
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) { 
            bGame = false;
        } 
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) { 
            bGame = false;
        } 
    }
    
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint2(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    public void paint2(Graphics graDibujo) {
        // si la imagen ya se cargo
        if (basPrincipal != null) {
                //Dibuja la imagen de principal en el Applet
                basPrincipal.paint(graDibujo, this);
                //Dibuja la imagen de malo en el Applet
                for (Base basChimpy : lklChimpy) {
                    basChimpy.paint(graDibujo, this);
                }
                for (Base basDiddy : lklDiddy) {
                    basDiddy.paint(graDibujo, this);
                }
                graDibujo.drawString("Puntos: " + iPuntos, 50, 50);            
                graDibujo.drawString("Vidas: " + iVidas, 50, 70);                            
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Crea un nuevo objeto JFrameHolaMundo
        JFrameJuanitoGame Juego = new JFrameJuanitoGame();
        //Despliega la ventana en pantalla al hacerla visible
        Juego.setVisible(true);
    }

    
}













