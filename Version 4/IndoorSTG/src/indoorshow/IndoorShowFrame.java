/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

import com.eltima.components.ui.DatePicker;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrator
 */
public class IndoorShowFrame extends JFrame implements MouseMotionListener {

    private final int MAXOBJECT = 4;//30;
    private final int MINOBJECT = 2;//10;
    private final int MAXSPEED = 300;
    private final int MINSPEED = 100;
    private final double DESTOBPROP = 0.8;
    private final int NUMDAY = 2;//10;
    private final int FLOORNUM = 6;
    private final double PRIMARYPROB = 0.9;
    private final double SECONDPROB = 0.8;
    private final int PRIMARYSTAY = 240;
    private final int SECONDSTAY = 60;
    private final int OTHERSTAY = 10;
    private final int LEFTSTAY = 10;
    private SimpleDateFormat dataFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    BufferedImage bufimg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    ArrayList<Graph> graphs = new ArrayList<Graph>();
    ArrayList<Graph> cir = new ArrayList<Graph>();
    ArrayList<Graph> notcir = new ArrayList<Graph>();
    ArrayList<Graph> start = new ArrayList<Graph>();
    ArrayList<Graph> end = new ArrayList<Graph>();
    ArrayList<Line> lines = new ArrayList<Line>();
    ArrayList<Person> persons = new ArrayList<Person>();
    ArrayList<DestinationId> destinations = new ArrayList<DestinationId>();//所有对象所到达的目的位置��
    ArrayList<DestinationId> objectdests = new ArrayList<DestinationId>();//某个对象所到达的目的位置��
    inforWindow infor = new inforWindow();
    PaintThread thread;
    Color color[];
    Line drawline = null;
    int floorNum = 0;
    int currentfloor = 0;
    int destid = 0;
    DatePicker startDatePicker;
    DatePicker startTimePicker;
    DatePicker endTimePicker;

    /**
     * Creates new form IndoorShowFrame
     */
    public IndoorShowFrame() {

        javax.swing.UIManager.put("Label.font", new java.awt.Font("Century", 0, 14));

        this.setTitle("indoorSTG Tool");
        this.setFont(new java.awt.Font("Century", 0, 14));

        initComponents();
        this.setLocationRelativeTo(null);

        this.addMouseMotionListener(this);

        DataRecordTable.getTableHeader().setFont(new java.awt.Font("Century", 0, 12));
        ColorTableCellRender colorRender = new ColorTableCellRender();
        colorRender.setRenderColor(DataRecordTable);

        //时间控件�       
        startDatePicker = new DatePicker(null, "yyyy-MM-dd", new java.awt.Font("Century", Font.PLAIN, 12), new Dimension(StartDateText.getWidth(), StartDateText.getHeight()));
        startDatePicker.setLocale(Locale.ENGLISH);
        StartDateText.add(startDatePicker);

        startTimePicker = new DatePicker(null, "HH:mm:ss", new java.awt.Font("Century", Font.PLAIN, 12), new Dimension(StartTimeText.getWidth(), StartTimeText.getHeight()));
        startTimePicker.setLocale(Locale.ENGLISH);
        startTimePicker.setTimePanleVisible(true);
        StartTimeText.add(startTimePicker);

        endTimePicker = new DatePicker(null, "HH:mm:ss", new java.awt.Font("Century", Font.PLAIN, 12), new Dimension(EndTimeText.getWidth(), EndTimeText.getHeight()));
        endTimePicker.setLocale(Locale.ENGLISH);
        endTimePicker.setTimePanleVisible(true);
        EndTimeText.add(endTimePicker);

        thread = new PaintThread();

        //颜色控件
        color = new Color[]{Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW,};
        JPanel panel[] = new JPanel[]{
            new ColorCell(color[0], "Black"),
            new ColorCell(color[1], "Blue"),
            new ColorCell(color[2], "Cyan"),
            new ColorCell(color[3], "Dark Gray"),
            new ColorCell(color[4], "Gray"),
            new ColorCell(color[5], "Green"),
            new ColorCell(color[6], "Ligth Gray"),
            new ColorCell(color[7], "Magenta"),
            new ColorCell(color[8], "Orange"),
            new ColorCell(color[9], "Pink"),
            new ColorCell(color[10], "Red"),
            new ColorCell(color[11], "White"),
            new ColorCell(color[12], "Yellow")
        };

        FirstLocColorComboBox.setModel(new javax.swing.DefaultComboBoxModel(panel));
        FirstLocColorComboBox.setSelectedIndex(12);
        SecondLocColorComboBox.setModel(new javax.swing.DefaultComboBoxModel(panel));
        SecondLocColorComboBox.setSelectedIndex(5);
        DestLocColorComboBox.setModel(new javax.swing.DefaultComboBoxModel(panel));
        DestLocColorComboBox.setSelectedIndex(10);

        ListCellRenderer renderer = new PanelComboBoxCellRenderer();
        FirstLocColorComboBox.setRenderer(renderer);
        SecondLocColorComboBox.setRenderer(renderer);
        DestLocColorComboBox.setRenderer(renderer);

        bufimg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        //加入小图标
        String src = "/ustc.jpg";
        try {
            Image image = ImageIO.read(this.getClass().getResource(src));
            this.setIconImage(image);
        } catch (IOException ex) {
            Logger.getLogger(IndoorShowFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        RootPanel = new javax.swing.JPanel();
        indoorPanel = new javax.swing.JPanel();
        pramatersPanel = new javax.swing.JPanel();
        InitialSettingPanel = new javax.swing.JPanel();
        MaxSpeedLabel = new javax.swing.JLabel();
        MaxSpeedText = new javax.swing.JTextField();
        InitialCheckBox = new javax.swing.JCheckBox();
        DayNumLabel = new javax.swing.JLabel();
        DayNumText = new javax.swing.JTextField();
        FloorNumLabel = new javax.swing.JLabel();
        FloorNumText = new javax.swing.JTextField();
        MinSpeedLabel = new javax.swing.JLabel();
        MinSpeedText = new javax.swing.JTextField();
        LocProbPanel = new javax.swing.JPanel();
        PrimaryProbLabel = new javax.swing.JLabel();
        SecondaryProbLabel = new javax.swing.JLabel();
        PrimaryProbText = new javax.swing.JTextField();
        SecondaryProbText = new javax.swing.JTextField();
        LocProbCheckBox = new javax.swing.JCheckBox();
        PersonInfoPanel = new javax.swing.JPanel();
        MaxNumObjectLabel = new javax.swing.JLabel();
        MaxNumObjectText = new javax.swing.JTextField();
        PersonInfoCheckBox = new javax.swing.JCheckBox();
        MinNumObjectLabel = new javax.swing.JLabel();
        DestObjectPropLabel = new javax.swing.JLabel();
        DestObjectPropText = new javax.swing.JTextField();
        MinNumObjectText = new javax.swing.JTextField();
        TrajectoryPanel = new javax.swing.JPanel();
        ObjectIdShowLabel = new javax.swing.JLabel();
        FloorLabel = new javax.swing.JLabel();
        FirstLocColorLabel = new javax.swing.JLabel();
        FirstLocColorComboBox = new javax.swing.JComboBox();
        SecondLocColorLabel = new javax.swing.JLabel();
        DestinationColorLabel = new javax.swing.JLabel();
        SecondLocColorComboBox = new javax.swing.JComboBox();
        DestLocColorComboBox = new javax.swing.JComboBox();
        FloorComboBox = new javax.swing.JComboBox();
        ObjectIdShowComboBox = new javax.swing.JComboBox();
        InOutTimePanel = new javax.swing.JPanel();
        StartDateLabel = new javax.swing.JLabel();
        StartDateText = new javax.swing.JTextField();
        EndTimeLabel = new javax.swing.JLabel();
        EndTimeText = new javax.swing.JTextField();
        DateFormatLabel = new javax.swing.JLabel();
        StartTimeLabel = new javax.swing.JLabel();
        StartTimeText = new javax.swing.JTextField();
        TimeFormatLabel = new javax.swing.JLabel();
        TimeFormatLabel1 = new javax.swing.JLabel();
        OpenButton = new javax.swing.JButton();
        FloorSetButton = new javax.swing.JButton();
        StayTimePanel = new javax.swing.JPanel();
        PrimaryStayLabel = new javax.swing.JLabel();
        PrimaryStayText = new javax.swing.JTextField();
        SecondStayLabel = new javax.swing.JLabel();
        SecondStayText = new javax.swing.JTextField();
        OtherStayLabel = new javax.swing.JLabel();
        OtherStayText = new javax.swing.JTextField();
        StayTimeCheckBox = new javax.swing.JCheckBox();
        LeftStayLabel = new javax.swing.JLabel();
        LeftStayText = new javax.swing.JTextField();
        ExportDataButton = new javax.swing.JButton();
        GeneratingDataButton = new javax.swing.JButton();
        DataTablePanel = new javax.swing.JPanel();
        DataRecordScroll = new javax.swing.JScrollPane();
        DataRecordTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        setPreferredSize(new java.awt.Dimension(1400, 800));

        indoorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Indoor Environment", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N
        indoorPanel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        indoorPanel.setOpaque(false);
        indoorPanel.setPreferredSize(new java.awt.Dimension(1083, 557));

        javax.swing.GroupLayout indoorPanelLayout = new javax.swing.GroupLayout(indoorPanel);
        indoorPanel.setLayout(indoorPanelLayout);
        indoorPanelLayout.setHorizontalGroup(
            indoorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        indoorPanelLayout.setVerticalGroup(
            indoorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );

        pramatersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pramaters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N
        pramatersPanel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        pramatersPanel.setOpaque(false);

        InitialSettingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Initial", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        MaxSpeedLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MaxSpeedLabel.setText("MaxSpeed");
        MaxSpeedLabel.setDoubleBuffered(true);
        MaxSpeedLabel.setOpaque(true);

        MaxSpeedText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MaxSpeedText.setDoubleBuffered(true);

        InitialCheckBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        InitialCheckBox.setText("Default Setting");
        InitialCheckBox.setDoubleBuffered(true);
        InitialCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InitialCheckBoxActionPerformed(evt);
            }
        });

        DayNumLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DayNumLabel.setText("DayNum");
        DayNumLabel.setDoubleBuffered(true);
        DayNumLabel.setOpaque(true);

        DayNumText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DayNumText.setDoubleBuffered(true);

        FloorNumLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FloorNumLabel.setText("FloorNumber");
        FloorNumLabel.setDoubleBuffered(true);
        FloorNumLabel.setOpaque(true);

        FloorNumText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FloorNumText.setDoubleBuffered(true);

        MinSpeedLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MinSpeedLabel.setText("MinSpeed");
        MinSpeedLabel.setDoubleBuffered(true);
        MinSpeedLabel.setOpaque(true);

        MinSpeedText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MinSpeedText.setDoubleBuffered(true);

        javax.swing.GroupLayout InitialSettingPanelLayout = new javax.swing.GroupLayout(InitialSettingPanel);
        InitialSettingPanel.setLayout(InitialSettingPanelLayout);
        InitialSettingPanelLayout.setHorizontalGroup(
            InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialSettingPanelLayout.createSequentialGroup()
                .addGroup(InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DayNumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MaxSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MaxSpeedText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DayNumText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(InitialCheckBox)
                    .addGroup(InitialSettingPanelLayout.createSequentialGroup()
                        .addGroup(InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(FloorNumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(MinSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MinSpeedText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FloorNumText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        InitialSettingPanelLayout.setVerticalGroup(
            InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialSettingPanelLayout.createSequentialGroup()
                .addGroup(InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MaxSpeedLabel)
                    .addComponent(MaxSpeedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MinSpeedLabel)
                    .addComponent(MinSpeedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DayNumLabel)
                    .addComponent(DayNumText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FloorNumLabel)
                    .addComponent(FloorNumText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(InitialCheckBox))
        );

        LocProbPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Probability", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        PrimaryProbLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        PrimaryProbLabel.setText("Primaryloc");
        PrimaryProbLabel.setDoubleBuffered(true);
        PrimaryProbLabel.setOpaque(true);

        SecondaryProbLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        SecondaryProbLabel.setText("Secondaryloc");
        SecondaryProbLabel.setDoubleBuffered(true);
        SecondaryProbLabel.setOpaque(true);

        PrimaryProbText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        PrimaryProbText.setDoubleBuffered(true);

        SecondaryProbText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        SecondaryProbText.setDoubleBuffered(true);

        LocProbCheckBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        LocProbCheckBox.setText("Default Setting");
        LocProbCheckBox.setDoubleBuffered(true);
        LocProbCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LocProbCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LocProbPanelLayout = new javax.swing.GroupLayout(LocProbPanel);
        LocProbPanel.setLayout(LocProbPanelLayout);
        LocProbPanelLayout.setHorizontalGroup(
            LocProbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LocProbPanelLayout.createSequentialGroup()
                .addComponent(PrimaryProbLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PrimaryProbText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(LocProbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LocProbPanelLayout.createSequentialGroup()
                        .addComponent(SecondaryProbLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SecondaryProbText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(LocProbCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LocProbPanelLayout.setVerticalGroup(
            LocProbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LocProbPanelLayout.createSequentialGroup()
                .addGroup(LocProbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PrimaryProbLabel)
                    .addComponent(PrimaryProbText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SecondaryProbLabel)
                    .addComponent(SecondaryProbText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LocProbCheckBox))
        );

        PersonInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Object", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        MaxNumObjectLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MaxNumObjectLabel.setText("MaxNum");
        MaxNumObjectLabel.setDoubleBuffered(true);

        MaxNumObjectText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MaxNumObjectText.setDoubleBuffered(true);
        MaxNumObjectText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaxNumObjectTextActionPerformed(evt);
            }
        });

        PersonInfoCheckBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        PersonInfoCheckBox.setText("Default Setting");
        PersonInfoCheckBox.setDoubleBuffered(true);
        PersonInfoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PersonInfoCheckBoxActionPerformed(evt);
            }
        });

        MinNumObjectLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MinNumObjectLabel.setText("MinNum");
        MinNumObjectLabel.setDoubleBuffered(true);

        DestObjectPropLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DestObjectPropLabel.setText("DestRatio");
        DestObjectPropLabel.setDoubleBuffered(true);

        DestObjectPropText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DestObjectPropText.setDoubleBuffered(true);

        MinNumObjectText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MinNumObjectText.setDoubleBuffered(true);

        javax.swing.GroupLayout PersonInfoPanelLayout = new javax.swing.GroupLayout(PersonInfoPanel);
        PersonInfoPanel.setLayout(PersonInfoPanelLayout);
        PersonInfoPanelLayout.setHorizontalGroup(
            PersonInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PersonInfoPanelLayout.createSequentialGroup()
                .addGroup(PersonInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(MaxNumObjectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DestObjectPropLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PersonInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DestObjectPropText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MaxNumObjectText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PersonInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PersonInfoPanelLayout.createSequentialGroup()
                        .addComponent(MinNumObjectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MinNumObjectText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PersonInfoCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PersonInfoPanelLayout.setVerticalGroup(
            PersonInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PersonInfoPanelLayout.createSequentialGroup()
                .addGroup(PersonInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MinNumObjectLabel)
                    .addComponent(MaxNumObjectText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MaxNumObjectLabel)
                    .addComponent(MinNumObjectText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PersonInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PersonInfoCheckBox)
                    .addComponent(DestObjectPropLabel)
                    .addComponent(DestObjectPropText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        TrajectoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Visualization", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N
        TrajectoryPanel.setOpaque(false);

        ObjectIdShowLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ObjectIdShowLabel.setText("Object");
        ObjectIdShowLabel.setDoubleBuffered(true);

        FloorLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FloorLabel.setText("Floor");
        FloorLabel.setDoubleBuffered(true);

        FirstLocColorLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FirstLocColorLabel.setText("FirstLocColor");

        FirstLocColorComboBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        SecondLocColorLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        SecondLocColorLabel.setText("SecondLocColor");

        DestinationColorLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DestinationColorLabel.setText("DestLocColor");

        SecondLocColorComboBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        DestLocColorComboBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        FloorComboBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "select" }));
        FloorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FloorComboBoxActionPerformed(evt);
            }
        });

        ObjectIdShowComboBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ObjectIdShowComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "select" }));
        ObjectIdShowComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ObjectIdShowComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TrajectoryPanelLayout = new javax.swing.GroupLayout(TrajectoryPanel);
        TrajectoryPanel.setLayout(TrajectoryPanelLayout);
        TrajectoryPanelLayout.setHorizontalGroup(
            TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TrajectoryPanelLayout.createSequentialGroup()
                .addComponent(ObjectIdShowLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ObjectIdShowComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FloorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FloorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(TrajectoryPanelLayout.createSequentialGroup()
                .addGroup(TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(DestinationColorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FirstLocColorLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SecondLocColorLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DestLocColorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FirstLocColorComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SecondLocColorComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        TrajectoryPanelLayout.setVerticalGroup(
            TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TrajectoryPanelLayout.createSequentialGroup()
                .addGroup(TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ObjectIdShowLabel)
                        .addComponent(ObjectIdShowComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(FloorLabel))
                    .addComponent(FloorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FirstLocColorLabel)
                    .addComponent(FirstLocColorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SecondLocColorLabel)
                    .addComponent(SecondLocColorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TrajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DestinationColorLabel)
                    .addComponent(DestLocColorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        InOutTimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "In Out Time", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N
        InOutTimePanel.setOpaque(false);

        StartDateLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        StartDateLabel.setText("StartDate");

        StartDateText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        StartDateText.setRequestFocusEnabled(false);

        EndTimeLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        EndTimeLabel.setText("EndTime");

        EndTimeText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        DateFormatLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DateFormatLabel.setText("yyyy-MM-dd");

        StartTimeLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        StartTimeLabel.setText("StartTime");

        StartTimeText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        TimeFormatLabel.setFont(new java.awt.Font("Centaur", 0, 12)); // NOI18N
        TimeFormatLabel.setText("HH:mm:ss");

        TimeFormatLabel1.setFont(new java.awt.Font("Centaur", 0, 12)); // NOI18N
        TimeFormatLabel1.setText("HH:mm:ss");

        javax.swing.GroupLayout InOutTimePanelLayout = new javax.swing.GroupLayout(InOutTimePanel);
        InOutTimePanel.setLayout(InOutTimePanelLayout);
        InOutTimePanelLayout.setHorizontalGroup(
            InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InOutTimePanelLayout.createSequentialGroup()
                .addGroup(InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(StartTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(StartDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(EndTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StartDateText, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StartTimeText, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EndTimeText, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DateFormatLabel)
                    .addComponent(TimeFormatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TimeFormatLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        InOutTimePanelLayout.setVerticalGroup(
            InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InOutTimePanelLayout.createSequentialGroup()
                .addGroup(InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StartDateText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DateFormatLabel)
                    .addComponent(StartDateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StartTimeText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StartTimeLabel)
                    .addComponent(TimeFormatLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InOutTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EndTimeText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EndTimeLabel)
                    .addComponent(TimeFormatLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        OpenButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        OpenButton.setText("OpenEnvirFile");
        OpenButton.setDoubleBuffered(true);
        OpenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenButtonActionPerformed(evt);
            }
        });

        FloorSetButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FloorSetButton.setText("EnvironmentSet");
        FloorSetButton.setDoubleBuffered(true);
        FloorSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FloorSetButtonActionPerformed(evt);
            }
        });

        StayTimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Stay Time", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N
        StayTimePanel.setOpaque(false);

        PrimaryStayLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        PrimaryStayLabel.setText("PrimaryLoc");

        PrimaryStayText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        SecondStayLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        SecondStayLabel.setText("SecondLoc");

        SecondStayText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        OtherStayLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        OtherStayLabel.setText("OtherLoc");

        OtherStayText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        StayTimeCheckBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        StayTimeCheckBox.setText("Default Setting");
        StayTimeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StayTimeCheckBoxActionPerformed(evt);
            }
        });

        LeftStayLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        LeftStayLabel.setText("LeftLoc");

        LeftStayText.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N

        javax.swing.GroupLayout StayTimePanelLayout = new javax.swing.GroupLayout(StayTimePanel);
        StayTimePanel.setLayout(StayTimePanelLayout);
        StayTimePanelLayout.setHorizontalGroup(
            StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StayTimePanelLayout.createSequentialGroup()
                .addGroup(StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(OtherStayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PrimaryStayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PrimaryStayText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OtherStayText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StayTimeCheckBox)
                    .addGroup(StayTimePanelLayout.createSequentialGroup()
                        .addGroup(StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(SecondStayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(LeftStayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SecondStayText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LeftStayText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        StayTimePanelLayout.setVerticalGroup(
            StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StayTimePanelLayout.createSequentialGroup()
                .addGroup(StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PrimaryStayLabel)
                    .addComponent(PrimaryStayText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SecondStayLabel)
                    .addComponent(SecondStayText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StayTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OtherStayLabel)
                    .addComponent(OtherStayText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LeftStayLabel)
                    .addComponent(LeftStayText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(StayTimeCheckBox))
        );

        ExportDataButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ExportDataButton.setText("Export Data");
        ExportDataButton.setDoubleBuffered(true);
        ExportDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportDataButtonActionPerformed(evt);
            }
        });

        GeneratingDataButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        GeneratingDataButton.setActionCommand("Generat Data");
        GeneratingDataButton.setDoubleBuffered(true);
        GeneratingDataButton.setLabel("Generate Data");
        GeneratingDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GeneratingDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pramatersPanelLayout = new javax.swing.GroupLayout(pramatersPanel);
        pramatersPanel.setLayout(pramatersPanelLayout);
        pramatersPanelLayout.setHorizontalGroup(
            pramatersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pramatersPanelLayout.createSequentialGroup()
                .addComponent(FloorSetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(OpenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pramatersPanelLayout.createSequentialGroup()
                .addGroup(pramatersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(TrajectoryPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InitialSettingPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PersonInfoPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LocProbPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StayTimePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InOutTimePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pramatersPanelLayout.createSequentialGroup()
                .addComponent(GeneratingDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ExportDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pramatersPanelLayout.setVerticalGroup(
            pramatersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pramatersPanelLayout.createSequentialGroup()
                .addGroup(pramatersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FloorSetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OpenButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InitialSettingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(PersonInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LocProbPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StayTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InOutTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TrajectoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(pramatersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ExportDataButton)
                    .addComponent(GeneratingDataButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DataTablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Records", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N
        DataTablePanel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DataTablePanel.setOpaque(false);

        DataRecordScroll.setDoubleBuffered(true);
        DataRecordScroll.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DataRecordScroll.setOpaque(false);

        DataRecordTable.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DataRecordTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Read_ID", "MOB_ID", "Enter_Time", "Leave_Time", "Move_Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Long.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        DataRecordTable.setDoubleBuffered(true);
        DataRecordScroll.setViewportView(DataRecordTable);

        javax.swing.GroupLayout DataTablePanelLayout = new javax.swing.GroupLayout(DataTablePanel);
        DataTablePanel.setLayout(DataTablePanelLayout);
        DataTablePanelLayout.setHorizontalGroup(
            DataTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DataRecordScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
        );
        DataTablePanelLayout.setVerticalGroup(
            DataTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DataRecordScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout RootPanelLayout = new javax.swing.GroupLayout(RootPanel);
        RootPanel.setLayout(RootPanelLayout);
        RootPanelLayout.setHorizontalGroup(
            RootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RootPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pramatersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DataTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(indoorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        RootPanelLayout.setVerticalGroup(
            RootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pramatersPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, RootPanelLayout.createSequentialGroup()
                .addComponent(indoorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DataTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(RootPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(RootPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FloorSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FloorSetButtonActionPerformed
        // TODO add your handling code here:

        String str = FloorNumText.getText();
        this.allClear();

        if (!str.equals("")) {
            floorNum = Integer.parseInt(str);
            String context[] = new String[floorNum];
            for (int i = 0; i < floorNum; i++) {
                context[i] = "Floor " + (i + 1);
            }
            FloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));
            currentfloor = FloorComboBox.getSelectedIndex();
            GraphHandle graphHandle = new GraphHandle(IndoorShowFrame.this, context);
        } else {
            String context[] = new String[]{"select"};
            FloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));
            GraphHandle graphHandle = new GraphHandle(IndoorShowFrame.this, new String[]{""});
            currentfloor = 0;
        }
    }//GEN-LAST:event_FloorSetButtonActionPerformed

    private void InitialCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InitialCheckBoxActionPerformed
        // TODO add your handling code here:
        if (InitialCheckBox.isSelected()) {
            MaxSpeedText.setText(MAXSPEED + "");
            MaxSpeedText.setEditable(false);
            MinSpeedText.setText(MINSPEED + "");
            MinSpeedText.setEditable(false);
            DayNumText.setText(NUMDAY + "");
            DayNumText.setEditable(false);
            FloorNumText.setText(FLOORNUM + "");
            FloorNumText.setEditable(false);

            ((DefaultTableModel) DataRecordTable.getModel()).getDataVector().removeAllElements();
            this.partClear();

            if (graphs.isEmpty()) {
                readFile(new File("Environment.txt"));

                FloorNumText.setText("" + floorNum);
                String context[] = new String[floorNum];

                for (int i = 0; i < floorNum; i++) {
                    context[i] = "Floor " + (i + 1);
                }
                FloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));
                currentfloor = FloorComboBox.getSelectedIndex();

                PaintThread th = new PaintThread();
                th.start();
            }
        } else {
            MaxSpeedText.setText("");
            MaxSpeedText.setEditable(true);
            DayNumText.setText("");
            DayNumText.setEditable(true);
            MinSpeedText.setText("");
            MinSpeedText.setEditable(true);
            FloorNumText.setText("");
            FloorNumText.setEditable(true);

            this.allClear();

            currentfloor = 0;
            ((DefaultTableModel) DataRecordTable.getModel()).getDataVector().removeAllElements();

            FloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"select"}));
            PaintThread th = new PaintThread();
            th.start();
        }
    }//GEN-LAST:event_InitialCheckBoxActionPerformed

    private void LocProbCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LocProbCheckBoxActionPerformed
        // TODO add your handling code here:
        if (LocProbCheckBox.isSelected()) {
            PrimaryProbText.setText(PRIMARYPROB + "");
            PrimaryProbText.setEditable(false);
            SecondaryProbText.setText(SECONDPROB + "");
            SecondaryProbText.setEditable(false);
        } else {
            PrimaryProbText.setText("");
            PrimaryProbText.setEditable(true);
            SecondaryProbText.setText("");
            SecondaryProbText.setEditable(true);
        }
    }//GEN-LAST:event_LocProbCheckBoxActionPerformed

    private void GeneratingDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GeneratingDataButtonActionPerformed
        // TODO add your handling code here:

        DefaultTableModel dtm = (DefaultTableModel) DataRecordTable.getModel();
        dtm.getDataVector().removeAllElements();
        this.partClear();

        if (graphs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no Graphs!!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (this.getStartDate().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Start Date can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (this.getStartTime().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Start Time can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (this.getEndTime().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "End Time can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            final GenerateData gen = new GenerateData(graphs, dtm, this.getMaxSpeed(),
                    this.getMinSpeed(), this.getDayNum(), this.getFloorNum(), this.getMaxNumObject(),
                    this.getMinNumObject(), this.getDestObjectProp(), this.getPrimaryProb(),
                    this.getSecondaryProb(), this.getPrimaryStayTime(), this.getSecondayStayTime(),
                    this.getOtherStayTime(), this.getLeftStayTime(), this.getStartDate(),
                    this.getStartTime(), this.getEndTime());

            if ((ObjectIdShowComboBox.getItemCount() >= 20) && (this.getDayNum() >= 10) || (this.getDayNum() >= 100)) {
                Thread thr = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, "This will take a little time to show the trajectories! Please waiting!", "Message", JOptionPane.INFORMATION_MESSAGE);
                        repaint();
                    }
                });
                thr.start();
            }
            long begin = System.currentTimeMillis();
            gen.generData();
            long endtime = System.currentTimeMillis();
            System.out.println("The time cost：" + (endtime - begin));

            cir = gen.getCircles();
            notcir = gen.getNotCircles();
            start = gen.getStartCircles();
            end = gen.getEndCircles();
            persons = gen.getPersons();
            destinations = gen.getDestinations();

            int numObject = gen.getObjectNumber();
            String context[] = new String[numObject + 1];
            context[0] = "select";
            for (int i = 1; i < numObject + 1; i++) {
                context[i] = "Object " + i;
            }
            ObjectIdShowComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));

            Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "Generate Data Finished!", "Message", JOptionPane.INFORMATION_MESSAGE);
                    repaint();
                }
            });
            thr.start();
        }
    }//GEN-LAST:event_GeneratingDataButtonActionPerformed

    private void ExportDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportDataButtonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) DataRecordTable.getModel();
        int rowCount = dtm.getRowCount();
        int colCount = dtm.getColumnCount();
        if (rowCount != 0 && colCount != 0) {
            FileWriter fw;
            BufferedWriter bw;
            try {
                fw = new FileWriter("Data.txt");
                bw = new BufferedWriter(fw);
                bw.write("ID " + "R_ID " + "M_ID " + "EnterTime " + "LeaveTime " + "MoveTime");
                bw.newLine();
                bw.flush();

                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < colCount; j++) {
                        try {
                            bw.write(dtm.getValueAt(i, j) + " ");
                        } catch (IOException ex) {
                            Logger.getLogger(IndoorShowFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    bw.newLine();
                    bw.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(IndoorShowFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "Export Data Finished!", "Message", JOptionPane.INFORMATION_MESSAGE);
                    repaint();
                }
            });
            thr.start();
        } else {
            Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "There are no data in the table!!!", "Warning", JOptionPane.WARNING_MESSAGE);
                    repaint();
                }
            });
            thr.start();
        }
    }//GEN-LAST:event_ExportDataButtonActionPerformed

    private void MaxNumObjectTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaxNumObjectTextActionPerformed
        // TODO add your handling code here:
        int numObject = Integer.parseInt(MaxNumObjectText.getText());
        String context[] = new String[numObject + 1];
        context[0] = "select";
        for (int i = 1; i < numObject + 1; i++) {
            context[i] = "Object " + i;
        }
        ObjectIdShowComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));
    }//GEN-LAST:event_MaxNumObjectTextActionPerformed

    private void OpenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenButtonActionPerformed
        // TODO add your handling code here:
        this.allClear();
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Open");
        int x = jfc.showOpenDialog(indoorPanel);
        if (x == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            this.readFile(f);
        }
        if (!graphs.isEmpty()) {
            FloorNumText.setText("" + floorNum);
            String context[] = new String[floorNum];
            for (int i = 0; i < floorNum; i++) {
                context[i] = "Floor " + (i + 1);
            }
            FloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));
            currentfloor = FloorComboBox.getSelectedIndex();
        }

        if (!MaxNumObjectText.getText().equalsIgnoreCase("")) {
            int numObject = Integer.parseInt(MaxNumObjectText.getText());
            String context[] = new String[numObject + 1];
            context[0] = "select";
            for (int i = 1; i < numObject + 1; i++) {
                context[i] = "Object " + i;
            }
            ObjectIdShowComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));
        } else {
            ObjectIdShowComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"select"}));
        }

        ((DefaultTableModel) DataRecordTable.getModel()).getDataVector().removeAllElements();

        PaintThread th = new PaintThread();
        th.start();
    }//GEN-LAST:event_OpenButtonActionPerformed

    private void PersonInfoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PersonInfoCheckBoxActionPerformed
        // TODO add your handling code here:
        if (PersonInfoCheckBox.isSelected()) {
            MaxNumObjectText.setText(MAXOBJECT + "");
            MaxNumObjectText.setEditable(false);
            MinNumObjectText.setText(MINOBJECT + "");
            MinNumObjectText.setEditable(false);
            DestObjectPropText.setText(DESTOBPROP + "");
            DestObjectPropText.setEditable(false);

            int numObject = Integer.parseInt(MaxNumObjectText.getText());
            String context[] = new String[numObject + 1];
            context[0] = "select";
            for (int i = 1; i < numObject + 1; i++) {
                context[i] = "Object " + i;
            }
            ObjectIdShowComboBox.setModel(new javax.swing.DefaultComboBoxModel(context));
            PaintThread th = new PaintThread();
            th.start();
        } else {
            MaxNumObjectText.setText("");
            MaxNumObjectText.setEditable(true);
            MinNumObjectText.setText("");
            MinNumObjectText.setEditable(true);
            DestObjectPropText.setText("");
            DestObjectPropText.setEditable(true);

            ObjectIdShowComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"select"}));
            PaintThread th = new PaintThread();
            th.start();
        }
    }//GEN-LAST:event_PersonInfoCheckBoxActionPerformed

    private void StayTimeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StayTimeCheckBoxActionPerformed
        // TODO add your handling code here:
        if (StayTimeCheckBox.isSelected()) {
            PrimaryStayText.setText(PRIMARYSTAY + "");
            PrimaryStayText.setEditable(false);
            SecondStayText.setText(SECONDSTAY + "");
            SecondStayText.setEditable(false);
            OtherStayText.setText(OTHERSTAY + "");
            OtherStayText.setEditable(false);
            LeftStayText.setText(LEFTSTAY + "");
            LeftStayText.setEditable(false);
        } else {
            PrimaryStayText.setText("");
            PrimaryStayText.setEditable(true);
            SecondStayText.setText("");
            SecondStayText.setEditable(true);
            OtherStayText.setText("");
            OtherStayText.setEditable(true);
            LeftStayText.setText("");
            LeftStayText.setEditable(true);
        }
    }//GEN-LAST:event_StayTimeCheckBoxActionPerformed

    private void ObjectIdShowComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ObjectIdShowComboBoxActionPerformed
        // TODO add your handling code here:
        if (thread.isAlive()) {
            thread.setStop(true);
            thread.interrupt();
            drawline = null;
        }

        infor.setVisible(false);
        int object = ObjectIdShowComboBox.getSelectedIndex();//对象id号
        this.getLines(object);

        objectdests.clear();
        for (DestinationId dest : destinations) {
            if (dest.object == object) {
                objectdests.add(dest);
            }
        }

        thread = new PaintThread();
        thread.setStop(false);
        thread.start();
    }//GEN-LAST:event_ObjectIdShowComboBoxActionPerformed

    private void FloorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FloorComboBoxActionPerformed
        // TODO add your handling code here:
        infor.setVisible(false);
        currentfloor = FloorComboBox.getSelectedIndex();

        PaintThread th = new PaintThread();
        th.start();
    }//GEN-LAST:event_FloorComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /*
         * Create and display the form
         */
        final IndoorShowFrame indoor = new IndoorShowFrame();
        //BufferedImage buffer = new BufferedImage(indoor.indoorPanel.getWidth() - 10, indoor.indoorPanel.getHeight() - 20,BufferedImage.TYPE_INT_ARGB);
        //indoor.setBufferImage(buffer);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");//Java默认风格
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                indoor.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane DataRecordScroll;
    private javax.swing.JTable DataRecordTable;
    private javax.swing.JPanel DataTablePanel;
    private javax.swing.JLabel DateFormatLabel;
    private javax.swing.JLabel DayNumLabel;
    private javax.swing.JTextField DayNumText;
    private javax.swing.JComboBox DestLocColorComboBox;
    private javax.swing.JLabel DestObjectPropLabel;
    private javax.swing.JTextField DestObjectPropText;
    private javax.swing.JLabel DestinationColorLabel;
    private javax.swing.JLabel EndTimeLabel;
    private javax.swing.JTextField EndTimeText;
    private javax.swing.JButton ExportDataButton;
    private javax.swing.JComboBox FirstLocColorComboBox;
    private javax.swing.JLabel FirstLocColorLabel;
    private javax.swing.JComboBox FloorComboBox;
    private javax.swing.JLabel FloorLabel;
    private javax.swing.JLabel FloorNumLabel;
    private javax.swing.JTextField FloorNumText;
    private javax.swing.JButton FloorSetButton;
    private javax.swing.JButton GeneratingDataButton;
    private javax.swing.JPanel InOutTimePanel;
    private javax.swing.JCheckBox InitialCheckBox;
    private javax.swing.JPanel InitialSettingPanel;
    private javax.swing.JLabel LeftStayLabel;
    private javax.swing.JTextField LeftStayText;
    private javax.swing.JCheckBox LocProbCheckBox;
    private javax.swing.JPanel LocProbPanel;
    private javax.swing.JLabel MaxNumObjectLabel;
    private javax.swing.JTextField MaxNumObjectText;
    private javax.swing.JLabel MaxSpeedLabel;
    private javax.swing.JTextField MaxSpeedText;
    private javax.swing.JLabel MinNumObjectLabel;
    private javax.swing.JTextField MinNumObjectText;
    private javax.swing.JLabel MinSpeedLabel;
    private javax.swing.JTextField MinSpeedText;
    private javax.swing.JComboBox ObjectIdShowComboBox;
    private javax.swing.JLabel ObjectIdShowLabel;
    private javax.swing.JButton OpenButton;
    private javax.swing.JLabel OtherStayLabel;
    private javax.swing.JTextField OtherStayText;
    private javax.swing.JCheckBox PersonInfoCheckBox;
    private javax.swing.JPanel PersonInfoPanel;
    private javax.swing.JLabel PrimaryProbLabel;
    private javax.swing.JTextField PrimaryProbText;
    private javax.swing.JLabel PrimaryStayLabel;
    private javax.swing.JTextField PrimaryStayText;
    private javax.swing.JPanel RootPanel;
    private javax.swing.JComboBox SecondLocColorComboBox;
    private javax.swing.JLabel SecondLocColorLabel;
    private javax.swing.JLabel SecondStayLabel;
    private javax.swing.JTextField SecondStayText;
    private javax.swing.JLabel SecondaryProbLabel;
    private javax.swing.JTextField SecondaryProbText;
    private javax.swing.JLabel StartDateLabel;
    private javax.swing.JTextField StartDateText;
    private javax.swing.JLabel StartTimeLabel;
    private javax.swing.JTextField StartTimeText;
    private javax.swing.JCheckBox StayTimeCheckBox;
    private javax.swing.JPanel StayTimePanel;
    private javax.swing.JLabel TimeFormatLabel;
    private javax.swing.JLabel TimeFormatLabel1;
    private javax.swing.JPanel TrajectoryPanel;
    private javax.swing.JPanel indoorPanel;
    private javax.swing.JPanel pramatersPanel;
    // End of variables declaration//GEN-END:variables

    public void partClear() {
        objectdests.clear();
        persons.clear();
        destid = 0;
        lines.clear();
        drawline = null;
        if (thread.isAlive()) {
            thread.setStop(true);
            thread.interrupt();
        }
    }

    public void allClear() {
        graphs.clear();
        cir.clear();
        notcir.clear();
        partClear();
    }

    public void setBufferImage(BufferedImage buffer) {
        bufimg = buffer;
    }

    public int getMaxSpeed() {
        int speed = MAXSPEED;
        //int speed = 0;
        try {
            if (MaxSpeedText.getText().equalsIgnoreCase("")) {
                MaxSpeedText.setText("" + MAXSPEED);
                JOptionPane.showMessageDialog(this, "Max Speed uses defualt value: " + MAXSPEED, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                speed = Integer.parseInt(MaxSpeedText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Max Speed: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return speed;
    }

    public int getMinSpeed() {
        int speed = MINSPEED;
        //int speed = 0;
        try {
            if (MinSpeedText.getText().equalsIgnoreCase("")) {
                MinSpeedText.setText("" + MINSPEED);
                JOptionPane.showMessageDialog(this, "Min Speed uses defualt value: " + MINSPEED, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                speed = Integer.parseInt(MinSpeedText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Min Speed: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return speed;
    }

    public int getDayNum() {
        int day = NUMDAY;
        //int day = 0;
        try {
            if (DayNumText.getText().equalsIgnoreCase("")) {
                DayNumText.setText("" + NUMDAY);
                JOptionPane.showMessageDialog(this, "Max Day uses defualt value: " + NUMDAY, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                day = Integer.parseInt(DayNumText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Max Day: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return day;
    }

    public int getFloorNum() {
        int floor = FLOORNUM;
        //int floor = 0;
        try {
            if (FloorNumText.getText().equalsIgnoreCase("")) {
                FloorNumText.setText("" + FloorNumText);
                JOptionPane.showMessageDialog(this, "Floor Number uses defualt value: " + FLOORNUM, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                floor = Integer.parseInt(FloorNumText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Floor Number: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return floor;
    }

    public int getMaxNumObject() {
        int number = MAXOBJECT;
        //int number = 0;
        try {
            if (MaxNumObjectText.getText().equalsIgnoreCase("")) {
                MaxNumObjectText.setText("" + MAXOBJECT);
                JOptionPane.showMessageDialog(this, "Max Number of Object uses defualt value: " + MAXOBJECT, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                number = Integer.parseInt(MaxNumObjectText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Max Number of Object: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return number;
    }

    public int getMinNumObject() {
        int number = MINOBJECT;
        //int number = 0;
        try {
            if (MinNumObjectText.getText().equalsIgnoreCase("")) {
                MinNumObjectText.setText("" + MINOBJECT);
                JOptionPane.showMessageDialog(this, "Min Number of Object uses defualt value: " + MINOBJECT, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                number = Integer.parseInt(MinNumObjectText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Min Number of Object: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return number;
    }

    public double getDestObjectProp() {
        double prop = DESTOBPROP;
        //double prop = 0;
        try {
            if (DestObjectPropText.getText().equalsIgnoreCase("")) {
                DestObjectPropText.setText("" + DESTOBPROP);
                JOptionPane.showMessageDialog(this, "Dest Object Probability uses defualt value: " + DESTOBPROP, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                prop = Double.parseDouble(DestObjectPropText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dest Object Probability: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return prop;
    }

    public double getPrimaryProb() {
        double prop = PRIMARYPROB;
        //double prop = 0;
        try {
            if (PrimaryProbText.getText().equalsIgnoreCase("")) {
                PrimaryProbText.setText("" + PRIMARYPROB);
                JOptionPane.showMessageDialog(this, "Primary Prob uses defualt value: " + PRIMARYPROB, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                prop = Double.parseDouble(PrimaryProbText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Primary Prob: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return prop;
    }

    public double getSecondaryProb() {
        double prop = SECONDPROB;
        //double prop = 0;
        try {
            if (SecondaryProbText.getText().equalsIgnoreCase("")) {
                SecondaryProbText.setText("" + SECONDPROB);
                JOptionPane.showMessageDialog(this, "Secondary Prob uses defualt value: " + SECONDPROB, "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                prop = Double.parseDouble(SecondaryProbText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Secondary Prob: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return prop;
    }

    public int getPrimaryStayTime() {
        int time = PRIMARYSTAY;
        //int time = 0;
        try {
            if (PrimaryStayText.getText().equalsIgnoreCase("")) {
                PrimaryStayText.setText("" + PRIMARYSTAY);
                JOptionPane.showMessageDialog(this, "Primary Location Stay Time uses defualt value: " + PRIMARYSTAY + " minute", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                time = Integer.parseInt(PrimaryStayText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Primary Location Stay Time: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return time;
    }

    public int getSecondayStayTime() {
        int time = SECONDSTAY;
        //int time = 0;
        try {
            if (SecondStayText.getText().equalsIgnoreCase("")) {
                SecondStayText.setText("" + SECONDSTAY);
                JOptionPane.showMessageDialog(this, "Seconday Location Stay Time uses defualt value: " + SECONDSTAY + " minute", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                time = Integer.parseInt(SecondStayText.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Seconday Location Stay Time: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return time;
    }

    public int getOtherStayTime() {
        int time = OTHERSTAY;
        //int time = 0;
        try {
            if (OtherStayText.getText().equalsIgnoreCase("")) {
                OtherStayText.setText("" + OTHERSTAY);
                //prob = 0.125;
                JOptionPane.showMessageDialog(this, "Other Location Stay Time uses defualt value: " + OTHERSTAY + " minute", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                time = Integer.parseInt(OtherStayText.getText());
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Other Location Stay Time: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return time;
    }

    public int getLeftStayTime() {
        int time = LEFTSTAY;
        //int time = 0;
        try {
            if (LeftStayText.getText().equalsIgnoreCase("")) {
                LeftStayText.setText("" + LEFTSTAY);
                JOptionPane.showMessageDialog(this, "Lift Stay Time uses defualt value: " + LEFTSTAY + " minute", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                time = Integer.parseInt(LeftStayText.getText());
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lift Stay Time: input is not a number!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return time;
    }

    public String getStartDate() {

        String time = startDatePicker.getText();
        return time;
    }

    public String getStartTime() {

        String time = startTimePicker.getText();
        return time;
    }

    public String getEndTime() {

        String time = endTimePicker.getText();
        return time;
    }

    public JComboBox getFloorComboBox() {
        return FloorComboBox;
    }

    public JTable getDataRecordTable() {
        return DataRecordTable;
    }

    public JComboBox getObjectIdShowComboBox() {
        return ObjectIdShowComboBox;
    }

    public String getMaxNumObjectText() {
        return MaxNumObjectText.getText();
    }

    public void setFloorNumText(String floornum) {
        this.FloorNumText.setText(floornum);
    }

    public void getLines(int object) {

        int row = DataRecordTable.getRowCount();//数据的行数
        if (row == 0) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    javax.swing.JOptionPane.showMessageDialog(null, "There are no Data!", "Error", JOptionPane.ERROR_MESSAGE);
                    repaint();
                }
            });
            thread.start();
            return;
        }
        lines.clear();
        int piror = 0;
        int current = 0;
        int obstart = 0;
        if (object != 0) {
            for (int i = 0; i < row; i++) {
                int id = (Integer) DataRecordTable.getValueAt(i, 2);
                if (object == id) {
                    obstart++;
                    boolean isStart = false;
                    current = (Integer) DataRecordTable.getValueAt(i, 1);
                    for (Graph g : start) {
                        int rid = Integer.parseInt(g.getContext());
                        if (rid == current) {
                            isStart = true;
                            break;
                        }
                    }
                    if (start == end) {
                        isStart = false;
                    }
                    if (!isStart && obstart != 1) {
                        Graph pirorGraph = cir.get(piror - 1);
                        Graph currentGraph = cir.get(current - 1);

                        Shape pirorShape = this.judgeShape(pirorGraph);
                        Shape currentShape = this.judgeShape(currentGraph);
                        Point p = new Point((int) pirorShape.getBounds().getCenterX(), (int) pirorShape.getBounds().getCenterY());
                        Point c = new Point((int) currentShape.getBounds().getCenterX(), (int) currentShape.getBounds().getCenterY());
                        Line line = new Line(pirorGraph.getFloor(), pirorGraph, currentGraph, p, c);
                        lines.add(line);
                    }
                    piror = current;
                }
            }
        }
    }

    @Override
    public void paint(Graphics gr) {

        Graphics2D g2d = bufimg.createGraphics();
        g2d.setColor(this.getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);//防锯齿

        super.paint(g2d);

        drawAll(g2d);

        g2d.dispose();
        gr.drawImage(bufimg, 0, 0, this);
        gr.dispose();
    }

    void drawAll(Graphics2D g) {

        Shape shape = null;

        if (!notcir.isEmpty()) {
            for (Graph graphic : notcir) {
                if (graphic.getFloor() == currentfloor) {
                    shape = judgeShape(graphic);
                    if (graphic.getFillColor() != null) {
                        g.setColor(graphic.getFillColor());
                        g.fill(shape);
                    }
                    if (graphic.getBordercolor() != null) {
                        g.setColor(Color.black);
                        g.draw(shape);
                    }
                    g.setColor(Color.black);
                    if (graphic.getStyle() != 4) {
                        g.setFont(new Font("Times New Roman", Font.PLAIN, 16));

                        if (graphic.getStyle() != 0 || (graphic.getStyle() == 0 && graphic.isIsContext())) {
                            g.drawString(graphic.getContext(), graphic.getX1() + graphic.getWide() / 2 - graphic.getContext().length() * g.getFont().getSize() / 4, graphic.getY1() + graphic.getHeight() / 2 + g.getFont().getSize() / 3);
                        }
                    }
                }
            }
        }
        if (!cir.isEmpty()) {
            for (Graph graphic : cir) {
                if (graphic.getFloor() == currentfloor) {
                    shape = judgeShape(graphic);
                    int id = Integer.parseInt(graphic.getContext());

                    int object = ObjectIdShowComboBox.getSelectedIndex() - 1;//对象对应下标
                    int firstid = 0;
                    int secondid = 0;

                    if (!persons.isEmpty() && object >= 0) {
                        firstid = persons.get(object).getFirstid();
                        secondid = persons.get(object).getSecondid();
                    }

                    if (id == firstid || id == secondid || id == destid) {
                        if (id == firstid) {
                            g.setColor(color[FirstLocColorComboBox.getSelectedIndex()]);
                            g.fill(shape);
                        } else if (id == secondid) {
                            g.setColor(color[SecondLocColorComboBox.getSelectedIndex()]);
                            g.fill(shape);
                        }
                        if (id == destid) {
                            g.setColor(color[DestLocColorComboBox.getSelectedIndex()]);
                            g.fill(shape);
                        }
                    } else {

                        if (graphic.getFillColor() != null) {
                            g.setColor(graphic.getFillColor());
                            g.fill(shape);
                        }
                    }

                    if (graphic.getBordercolor() != null) {
                        g.setColor(Color.black);
                        g.draw(shape);
                    }
                    g.setColor(Color.black);
                    g.setFont(new Font("Times New Roman", Font.BOLD, 2 * graphic.getWide() / 3));
                    g.drawString(graphic.getContext(), graphic.getX1() + graphic.getWide() / 2 - graphic.getContext().length() * g.getFont().getSize() / 4, graphic.getY1() + graphic.getHeight() / 2 + g.getFont().getSize() / 3);
                }
            }
        }

        if (drawline != null) {
            g.setColor(Color.LIGHT_GRAY);
            if (drawline.start.getFloor() == currentfloor) {
                Graph startg = drawline.start;
                g.draw3DRect(startg.getX1(), startg.getY1(), startg.getWide(), startg.getHeight(), true);
            }
            if (drawline.over.getFloor() == currentfloor) {
                Graph endg = drawline.over;
                g.draw3DRect(endg.getX1(), endg.getY1(), endg.getWide(), endg.getHeight(), true);
            }
            if (drawline.floor == currentfloor) {
                g.setColor(Color.GREEN);
                drawArrows(drawline.begin.x, drawline.begin.y, drawline.end.x, drawline.end.y, (Graphics2D) g);
            }
        }
    }

    //画带箭头的线段
    public static void drawArrows(int sx, int sy, int ex, int ey, Graphics2D g) {
        double H = 10; //箭头高度
        double L = 4; //底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H);//箭头角度 
        double arraow_len = Math.sqrt(L * L + H * H); //箭头的长度
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; //(x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0];//(x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];

        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        //画线
        g.setStroke(new BasicStroke(2.0F));
        g.drawLine(sx, sy, ex, ey);

        GeneralPath triangle = new GeneralPath();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.closePath();
        //实心箭头
        g.fill(triangle);
    }

    // 计算
    public static double[] rotateVec(int px, int py, double ang,
            boolean isChLen, double newLen) {

        double mathstr[] = new double[2];
        //矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }

    Shape judgeShape(Graph graph) {
        Shape shape = null;
        switch (graph.getStyle()) {
            case 0:
            case 1:
                shape = new Rectangle(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight());
                break;
            case 2:
                shape = new Ellipse2D.Double(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight());
                break;
            case 3:
                shape = new RoundRectangle2D.Double(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight(), 5, 15);
                break;
            case 4:
                shape = new Line2D.Float(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight());
                break;
            default:
        }
        return shape;
    }

    void readFile(File file) {
        File f = file;
        try {
            floorNum = 0;
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(f));
            Object r = oos.readObject();
            if (r != null) {
                graphs = (ArrayList<Graph>) r;
                coordChange();
                for (Graph g : graphs) {
                    if (g.getStyle() == 2) {
                        cir.add(g);
                    } else {
                        notcir.add(g);
                    }
                }
                PaintThread th = new PaintThread();
                th.start();
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void coordChange() {
        int addx = 0, addy = 0;

        int x = indoorPanel.getX();
        int y = indoorPanel.getY();
        int w = indoorPanel.getWidth();
        int h = indoorPanel.getHeight();

        if (graphs.isEmpty()) {
            return;
        }
        int minX = graphs.get(0).getX1();
        int maxX = graphs.get(0).getX1() + graphs.get(0).getWide();
        int minY = graphs.get(0).getY1();
        int maxY = graphs.get(0).getY1() + graphs.get(0).getHeight();

        if (!graphs.isEmpty()) {
            for (Graph g : graphs) {
                if (g.getFloor() >= (floorNum - 1)) {
                    floorNum = g.getFloor() + 1;
                }
            }
        }

        for (int i = 0; i < floorNum; i++) {
            for (Graph g : graphs) {
                if (g.getFloor() == i) {
                    if (minX >= g.getX1()) {
                        minX = g.getX1();
                    }
                    if (minY >= g.getY1()) {
                        minY = g.getY1();
                    }
                    if (g.getStyle() != 4) {
                        if (maxX <= g.getX1() + g.getWide()) {
                            maxX = g.getX1() + g.getWide();
                        }
                        if (maxY <= g.getY1() + g.getHeight()) {
                            maxY = g.getY1() + g.getHeight();
                        }
                    } else {
                        if (maxX <= g.getWide()) {
                            maxX = g.getWide();
                        }
                        if (maxY <= g.getHeight()) {
                            maxY = g.getHeight();
                        }
                    }
                }
            }

            int length = maxX - minX;
            int height = maxY - minY;
            if (w - length >= 0) {
                addx = (w - length) / 2;
            }
            if (h - height >= 0) {
                addy = (h - height + 60) / 2;
            }
            for (Graph graph : graphs) {
                if (graph.getFloor() == i) {
                    int x1 = graph.getX1();
                    int y1 = graph.getY1();

                    graph.setX1(x1 + x + addx - minX);
                    graph.setY1(y1 + y + addy - minY);
                    if (graph.getStyle() == 4) {
                        int x2 = graph.getWide();
                        int y2 = graph.getHeight();
                        graph.setWide(x2 + x + addx - minX);
                        graph.setHeight(y2 + y + addy - minY);
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        int row = DataRecordTable.getRowCount();//��ݵ�����
        if (row != 0) {

            int startx = this.getX();
            int starty = this.getY();

            javax.swing.JTable table = infor.getTable();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.getDataVector().removeAllElements();

            int rid = -1;
            Point p = e.getPoint();
            Shape temp = null;
            for (Graph g : cir) {
                Shape s = this.judgeShape(g);

                if (g.getFloor() == currentfloor && s.getBounds().contains(p)) {
                    rid = Integer.parseInt(g.getContext());
                    temp = s;
                    break;
                }
            }

            if (rid != -1 && temp != null) {

                infor.setLocation((int) temp.getBounds().getCenterX() + startx - table.getWidth() / 2, (int) (temp.getBounds().getCenterY() + temp.getBounds().height / 2 + starty));

                for (int i = 0; i < row; i++) {
                    int id = (Integer) DataRecordTable.getValueAt(i, 1);
                    if (id == rid) {
                        try {
                            @SuppressWarnings("rawtypes")
                            Vector<Comparable> newRow = new Vector<Comparable>();
                            int object = (Integer) DataRecordTable.getValueAt(i, 2);
                            Date begin;
                            Date end;
                            begin = dataFormate.parse((String) DataRecordTable.getValueAt(i, 3));
                            end = dataFormate.parse((String) DataRecordTable.getValueAt(i, 4));
                            long stay = (end.getTime() - begin.getTime()) / 1000;

                            newRow.add(object);
                            newRow.add(dataFormate.format(begin));
                            newRow.add(dataFormate.format(end));
                            newRow.add(stay);

                            model.getDataVector().add(newRow);
                            model.fireTableDataChanged();

                        } catch (ParseException ex) {
                            Logger.getLogger(IndoorShowFrame.class.getName()).log(Level.SEVERE, null, ex);
                            PaintThread th = new PaintThread();
                            th.start();
                        }
                    }
                }
                if (table.getRowCount() != 0) {
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                    if (table.getRowCount() <= 5) {
                        infor.setSize(infor.getWidth(), table.getRowHeight() * (table.getRowCount() + 1));
                    } else {
                        infor.setSize(infor.getWidth(), table.getRowHeight() * 6);
                    }
                    infor.setVisible(true);
                } else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            javax.swing.JOptionPane.showMessageDialog(null, "There are no records at the location ", "Message", JOptionPane.INFORMATION_MESSAGE);
                            repaint();
                        }
                    });
                    thread.start();
                }
            } else {
                infor.setVisible(false);
                PaintThread th = new PaintThread();
                th.start();
            }
        }
    }

    /*
     * 保存轨迹中的线的两端点
     */
    class Line {

        int floor;
        Graph start;
        Graph over;
        Point begin;
        Point end;
        boolean isMark;

        public Line(int floor, Graph start, Graph over, Point x, Point y) {
            this.floor = floor;
            this.start = start;
            this.over = over;
            this.begin = x;
            this.end = y;
            this.isMark = false;
        }

        public void setMark(boolean mark) {
            this.isMark = mark;
        }
    }

    class inforWindow extends javax.swing.JWindow {

        private javax.swing.JPanel InforPanel;
        private javax.swing.JScrollPane InforScrollPane;
        private javax.swing.JTable InforTable;

        public inforWindow() {
            initComponents();
            InforTable.getTableHeader().setFont(new java.awt.Font("Century", 0, 12));
            InforTable.setFont(new java.awt.Font("Century", 0, 12));

            ColorTableCellRender colorRender = new ColorTableCellRender();
            colorRender.setRenderColor(InforTable);

        }

        private void initComponents() {

            InforPanel = new javax.swing.JPanel();
            InforScrollPane = new javax.swing.JScrollPane();
            InforTable = new javax.swing.JTable();

            InforTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "MOB_ID", "Enter_Time", "Leave_Time", "Stay_Time"
                    }) {
                Class[] types = new Class[]{
                    java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Long.class
                };

                @Override
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
            });
            InforScrollPane.setViewportView(InforTable);

            javax.swing.GroupLayout InforPanelLayout = new javax.swing.GroupLayout(InforPanel);
            InforPanel.setLayout(InforPanelLayout);
            InforPanelLayout.setHorizontalGroup(
                    InforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(InforScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE));
            InforPanelLayout.setVerticalGroup(
                    InforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InforPanelLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(InforScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)));

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(InforPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(InforPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

            pack();
        }

        javax.swing.JTable getTable() {
            return this.InforTable;
        }
    }

    class PaintThread extends Thread {
        //int count = 0;

        ArrayList<Line> l = new ArrayList<Line>();
        volatile boolean isStop = true;

        PaintThread() {
            this.l = lines;
        }

        @Override
        public void run() {
            //throw new UnsupportedOperationException("Not supported yet.");
            if (!isStop) {
                long begin = System.currentTimeMillis() / 1000;
                long curtime = begin;

                int count = 1;
                if (!objectdests.isEmpty()) {
                    destid = objectdests.get(0).getId();
                }
                for (int i = 0; i < l.size() + 1;) {
                    try {
                        if (i != l.size()) {
                            Line templine = l.get(i);
                            currentfloor = templine.start.getFloor();
                            FloorComboBox.setSelectedIndex(currentfloor);

                            if (templine.start.getFloor() != templine.over.getFloor() && !templine.isMark) {
                                int id = Integer.parseInt(templine.over.getContext());
                                currentfloor = templine.over.getFloor();
                                FloorComboBox.setSelectedIndex(currentfloor);
                                templine.setMark(true);
                                drawline = templine;
                                if (id == destid && count < objectdests.size()) {
                                    destid = objectdests.get(count).getId();
                                    count++;
                                }
                                repaint();
                                i++;
                            } else {
                                if ((curtime - begin) % 1 == 0 && !templine.isMark) {//间隔2s，绘制一条路径
                                    int id = Integer.parseInt(templine.over.getContext());
                                    templine.setMark(true);
                                    drawline = templine;
                                    if (id == destid && count < objectdests.size()) {
                                        destid = objectdests.get(count).getId();
                                        count++;
                                    }
                                    repaint();
                                    i++;
                                }
                            }
                            Thread.sleep(1000);
                            curtime = System.currentTimeMillis() / 1000;
                        } else {
                            drawline = null;
                            destid = 0;
                            repaint();
                            i++;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(IndoorShowFrame.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                }
                Thread.currentThread().interrupt();
            } else {
                repaint();
            }
        }

        public void setStop(boolean stop) {
            isStop = stop;
        }
    }

    //继承JListCellRenderer实现自己的CellRenderer类
    class PanelComboBoxCellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof JPanel) {
                if (isSelected) {//设置选中时的背景色
                    ((JPanel) value).setBackground(Color.LIGHT_GRAY);
                } else {
                    ((JPanel) value).setBackground(Color.white);
                }
                return (JPanel) value;
            }
            return null;
        }
    }

    //自定义的下拉列表选项对象�
    class ColorCell extends JPanel {

        private Color cellColor;
        private JPanel colorLabel;
        private JLabel colorNameLabel;
        private String colorName;

        public ColorCell(Color color, String name) {
            this.cellColor = color;
            this.colorName = name;
            colorLabel = new JPanel();
            colorLabel.setBackground(color);

            colorNameLabel = new JLabel(name);
            colorNameLabel.setForeground(color);
            colorNameLabel.setFont(new java.awt.Font("Century", 0, 12));
            colorLabel.setPreferredSize(new Dimension(20, 15));//约束组件大小

            setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
            add(colorLabel);
            add(colorNameLabel);
        }

        public Color getCellColor() {
            return cellColor;
        }

        public void setCellColor(Color cellColor) {
            this.cellColor = cellColor;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }
    }

    class ColorTableCellRender extends DefaultTableCellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            if (row % 2 == 0) {
                setBackground(new Color(255, 255, 255));//白色
            } else if (row % 2 == 1) {
                setBackground(new Color(220, 220, 220));//浅灰色
            }
            this.setHorizontalAlignment(SwingConstants.CENTER);

            return super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
        }

        void setRenderColor(JTable table) {

            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(this);
            }
        }
    }
}
