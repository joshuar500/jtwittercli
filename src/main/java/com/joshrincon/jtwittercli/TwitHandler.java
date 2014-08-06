package com.joshrincon.jtwittercli;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by on 8/6/2014.
 */
public class TwitHandler {

    String[] keywords = {"coupon", "promo", "discount", "fucking", "fuck"};

    //Twitter App's Consumer Key
    String consumerKey = "xxx";
    //Twitter App's Consumer Secret
    String consumerSecret = "xxx";
    //Twitter Access Token
    String accessToken = "xxx";
    //witter Access Token Secret
    String accessTokenSecret = "xxx";
    TwitterStreamFactory twitterStreamFactory;
    TwitterStream twitterStream;


    public TwitHandler() throws Exception {

        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessTokenUser= null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(null == accessTokenUser) {
            System.out.println("Open the following URL and grant access to your account: ");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.println("Enter the PIN (if available) or just hit enter. [PIN]:");
            String pin = reader.readLine();

            try {
                if(pin.length() > 0) {
                    accessTokenUser = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessTokenUser = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }

        storeAccessToken(twitter.verifyCredentials().getId(), accessTokenUser);
        Status status = twitter.updateStatus("testinggg");
        System.out.println("Successfully updated the status to" + status);

    }

    public void storeAccessToken(long useId, AccessToken accessToken) {
        accessToken.getToken();
        accessToken.getTokenSecret();
    }

    public void setupStreamOfTweets() {

        //TODO: set a flag TRUE if there is an instance of this already

        //Instantiate a re-usable and thread-safe factory
        twitterStreamFactory = new TwitterStreamFactory();
        //Instantiate a new Twitter instance
        twitterStream = twitterStreamFactory.getInstance();
        //setup OAuth Consumer Credentials
        twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
        //setup OAuth Access Token
        twitterStream.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

    }

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
    }

}
