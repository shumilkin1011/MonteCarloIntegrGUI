package project;

import javax.swing.*;
import java.awt.*;
import java.lang.invoke.ConstantCallSite;

public class IntegrPanel extends JPanel {
    private static int count; // кол-во созданных панелей
    private int id; // id для каждого элемента
    private JLabel imgLbl;
    private DataPanel dataPanel;
    private JTextField dataPanelA;
    private JTextField dataPanelB;
    private JTextField dataPanelN;
    private JButton button;
    private JPanel ansPanel;
    private JLabel ansPanelLbl;
    private JPanel analitAnsPanel;
    private JLabel analitAnsPanelLbl;

    // статическая инициализация поля
    static {
        count = -1;
    }

    public IntegrPanel(IntegrObj inObj) {
        super();
        count++;
        id = count; // вычисляем id новой панели
        initGUI(inObj);
    }

    public int getId() {
        return id;
    }

    public JTextField getDataPanelA() {
        return dataPanelA;
    }

    public JTextField getDataPanelB() {
        return dataPanelB;
    }

    public JLabel getAnsPanelLbl() {
        return ansPanelLbl;
    }

    public JButton getButton() {
        return button;
    }

    public JTextField getDataPanelN() {
        return dataPanelN;
    }

    public JLabel getAnalitAnsPanelLbl() {
        return analitAnsPanelLbl;
    }

    // настраиваем внешний вид панели
    private void initGUI(IntegrObj inObj) {
        // выбираем менеджер расположения
        // BoxLayout располагает элементы по одной из осей
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        imgLbl = new JLabel(inObj.getImage());
        dataPanel = new DataPanel();
        button = new JButton("Вычислить интеграл " + (id+1));
        ansPanel = new AnswerPanel();
        analitAnsPanel = new AnalitAnswerPanel();

        button.setFont(new Font("Sans-Serif",Font.BOLD,12));
        // рамка вокруг картинки
        imgLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));

        // создаём панель для каждого элемента
        // после чего помещаем элемент внутрь неё
        JPanel imgPanel = new JPanel();
        imgPanel.add(imgLbl);
        JPanel buttPanel = new JPanel();
        buttPanel.add(button);

        // свободное пространство между элементами
        add(Box.createRigidArea(new Dimension(0,5)));
        add(imgPanel);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(dataPanel);
        add(Box.createRigidArea(new Dimension(0,5)));
        // разделительная линия
        add(new JSeparator());
        add(buttPanel);
        add(new JSeparator());
        add(ansPanel);
        add(analitAnsPanel);

    }

    // создаём свои панели под разные цели
    private class DataPanel extends JPanel {
        public DataPanel() {
            super();
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            c.gridx = 0;
            add(new JLabel("a:"),c);
            c.gridx = 1;
            dataPanelA = new JTextField(5);
            add(dataPanelA,c);
            c.gridx = 2;
            add(new JLabel("   b: "),c);
            c.gridx = 3;
            dataPanelB = new JTextField(5);
            add(dataPanelB,c);
            c.gridy = 1;
            c.gridx = 0;
            c.anchor = GridBagConstraints.CENTER;
            add(new JLabel("N: "),c);
            c.gridy = 1;
            c.gridx = 1;
            c.gridwidth = 3;
            c.fill = GridBagConstraints.HORIZONTAL;
            dataPanelN = new JTextField(4);
            add(dataPanelN,c);

        }
    }
    private class AnswerPanel extends JPanel {
        public AnswerPanel() {
            super();
            JLabel label = new JLabel("Ответ: ");
            add(label);
            label.setFont(new Font("Sans-Serif",Font.BOLD,12));
            ansPanelLbl = new JLabel("0.0");
            ansPanelLbl.setFont(new Font("Sans-Serif",Font.PLAIN,12));
            add(ansPanelLbl);
        }
    }
    private class AnalitAnswerPanel extends JPanel {
        public AnalitAnswerPanel() {
            super();
            JLabel label = new JLabel("Ответ (аналит): ");
            add(label);
            label.setFont(new Font("Sans-Serif",Font.BOLD,12));
            analitAnsPanelLbl = new JLabel("0.0");
            analitAnsPanelLbl.setFont(new Font("Sans-Serif",Font.PLAIN,12));
            add(analitAnsPanelLbl);
        }
    }
}
