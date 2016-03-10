/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;//BufferedImage 子类描述具有可访问图像数据缓冲区的 Image
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class EnvirSet extends javax.swing.JFrame implements MouseListener, MouseMotionListener {

    int x1, y1, x2, y2;
    int method;
    int element;//矩形房间、圆RFID、电梯等
    String elem_sem="Semantics";
    int floor;
    Color fillcolor;
    Color bordercolor;
    boolean isfill;
    boolean isborder;
    boolean iscontext;
    BufferedImage bufimg, buffimg;
    IndoorShowFrame indoorFrame = null;
    ArrayList<Graph> graphs = new ArrayList<Graph>();
    ArrayList<Graph> cir = new ArrayList<Graph>();
    ArrayList<Graph> notcir = new ArrayList<Graph>();

    /**
     * Creates new form test
     */
    public EnvirSet(final IndoorShowFrame indoor) {
        this.setTitle("Environment Setting");
        initComponents();
        this.setLocationRelativeTo(null);
        indoor.graphs.clear();
        indoor.cir.clear();
        indoor.notcir.clear();
        indoorFrame = indoor;
        this.graphs.clear();
        this.cir.clear();
        this.notcir.clear();

        bufimg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        String src = "/ustc.jpg";
        try {
            Image image = ImageIO.read(this.getClass().getResource(src));
            this.setIconImage(image);
        } catch (IOException ex) {
            Logger.getLogger(IndoorShowFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        fillcolor = FillColorLabel.getBackground();
        bordercolor = Color.BLACK;
        isfill = FillColorCheckBox.isSelected();
        isborder = BorderColorCheckBox.isSelected();
        iscontext = ContextCheckBox.isSelected();

        this.setVisible(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DrawPanel = new javax.swing.JPanel();
        MenuPanel = new javax.swing.JPanel();
        MethodBox = new javax.swing.JComboBox();
        FloorBox = new javax.swing.JComboBox();
        ElementBox = new javax.swing.JComboBox();
        ElementBox1 = new javax.swing.JComboBox();
        ContextCheckBox1 = new javax.swing.JCheckBox();
        ElementBox2 = new javax.swing.JComboBox();
        FillColorLabel = new javax.swing.JLabel();
        FillColorCheckBox = new javax.swing.JCheckBox();
        BorderColorCheckBox = new javax.swing.JCheckBox();
        ContextCheckBox = new javax.swing.JCheckBox();
        SaveButton = new javax.swing.JButton();
        LoadButton = new javax.swing.JButton();
        ClearButton = new javax.swing.JButton();
        OkButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1106, 800));
        setResizable(false);

        DrawPanel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        DrawPanel.setPreferredSize(new java.awt.Dimension(900, 604));

        javax.swing.GroupLayout DrawPanelLayout = new javax.swing.GroupLayout(DrawPanel);
        DrawPanel.setLayout(DrawPanelLayout);
        DrawPanelLayout.setHorizontalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        DrawPanelLayout.setVerticalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 604, Short.MAX_VALUE)
        );

        MenuPanel.setPreferredSize(new java.awt.Dimension(750, 40));
        MenuPanel.setLayout(new javax.swing.BoxLayout(MenuPanel, javax.swing.BoxLayout.LINE_AXIS));

        MethodBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        MethodBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Design", "Erase", "Move", "Semantics" }));
        MethodBox.setPreferredSize(new java.awt.Dimension(50, 21));
        MethodBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MethodBoxActionPerformed(evt);
            }
        });
        MenuPanel.add(MethodBox);

        FloorBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FloorBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Floor 1", "Floor 2", "Floor 3", "Floor 4", "Floor 5", "Floor 6" }));
        FloorBox.setPreferredSize(new java.awt.Dimension(50, 21));
        FloorBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FloorBoxActionPerformed(evt);
            }
        });
        MenuPanel.add(FloorBox);

        ElementBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ElementBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Room(Rectangle)", "Lift(Square)", "Sensor(Circle)", "Stair(RoundedRect)", "Boundary(Line)", "Semantics(Text)" }));
        ElementBox.setPreferredSize(new java.awt.Dimension(110, 21));
        ElementBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ElementBoxActionPerformed(evt);
            }
        });
        MenuPanel.add(ElementBox);

        ElementBox1.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ElementBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semantics", "Hotel", "shopping mall", "cinema", "KTV" }));
        ElementBox1.setMinimumSize(new java.awt.Dimension(60, 19));
        ElementBox1.setPreferredSize(new java.awt.Dimension(70, 21));
        ElementBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SemanticsBoxActionPerformed(evt);
            }
        });
        MenuPanel.add(ElementBox1);

        ContextCheckBox1.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ContextCheckBox1.setSelected(true);
        ContextCheckBox1.setText("Add Semantics");
        ContextCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContextCheckBox1ActionPerformed(evt);
            }
        });
        MenuPanel.add(ContextCheckBox1);

        ElementBox2.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ElementBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hotel", "shopping mall", "cinema", "KTV" }));
        ElementBox2.setMinimumSize(new java.awt.Dimension(50, 19));
        ElementBox2.setPreferredSize(new java.awt.Dimension(60, 21));
        ElementBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ElementBox2SemanticsBoxActionPerformed(evt);
            }
        });
        MenuPanel.add(ElementBox2);

        FillColorLabel.setBackground(new java.awt.Color(204, 204, 204));
        FillColorLabel.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FillColorLabel.setText("Filled Color");
        FillColorLabel.setOpaque(true);
        FillColorLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FillColorClicked(evt);
            }
        });
        MenuPanel.add(FillColorLabel);

        FillColorCheckBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        FillColorCheckBox.setText("Fiiled Color");
        FillColorCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FillColorCheckBoxButtonActionPerformed(evt);
            }
        });
        MenuPanel.add(FillColorCheckBox);

        BorderColorCheckBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        BorderColorCheckBox.setSelected(true);
        BorderColorCheckBox.setText("Boundary");
        BorderColorCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorderColorCheckBoxActionPerformed(evt);
            }
        });
        MenuPanel.add(BorderColorCheckBox);

        ContextCheckBox.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ContextCheckBox.setSelected(true);
        ContextCheckBox.setText("Context");
        ContextCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContextCheckBoxActionPerformed(evt);
            }
        });
        MenuPanel.add(ContextCheckBox);

        SaveButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });
        MenuPanel.add(SaveButton);

        LoadButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        LoadButton.setText("Open");
        LoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadButtonActionPerformed(evt);
            }
        });
        MenuPanel.add(LoadButton);

        ClearButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        ClearButton.setText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });
        MenuPanel.add(ClearButton);

        OkButton.setFont(new java.awt.Font("Century", 0, 12)); // NOI18N
        OkButton.setText("OK");
        OkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkButtonActionPerformed(evt);
            }
        });
        MenuPanel.add(OkButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1097, Short.MAX_VALUE)
            .addComponent(DrawPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1097, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(DrawPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadButtonActionPerformed
        // TODO add your handling code here:
        GraphHandle.load();
    }//GEN-LAST:event_LoadButtonActionPerformed

    private void FillColorCheckBoxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FillColorCheckBoxButtonActionPerformed
        // TODO add your handling code here:
        isfill = FillColorCheckBox.isSelected();
        if (!isfill) {
            fillcolor = null;
        } else {
            fillcolor = FillColorLabel.getBackground();
        }
        GraphHandle.cPressed();
    }//GEN-LAST:event_FillColorCheckBoxButtonActionPerformed

    private void BorderColorCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorderColorCheckBoxActionPerformed
        // TODO add your handling code here:
        isborder = BorderColorCheckBox.isSelected();
        if (!isborder) {
            bordercolor = null;
        } else {
            bordercolor = Color.BLACK;
        }
        GraphHandle.cPressed();
    }//GEN-LAST:event_BorderColorCheckBoxActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        // TODO add your handling code here:
        GraphHandle.save();
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        // TODO add your handling code here:
        GraphHandle.clear();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void FillColorClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FillColorClicked
        // TODO add your handling code here:
        JLabel l = (JLabel) evt.getSource();
        if (evt.getClickCount() == 1) {
            Color sc = JColorChooser.showDialog(null, "Color chooser", l.getBackground());
            l.setBackground(sc);
            if (isfill) {
                fillcolor = l.getBackground();
            }
        }
        GraphHandle.cPressed();
    }//GEN-LAST:event_FillColorClicked

    private void MethodBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MethodBoxActionPerformed
        // TODO add your handling code here:
        method = MethodBox.getSelectedIndex();
    }//GEN-LAST:event_MethodBoxActionPerformed

    private void ElementBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ElementBoxActionPerformed
        // TODO add your handling code here:
        element = ElementBox.getSelectedIndex();
    }//GEN-LAST:event_ElementBoxActionPerformed

    private void FloorBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FloorBoxActionPerformed
        // TODO add your handling code here:
        floor = FloorBox.getSelectedIndex();
        repaint();
    }//GEN-LAST:event_FloorBoxActionPerformed

    private void OkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkButtonActionPerformed
        // TODO add your handling code here:
        indoorFrame.graphs = graphs;
        indoorFrame.cir = cir;
        indoorFrame.notcir = notcir;
        indoorFrame.coordChange();
        if (graphs.isEmpty()) {
            indoorFrame.floorNum = 0;
            indoorFrame.setFloorNumText(indoorFrame.floorNum + "");
            indoorFrame.getFloorComboBox().setModel(new javax.swing.DefaultComboBoxModel(new String[]{"select"}));
        } else {
            int floornum = 1;
            for (Graph g : graphs) {
                if ((floornum - 1) <= g.getFloor()) {
                    floornum = g.getFloor() + 1;
                }
            }
            indoorFrame.floorNum = floornum;
            indoorFrame.setFloorNumText(indoorFrame.floorNum + "");
            String context[] = new String[floornum];
            for (int i = 0; i < floornum; i++) {
                context[i] = "Floor " + (i + 1);
            }
            indoorFrame.getFloorComboBox().setModel(new javax.swing.DefaultComboBoxModel(context));
        }
        indoorFrame.currentfloor = indoorFrame.getFloorComboBox().getSelectedIndex();
        if (!indoorFrame.getMaxNumObjectText().equalsIgnoreCase("")) {
            int object = Integer.parseInt(indoorFrame.getMaxNumObjectText());
            String number[] = new String[object + 1];
            number[0] = "select";
            for (int i = 1; i <= object; i++) {
                number[i] = "Object " + i;
            }
            indoorFrame.getObjectIdShowComboBox().setModel(new javax.swing.DefaultComboBoxModel(number));
        }
        ((DefaultTableModel) indoorFrame.getDataRecordTable().getModel()).getDataVector().removeAllElements();
        indoorFrame.repaint();
        this.setVisible(false);
    }//GEN-LAST:event_OkButtonActionPerformed

    private void ContextCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContextCheckBoxActionPerformed
        // TODO add your handling code here:
        iscontext = ContextCheckBox.isSelected();
        GraphHandle.cPressed();
    }//GEN-LAST:event_ContextCheckBoxActionPerformed

    private void SemanticsBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SemanticsBoxActionPerformed
        // TODO add your handling code here:
        elem_sem = (String)ElementBox1.getSelectedItem();
    }//GEN-LAST:event_SemanticsBoxActionPerformed

    private void ContextCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContextCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ContextCheckBox1ActionPerformed

    private void ElementBox2SemanticsBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ElementBox2SemanticsBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ElementBox2SemanticsBoxActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new EnvirSet().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox BorderColorCheckBox;
    private javax.swing.JButton ClearButton;
    private javax.swing.JCheckBox ContextCheckBox;
    private javax.swing.JCheckBox ContextCheckBox1;
    private javax.swing.JPanel DrawPanel;
    private javax.swing.JComboBox ElementBox;
    private javax.swing.JComboBox ElementBox1;
    private javax.swing.JComboBox ElementBox2;
    private javax.swing.JCheckBox FillColorCheckBox;
    private javax.swing.JLabel FillColorLabel;
    private javax.swing.JComboBox FloorBox;
    private javax.swing.JButton LoadButton;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JComboBox MethodBox;
    private javax.swing.JButton OkButton;
    private javax.swing.JButton SaveButton;
    // End of variables declaration//GEN-END:variables

    public JPanel getDrawPanel() {
        return DrawPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Point p = new Point();
        p.x = e.getPoint().x;
        p.y = e.getPoint().y;
        GraphHandle.hPressed(p);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Point p = new Point();
        p.x = e.getPoint().x;
        p.y = e.getPoint().y;
        GraphHandle.hReleassed(p);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Point p = new Point();
        p.x = e.getPoint().x;
        p.y = e.getPoint().y;
        GraphHandle.hDragged(p);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paint(Graphics g) {//repaint 重绘此组件。如果组件是轻量组件，则此方法会尽快调用此组件的 paint 方法。否则此方法会尽快调用此组件的 update 方法。

        Graphics2D g2d = bufimg.createGraphics();
        g2d.setColor(DrawPanel.getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        super.paint(g2d);

        GraphHandle.drawAll(g2d);

        g2d.dispose();
        g.drawImage(bufimg, 0, 0, this);
        g.dispose();
    }

    public JComboBox getFloorBox() {
        return FloorBox;
    }
}
