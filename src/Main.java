import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main (String args[]) {
        int width = 300;
        int height = 300;
        int iter = 100;
        int images = 1000;
        Complex min = new Complex(-2, -2);
        Complex max = new Complex(2, 2);
        int window_width = 20;
        int window_height = 20;
        double zoom = 0.01;
        Expr expr = new Expr(new Expr(Expr.Ref.Z, Expr.Op.TIMES, Expr.Ref.Z), Expr.Op.PLUS, new Expr(Expr.Ref.C, Expr.Op.PLUS, Expr.Ref.Z));

        for (int i = 0; i < images; i++) {
            BufferedImage image = createImage(width, height, iter, min, max, expr);
            BufferedImage edges = getEdges(image);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    edges.setRGB(x, y, edges.getRGB(x, y)*50);
                }
            }
            int[] best_pos = getBestWindow(edges, window_width, window_height);
            Complex min_best = min.plus(new Complex((double)best_pos[0]/(width-1)*(max.a-min.a), (double)best_pos[1]/(height-1)*(max.b-min.b)));
            Complex max_best = min.plus(new Complex((double)(best_pos[0]+window_width)/(width-1)*(max.a-min.a), (double)(best_pos[1]+window_height)/(height-1)*(max.b-min.b)));
            Complex temp = min.times(1-zoom).plus(min_best.times(zoom));
            max = max.times(1-zoom).plus(max_best.times(zoom));
            min = temp;
            try {
                File outputfile = new File("mandelbrot2_" + i/100 + "" + (i/10)%10 + "" + i%10 + ".png");
                ImageIO.write(image, "png", outputfile);
            }
            catch (IOException e) { e.printStackTrace(); }
        }

    }

    public static BufferedImage createImage(int width, int height, int iter, Complex min, Complex max, Expr expr) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Complex c = min.plus(new Complex((double)x/(width-1)*(max.a-min.a), (double)y/(height-1)*(max.b-min.b)));
                Complex z = new Complex(0, 0);
                for (int i = 0; i < iter; i++) {
                    z = expr.getValue(new Complex[] {z, c});
                    if (z.abs() > 2) {
                        break;
                    }
                }
                if (z.abs() > 2) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                }
                else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return image;
    }

    public static BufferedImage getEdges(BufferedImage image) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (x > 0) {
                    out.setRGB(x, y, out.getRGB(x, y) + (image.getRGB(x, y)==image.getRGB(x-1, y)?0:1));
                }
                if (y > 0) {
                    out.setRGB(x, y, out.getRGB(x, y) + (image.getRGB(x, y)==image.getRGB(x, y-1)?0:1));
                }
                if (x < image.getWidth()-1) {
                    out.setRGB(x, y, out.getRGB(x, y) + (image.getRGB(x, y)==image.getRGB(x+1, y)?0:1));
                }
                if (y < image.getHeight()-1) {
                    out.setRGB(x, y, out.getRGB(x, y) + (image.getRGB(x, y)==image.getRGB(x, y+1)?0:1));
                }
            }
        }
        return out;
    }

    public static int[] getBestWindow(BufferedImage image, int w, int h) {
        int best_x = 0;
        int best_y = 0;
        int best_val = -1;
        for (int x = 0; x < image.getWidth() - (w-1); x++) {
            for (int y = 0; y < image.getHeight() - (h-1); y++) {
                int val = 0;
                for (int dx = 0; dx < w; dx++) {
                    for (int dy = 0; dy < h; dy++) {
                        val += image.getRGB(x+dx, y+dx);
                    }
                }
                if (val > best_val) {
                    best_val = val;
                    best_x = x;
                    best_y = y;
                }
            }
        }
        return new int[] {best_x, best_y};
    }
}
