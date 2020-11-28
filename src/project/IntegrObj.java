package project;

import javax.swing.*;
import java.net.URL;

abstract public class IntegrObj {
    // картинка
    private ImageIcon integrImg;

    public IntegrObj(String fileName) {
        // находим картинку внутри jar файла
        URL imageURL = getClass().getResource("/images/"+fileName);

        if(imageURL != null) {
            integrImg = new ImageIcon(imageURL);
        }
    }

    public ImageIcon getImage() { return integrImg;}

    // абстрактный метод - подинтегр. функция
    abstract public double fX(double x);

    // абстрактный метод - аналит. решение
    abstract public double fXAnalit(double x);
}
