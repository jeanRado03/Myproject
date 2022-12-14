import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Action1 implements ActionListener {
    JFrame frame;

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Action1(JFrame frame){
        this.setFrame(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().matches("Exit")){
            this.getFrame().dispose();
        }else{
            try {
                ClientServ1.choice1();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
