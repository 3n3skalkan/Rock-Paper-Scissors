package proje4;

public class Labirent {

    int i, j;

    public Labirent(int i, int j) {
        this.i = i;
        this.j = j;
    }

    ;
    public int i() {
        return i;
    }   // get i (for overloaded methods)

    public int j() {
        return j;
    }   // get j (for overloaded methods)

    public void Print() {
        System.out.println("(" + i + "," + j + ")");
    }

    public Labirent yukari() {
        return new Labirent(i - 1, j);
    }

    public Labirent asagi() {
        return new Labirent(i + 1, j);
    }

    public Labirent sag() {
        return new Labirent(i, j + 1);
    }

    public Labirent sol() {
        return new Labirent(i, j - 1);
    }

}
