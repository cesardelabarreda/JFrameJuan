/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JFrameJuanitoGame;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Cesar
 */
                                       
public class SoundClip { 
                                                       
        private AudioInputStream sample;
        private Clip clip; 
        private boolean looping = false;
        private int repeat = 0;
        private String filename = "";
//El siguiente mÃ©todo es el constructor vacio, crea el objeto SoundClip con el buffer de sonido.

        public SoundClip() {                                      
                try {  
                                                      
                      clip = AudioSystem.getClip();
                }catch (LineUnavailableException e) {  
                                                      
                      System.out.println("Error en " + e.toString());
                }
        }
//AdemÃ¡s se usa un constructor con parÃ¡metros que lo que hace es, manda llamar al constructor default y carga el archivo de sonido del nombre del archivo de sonido dado como parÃ¡metro.

        public SoundClip(String filename) {                                       
                this();
                load(filename);
        }
//Ahora tenemos los mÃ©todos modificadores, usados para cambiar los valores de nuestro objeto SoundClip.

        public void setLooping(boolean looping) {
                this.looping = looping; 
        }           
                                             
        public void setRepeat(int repeat) {
                this.repeat = repeat;
        }  
                                                      
        public void setFilename(String filename) {
                this.filename = filename; 
        }
//Los mÃ©todos de acceso son usados para obtener los valores del objeto SoundClip.

        public Clip getClip() {                                                        
                return clip; 
        }
                                                   
        public boolean getLooping() {                                                       
                return looping;
        }
                                                      
        public int getRepeat() {                                                        
                return repeat; 
        }
                                                     
        public String getFilename() { 
                                                       
                return filename;
        }
                                                    
        private URL getURL(String filename) {                                                       
                URL url = null;
                try {
                                                     
                      url = this.getClass().getResource(filename);
                }catch (Exception e) {
                                                       
                      System.out.println("Error en " + e.toString());
                }
                return url;
        }
//Para verificar si el archivo esta cargado o no, usamos el mÃ©todo isLoaded.

        public boolean isLoaded() {                                                       
                return (boolean)(sample != null);
        }
//El mÃ©todo load nos sirve para poder cargar el archivo de audio, recibe como parÃ¡metro un String con el nombre del archivo.

        public boolean load(String audiofile) {                                                       
                try {

                      setFilename(audiofile);
                      sample = AudioSystem.getAudioInputStream(getURL(filename)); 
                      clip.open(sample); 
                      return true;
                } catch (IOException e) { 
                                                       
                      System.out.println("Error en " + e.toString());
                      return false;
                }catch (UnsupportedAudioFileException e) {
                                                        
                      System.out.println("Error en " + e.toString());
                      return false;
                }catch (LineUnavailableException e) {
                                                      
                      System.out.println("Error en " + e.toString());
                      return false;
                }
        }
//Para reproducir el archivo de sonido utilizamos el mÃ©todo play, que se encarga de verificar si el archivo ha sido cargado o no y ademÃ¡s si el archivo debe reproducirse en forma continua.

        public void play() { 
                                                       
                if (!isLoaded()) 
                    return;
                                                        
                clip.setFramePosition(0);
                                                     
                clip.loop(repeat);
        }
//El mÃ©todo stop se encarga simplemente de parar la reproducciÃ³n del sonido.

        public void stop() { 
                                                       
                clip.stop();
        }
}
