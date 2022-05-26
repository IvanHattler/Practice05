import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.xphoto.GrayworldWB;
import org.opencv.xphoto.Xphoto;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Model {

    public void whiteBalance(BufferedImage img) throws IOException {
        float[][] m = {
                {255/222f, 0, 0},
                {0, 255/243f, 0},
                {0, 0, 255/255f}
        };
        int h = img.getHeight();
        int w = img.getWidth();
        BufferedImage result = new BufferedImage(w, h, TYPE_INT_RGB);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = img.getRGB(j, i);
                int r = Utils.ch1(color);
                int g = Utils.ch2(color);
                int b = Utils.ch3(color);
                int balancedColor = Utils.color(
                        Math.round(r * m[0][0] + g * m[0][1] + b * m[0][2]),
                        Math.round(r * m[1][0] + g * m[1][1] + b * m[1][2]),
                        Math.round(r * m[2][0] + g * m[2][1] + b * m[2][2])
                );
                result.setRGB(j, i, balancedColor);
            }
        }
        Utils.save(result, "result/whiteBalance", "result", "jpg");
    }

    public void grayWorld(BufferedImage img) throws IOException {
        int h = img.getHeight();
        int w = img.getWidth();
        float avgRed = 0;
        float avgGreen = 0;
        float avgBlue = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = img.getRGB(j, i);
                avgRed += Utils.ch1(color);
                avgGreen += Utils.ch2(color);
                avgBlue += Utils.ch3(color);
            }
        }
        float pixelCount = h*w;
        avgRed = avgRed/pixelCount;
        avgGreen = avgGreen/pixelCount;
        avgBlue = avgBlue/pixelCount;
        float avgGray = (avgRed+avgGreen+avgBlue)/3f;
        float kr = avgGray/avgRed;
        float kg = avgGray/avgGreen;
        float kb = avgGray/avgBlue;
        BufferedImage result = new BufferedImage(w, h, TYPE_INT_RGB);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = img.getRGB(j, i);
                int r = Math.round(Utils.ch1(color)*kr);
                int g = Math.round(Utils.ch2(color)*kg);
                int b = Math.round(Utils.ch3(color)*kb);
                if (r<0) r=0;
                if (g<0) g=0;
                if (b<0) b=0;
                result.setRGB(j, i, Utils.color(r, g, b));
            }
        }
        Utils.save(result, "result/grayWorld", "result", "jpg");
    }

    public void grayWorldLib(BufferedImage img) throws IOException {
        Mat mat = new Mat();
        GrayworldWB alg = Xphoto.createGrayworldWB();
        alg.balanceWhite(img2Mat(img), mat);
        BufferedImage result = (BufferedImage) HighGui.toBufferedImage(mat);
        Utils.save(result, "result/grayWorldLib", "result", "jpg");
    }

    public static Mat img2Mat(BufferedImage image) {
        image = convertTo3ByteBGRType(image);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }

    public static BufferedImage convertTo3ByteBGRType(BufferedImage image) {
        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        convertedImage.getGraphics().drawImage(image, 0, 0, null);
        return convertedImage;
    }
}
