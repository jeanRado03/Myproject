import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MouseL implements MouseListener {
    JTree trees;

    public JTree getTrees() {
        return trees;
    }

    public void setTrees(JTree trees) {
        this.trees = trees;
    }

    public MouseL(JTree trees) throws IOException {
        this.setTrees(trees);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 3){
            StringBuilder src = new StringBuilder(this.getTrees().getAnchorSelectionPath().getPathComponent(0).toString());
            for (int i = 1; i < this.getTrees().getAnchorSelectionPath().getPathCount(); i++) {
                src.append(this.getTrees().getAnchorSelectionPath().getPathComponent(i).toString()).append("\\");
            }
            src.deleteCharAt(src.lastIndexOf("\\"));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
