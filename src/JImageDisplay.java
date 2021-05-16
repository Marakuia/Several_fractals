import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent{
    private BufferedImage image;   //управляет изображением

    public BufferedImage getImage() {
        return image;
    }

    public JImageDisplay(int width, int height) {
        //инициализировать объект новым изображением с  шириной и высотой, и типом изображения RGB.
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //включаем компонент в пользовательский интерфейс, чтобы отобразить всё изображение.
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);

    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        //выводит на экран изображение
        g.drawImage (image, 0, 0, image.getWidth(), image.getHeight(), null);

    }

    //закрашиваем все пиксли черным
    public void clearImage(){
        int[] blackPix = new int[getWidth() * getHeight()];
        image.setRGB(0, 0, getWidth(), getHeight(), blackPix, 0, 1);

    }

    //закрашиваем конкретный пиксель конкретным цветом
    public void  drawPixel (int x, int y, int rgbColor){
        image.setRGB(x, y, rgbColor);
    }

}

