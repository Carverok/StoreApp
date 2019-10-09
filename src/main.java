
import forms.MainForm;
import java.awt.Window;
import javax.swing.JFrame;

public class main {
    private static MainForm mainForm;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mainForm  = new MainForm();
        mainForm.setResizable(false);
        mainForm.setLocationRelativeTo(null);
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainForm.show();
    }
}
