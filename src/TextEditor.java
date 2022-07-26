import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {

    JLabel fontLabel;
    JTextArea textArea;
    JScrollPane scrollPane;
    JSpinner fontSizeSpinner;
    JButton fontColorButton;
    JComboBox fontBox;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    TextEditor(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(500,600);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        // text area
        textArea = new JTextArea();
        textArea.setText("This is the first day of the rest of your life...");
        textArea.setForeground(Color.MAGENTA);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, 28));

        // scroll pane for text area
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // font label
        fontLabel = new JLabel("Font: ");

        // font spinner
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50,25));
        fontSizeSpinner.setValue(28);
        fontSizeSpinner.addChangeListener(e -> textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN,
                (int) fontSizeSpinner.getValue())));

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        // font box with standard Java fonts
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Kannada MN");

        // -------- menubar --------

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // -------- /menubar --------

        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // color chooser - change color of text area
        if (e.getSource()==fontColorButton){
            Color color = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
            textArea.setForeground(color);
        }

        // font box - change font of text area
        if (e.getSource()==fontBox){
            textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        // "Open" menu item - imports a text file, starts search from Desktop folder
        if (e.getSource()==openItem){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("/Users/bohdan/Desktop"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);
            if (response==JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;
                try {
                    fileIn = new Scanner(file);
                    if (file.isFile()){
                        textArea.setText(""); // reset text area before importing a new file
                        while (fileIn.hasNextLine()){
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    fileIn.close();
                }
            }

        }

        // "Save" menu item - saves a file
        if (e.getSource()==saveItem){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("/Users/bohdan/Desktop"));
            
            int response = fileChooser.showSaveDialog(null);
            if (response==JFileChooser.APPROVE_OPTION){
                File file;
                PrintWriter fileOut = null;
                
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    fileOut.close();
                }
            }
        }

        // Exit menu item
        if (e.getSource()==exitItem){
            System.exit(0);
        }
    }
}
