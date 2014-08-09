package com.joshrincon.jtwittercli;

import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.io.*;
import java.util.ArrayList;

/*
    Class that handles some of the Twitter stuff
    A lot of other methods are used in ConsolePanel
    which needs to come into this class (keylisteners?).

    fileExists()
    storeAccessToken(AccessToken)
    loadAccessToken

    other methods have not yet been implemented or
    are self explanatory
*/


public class TwitterHandler {

    // TODO: don't forget to remove these

    // Twitter Access Token
    private String accessToken;
    // Twitter Access Token Secret
    private String accessTokenSecret;

    // Set up variable to be used throughout application
    private TwitterStream twitterStream;
    private Twitter twitter;

    String fileName = "tempTokens";


    public TwitterHandler() throws Exception {
        twitter = TwitterFactory.getSingleton();
        //twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    public boolean fileExists() {
        String property = "java.io.tmpdir";
        String tempDir = System.getProperty(property);
        File tempFile = new File(tempDir + fileName);
        return tempFile.exists();
    }

    public void storeAccessToken(AccessToken accessToken) {
        BufferedWriter bufferedWriter = null;
        try {
            this.accessToken = accessToken.getToken();
            this.accessTokenSecret = accessToken.getTokenSecret();

            String property = "java.io.tmpdir";
            String tempDir = System.getProperty(property);
            File tempFile = new File(tempDir + fileName);

            if(!tempFile.exists()) {
                bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
                bufferedWriter.write(this.accessToken);
                bufferedWriter.newLine();
                bufferedWriter.write(this.accessTokenSecret);
            } else {
                System.out.println("Something went wrong, file not written");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedWriter != null) try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AccessToken retrieveAccessToken() throws IOException {

        String property = "java.io.tmpdir";
        String tempDir = System.getProperty(property);
        File tempFile = new File(tempDir + fileName);

        if (tempFile.exists()) {
            String strLine;
            ArrayList<String> lines = new ArrayList<>(2);
            try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
                while ((strLine = br.readLine()) != null) {
                    lines.add(strLine);
                }
                accessToken = lines.get(0);
                accessTokenSecret = lines.get(1);
                System.out.println(this.accessToken + " " + this.accessTokenSecret);
                return new AccessToken(this.accessToken, this.accessTokenSecret);
            }

        } else {
            System.out.println("File could not be loaded.");
            return null;
        }
    }

    public void authStreamOfTweets() {

        //TODO: set a flag TRUE if there is an instance of this already

        //Instantiate a re-usable and thread-safe factory
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory();
        //Instantiate a new Twitter instance
        twitterStream = twitterStreamFactory.getInstance();
        //setup OAuth Consumer Credentials
        //twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
        //setup OAuth Access Token
        twitterStream.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

    }
/*
    public void getStreamOfTweets() {
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {

                //loop through keywords
                for(int i = 0; i < keywords.length-1; i++) {
                    if(status.getText().contains(keywords[i])){
                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                    }
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id: " + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int i) {
                System.out.println("Got track limitation notice: " + i);
            }

            @Override
            public void onScrubGeo(long l, long l2) {
                System.out.println("Got scrub_geo event userId: " + l + " upToStatusId: " + l2);
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {
                System.out.println("Got a stall warning: " + stallWarning);
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        };

        twitterStream.addListener(listener);
        twitterStream.sample();
    }*/

    public Twitter getTwitter() {
        return twitter;
    }
}
