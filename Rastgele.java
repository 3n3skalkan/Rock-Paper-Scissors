package proje4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Rastgele extends JFrame {

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

    //başlangıç noktası koordinatları
    final static int START_I = 1, START_J = 1;
    //bitiş noktası koordinatları
    final static int END_I = 8, END_J = 8;

    int[][] maze = new int[][]{
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 2, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 1, 1, 0, 1},
        {1, 0, 1, 1, 1, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 0, 1, 0, 0, 0, 1},
        {1, 1, 0, 1, 0, 1, 1, 0, 0, 1},
        {1, 1, 0, 0, 0, 0, 0, 0, 8, 1},
        {1, 1, 1, 1, 1, 1, 0, 1, 1, 1}
    };

    //rastgele oluşturulmuş diziyi saklayacağımız rastgele dizi için.
    int[][] arr;
    int step, boyut;

    JButton labCoz;
    JButton anaMenu;
    JButton cikis;
    JButton genRandom;
    JButton giris;

    JLabel elapsedDfs;
    JLabel soru;
    JLabel stepCount;
    JTextField textDfs;
    JTextField take;
    JTextField counter;

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
    int[][] savedMaze = clone();

    public Rastgele() {

        setTitle("Rastgele Oyun");     //JFrame adı
        setSize(960, 530);    //JFrame' in boyut ayarlaması

        setLocationRelativeTo(null);    //JFrame' in ekranın ortasında durması için
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //JFrame içerisindeki hazır kodlardan
        setLayout(null);    //buton konumlarını kendimizin ayarlaması için yazılan kod

        elapsedDfs = new JLabel("Çözüm Süresi :");
        soru = new JLabel("Labirentin Boyutunu Giriniz :");
        stepCount = new JLabel("Gidilen Adım Sayısı :");
        textDfs = new JTextField();
        take = new JTextField();
        counter = new JTextField();

        //buton isimlendirme
        labCoz = new JButton("Labirent Çöz");
        labCoz.setFont(new java.awt.Font("Segoe UI", 1, 12));
        anaMenu = new JButton("Ana Menü");
        anaMenu.setFont(new java.awt.Font("Segoe UI", 1, 12));
        cikis = new JButton("Çıkış");
        cikis.setFont(new java.awt.Font("Segoe UI", 1, 12));
        genRandom = new JButton("Rastgele Labirent Oluştur");
        genRandom.setFont(new java.awt.Font("Segoe UI", 1, 12));
        giris = new JButton("Oyuna Başla");
        giris.setFont(new java.awt.Font("Segoe UI", 1, 12));

        //buton ekleme
        add(labCoz);
        add(anaMenu);
        add(elapsedDfs);
        add(textDfs);
        add(cikis);
        add(genRandom);
        add(giris);
        add(soru);
        add(take);
        add(stepCount);
        add(counter);

        //görünürlüğü açıyor(bunu yazmayınca ekran görünmüyor)
        setVisible(true);
        
        textDfs.setEnabled(false);
        counter.setEnabled(false);

        //buton konumları
        labCoz.setBounds(580, 50, 110, 40);
        anaMenu.setBounds(710, 50, 110, 40);
        cikis.setBounds(710, 115, 110, 40);
        elapsedDfs.setBounds(580, 100, 110, 40);
        genRandom.setBounds(580, 180, 242, 40);
        textDfs.setBounds(580, 130, 110, 25);
        giris.setBounds(580, 180, 110, 40);
        take.setBounds(580, 140, 242, 25);
        soru.setBounds(580, 100, 242, 40);
        stepCount.setBounds(580, 225, 242, 40);
        counter.setBounds(580, 255, 242, 25);

        giris.setEnabled(false);
        labCoz.setVisible(false);
        anaMenu.setVisible(false);
        cikis.setVisible(false);
        elapsedDfs.setVisible(false);
        genRandom.setVisible(false);
        textDfs.setVisible(false);
        stepCount.setVisible(false);
        counter.setVisible(false);

        giris.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                girisMouseClicked(evt);
            }

            private void girisMouseClicked(MouseEvent evt) {
                giris.setVisible(false);
                take.setVisible(false);
                soru.setVisible(false);
                labCoz.setVisible(true);
                anaMenu.setVisible(true);
                cikis.setVisible(true);
                elapsedDfs.setVisible(true);
                genRandom.setVisible(true);
                textDfs.setVisible(true);
                stepCount.setVisible(true);
                counter.setVisible(true);
            }
        });

        take.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sizeCtrl(take.getText());
            }
        });

        //rastgele labirent oluştur butonuna ait actionlistener kodları
        genRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Rastgele r = new Rastgele();
                ekranKapa();
                r.setVisible(true);*/
                int x[][] = diziOlustur();    //rastgele dizi oluşturur ve x' in içine atar
                repaint = true;
                restore(x);     //ekrandaki labirenti düzenleyerek x' in içindeki matrise ait labirenti atar
                repaint();    //labirenti x' e uygun olarak yeniden boyar
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
                    step = 0;
                    restore(savedMaze);  //labirenti orijinal haline döndürür
                    repaint = false;    //labirent çözümünü boyamak için ayarlama kodu
                    labCoz();       //labCoz methodunu çağırır ve labirenti DFS ile çözer
                    repaint();      //labirenti boyar (gidilen yolları)
                    counter.setText(String.format("%d", step - 2));
                } else {    //rastgele dizi oluşturulmuşsa
                    step = 0;
                    restore(arr);       //rastgele dizi içerisindeki labirenti oluşturur
                    repaint = false;    //labirent çözümünü boyamak için ayarlama kodu
                    labCoz();       //labCoz methodunu çağırır ve labirenti DFS ile çözer
                    repaint();      //labirenti boyar (gidilen yolları)
                    counter.setText(String.format("%d", step - 2));
                }
            }
        });
    }

    public void sizeCtrl(String value) {
        take.setEnabled(false);
        if (Integer.valueOf(value) > 5 && Integer.valueOf(value) <= 20) {
            boyut = Integer.valueOf(value);
            giris.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Hatalı Değer Girildi");
            take.setEnabled(true);
            giris.setEnabled(false);
        }
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
        return (maze[i][j] != X && maze[i][j] != V);
    }

    public boolean isClear(Labirent konum) {
        return isClear(konum.i(), konum.j());
    }

    //hedefe ulaşıp ulaşmadığını kontrol eder
    public boolean isFinal(int i, int j) {

        return (i == Rastgele.END_I && j == Rastgele.END_J);
    }

    public boolean isFinal(Labirent konum) {
        return isFinal(konum.i(), konum.j());
    }

    //ilk oluşturduğumuz labirenti kopyalayan method
    public int[][] clone() {
        int[][] kopya = new int[Size()][Size()];
        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
                kopya[i][j] = maze[i][j];
            }
        }
        return kopya;
    }

    //eğer rastgele labirent oluşturamazsa ilk haline alan method
    public void restore(int[][] savedMazed) {
        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
                maze[i][j] = savedMazed[i][j];
            }
        }
        maze[1][1] = 2;
        maze[8][8] = 8;
    }

    //0 ve 1 kullanarak rastgele labirent oluşturur
    public int[][] diziOlustur() {
        arr = new int[10][10];
        Random r = new Random();
        int sifir = 0;
        int bir = 1;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int n = r.nextInt((bir - sifir) + 1) + sifir;
                arr[i][j] = n;

            }
        }
        //labirentte sıkışmamak için başlangıç karesinin etrafını duvarsız yaptık
        arr[0][1] = 0;
        arr[1][0] = 0;
        arr[2][1] = 0;
        arr[1][2] = 0;

        return arr;
    }

    //JFrame' de labirent çizme
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(70, 70);    //labirentin (70,70) konumunda başlamasını sağlar

        if (repaint == true) {  //repaint true değer döndürünce çıkışı olmayan labirent oluşturur
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color color;
                    switch (maze[row][col]) {
                        case 1:
                            color = Color.black;
                            break;
                        case 2:
                            color = Color.YELLOW;
                            break;
                        case 8:
                            color = Color.RED;
                            break;
                        default:
                            color = Color.WHITE;
                    }
                    g.setColor(color);
                    g.fillRect(40 * col, 40 * row, 40, 40);     //blok içerisini renklendirme
                    g.setColor(Color.BLUE);     //blokları birbirinden ayıran çizgi rengi
                    g.drawRect(40 * col, 40 * row, 40, 40);
                }
            }
        }

        if (repaint == false) {
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color color;
                    switch (maze[row][col]) {
                        case 1:
                            color = Color.black;
                            break;
                        case 2:
                            color = Color.YELLOW;
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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Rastgele lab = new Rastgele();      //constructor' ı çağıracak yeni sınıfı oluşturuyoruz
            }
        });
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
                step++;
                yigin.push(sonra);
            }
            sonra = simdi.sag();    //şu an ki konumu sağa hareket ettirme
            if (isInMaze(sonra) && isClear(sonra)) {
                step++;
                yigin.push(sonra);
            }
            sonra = simdi.sol();    //şu an ki konumu sola hareket ettirme
            if (isInMaze(sonra) && isClear(sonra)) {
                step++;
                yigin.push(sonra);
            }
            sonra = simdi.asagi();    //şu an ki konumu aşağı hareket ettirme
            if (isInMaze(sonra) && isClear(sonra)) {
                step++;
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
}
