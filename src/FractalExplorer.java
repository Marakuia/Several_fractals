import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Handler;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer {
    private int size; //для отслеживания размера экрана
    private JImageDisplay img;  //для отображения фрактала
    private FractalGenerator fract;
    private Rectangle2D.Double rect; //диапазон

    public FractalExplorer(int size){
        this.size = size;
        fract = new Mandelbrot();
        rect = new Rectangle2D.Double();
        fract.getInitialRange(rect);
        img = new JImageDisplay(size, size);

    }
    public void createAndShowGUI (){
        //Создаем форму и даем ей заголовок
        img.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");

        //помещаем изображение в центр формы
        frame.add(img, BorderLayout.CENTER);

        //Создаем кнопку для сброса изображений
        JButton btnReset = new JButton("Reset");
        //Создаем слушатель для кнопки
        Buttons ResListener = new Buttons();
        btnReset.addActionListener(ResListener);
        //указываем расположение кнопки
        frame.add(btnReset, BorderLayout.SOUTH);

        //Создаем слушатель для щелчков мыши
        Mouse MouseListener = new Mouse();
        img.addMouseListener(MouseListener);

        //реализуем операцию закрытыя по умолчанию
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Реализуем combo-box
       JComboBox ComboBox = new JComboBox();
        FractalGenerator mandelbrot = new Mandelbrot();
        ComboBox.addItem(mandelbrot);
        FractalGenerator tricorn = new Tricorn();
        ComboBox.addItem(tricorn);
        FractalGenerator burningShip = new BurningShip();
        ComboBox.addItem(burningShip);


        //создаем слушатель для comb0-box
        Buttons fChoose = new Buttons();
        ComboBox.addActionListener(fChoose);

        //Создаем панель и label
        JPanel Panel = new JPanel();
        JLabel Label = new JLabel("Fractal:");
        //добавляем в панель combo-list и label
        Panel.add(Label);
        Panel.add(ComboBox);
        frame.add(Panel, BorderLayout.NORTH);

        //Создаем кнопку save и панель для кнопок
        JButton btnSave = new JButton("Save");
        JPanel btnPanel = new JPanel();
        //добавляем в панель кнопки save и reset
        btnPanel.add(btnSave);
        btnPanel.add(btnReset);
        frame.add(btnPanel, BorderLayout.SOUTH);

        //слушатель для кнопки save
        Buttons saveHandler = new Buttons();
        btnSave.addActionListener(saveHandler);

        frame.pack ();  //правильное размещение содержимого окна
        frame.setVisible (true);    //содержимое делаем видимым
        frame.setResizable (false);  //запрет  изменения размера окна
    }


    public void drawFractal (){
        for (int x = 0; x<size; x++){
            for (int y = 0; y < size; y++){
                double xCoord = fract.getCoord(rect.x, rect.x + rect.width, size, x);
                double yCoord = fract.getCoord(rect.y, rect.y + rect.height, size, y);

                int numIters = fract.numIterations(xCoord, yCoord); //Вычисляем количество итераций для соответствующих
                // координат в области отображения фрактала

                if(numIters == -1)
                    img.drawPixel(x, y, 0);
                else{
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    img.drawPixel(x, y, rgbColor);
                }
            }
        }
        img.repaint();  //обновляем изображение
    }
    private class Buttons implements ActionListener {

        //Создаем метод, реагирующий на нажатие кнопки
        public void actionPerformed(ActionEvent e) {
            String btn = e.getActionCommand(); //значение выбранной кнопки

            // если событие поступило из combo-box
            if (e.getSource() instanceof JComboBox) {
                JComboBox Source = (JComboBox) e.getSource();
                fract = (FractalGenerator) Source.getSelectedItem(); //выбираем фрактал
                fract.getInitialRange(rect); //сбрасывает к изначальному диапазону
                drawFractal(); //перерисовываем фрактал
            }
            else if (btn.equals("Reset")){
                fract.getInitialRange(rect); //сбрасывает к изначальному диапазону
                drawFractal(); //перерисовываем фрактал
            }
            else if (btn.equals("Save")){
                //сохраняем изображения только в формате PNG
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false); //запрещаем использование отличных от png форматов

                int select = chooser.showSaveDialog(img);//открываем диалоговое окно
                if (select == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = chooser.getSelectedFile(); //получаем путь к файлу
                    //String file_name = file.toString();
                    try {
                        BufferedImage displayImage = img.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }

                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(img, exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }


    private class Mouse extends MouseAdapter {
        public void mouseClicked(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            //отображает пиксельные кооринаты щелчка в область фрактала
            double xCoord = fract.getCoord(rect.x, rect.x + rect.width, size, x);
            double yCoord = fract.getCoord(rect.y, rect.y + rect.width, size, y);

            //вызывает метод с координатами, по которым щелкнули, и масштабом 0.5
            fract.recenterAndZoomRange(rect, xCoord, yCoord,0.5);
            drawFractal();

        }

    }

    public static void main(String[] args)
    {
        FractalExplorer ResFrat  = new FractalExplorer(600); //размер отображения
        ResFrat.createAndShowGUI();
        ResFrat.drawFractal(); //отображение начального представления фрактала
    }
}

