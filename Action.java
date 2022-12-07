import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        }else{

        }
    }
}
