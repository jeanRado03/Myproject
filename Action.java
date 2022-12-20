import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Action implements ActionListener {
    JFrame frame;
    JTextField text = new JTextField();
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
            JFrame frame = new JFrame();
            frame.setVisible(true);
            frame.setLayout(null);
            frame.setLocation(500,300);
            frame.setSize(140,140);
            JButton bt = new JButton("SetFileName");
            text.setBounds(4,4,115,46);
            bt.setBounds(4,52,115,44);
            bt.addActionListener(this);
            frame.add(text);
            frame.add(bt);
        }
        if(e.getActionCommand().matches("SetFileName")){
            String nom = text.getText();
            ClientServ.waiting(nom);
        }
    }
}
