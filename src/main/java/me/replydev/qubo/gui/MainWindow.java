/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.StyleContext;
import me.replydev.qubo.Info;
import me.replydev.qubo.InputData;
import me.replydev.qubo.QuboInstance;
import me.replydev.qubo.gui.Confirm;
import me.replydev.qubo.gui.InstanceRunnable;
import me.replydev.qubo.gui.MessageWindow;
import me.replydev.qubo.gui.MyTableModel;
import me.replydev.qubo.gui.ProgressBarRunnable;
import me.replydev.utils.InvalidRangeException;
import me.replydev.utils.Log;

public class MainWindow
extends JFrame {
    private static final long serialVersionUID = 1L;
    public static DefaultTableModel dtm;
    private QuboInstance quboInstance;
    private Thread instanceThread;
    private InstanceRunnable instanceRunnable;
    private Point initialClick;
    private final JFrame meMyselfAndI;
    private ScheduledExecutorService schedulerProgressBarService;
    private JPanel pannello;
    private JLabel ipRangeLabel;
    private JTextField ipRangeTextField;
    private JTextField portRangeTextField;
    private JTextField timeoutTextField;
    private JLabel timeoutLabel;
    private JLabel portRangeLabel;
    private JButton stopButton;
    private JTable resultsTable;
    public JProgressBar progressBar1;
    private JLabel stateLabel;
    private JToolBar toolbar;
    private JLabel me;
    private JButton saveResultsButton;
    private JButton exitButton;
    private JCheckBox pingCheckBox;
    private JCheckBox doAllCheckBox;
    private JTextField threadTextField;
    private JButton startButton;
    private JLabel threadsLabel;
    private JTextField motdText;
    private JTextField minPlayersText;
    private JTextField versionText;

    public MainWindow() {
        this.$$$setupUI$$$();
        this.setUndecorated(true);
        this.meMyselfAndI = this;
        this.me.setText(" QuboScanner - 0.3.7  | ");
        this.setDefaultCloseOperation(3);
        this.setSize(1100, 750);
        this.setContentPane(this.pannello);
        URL url = ClassLoader.getSystemResource("icon.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        this.setIconImage(img);
        this.progressBar1.setString("Idle");
        this.setVisible(true);
        this.setupTable();
        this.startButton.addActionListener(e -> {
            InputData i;
            try {
                i = new InputData(this.getArgsFromInputMask());
            }
            catch (InvalidRangeException invalidRangeException) {
                if (Confirm.requestConfirm("Check ip range and relaunch program, would you like to see an example configuration?")) {
                    this.exampleConf();
                }
                return;
            }
            catch (NumberFormatException ex) {
                if (Confirm.requestConfirm("Check port range and relaunch program, would you like to see an example configuration?")) {
                    this.exampleConf();
                }
                return;
            }
            this.running(i);
            this.quboInstance = null;
        });
        this.stopButton.addActionListener(e -> this.idle());
        this.saveResultsButton.addActionListener(e -> {
            if (this.resultsTable.getRowCount() <= 0) {
                MessageWindow.showMessage("Error during saving", "Results table is empty!");
                return;
            }
            try {
                this.saveToFile();
            }
            catch (IOException ex) {
                Log.log_to_file(ex.toString(), "log.txt");
            }
        });
        this.me.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/replydev/Quboscanner"));
                    }
                    catch (IOException | URISyntaxException ex) {
                        Log.log_to_file(ex.toString(), "log.txt");
                    }
                }
            }
        });
        this.exitButton.addActionListener(e -> {
            if (this.instanceRunnable != null) {
                this.instanceRunnable.stop();
            }
            System.exit(0);
        });
        this.toolbar.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {
                MainWindow.this.initialClick = e.getPoint();
                MainWindow.this.getComponentAt(MainWindow.this.initialClick);
            }
        });
        this.toolbar.addMouseMotionListener(new MouseMotionAdapter(){

            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = ((MainWindow)MainWindow.this).meMyselfAndI.getLocation().x;
                int thisY = ((MainWindow)MainWindow.this).meMyselfAndI.getLocation().y;
                int xMoved = e.getX() - ((MainWindow)MainWindow.this).initialClick.x;
                int yMoved = e.getY() - ((MainWindow)MainWindow.this).initialClick.y;
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                MainWindow.this.meMyselfAndI.setLocation(X, Y);
            }
        });
    }

    public void idle() {
        this.instanceRunnable.stop();
        this.instanceThread = null;
        this.schedulerProgressBarService.shutdown();
        this.quboInstance = null;
        this.progressBar1.setString("100%");
        this.progressBar1.setValue(100);
        this.stopButton.setEnabled(false);
        this.ipRangeTextField.setEnabled(true);
        this.portRangeTextField.setEnabled(true);
        this.threadTextField.setEnabled(true);
        this.timeoutTextField.setEnabled(true);
        this.startButton.setEnabled(true);
        this.stateLabel.setText("Idle");
        this.stateLabel.setForeground(Color.green.darker().darker());
        this.doAllCheckBox.setEnabled(true);
        this.pingCheckBox.setEnabled(true);
        this.motdText.setEnabled(true);
        this.minPlayersText.setEnabled(true);
        this.versionText.setEnabled(true);
    }

    private void running(InputData i) {
        this.quboInstance = new QuboInstance(i);
        dtm.setRowCount(0);
        Info.serverFound = 0;
        Info.serverNotFilteredFound = 0;
        this.instanceRunnable = new InstanceRunnable(this.quboInstance, this);
        this.instanceThread = new Thread(this.instanceRunnable);
        this.instanceThread.start();
        this.schedulerProgressBarService = Executors.newScheduledThreadPool(1);
        this.schedulerProgressBarService.scheduleAtFixedRate(new ProgressBarRunnable(this.progressBar1, this.quboInstance), 0L, TimeUnit.SECONDS.toSeconds(1L), TimeUnit.SECONDS);
        this.ipRangeTextField.setEnabled(false);
        this.portRangeTextField.setEnabled(false);
        this.threadTextField.setEnabled(false);
        this.timeoutTextField.setEnabled(false);
        this.startButton.setEnabled(false);
        this.stopButton.setEnabled(true);
        this.stateLabel.setText("Running");
        this.stateLabel.setForeground(Color.red);
        this.doAllCheckBox.setEnabled(false);
        this.pingCheckBox.setEnabled(false);
        this.motdText.setEnabled(false);
        this.minPlayersText.setEnabled(false);
        this.versionText.setEnabled(false);
    }

    private void saveToFile() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this.meMyselfAndI);
        if (option != 0) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        if (!file.createNewFile()) {
            MessageWindow.showMessage("Error during saving", "Cannot create file, try to run me as administrator");
            return;
        }
        PrintWriter os = new PrintWriter(file);
        for (int row = 0; row < this.resultsTable.getRowCount(); ++row) {
            for (int col = 0; col < this.resultsTable.getColumnCount(); ++col) {
                os.print(this.resultsTable.getValueAt(row, col));
                os.print(" - ");
            }
            os.println();
        }
        os.close();
    }

    public void exampleConf() {
        this.ipRangeTextField.setText("192.168.1.*");
        this.portRangeTextField.setText("25565-25577");
        this.threadTextField.setText("50");
        this.timeoutTextField.setText("1000");
    }

    private String[] getArgsFromInputMask() {
        String command = "-range " + this.ipRangeTextField.getText() + " -ports " + this.portRangeTextField.getText() + " -th " + this.threadTextField.getText() + " -ti " + this.timeoutTextField.getText();
        if (!this.pingCheckBox.isSelected()) {
            command = command + " -noping";
        }
        if (this.doAllCheckBox.isSelected()) {
            command = command + " -all";
        }
        if (!this.versionText.getText().isEmpty()) {
            command = command + " -ver " + this.versionText.getText();
        }
        if (!this.motdText.getText().isEmpty()) {
            command = command + " -motd " + this.motdText.getText();
        }
        if (!this.minPlayersText.getText().isEmpty()) {
            command = command + " -on " + this.minPlayersText.getText();
        }
        return command.split(" ");
    }

    private void setupTable() {
        dtm = new MyTableModel();
        this.resultsTable.setModel(dtm);
        TableColumnModel columnModel = this.resultsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(5);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(136);
        columnModel.getColumn(5).setPreferredWidth(453);
        this.resultsTable.setSelectionBackground(Color.white);
        this.resultsTable.setSelectionForeground(Color.black);
        this.resultsTable.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable)mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String ip = table.getModel().getValueAt(row, 1).toString();
                    String port = table.getModel().getValueAt(row, 2).toString();
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(ip + ":" + port), null);
                }
            }
        });
        this.resultsTable.getTableHeader().setReorderingAllowed(false);
    }

    private void $$$setupUI$$$() {
        this.pannello = new JPanel();
        this.pannello.setLayout(new GridLayoutManager(7, 9, new Insets(0, 0, 0, 0), -1, -1));
        this.pannello.setBackground(new Color(-14605013));
        this.pannello.setEnabled(false);
        this.pannello.setForeground(new Color(-5524801));
        this.ipRangeLabel = new JLabel();
        this.ipRangeLabel.setBackground(new Color(-5524801));
        this.ipRangeLabel.setForeground(new Color(-5524801));
        this.ipRangeLabel.setText("Ip Range");
        this.pannello.add((Component)this.ipRangeLabel, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, null, null, null, 1, false));
        this.ipRangeTextField = new JTextField();
        this.ipRangeTextField.setBackground(new Color(-14605013));
        this.ipRangeTextField.setForeground(new Color(-5524801));
        this.ipRangeTextField.setToolTipText("Put here the starting ip");
        this.pannello.add((Component)this.ipRangeTextField, new GridConstraints(1, 1, 1, 8, 8, 1, 2, 0, null, null, null, 0, false));
        this.portRangeTextField = new JTextField();
        this.portRangeTextField.setBackground(new Color(-14605013));
        this.portRangeTextField.setForeground(new Color(-5524801));
        this.portRangeTextField.setToolTipText("Like: 25565-25577");
        this.pannello.add((Component)this.portRangeTextField, new GridConstraints(2, 1, 1, 1, 8, 1, 4, 0, null, null, null, 0, false));
        this.timeoutTextField = new JTextField();
        this.timeoutTextField.setBackground(new Color(-14605013));
        this.timeoutTextField.setForeground(new Color(-5524801));
        this.timeoutTextField.setText("");
        this.timeoutTextField.setToolTipText("Best timeout option is 500");
        this.pannello.add((Component)this.timeoutTextField, new GridConstraints(3, 1, 1, 1, 8, 1, 4, 0, null, null, null, 0, false));
        this.timeoutLabel = new JLabel();
        this.timeoutLabel.setBackground(new Color(-5524801));
        this.timeoutLabel.setForeground(new Color(-5524801));
        this.timeoutLabel.setText("Timout");
        this.pannello.add((Component)this.timeoutLabel, new GridConstraints(3, 0, 1, 1, 8, 0, 0, 0, null, null, null, 1, false));
        this.portRangeLabel = new JLabel();
        this.portRangeLabel.setBackground(new Color(-5524801));
        this.portRangeLabel.setForeground(new Color(-5524801));
        this.portRangeLabel.setText("Port Range");
        this.pannello.add((Component)this.portRangeLabel, new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, null, null, null, 1, false));
        this.stopButton = new JButton();
        this.stopButton.setBackground(new Color(-14605013));
        this.stopButton.setEnabled(false);
        this.stopButton.setFocusable(false);
        this.stopButton.setForeground(new Color(-1));
        this.stopButton.setText("Stop");
        this.pannello.add((Component)this.stopButton, new GridConstraints(4, 3, 1, 2, 0, 1, 3, 0, null, null, null, 0, false));
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBackground(new Color(-14605013));
        scrollPane1.setForeground(new Color(-5524801));
        scrollPane1.setVisible(true);
        this.pannello.add((Component)scrollPane1, new GridConstraints(6, 0, 1, 9, 0, 3, 5, 5, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "", 0, 0, null, null));
        this.resultsTable = new JTable();
        this.resultsTable.setAutoResizeMode(4);
        this.resultsTable.setBackground(new Color(-14605013));
        this.resultsTable.setFillsViewportHeight(true);
        this.resultsTable.setForeground(new Color(-5524801));
        this.resultsTable.setGridColor(new Color(-5524801));
        this.resultsTable.setSelectionForeground(new Color(-10855846));
        this.resultsTable.setVisible(true);
        scrollPane1.setViewportView(this.resultsTable);
        this.progressBar1 = new JProgressBar();
        this.progressBar1.setBackground(new Color(-14605013));
        this.progressBar1.setForeground(new Color(-5524801));
        this.pannello.add((Component)this.progressBar1, new GridConstraints(5, 0, 1, 9, 0, 1, 4, 0, null, null, null, 0, false));
        this.stateLabel = new JLabel();
        this.stateLabel.setBackground(new Color(-5524801));
        this.stateLabel.setForeground(new Color(-5524801));
        this.stateLabel.setText("Idle");
        this.pannello.add((Component)this.stateLabel, new GridConstraints(4, 5, 1, 1, 0, 0, 0, 0, null, null, null, 0, false));
        this.toolbar = new JToolBar();
        this.toolbar.setBackground(new Color(-14605013));
        this.toolbar.setFloatable(false);
        this.pannello.add((Component)this.toolbar, new GridConstraints(0, 0, 1, 9, 0, 1, 4, 0, null, new Dimension(-1, 20), null, 0, false));
        this.me = new JLabel();
        Font meFont = this.$$$getFont$$$(null, -1, 16, this.me.getFont());
        if (meFont != null) {
            this.me.setFont(meFont);
        }
        this.me.setText("  QuboScanner  ");
        this.toolbar.add(this.me);
        this.saveResultsButton = new JButton();
        this.saveResultsButton.setBackground(new Color(-1));
        this.saveResultsButton.setFocusable(false);
        this.saveResultsButton.setForeground(new Color(-5524801));
        this.saveResultsButton.setOpaque(false);
        this.saveResultsButton.setText("Save Results");
        this.toolbar.add(this.saveResultsButton);
        this.exitButton = new JButton();
        this.exitButton.setText("Exit");
        this.toolbar.add(this.exitButton);
        this.threadTextField = new JTextField();
        this.threadTextField.setBackground(new Color(-14605013));
        this.threadTextField.setForeground(new Color(-5524801));
        this.threadTextField.setToolTipText("Put here the ending ip");
        this.pannello.add((Component)this.threadTextField, new GridConstraints(2, 3, 1, 6, 8, 1, 4, 0, null, null, null, 0, false));
        this.startButton = new JButton();
        this.startButton.setAutoscrolls(true);
        this.startButton.setBackground(new Color(-14605013));
        this.startButton.setFocusable(false);
        this.startButton.setForeground(new Color(-5524801));
        this.startButton.setText("Start");
        this.startButton.setToolTipText("Start the party!");
        this.pannello.add((Component)this.startButton, new GridConstraints(4, 0, 1, 3, 0, 1, 3, 0, null, null, null, 1, false));
        this.threadsLabel = new JLabel();
        this.threadsLabel.setBackground(new Color(-5524801));
        this.threadsLabel.setForeground(new Color(-5524801));
        this.threadsLabel.setText("Threads");
        this.pannello.add((Component)this.threadsLabel, new GridConstraints(2, 2, 1, 1, 8, 0, 0, 0, null, null, null, 1, false));
        JLabel label1 = new JLabel();
        label1.setBackground(new Color(-5524801));
        label1.setForeground(new Color(-5524801));
        label1.setText("Motd");
        this.pannello.add((Component)label1, new GridConstraints(3, 2, 1, 1, 8, 0, 0, 0, null, null, null, 1, false));
        this.motdText = new JTextField();
        this.motdText.setBackground(new Color(-14605013));
        this.motdText.setForeground(new Color(-5524801));
        this.pannello.add((Component)this.motdText, new GridConstraints(3, 3, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
        JLabel label2 = new JLabel();
        label2.setBackground(new Color(-5524801));
        label2.setForeground(new Color(-5524801));
        label2.setText("Version");
        this.pannello.add((Component)label2, new GridConstraints(3, 4, 1, 1, 8, 0, 0, 0, null, null, null, 1, false));
        JLabel label3 = new JLabel();
        label3.setBackground(new Color(-5524801));
        label3.setForeground(new Color(-5524801));
        label3.setText("MinPlayers");
        this.pannello.add((Component)label3, new GridConstraints(3, 7, 1, 1, 8, 0, 0, 0, null, null, null, 1, false));
        this.minPlayersText = new JTextField();
        this.minPlayersText.setBackground(new Color(-14605013));
        this.minPlayersText.setForeground(new Color(-5524801));
        this.pannello.add((Component)this.minPlayersText, new GridConstraints(3, 8, 1, 1, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
        this.versionText = new JTextField();
        this.versionText.setBackground(new Color(-14605013));
        this.versionText.setForeground(new Color(-5524801));
        this.pannello.add((Component)this.versionText, new GridConstraints(3, 5, 1, 2, 8, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
        this.doAllCheckBox = new JCheckBox();
        this.doAllCheckBox.setBackground(new Color(-14605013));
        this.doAllCheckBox.setFocusable(false);
        this.doAllCheckBox.setText("Check all");
        this.pannello.add((Component)this.doAllCheckBox, new GridConstraints(4, 6, 1, 1, 8, 0, 3, 0, null, null, null, 0, false));
        this.pingCheckBox = new JCheckBox();
        this.pingCheckBox.setBackground(new Color(-14605013));
        this.pingCheckBox.setFocusable(false);
        this.pingCheckBox.setText("Ping");
        this.pannello.add((Component)this.pingCheckBox, new GridConstraints(4, 7, 1, 1, 8, 0, 3, 0, null, null, null, 0, false));
    }

    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        Font testFont;
        if (currentFont == null) {
            return null;
        }
        String resultName = fontName == null ? currentFont.getName() : ((testFont = new Font(fontName, 0, 10)).canDisplay('a') && testFont.canDisplay('1') ? fontName : currentFont.getName());
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    public JComponent $$$getRootComponent$$$() {
        return this.pannello;
    }
}

