package com.joshrincon.jtwittercli;

import twitter4j.*;
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

/*
    There are 3 different panels
    commandPanel that holds the commandField and textField
    in this classes Border.SOUTH field
    backOutputPanel that lets me position the outputPanel
    in the Border.SOUTH field

    setupPanels()
    setKeyBindings() - set up VK_ENTER is pressed
    designView() - sets design for commandField and textField
    addInfoText() - if it's first time logging in, show this, else show something else
    setupOutput(JComponent) - displays the component in output
    textAreaDesigner(JTextArea, int) - sets up the text according to input
    KeyAction class extends AbstractAction - all logic for what to do when VK_ENTER is pressed
*/



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

    public static final int SUCCESS_TEXT = 1;
    public static final int ERROR_TEXT = 2;
    public static final int UPDATE_TEXT = 3;

    public ConsolePanel() {

        try {
            twitHandler = new TwitHandler();
            twitter = twitHandler.getTwitter();
            requestToken = twitter.getOAuthRequestToken();
            accessToken = null;
            if(twitHandler.fileExists()) {
                twitter.setOAuthAccessToken(twitHandler.loadAccessToken());
            }
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
        setupPanels();
        setKeyBindings();

    }

    private void setupPanels() {

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

        if(!twitHandler.fileExists()) {
            textAreaDesigner(output, SUCCESS_TEXT);
            output.append("Type \"auth\" or copy paste the following URL into");
            outputPanel.add(output);

            textAreaDesigner(output, SUCCESS_TEXT);
            output.append("your browser and grant access to your account:");
            outputPanel.add(output);

            textAreaDesigner(output, SUCCESS_TEXT);
            output.append(requestToken.getAuthorizationURL());
            outputPanel.add(output);
        } else {
            textAreaDesigner(output, SUCCESS_TEXT);
            output.append("Welcome back!");
            outputPanel.add(output);
        }
        revalidate();
    }

    private void setupOutput(JComponent component) {
        outputPanel.add(component);
        commandField.setText("");
    }

    private void updateGUI() {
        int height = (int) getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0, height, 10, 10);
        scrollRectToVisible(rect);

        System.out.println("Height: " + height);

        revalidate();
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

    public void textAreaDesigner(JTextArea textArea, int i) {
        switch (i) {
            case SUCCESS_TEXT:
                output = new JTextArea();
                output.setBackground(new Color(0, 0, 0));
                output.setForeground(new Color(200, 0, 100));
                output.setFocusable(false);
                output.setEnabled(true);
                output.setLineWrap(true);
                output.setWrapStyleWord(true);
                break;
            case ERROR_TEXT:
                output = new JTextArea();
                output.setText("Something went wrong...");
                output.setBackground(new Color(0, 0, 0));
                output.setForeground(new Color(255, 0, 0));
                output.setFocusable(false);
                output.setLineWrap(true);
                output.setWrapStyleWord(true);
                break;
            case UPDATE_TEXT:
                output = new JTextArea();
                output.setBackground(new Color(0, 0, 0));
                output.setForeground(new Color(0, 150, 150));
                output.setFocusable(false);
                output.setLineWrap(true);
                output.setWrapStyleWord(true);
                break;
            default:
                output = new JTextArea();
                output.setBackground(new Color(0, 0, 0));
                output.setForeground(new Color(200, 0, 100));
                output.setFocusable(false);
                output.setEnabled(false);
                output.setLineWrap(true);
                output.setWrapStyleWord(true);
                break;
        }
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

                        textAreaDesigner(output, SUCCESS_TEXT);
                        output.setText(commandField.getText());
                        setupOutput(output);

                        textAreaDesigner(output, SUCCESS_TEXT);
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
                    textAreaDesigner(output, SUCCESS_TEXT);
                    output.setText(commandField.getText());
                    setupOutput(output);

                    textAreaDesigner(output, SUCCESS_TEXT);
                    output.setText("Authentication successful. Please type a command or /help.");
                    setupOutput(output);

                    updateGUI();

                    twitHandler.storeAccessToken(accessToken);

                } catch (TwitterException e) {
                    e.printStackTrace();
                    textAreaDesigner(output, ERROR_TEXT);
                    setupOutput(output);
                }
            }

            else if(commandField.getText().contains("/tweet")) {
                String regex = "\\/*\\btweet\\b\\s*";
                String newString = commandField.getText().replaceAll(regex, "");
                System.out.println(newString);

                try {
                    Status status = twitter.updateStatus(newString);
                    textAreaDesigner(output, SUCCESS_TEXT);
                    output.setText(commandField.getText());
                    setupOutput(output);

                    textAreaDesigner(output, UPDATE_TEXT);
                    output.setText("Updated status: " + status.getText());
                    setupOutput(output);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    textAreaDesigner(output, ERROR_TEXT);
                    output.setText("Something went wrong...");
                    setupOutput(output);
                }

                updateGUI();
            }

            else if(commandField.getText().contains("/get tweets")) {
                String regex = "\\/*\\bget\\b\\s*\\btweets\\b\\s*";
                String newString = commandField.getText().replaceAll(regex, "");
                System.out.println(newString);

                try {
                    java.util.List<Status> statuses = twitter.getHomeTimeline();
                    int i = 0;
                    for(Status status : statuses) {
                        i++;
                        if(i % 2 == 0) {
                            textAreaDesigner(output, SUCCESS_TEXT);
                        } else {
                            textAreaDesigner(output, UPDATE_TEXT);
                        }
                        output.setText("@" + status.getUser().getName() + " : " + status.getText());
                        setupOutput(output);
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                updateGUI();
            }

            else if(commandField.getText().contains("/search")) {
                String regex = "\\/*\\bsearch\\b\\s*";
                String newString = commandField.getText().replaceAll(regex, "");
                System.out.println(newString);

                Query query = new Query(newString);
                try {
                    QueryResult result = twitter.search(query);
                    int i = 0;
                    for(Status status : result.getTweets()) {
                        i++;
                        if(i % 2 == 0) {
                            textAreaDesigner(output, SUCCESS_TEXT);
                        } else {
                            textAreaDesigner(output, UPDATE_TEXT);
                        }
                        output.setText("@" + status.getUser().getScreenName() + " : " + status.getText());
                        setupOutput(output);
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                updateGUI();

            }

            else {
                textAreaDesigner(output, ERROR_TEXT);
                output.setText("'" + commandField.getText() + "' is not a valid command. Please verify again or type /help for a list of commands. :)");
                setupOutput(output);
                updateGUI();
            }
        }
    }
}
