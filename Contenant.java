import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Contenant extends JPanel {
    public JTree trees;
    public JScrollPane scroll ;
    public JButton button1,button2,button3;
    public JLabel label;
    public JLabel statut;
    public static String connecter;

    Vector<DefaultMutableTreeNode> lists = new Vector<>();
    JFrame frame;

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Contenant(JFrame frame,String host) throws IOException {
        this.setFrame(frame);
        this.setLayout(null);
        this.setFocusable(true);
        File files = new File("D:\\myXender\\");
        for(File fichiers : files.listFiles()){
            DefaultMutableTreeNode parents = new DefaultMutableTreeNode(fichiers.getName());
            if(fichiers.isDirectory()){
                for(File lists : fichiers.listFiles()) {
                    DefaultMutableTreeNode children = new DefaultMutableTreeNode(lists.getName());
                    parents.add(children);
                }
            }
            lists.add(parents);
        }
        DefaultMutableTreeNode racines = new DefaultMutableTreeNode("D:\\myXender\\");
        for (int i = 0; i < lists.size(); i++) {
            racines.add(lists.get(i));
        }
        trees = new JTree(racines);
        trees.addMouseListener(new MouseL(trees));
        label = new JLabel("Client : "+host);
        label.setBounds(450,2,100,50);
        this.add(label);
        statut = new JLabel(connecter);
        statut.setBounds(410,26,300,50);
        this.add(statut);
        scroll = new JScrollPane(trees);
        scroll.setBounds(100,70,800,400);
        this.add(scroll);
        button1 = new JButton("Send a File");
        button1.setBounds(100,495,220,40);
        button1.addActionListener(new Action(this.getFrame()));
        button2 = new JButton("Exit");
        button2.addActionListener(new Action(this.getFrame()));
        button2.setBounds(680,495,220,40);
        button3 = new JButton("receive");
        button3.addActionListener(new Action(this.getFrame()));
        button3.setFocusable(false);
        button3.setBounds(390,495,220,40);
        this.add(button1);
        this.add(button2);
        this.add(button3);
    }
}
