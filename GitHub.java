import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.client.RequestParams;

// import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class GitHub extends JFrame {

    private static String[] gitIgnoreItems = new String[] {".classpath", 
    ".project", ".settings", "bin/", ".settings", ".DS_Store", ".vscode", "settings.json" };

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
        JCheckBox publicCheckbox = new JCheckBox("Public");
            publicCheckbox.setSelected(true);
            //TODO: add public command
        ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(privateCheckbox);
            buttonGroup.add(publicCheckbox);
        JLabel userLabel = new JLabel("User:");
        JTextField userInput = new JTextField();
            //TODO: set to user 
        JLabel tokenLabel = new JLabel("Token:");
        JTextField tokenInput = new JTextField();
            //TODO: set to token
        JButton create = new JButton("Create Repo");
            //TODO: add repo create command
            //TODO: link repo to local project
        
        JLabel repoURL = new JLabel("Repo url:");
            repoURL.setForeground(Color.white);
        JTextField url = new JTextField(" ");
            url.setEditable(false);
        create.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String githubRepoUrl = createRepo(fpInput.getText(), repoInput.getText(), userInput.getText(), tokenInput.getText(), privateCheckbox.isSelected());
                url.setText(githubRepoUrl);
            }

        });

        //panel adds
        topPanel.add(QULabel);
        topPanel.add(MicrosoftLabel);
        topPanel.add(disclaimer);
        panel.add(fpLabel);
        panel.add(fpInput);
        panel.add(repoLabel);
        panel.add(repoInput);
        panel.add(publicCheckbox);
        panel.add(privateCheckbox);
        panel.add(userLabel);
        panel.add(userInput);
        panel.add(tokenLabel);
        panel.add(tokenInput);
        panel.add(Box.createHorizontalStrut(0));
        panel.add(create);
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

    /**
     * The method called by the "Create Repo" button. Handles the data given to the program.
     * Each process should have it's own separate function
     * 
     * @param filePath the path of the project
     * @param repoName the name of the GitHub repo
     * @param username the user's username
     * @param token the API token of the user
     * @param privateRepo whether or not the repo is private
     */
    public static String createRepo(String filePath, String repoName, String username, String token, boolean privateRepo) {
        //checks if any of the fields are empty and won't continue until it's resolved
        if (!(filePath.equalsIgnoreCase("") || 
            repoName.equalsIgnoreCase("") || 
            username.equalsIgnoreCase("") || 
            token.equalsIgnoreCase(""))) {
            //Subprocess and API initialization
            GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(filePath);
            GitHubApiClient gitHubApiClient = new GitHubApiClient(username, token);

            //adds the gitignore and readme files to local project
            addGitFiles(filePath, repoName);
            
            //make local project a git repo
            gitSubprocessClient.gitInit();

            //make online github repo
            RequestParams rqParams = new RequestParams();
            rqParams.addParam("name", repoName);
            rqParams.addParam("private", privateRepo);
            gitHubApiClient.createRepo(rqParams);

            //link the 2 repos together
            gitSubprocessClient.gitRemoteAdd("origin", "https://www.github.com/" + username + "/" + repoName + ".git");

            //push an initial commit
            initialCommit(gitSubprocessClient);
            return "https://www.github.com/" + username + "/" + repoName;
        }
        return "One or more fields empty!";
    }

    /**
     * Adds the .gitignore files and the README.md files to the local project.
     * 
     * @param filePath the path to make the files
     * @param repoName the name of the GitHub repo
     * 
     * @author Jacob Hogrefe
     */
    public static void addGitFiles(String filePath, String repoName) {
        //create the files at the path of the local repo
        File gitIgnore = new File(filePath + "/.gitignore");
        File readme = new File(filePath + "/README.md");

        //write to the files if they exist (they do)
        try {
            gitIgnore.createNewFile(); readme.createNewFile();
            PrintWriter gitWriter = new PrintWriter(new File(filePath + "/.gitignore"));
            PrintWriter readWriter = new PrintWriter(new File(filePath + "/README.md"));

            //items in .gitignore can be added in the array
            for (int i = 0; i < gitIgnoreItems.length; i++) {
                gitWriter.println(gitIgnoreItems[i]);
            }
            gitWriter.close();
            
            //simple README.md file
            readWriter.println("## " + repoName);
            readWriter.println("This is an auto generated README.md file.");
            readWriter.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        }
    }

    /**
     * A method used to perform all of the commands to achieve an initial commit to GitHub. The
     * local project and GitHub repo must be connected in order for this to work.
     * 
     * @param consoleClient client to make all of the Git commands
     * @author Jacob Hogrefe
     */
    public static void initialCommit(GitSubprocessClient consoleClient) {
        consoleClient.gitAddAll();
        consoleClient.gitCommit("initial commit");
        consoleClient.gitPush("master");
    }
}


//Example on GitInit
// String repoPath = "/Users/elizabethdelea/Desktop/Heyy";
// GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(repoPath);
// String gitInit = gitSubprocessClient.gitInit();
    //debug print
// System.out.print(gitInit);

