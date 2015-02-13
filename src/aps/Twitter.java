package aps;

import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter {
	
	// Tokens for identification
	private String consumerKeyStr = "HlRwUgUaBmWfJsNzlHqIgC7Fp";
	private String consumerSecretStr = "SfXOv6mJhCDdJ6dGqb2cU4kSfuOwXArh54NsBnrRazlVratcFg";
	private String app_twitter_key = "2317121491-iiJ45yU2RMuro5uL3lm52MIvXSkCrQ2Ix8hJFni";
	private String app_twitter_secret = "vlkCMFkgS8ujG7bXriCUgbxR9ag05Doseo3RP0IDEF3FM";
	
	private twitter4j.Twitter twitter;

	public Twitter () {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerKeyStr)
		  .setOAuthConsumerSecret(consumerSecretStr)
		  .setOAuthAccessToken(app_twitter_key)
		  .setOAuthAccessTokenSecret(app_twitter_secret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}
	
	public void getTimeline () throws TwitterException {
		
		System.out.println("TWITTER \nGet timeline : \n");
		List<Status> timeline = twitter.getHomeTimeline();
		for (int i=0;i<timeline.size();i++){
			Status post = timeline.get(i);
			System.out.println("id = " + post.getId());
			System.out.println("text = " + post.getText());
			System.out.println("source = " + post.getSource());
			System.out.println("name = " + post.getUser().getScreenName()+"\n");
		}
	}
	
	
	public void getFriendsAndFollowers () throws TwitterException {
		
		long id_twitter = twitter.getId();
		long cursor = -1;
									
		System.out.println("Get Friends : \n");
		PagableResponseList<User> friends = twitter.getFriendsList(id_twitter, cursor);
		for (int i=0;i<friends.size();i++){
			User friend = friends.get(i);
			System.out.println("id = " + friend.getId());
			System.out.println("name = " + friend.getName());
			System.out.println("screen name = " + friend.getScreenName()+"\n");
		}
									
		System.out.println("Get Followers : \n");
		PagableResponseList<User> followers = twitter.getFriendsList(id_twitter, cursor);
		for (int i=0;i<followers.size();i++){
			User follower = followers.get(i);
			System.out.println("id = " + follower.getId());
			System.out.println("name = " + follower.getName());
			System.out.println("screen name = " + follower.getScreenName()+"\n");
		}
	}
	
}
