package proje4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AnaMenu extends JFrame {
    
    Klasik1 k = new Klasik1();
    Rastgele r = new Rastgele();

    JButton klasik;
    JButton rastgele;
    JButton cikis;
    
    JLabel ad;

    public AnaMenu() {
        
        k.setVisible(false);
        r.setVisible(false);
        
        setTitle("Ana Menü");
        setSize(960, 530);

        setLocationRelativeTo(null);    //JFrame' in ekranın ortasında durması için
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //çıkışa ya da x tuşuna basıldığında çıkması için
        setLayout(null);    //ekrandaki öğelerin ortalanması için
        
        
        klasik = new JButton("Klasik Oyun");
        rastgele = new JButton("Rastgele Oyun");
        cikis = new JButton("Çıkış");
        
        ad = new JLabel("GEZGIN ROBOT");
        
        add(klasik);
        add(rastgele);
        add(cikis);
        add(ad);
        
        klasik.setFont(new java.awt.Font("Segoe UI", 1, 12));
        klasik.setBounds(150, 250, 120, 40);
        klasik.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    klasikMouseClicked(evt);
                } catch (IOException ex) {
                    Logger.getLogger(AnaMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        rastgele.setFont(new java.awt.Font("Segoe UI", 1, 12));
        rastgele.setBounds(410, 250, 130, 40);
        rastgele.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rastgeleMouseClicked(evt);
            }
        });
        
        cikis.setFont(new java.awt.Font("Segoe UI", 1, 12));
        cikis.setBounds(680, 250, 120, 40);
        cikis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cikisMouseClicked(evt);
            }
        });
        
        
        ad.setFont(new java.awt.Font("Wide Latin", 1, 36)); // NOI18N
        ad.setBounds(210, 80, 600, 90);
        
        
        
        cikis.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    private void klasikMouseClicked(java.awt.event.MouseEvent evt) throws IOException {                                    
        this.setVisible(false);
        k.labYap();
        k.setVisible(true);
    }   
    
    private void rastgeleMouseClicked(java.awt.event.MouseEvent evt) {                                      
        this.setVisible(false);
        r.setVisible(true);
    } 
    
    private void cikisMouseClicked(java.awt.event.MouseEvent evt) {                                   
        System.exit(0);
    }  
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AnaMenu().setVisible(true);
            }
        });
    }

}
