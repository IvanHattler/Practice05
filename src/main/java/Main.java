import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_java;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.xphoto.GrayworldWB;
import org.opencv.xphoto.Xphoto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = getUrl();
        if (url == null) return;

        Model m = new Model();
        Loader.load(opencv_java.class);
        BufferedImage orig = ImageIO.read(url);
        m.whiteBalance(orig);
        m.grayWorld(orig);
        m.grayWorldLib(orig);
    }

    public static URL getUrl() {
        return Main.class.getResource("img.jpg");
    }
}
