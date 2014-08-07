package com.joshrincon.jtwittercli;

import twitter4j.*;
import twitter4j.auth.AccessToken;

/**
 * Created by on 8/6/2014.
 */
public class TwitHandler {

    private String[] keywords = {"coupon", "promo", "discount", "fucking", "fuck"};

    //TODO: don't forget to remove these
    //Twitter App's Consumer Key
    private String consumerKey = "xxx";

    //Twitter App's Consumer Secret
    private String consumerSecret = "xxx";
    //Twitter Access Token
    private String accessToken;
    //Twitter Access Token Secret
    private String accessTokenSecret;

    private TwitterStreamFactory twitterStreamFactory;
    private TwitterStream twitterStream;
    private Twitter twitter;

    long userId;


    public TwitHandler() throws Exception {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    public void setPermissions() {

    }

    public void storeAccessToken(long userId, AccessToken accessToken) {
        this.userId = userId;
        this.accessToken = accessToken.getToken();
        this.accessTokenSecret = accessToken.getTokenSecret();
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

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public TwitterStream getTwitterStream() {
        return twitterStream;
    }

    public void setTwitterStream(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

}
