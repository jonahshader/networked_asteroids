package jonahshader;

import jonahshader.client.MainApp;
import processing.core.PApplet;

import javax.swing.*;

public class ClientLauncher {
    public static void main(String[] args) {
        MainApp app = new MainApp(JOptionPane.showInputDialog(new JFrame(), "Enter IP (without port):"), Integer.parseInt(JOptionPane.showInputDialog(new JFrame(),
                "Enter port:")));
        PApplet.runSketch(new String[]{"MainApp"}, app);
    }
}
