package BetterSandSim;

import javax.swing.JFrame;

public class SandSim extends JFrame {
    
    public SandSim() {
        add(new GamePanel());

        setSize(500, 550);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new SandSim();
    }
}
