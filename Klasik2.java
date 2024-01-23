package proje4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Klasik2 extends JFrame {

    //duvarlar
    final static int X = 1;
    //boş alanlar
    final static int Y = 0;
    //başlangıç noktası
    final static int S = 2;
    //bitiş noktası
    final static int E = 8;
    // ilerlenen yol
    final static int V = 9;

    Random r = new Random();
    //başlangıç noktası koordinatları
    //int START_I = r.nextInt(0, 10), START_J = r.nextInt(0, 10);
    final static int START_I = 1, START_J = 1;
    //bitiş noktası koordinatları
    //int END_I = r.nextInt(0, 10), END_J = r.nextInt(0, 10);
    final static int END_I = 8, END_J = 6;

    int[][] maze = new int[10][10];

    //rastgele oluşturulmuş diziyi saklayacağımız rastgele dizi için.
    int[][] arr;

    JButton labCoz;
    JButton anaMenu;
    JButton cikis;
    JButton otMaze;

    JLabel elapsedDfs;
    JTextField textDfs;

    boolean repaint = false;

    //süreyi başlat
    long startTime;
    //süreyi durdur
    long stopTime;
    //geçen süreyi hesapla
    long duration;
    //DFS süresi
    double dfsTime;

    //orijinal labirenti koplayayıp çözümü göstermek için kullanacağımız dizi
    int[][] savedMaze = maze.clone();

    public Klasik2() {
        setTitle("Klasik Oyun - 2");     //JFrame adı
        setSize(960, 530);    //JFrame' in boyut ayarlaması

        setLocationRelativeTo(null);    //JFrame' in ekranın ortasında durması için
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //JFrame içerisindeki hazır kodlardan
        setLayout(null);    //buton konumlarını kendimizin ayarlaması için yazılan kod

        elapsedDfs = new JLabel("Çözüm Süresi :");
        textDfs = new JTextField();

        //buton isimlendirme
        labCoz = new JButton("Labirent Çöz");
        labCoz.setFont(new java.awt.Font("Segoe UI", 1, 12));
        anaMenu = new JButton("Ana Menü");
        anaMenu.setFont(new java.awt.Font("Segoe UI", 1, 12));
        cikis = new JButton("Çıkış");
        cikis.setFont(new java.awt.Font("Segoe UI", 1, 12));
        otMaze = new JButton("1. Labirente Geç");
        otMaze.setFont(new java.awt.Font("Segoe UI", 1, 12));

        //buton ekleme
        add(labCoz);
        add(anaMenu);
        add(elapsedDfs);
        add(textDfs);
        add(cikis);
        add(otMaze);

        //görünürlüğü açıyor(bunu yazmayınca ekran görünmüyor)
        setVisible(true);

        //buton konumları
        labCoz.setBounds(580, 50, 110, 40);
        anaMenu.setBounds(710, 50, 110, 40);
        cikis.setBounds(710, 115, 110, 40);
        elapsedDfs.setBounds(580, 100, 110, 40);
        textDfs.setBounds(580, 130, 110, 25);
        otMaze.setBounds(580, 180, 242, 40);
        textDfs.setEnabled(false);

        otMaze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Klasik1 k = new Klasik1();
                try {
                    k.labYap();
                } catch (IOException ex) {
                    Logger.getLogger(Klasik2.class.getName()).log(Level.SEVERE, null, ex);
                }
                ekranKapa();
                k.setVisible(true);
            }
        });

        cikis.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        anaMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ekranKapa();
                AnaMenu a = new AnaMenu();
                a.setVisible(true);
            }
        });

        labCoz.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (arr == null) {       //rastgele dizi boşsa
                    restore(savedMaze);  //labirenti orijinal haline döndürür
                    repaint = false;    //labirent çözümünü boyamak için ayarlama kodu
                    labCoz();       //labCoz methodunu çağırır ve labirenti DFS ile çözer
                    sRoute();       //gidilen yol içinden en kısa olanı işaretler
                    repaint();      //labirenti boyar (gidilen yolları)
                }
            }
        });
    }

    //labirent boyutunu döndürür
    public int Size() {
        return maze.length;
    }

    //labirent içindeyse true değer döndürür
    public boolean isInMaze(int i, int j) {  //parametreler konum bilgisi için

        if (i >= 0 && i < Size() && j >= 0 && j < Size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInMaze(Labirent konum) {
        return isInMaze(konum.i(), konum.j());
    }

    //dizideki konumu belirli bir değerle işaretlemek için (örneğin yeni gidilen yeri yeşile boyama gibi)
    public int mark(int i, int j, int value) {
        assert (isInMaze(i, j));    //deneme için yazıldı eğer hata olursa AssertionError' u döndürür
        int temp = maze[i][j];    //temp' i orjinal değerle doldurur
        maze[i][j] = value;     //belirtilen konuma value de karşılık gelen değeri atar
        return temp;
    }

    public int mark(Labirent konum, int value) {
        return mark(konum.i(), konum.j(), value);   //konum değerlerini alıp ilk işaretleme methodunu çağırır
    }

    //gidilen konum değeri 9' a eşitse true döndürür
    public boolean isMarked(int i, int j) {
        assert (isInMaze(i, j));
        return (maze[i][j] == V);
    }

    public boolean isMarked(Labirent konum) {
        return isMarked(konum.i(), konum.j());
    }

    //gidilen konum değeri 0' a eşitse true döndürür
    public boolean isClear(int i, int j) {
        assert (isInMaze(i, j));
        return (maze[i][j] != X && maze[i][j] != V && maze[i][j] != 3 && maze[i][j] != 2);
    }

    public boolean isClear(Labirent konum) {
        return isClear(konum.i(), konum.j());
    }

    //hedefe ulaşıp ulaşmadığını kontrol eder
    public boolean isFinal(int i, int j) {

        return (i == Klasik2.END_I && j == Klasik2.END_J);
    }

    public boolean isFinal(Labirent konum) {
        return isFinal(konum.i(), konum.j());
    }

    //eğer rastgele labirent oluşturamazsa ilk haline alan method
    public void restore(int[][] savedMazed) {
        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
                maze[i][j] = savedMazed[i][j];
            }
        }
        maze[1][1] = 2;
        maze[8][6] = 8;
    }

    //JFrame' de labirent çizme
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(70, 70);    //labirentin (70,70) konumunda başlamasını sağlar

        if (repaint == false) {
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color color;
                    switch (maze[row][col]) {
                        case 1:
                            color = Color.GRAY;
                            break;
                        case 2:
                            color = Color.black;
                            break;
                        case 3:
                            color = Color.darkGray;
                            break;
                        case 4:
                            color = Color.GREEN;
                            break;
                        case 5:
                            color = Color.CYAN;
                            break;
                        case 8:
                            color = Color.RED;
                            break;
                        case 9:
                            color = Color.orange;
                            break;
                        default:
                            color = Color.WHITE;
                    }
                    g.setColor(color);
                    g.fillRect(40 * col, 40 * row, 40, 40);
                    g.setColor(Color.BLUE);
                    g.drawRect(40 * col, 40 * row, 40, 40);
                }
            }
        }
    }

    public void ekranKapa() {
        this.setVisible(false);
    }

    public void labCoz() {

        startTime = System.nanoTime();

        //Labirent yığını oluşturup nereye gideceğini karar vermesini sağlıyoruz
        Stack<Labirent> yigin = new Stack<Labirent>();

        //başlangıç konumunu yığına ekliyoruz
        yigin.push(new Labirent(START_I, START_I));

        Labirent simdi;   //şimdiki düğüm
        Labirent sonra;  //sonraki düğüm
        while (!yigin.empty()) {

            //şu an ki konumu değişkene atama
            simdi = yigin.pop();
            if (isFinal(simdi)) {
                break;
            }

            //şu an ki konumu keşfedince işaretle
            mark(simdi, V);

            //komşu konumları yığına ekleme
            sonra = simdi.yukari();    //şu an ki konumu yukarı hareket ettirme
            if (isInMaze(sonra) && isClear(sonra)) {
                yigin.push(sonra);
            }
            sonra = simdi.sag();    //şu an ki konumu sağa hareket ettirme
            if (isInMaze(sonra) && isClear(sonra)) {
                yigin.push(sonra);
            }
            sonra = simdi.sol();    //şu an ki konumu sola hareket ettirme
            if (isInMaze(sonra) && isClear(sonra)) {
                yigin.push(sonra);
            }
            sonra = simdi.asagi();    //şu an ki konumu aşağı hareket ettirme
            if (isInMaze(sonra) && isClear(sonra)) {
                yigin.push(sonra);
            }
        }

        if (!yigin.empty()) {   //çıkışa ulaşılıca gösterilen mesaj ekranı
            stopTime = System.nanoTime();
            JOptionPane.showMessageDialog(rootPane, "Çıkışa Ulaşıldı");
        } else {    //labirent çözümsüzse verilen mesaj ekranı
            JOptionPane.showMessageDialog(rootPane, "Labirentte Sıkıştın :(");
        }

        duration = stopTime - startTime;

        dfsTime = (double) duration / 100000;   //saniyeye çevirme
        System.out.println(String.format("Time %1.3f s", dfsTime));

        textDfs.setText(String.format("%1.3f s", dfsTime));

    }

    public int[][] labYap() throws IOException {

        URL url = new URL("http://bilgisayar.kocaeli.edu.tr/prolab2/url2.txt");
        Scanner s = new Scanner(url.openStream());
        StringBuffer sB = new StringBuffer();
        while (s.hasNext()) {
            sB.append(s.next());
        }
        String result = sB.toString();
        int c = 0;
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 10; b++) {
                maze[a][b] = Integer.valueOf(result.charAt(c)) - 48;
                c++;
            }
        }
        Random rnd = new Random();
        maze[1][1] = 4;
        maze[8][6] = 8;
        maze[3][2] = 0;
        maze[3][3] = 0;
        maze[6][4] = 0;
        maze[6][3] = 0;
        maze[7][4] = 0;
        maze[3][6] = 0;
        maze[2][5] = 0;
        maze[1][6] = 0;
        maze[6][8] = 0;
        maze[1][5] = 0;
        maze[3][5] = 0;
        return maze;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Klasik1 lab = new Klasik1();      //constructor' ı çağıracak yeni sınıfı oluşturuyoruz
            }
        });
    }

    public void sRoute(int[][] maze) {
        Stack<Integer> yigin = new Stack<Integer>();
        int c = 0;
        for(int a = 0; a<maze.length; a++){
            for(int b = 0; b<maze[a].length; b++){
                if (maze[a][b] == yigin.get(c)){
                    maze[a][b] = 5;
                    c++;
                }
            }
        }
        
    }

    
    
    
    
    
    
    
    public int[][] sRoute() {
        maze[1][1] = 5;
        maze[1][0] = 5;
        maze[2][0] = 5;
        maze[3][0] = 5;
        maze[4][0] = 5;
        maze[5][0] = 5;
        maze[5][1] = 5;
        maze[5][2] = 5;
        maze[5][3] = 5;
        maze[5][4] = 5;
        maze[5][5] = 5;
        maze[6][5] = 5;
        maze[7][5] = 5;
        maze[8][5] = 5;
        maze[8][6] = 5;
        return maze;
    }
}
