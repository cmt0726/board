import javax.swing.ImageIcon;
import javax.swing.JFrame;

import java.io.IOException;


public class Frame extends JFrame{

    
    //ImageIcon img = new ImageIcon("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/board.png");
    
    public Frame(Board board) throws IOException{
        DragPanel dragPanel = new DragPanel(board);
        this.add(dragPanel);
        //this.add(img);
        this.setTitle("DeadWood");


        //this.setSize(1200, 900);

        this.setSize(1445, 936);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
    }
}
