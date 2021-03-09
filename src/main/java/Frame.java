import javax.swing.ImageIcon;
import javax.swing.JFrame;

import java.io.IOException;


public class Frame extends JFrame{

    
    //ImageIcon img = new ImageIcon("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/board.png");
    
    public Frame() throws IOException{
        DragPanel dragPanel = new DragPanel();
        this.add(dragPanel);
        //this.add(img);
        this.setTitle("DeadWood");


<<<<<<< HEAD
        this.setSize(1200, 900);
=======
        this.setSize(1215, 936);
>>>>>>> refs/remotes/origin/master
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
