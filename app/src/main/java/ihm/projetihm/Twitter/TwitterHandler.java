package ihm.projetihm.Twitter;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ihm.projetihm.Database.DBHelper;
import ihm.projetihm.Database.QueryBuilder;
import ihm.projetihm.MainActivity;
import ihm.projetihm.R;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler extends AsyncTask<Void, Void, Void> {

    private final static String SUBJECT = "#CapSophia";
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivity context;


    public TwitterHandler(MainActivity context, SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("3kPXeUW6mGudUZcj8oTq2XFCb")
                .setOAuthConsumerSecret("J4oRA2zgCcfSilGkFiq7rDxBUaRvNtTAFhv6SXtp3EEwH7QPd5")
                .setOAuthAccessToken("861952785218232321-NH3VUMVLlLQ1Anb0xC9XOEYvPYbtGjU")
                .setOAuthAccessTokenSecret("PG95rtf9EgnT5ogAD54TSVyNbw2ngNyQGENjrxYxwlXvU");
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        Query query = new Query(SUBJECT);
        List<twitter4j.Status> tweets = new ArrayList<>();
        try {
            query.setCount(100);
            QueryResult result = twitter.search(query);
            tweets.addAll(result.getTweets());
            Log.d("TWEETSGATHERED", String.valueOf(tweets.size()));

        } catch (TwitterException te) {
            Log.w("TWITTEREXCEPT", te);
        }
        for (int i = 0; i < tweets.size(); i++) {
            twitter4j.Status t = tweets.get(i);
            String user = t.getUser().getScreenName();
            String title = context.getString(R.string.tweet_from) + " " + user;
            String msg = t.getText();
            String img = t.getUser().getOriginalProfileImageURL();
            long date = t.getCreatedAt().getTime();
            DBHelper db = DBHelper.getInstance(context);
            long author = db.getAuthorId(user);
            db.execWriteQuery(new QueryBuilder().buildTweet(title, author, img, msg, date));

        }
        return null;
    }

    @Override
    public void onPostExecute(Void params) {
        context.switchContent();
        swipeRefreshLayout.setRefreshing(false);
    }
}

