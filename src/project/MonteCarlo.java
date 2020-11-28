package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class MonteCarlo {

    // поля класса
    static private JFrame mainFrame; //основное окно
    static private ArrayList<IntegrObj> integrArr; // массив интеграллов
    static private ArrayList<IntegrPanel> integrPanels; // массив панелей с интеграллами

    // начальный метод
    public static void main(String[] args) {
        // новый объект класса
        MonteCarlo main = new MonteCarlo();
        main.go(); // начало работы алгоритма
    }

    // инициализация приложения
    public void go() {
        initIntegr(); // иниц. объекты-интеграллы
        initGui(); // иниц. граф. интерфейс
        initListeners(); // подключаем слушателей
    }

    // создаём объекты-интеграллы
    public void initIntegr() {
        integrArr = new ArrayList<IntegrObj>();

        IntegrObj integr1 = new IntegrObj("1.png") {
            @Override
            public double fX(double x) {
                return 1.0+x;
            }
            @Override
            public double fXAnalit(double x) {
                return x+x*x/2;
            }
        };

        IntegrObj integr2 = new IntegrObj("2.png") {
            @Override
            public double fX(double x) {
                return (2.0*x+3.0)*(2.0*x+3);
            }
            @Override
            public double fXAnalit(double x) {
                return 4.0*x*x*x/3.0+6*x*x+9*x;
            }
        };

        IntegrObj integr3 = new IntegrObj("3.png") {
            @Override
            public double fX(double x) {
                return 1.0 / Math.sqrt(x*x+4);
            }
            @Override
            public double fXAnalit(double x) {
                return Math.log(x/2.0+Math.sqrt(x*x/4.0+1.0));
            }
        };

        IntegrObj integr4 = new IntegrObj("4.png") {
            @Override
            public double fX(double x) {
                return 6.0*Math.cos(x);
            }
            @Override
            public double fXAnalit(double x) {
                return 6.0*Math.sin(x);
            }
        };

        IntegrObj integr5 = new IntegrObj("5.png") {
            @Override
            public double fX(double x) {
                return (8*x*x-4*x+7)/((x*x+1)*(4*x+1));
            }
            @Override
            public double fXAnalit(double x) {
                return 2*Math.log(4*x+1)-Math.atan(x);
            }
        };

        integrArr.add(integr1);
        integrArr.add(integr2);
        integrArr.add(integr3);
        integrArr.add(integr4);
        integrArr.add(integr5);
    }

    // настройка граф. интерфейса
    public void initGui() {
        try {
            // используем системную тему интерфейса
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        // создаём главное окно приложения
        mainFrame = new JFrame("Метод Монте-Карло");
        // устанавливаем менеджер расположения
        mainFrame.setLayout(new FlowLayout());
        // устанавливаем действие при нажатии на "крестик"
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // создаём панель для меню
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorderPainted(true);
        // создаём пункт меню в панели
        JMenu menu = new JMenu("Опции");

        // создаём опции внутри меню
        JMenuItem item1 = new JMenuItem("Очистить все ячейки");
        item1.addActionListener(new ClearAllListener());
        JMenuItem item2 = new JMenuItem("Заполнить поля случайными числами");
        item2.addActionListener(new FillRandomListener());

        //добавляем созданные опции в меню
        menu.add(item2);
        menu.add(item1);

        // добавляем меню на панель
        menuBar.add(menu);
        // устанавливаем панель меню для окна
        mainFrame.setJMenuBar(menuBar);

        // массив с панелями каждого интегралла
        integrPanels = new ArrayList<IntegrPanel>();
        for(IntegrObj inObj : integrArr) {
            // для каждого интегр. создаём панель
            integrPanels.add(new IntegrPanel(inObj));
            // добавляем панели в главное окно
            mainFrame.add(integrPanels.get(integrPanels.size()-1));
        }

        for(int i = 0; i<integrPanels.size();i++) {
            // для каждой панели настр. рамку
            integrPanels.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
            // настраиваем сокращения для панелей (alt+1, alt+2,...)
            integrPanels.get(i).getButton().setMnemonic(49+i);
        }

        // подгоняем компоненты по размеру
        mainFrame.pack();
        // отображаем форму
        mainFrame.setVisible(true);
    }

    // натройка слушателей
    public void initListeners() {
        for(IntegrPanel panel : integrPanels) {
            // присоединяем обработчик события
            panel.getButton().addActionListener(new ButtonActionListener());
            // задаём сообщение внутри события, чтобы
            // позже определить номер нажатой кнопки
            // сообщение = номеру нажатой кнопки
            panel.getButton().setActionCommand(String.valueOf(panel.getId()));
        }
    }

    // метод Монте-Карло
    public double solve(IntegrObj integr, double a, double b, int N) {
        double res;
        double sum = 0;
        for(int i =0;i<N;i++) {
            sum+= integr.fX( (a + (b-a)*Math.random()) );
        }
        res = (b-a)*sum/N;
        return res;
    }

    // аналит. решение
    public double solveAnalit(IntegrObj integr, double a, double b) {
        return integr.fXAnalit(b)-integr.fXAnalit(a);
    }

    // отображение ответа
    public void showAnswer(int integrNum) {
        // находим панель, связанную с интеграллом
        // и находим на ней каждый её элемент
        IntegrPanel currPanel = integrPanels.get(integrNum); //панель с данными
        JTextField textA = currPanel.getDataPanelA(); // поле со значением А
        JTextField textB = currPanel.getDataPanelB(); // поле со значением B
        JTextField textN = currPanel.getDataPanelN(); // поле со значением N
        JLabel ans = currPanel.getAnsPanelLbl(); // поле с ответом
        JLabel ansAnalit = currPanel.getAnalitAnsPanelLbl(); // поле с ответом

        // проверяем, чтоб поля были не пустыми
        if(textA.getText().equals("") || textB.getText().equals("") || textN.getText().equals("")) {
            JOptionPane.showMessageDialog(mainFrame,"Проверьте корректность исходных данных!!!",
                    "Ошибка",JOptionPane.ERROR_MESSAGE);
        } else {
            double a = Double.parseDouble(textA.getText().replace(",","."));
            double b = Double.parseDouble(textB.getText().replace(",","."));
            int N = Integer.parseInt(textN.getText());

            ans.setText( String.format("%.5f", solve(integrArr.get(integrNum), a, b, N),(1.0/Math.sqrt(N)) ) );
            double analit = solveAnalit(integrArr.get(integrNum), a, b);

            // особое ограничение для аналит. решения
            // последнего интегралла (т.к. в аналит. решении есть log)
            if(Double.isNaN(analit)) {
                ansAnalit.setText("a должно быть >= 0");
            }
            else
                ansAnalit.setText(String.format("%.5f", analit));
        }
    }

    // обработчик нажатий
    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // читаем сообщение, ранее заданное для каждой кнопки
            // сообщ. = номеру кнопки
            switch ( Integer.parseInt(actionEvent.getActionCommand()) ) {
                case 0:
                    showAnswer(0);
                    break;
                case 1:
                    showAnswer(1);
                    break;
                case 2:
                    showAnswer(2);
                    break;
                case 3:
                    showAnswer(3);
                    break;
                case 4:
                    showAnswer(4);
                    break;
                default:
                    break;
            }
        }
    }

    // очистка полей в окне
    private class ClearAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for(int i =0;i<integrPanels.size();i++) {
                integrPanels.get(i).getDataPanelA().setText("");
                integrPanels.get(i).getDataPanelB().setText("");
                integrPanels.get(i).getDataPanelN().setText("");
                integrPanels.get(i).getAnsPanelLbl().setText("0.0");
                integrPanels.get(i).getAnalitAnsPanelLbl().setText("0.0");
            }
        }
    }

    // случайные числа в полях окна
    private class FillRandomListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Random rnd = new Random();
            for(int i =0;i<integrPanels.size();i++) {
                int A = rnd.nextInt(100)-50;
                int B = A + rnd.nextInt(25);
                int N = rnd.nextInt(10000);
                integrPanels.get(i).getDataPanelA().setText(String.valueOf(A));
                integrPanels.get(i).getDataPanelB().setText(String.valueOf(B));
                integrPanels.get(i).getDataPanelN().setText(String.valueOf(N));
            }
        }
    }
}


