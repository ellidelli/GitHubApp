import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

// import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GitHub extends JFrame {
    public static void main (String[] args) {
        JFrame frame = new JFrame("GitHub App");

        //create panel
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        topPanel.setBackground(new Color(119, 141, 163));
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 2));
        panel.setBackground(new Color(191, 205, 219));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        bottomPanel.setLayout(new GridLayout(0, 2));
        bottomPanel.setBackground(new Color(119, 141, 163));


        //import images
        ImageIcon QU = new ImageIcon(new ImageIcon("images/QULogo.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        ImageIcon Microsoft = new ImageIcon(new ImageIcon("images/MLogo.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        //import font
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/SFPro.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        UIManager.put("Label.font", customFont);
        UIManager.put("Button.font", customFont);
        UIManager.put("TextField.font", customFont);
        UIManager.put("CheckBox.font", customFont);

        //object adds
        JLabel QULabel = new JLabel();
            QULabel.setIcon(QU);
        JLabel MicrosoftLabel = new JLabel();
            MicrosoftLabel.setIcon(Microsoft);
        JLabel disclaimer = new JLabel("Disclaimer: This application is a prototype and not yet meant for commercial use.");
            disclaimer.setForeground(Color.white);
        JLabel fpLabel = new JLabel("Project filepath:");
        JTextField fpInput = new JTextField();
            //TODO: add the gitinit command when there is a filepath in this textfield
        JLabel repoLabel = new JLabel("Repo name:");
        JTextField repoInput = new JTextField();
            //TODO: set this input to the repo name
        JCheckBox privateCheckbox = new JCheckBox("Private");
            //TODO add private command
        JCheckBox publicCheckBox = new JCheckBox("Public");
            publicCheckBox.setSelected(true);
            //TODO: add public command
        ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(privateCheckbox);
            buttonGroup.add(publicCheckBox);
        JLabel userLabel = new JLabel("User:");
        JTextField userInput = new JTextField();
            //TODO: set to user 
        JLabel tokenLabel = new JLabel("Token:");
        JTextField tokenInput = new JTextField();
            //TODO: set to token
        JButton create = new JButton("Create Repo");
            //TODO: add repo create command
            //TODO: link repo to local project
        JButton commit = new JButton("Push Initital Commit");
            //TODO: add initial commit command
            //TODO: make sure to add a .gitignore file to the filepath before adding, committing, pushing
            //TODO: add a README.md file with the repo name as a title (##)
        JLabel repoURL = new JLabel("Repo url:");
            repoURL.setForeground(Color.white);
        JTextField url = new JTextField(" ");
            url.setEditable(false);
            //TODO: make this display the new url when it is created

        //panel adds
        topPanel.add(QULabel);
        topPanel.add(MicrosoftLabel);
        topPanel.add(disclaimer);
        panel.add(fpLabel);
        panel.add(fpInput);
        panel.add(repoLabel);
        panel.add(repoInput);
        panel.add(publicCheckBox);
        panel.add(privateCheckbox);
        panel.add(userLabel);
        panel.add(userInput);
        panel.add(tokenLabel);
        panel.add(tokenInput);
        panel.add(create);
        panel.add(commit);
        bottomPanel.add(repoURL);
        bottomPanel.add(url);

        //frame stuff
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}


//Example on GitInit
// String repoPath = "/Users/elizabethdelea/Desktop/Heyy";
// GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(repoPath);
// String gitInit = gitSubprocessClient.gitInit();
    //debug print
// System.out.print(gitInit);

