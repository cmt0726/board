import java.io.IOException;

public class Gui {
    public Gui() throws IOException { 
        Frame f = new Frame();
    }
}






// import java.awt.Dimension;
// import java.awt.Graphics;
// import java.awt.Image;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// //ww  w  .  j av a2s  . com
// import javax.swing.ImageIcon;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.JLabel;
// import javax.swing.*;

// import java.awt.Color;
// import java.awt.Container;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Image;
// import java.awt.MediaTracker;
// import java.awt.event.MouseEvent;
// import java.awt.event.MouseMotionAdapter;
// import java.awt.event.WindowAdapter;
// import java.awt.event.WindowEvent;
// import java.awt.image.BufferedImage;

// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.border.TitledBorder;

// public class Gui extends javax.swing.JFrame {
//     ImagePanel canvas;
//     public Gui() throws Exception{
//         super();
//         Container container = getContentPane();

//         canvas = new ImagePanel();
//         container.add(canvas);

//         addWindowListener(new WindowAdapter() {
//             public void windowClosing(WindowEvent e) {
//                 System.exit(0);
//             }
//         });
        
//         //JFrame f = new JFrame();
        
//         //Image img = Toolkit.getDefaultToolkit().getImage("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/board.png");

//         // ImagePanel panel = new ImagePanel(
//         // new ImageIcon("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/board.png").getImage());

//         // ImageIcon panelDice = new ImageIcon(
//         //     new ImageIcon("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/dice_b1.png").getImage()
//         // ); 
//         // //panelDice.action(evt, what)

//         // var label1 = new JLabel(panelDice, JLabel.CENTER);
//         // var listener = new DragMouseAdapter();
//         // JFrame frame = new JFrame();
//         // label1.addMouseListener(listener);
//         // label1.setTransferHandler(new TransferHandler("icon"));
//         // frame.getContentPane().add(label1);
//         // //frame.getContentPane().add(panel);
        
//         // frame.pack();
//         // frame.setVisible(true);
//         // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         // setLocationRelativeTo(null);
//     }


//     private class DragMouseAdapter extends MouseAdapter {
//         public void mousePressed(MouseEvent e) {
//             JComponent c = (JComponent) e.getSource();
//             var handler = c.getTransferHandler();
//             handler.exportAsDrag(c, e, TransferHandler.COPY);
//         }
//     }
        
        
// }

// class ImagePanel extends JPanel {
//     int x, y;

//     BufferedImage bi;

//     public ImagePanel() {
//         setBackground(Color.white);
//         setSize(450, 400);
//         addMouseMotionListener(new MouseMotionHandler());

//         Image image = getToolkit().getImage("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/dice_b1.png");
//         Image imageBoard = getToolkit().getImage("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/board.png");
        
//         MediaTracker mt = new MediaTracker(this);
//         mt.addImage(imageBoard, 1);
//         //mt.addImage(image, 1);
//         try {
//             mt.waitForAll();
//         } catch (Exception e) {
//             System.out.println("Exception while loading image.");
//         }

//         if (imageBoard.getWidth(this) == -1) {
//             System.out.println("no gif file");
//             System.exit(0);
//         }

        

//         //bi = new BufferedImage(image.getWidth(this), image.getHeight(this),
//             //BufferedImage.TYPE_INT_ARGB);
//         BufferedImage bi2 = new BufferedImage(imageBoard.getWidth(this), imageBoard.getHeight(this),
//             BufferedImage.TYPE_INT_ARGB); 
//         //Graphics2D big = bi.createGraphics();
//         Graphics2D big2 = bi2.createGraphics();
//         //big.drawImage(image, 0, 0, this);
//         big2.drawImage(imageBoard, 0, 0, this);
//   }

//   public void paintComponent(Graphics g) {
//     super.paintComponent(g);
//     Graphics2D g2D = (Graphics2D) g;

//     g2D.drawImage(bi, x, y, this);
//   }

//   class MouseMotionHandler extends MouseMotionAdapter {
//     public void mouseDragged(MouseEvent e) {
//       x = e.getX();
//       y = e.getY();
//       repaint();
//     }
//   }
//     // private Image img;
      
//     // public ImagePanel(String img) {
//     //     this(new ImageIcon(img).getImage());
//     // }
      
//     // public ImagePanel(Image img) {
        
//     //     this.img = img;
//     //     Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
//     //     setPreferredSize(size);
//     //     setMinimumSize(size);
//     //     setMaximumSize(size);
//     //     setSize(size);
//     //     setLayout(null);
//     // }
      
//     // public void paintComponent(Graphics g) {
//     //     g.drawImage(img, 0, 0, null);
//     // }

    
// }



