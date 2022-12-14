import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Action implements ActionListener {
    JFrame frame;

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Action(JFrame frame){
        this.setFrame(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().matches("Exit")){
            this.getFrame().dispose();
        }else if(e.getActionCommand().matches("Send a File")){
            try {
                ClientServ.choice();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else if(e.getActionCommand().matches("receive")){
            ClientServ.waiting();
        }
    }
}
