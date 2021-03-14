import javax.swing.JFrame;

import java.io.IOException;


public class Frame extends JFrame{
    
    public Frame(Board board) throws IOException{
        DragPanel dragPanel = new DragPanel(board);
        this.add(dragPanel);
        this.setTitle("DeadWood");
        this.setSize(1445, 936);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
    }
}
