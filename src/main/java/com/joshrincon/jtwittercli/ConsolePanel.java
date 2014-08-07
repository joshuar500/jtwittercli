package com.joshrincon.jtwittercli;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class ConsolePanel extends JPanel{

    private JPanel commandPanel;
    private JPanel outputPanel;
    private JPanel backOutputPanel;

    ConsoleText consoleCursor;

    private JTextField commandField;
    private JTextField textField;
    JTextArea output;

    TwitHandler twitHandler;
    Twitter twitter;
    RequestToken requestToken;
    AccessToken accessToken;

    GridBagConstraints commandGC = new GridBagConstraints();

    public ConsolePanel() {

        try {
            twitHandler = new TwitHandler();
            twitter = twitHandler.getTwitter();
            requestToken = twitter.getOAuthRequestToken();
            accessToken = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        textField = new JTextField(20);
        consoleCursor = new ConsoleText();
        commandField = new JTextField(20);

        //design all the fields
        designView();

        //set background color for panel
        setBackground(Color.BLACK);

        //set up layouts innerborder and gridbag
        Border innerBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        setBorder(innerBorder);

        //set up commandPanel, set background to black
        commandPanel = new JPanel();
        commandPanel.setBackground(new Color(0, 0, 0));

        outputPanel = new JPanel();
        outputPanel.setBackground(new Color(0, 0, 0));

        backOutputPanel = new JPanel();
        backOutputPanel.setBackground(new Color(0, 0, 0));

        //set up layout with gridbag layouts
        setupInputPanel();
        setKeyBindings();

    }

    private void setupInputPanel() {

        backOutputPanel.setLayout(new BorderLayout());
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        commandPanel.setLayout(new GridBagLayout());

        // set the constraints for the GridBagLayout
        commandGC.gridy = getHeight();
        commandGC.gridx = 0;
        commandGC.weightx = 1;
        commandGC.weighty = 0.1;

        commandGC.fill = GridBagConstraints.NONE;
        commandGC.anchor = GridBagConstraints.SOUTHWEST;
        commandGC.insets = new Insets(0,0,0,0);
        commandPanel.add(consoleCursor.createConsoleCursor(), commandGC);
        commandGC.insets = new Insets(0,10,0,0);
        commandPanel.add(commandField, commandGC);


        // this createGlue() method takes up the space in the north,
        // so that the text moves from bottom to top
        // then add all layouts to their respective panels
        backOutputPanel.add(outputPanel, BorderLayout.SOUTH);
        add(commandPanel, BorderLayout.SOUTH);
        add(Box.createGlue(), BorderLayout.NORTH);
        add(backOutputPanel, BorderLayout.CENTER);

        addInfoText();
    }

    private void addInfoText() {

        output = new JTextArea();
        output.setBackground(new Color(0, 0, 0));
        output.setForeground(new Color(200, 0, 100));
        output.setFocusable(true);
        output.setEnabled(false);
        output.append("Type \"auth\" or copy paste the following URL into");
        outputPanel.add(output);
        output = new JTextArea();
        output.setBackground(new Color(0, 0, 0));
        output.setForeground(new Color(200, 0, 100));
        output.setFocusable(true);
        output.setEnabled(false);
        output.append("your browser and grant access to your account:");
        outputPanel.add(output);
        output = new JTextArea();
        output.setBackground(new Color(0, 0, 0));
        output.setForeground(new Color(200, 0, 100));
        output.setFocusable(true);
        output.setEditable(false);
        output.setFocusable(false);
        output.append(requestToken.getAuthorizationURL());
        outputPanel.add(output);

        revalidate();
    }

    private void setupOutput(JComponent component) {

        outputPanel.add(component);

        revalidate();

        int height = (int) getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0, height, 10, 10);
        scrollRectToVisible(rect);

        commandField.setText("");

    }

    private void designView() {
        textField.setBackground(new Color(0, 0, 0));
        textField.setForeground(new Color(100, 100, 0));
        textField.setCaretColor(new Color(0, 0, 0));
        textField.setBorder(null);
        textField.setVisible(false);
        textField.getCaret().setVisible(true);
        textField.getCaret().setBlinkRate(0);

        commandField.setBackground(new Color(0, 0, 0));
        commandField.setForeground(new Color(200, 100, 50));
        commandField.setCaretColor(new Color(0, 0, 0));
        commandField.setBorder(null);
        commandField.getCaret().setVisible(false);
        commandField.getCaret().setBlinkRate(0);
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);

        String enter = "VK_ENTER";

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);

        actionMap.put(enter, new KeyAction(enter));
    }


    private class KeyAction extends AbstractAction {

        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(commandField.getText().equals("")) {
                output = new JTextArea();
                output.setText(commandField.getText());
                output.setBackground(new Color(0, 0, 0));
                output.setForeground(new Color(200, 0, 100));
                output.setFocusable(true);
                output.setEnabled(false);
                setupOutput(output);
            }

            else if(commandField.getText().equals("auth")) {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)){
                    try {
                        URI uri = new URI(requestToken.getAuthenticationURL());
                        desktop.browse(uri);

                        output = new JTextArea();
                        output.setText(commandField.getText());
                        output.setBackground(new Color(0, 0, 0));
                        output.setForeground(new Color(200, 0, 100));
                        output.setFocusable(true);
                        setupOutput(output);
                        output = new JTextArea();
                        output.setBackground(new Color(0, 0, 0));
                        output.setForeground(new Color(200, 0, 100));
                        output.setFocusable(true);
                        output.setEnabled(false);
                        output.append("Enter the PIN when given...");
                        outputPanel.add(output);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }

            else if(commandField.getText().matches(".*\\d.*")) {
                try {
                    accessToken = twitter.getOAuthAccessToken(requestToken, commandField.getText());
                    //TODO: save request token so that users don't have to type in an auth number all the time
                    //twitHandler.storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
                    output = new JTextArea();
                    output.setText(commandField.getText());
                    output.setBackground(new Color(0, 0, 0));
                    output.setForeground(new Color(200, 0, 100));
                    output.setEditable(false);
                    output.setFocusable(true);
                    setupOutput(output);
                    output = new JTextArea();
                    output.setText("Authentication successful. Please type a command or /help.");
                    output.setBackground(new Color(0, 0, 0));
                    output.setForeground(new Color(100, 0, 100));
                    output.setFocusable(true);
                    setupOutput(output);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    output = new JTextArea();
                    output.setText("Something went wrong...");
                    output.setBackground(new Color(0, 0, 0));
                    output.setForeground(new Color(255, 0, 0));
                    output.setFocusable(true);
                    setupOutput(output);
                }
            }

            else if(commandField.getText().contains("/update")) {
                String regex = "\\/*\\bupdate\\b\\s*";
                String newString = commandField.getText().replaceAll(regex, "");
                System.out.println(newString);

                try {
                    Status status = twitter.updateStatus(newString);
                    output = new JTextArea();
                    output.setText(commandField.getText());
                    output.setBackground(new Color(0, 0, 0));
                    output.setForeground(new Color(200, 0, 100));
                    output.setEditable(false);
                    output.setFocusable(true);
                    setupOutput(output);
                    output = new JTextArea();
                    output.setText("Updated status: " + status.getText());
                    output.setBackground(new Color(0, 0, 0));
                    output.setForeground(new Color(0, 150, 150));
                    output.setFocusable(true);
                    setupOutput(output);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    output = new JTextArea();
                    output.setText("Something went wrong...");
                    output.setBackground(new Color(0, 0, 0));
                    output.setForeground(new Color(255, 0, 0));
                    output.setFocusable(true);
                    setupOutput(output);
                }
            }

            else {
                output = new JTextArea();
                output.setText("'" + commandField.getText() + "' is not a valid command. Please verify again or type /help for a list of commands. :)");
                output.setBackground(new Color(0, 0, 0));
                output.setForeground(new Color(255, 0, 0));
                output.setFocusable(true);
                output.setEnabled(false);
                outputPanel.add(output);
                revalidate();
                int height = (int) getPreferredSize().getHeight();
                Rectangle rect = new Rectangle(0, height, 10, 10);
                scrollRectToVisible(rect);
                commandField.setText("");
            }
        }
    }
}
