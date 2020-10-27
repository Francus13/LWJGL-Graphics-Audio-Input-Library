package graphics;

public class Color {
    public static Color WHITE = new Color(1, 1, 1, 1);
    public static Color BLACK = new Color(0, 0, 0, 1);
    public static Color ERROR_COLOR = new Color(1, 0, 0, 1);

    private float r;
    private float g;
    private float b;
    private float a;

    public Color(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float r() {return r;}
    public float g() {return g;}
    public float b() {return b;}
    public float a() {return a;}
}
