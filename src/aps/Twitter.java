package aps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private PostgresHelper client;
	private long id_twitter;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Twitter () {
		
		// Connection to DataBase
		client = new PostgresHelper(
				DbContract.HOST, 
				DbContract.DB_NAME,
				DbContract.USERNAME,
				DbContract.PASSWORD);	
		try {
			if (client.connect()) {
				System.out.println("DB connected");
			}	
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		// Getting an instance of Twitter4J
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerKeyStr)
		  .setOAuthConsumerSecret(consumerSecretStr)
		  .setOAuthAccessToken(app_twitter_key)
		  .setOAuthAccessTokenSecret(app_twitter_secret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}
	
	
	
	public void getTimeline () throws TwitterException, SQLException {
		
		id_twitter = twitter.getId();
		User user = twitter.showUser(id_twitter);	
		List<Status> timeline = getTimeline(user);
		// Store posts in Database
		insertTwitterStatuses(timeline);
	}
	
	
	public void main () throws TwitterException, SQLException {
		
		id_twitter = twitter.getId();
		User user = twitter.showUser(id_twitter);
		
		// Store main user
		insertTwitterUser(user);
	
		// Get friends and store in database
		PagableResponseList<User> friends = getFriends(user);
		insertTwitterFriendUsers(friends);	
		
		// Get Followers and store in database
		PagableResponseList<User> followers = getFollowers(user);
		insertTwitterFollowingUsers(followers);
		
		for (int i=0;i<followers.size();i++){
			User follower = followers.get(i);
			List<Status> timeline = getTimeline(follower);
			// Store posts in Database
			insertTwitterStatuses(timeline);

//			PagableResponseList<User> friendsOfFollowers = getFriends(follower);
//			insertTwitterFriendUsers(friendsOfFollowers);
//
//			PagableResponseList<User> followerOfFollowers = getFollowers(follower);
//			insertTwitterFollowingUsers(followerOfFollowers);

		}
	}
	
	
	public List<Status> getTimeline (User user) throws TwitterException {
		
		List<Status> timeline = twitter.getUserTimeline(user.getId());
		return(timeline);
	}
	
	
	public PagableResponseList<User> getFriends (User user) throws TwitterException{
		
		id_twitter = user.getId();
		long cursor = -1;
		PagableResponseList<User> friends = twitter.getFriendsList(id_twitter, cursor);
		return(friends);
	}
	
	
	public PagableResponseList<User> getFollowers (User user) throws TwitterException {
		
		id_twitter = user.getId();
		long cursor = -1;
		PagableResponseList<User> followers = twitter.getFollowersList(id_twitter, cursor);
		return(followers);
	}
	
	
	public void insertTwitterUser(User user) throws SQLException {
		List<Long> idUsers = selectTwitterUsers();
		
		if(!idUsers.contains(user.getId())) {
			// Store user in database
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("user_id", null);
			map.put("name", user.getName());
			map.put("profile_image_url", user.getProfileImageURL());
			map.put("created_at", user.getCreatedAt().toString());
			map.put("location", user.getLocation());
			map.put("favourites_count", user.getFavouritesCount());
			map.put("listed_count", user.getListedCount());
			map.put("followers_count", user.getFollowersCount());
			map.put("verified", false);
			map.put("geo_enabled", false);
			map.put("time_zone", user.getTimeZone());
			map.put("description", user.getDescription());
			map.put("statuses_count", user.getStatusesCount());
			map.put("friends_count", user.getFriendsCount());
			map.put("following", null);
			map.put("screen_name", user.getScreenName());
			map.put("created", user.getCreatedAt().toString());
			map.put("modified", null);
			if (client.insert("twitter_users", map) == 1) {
				System.out.println("Record added");
			}
		}
	}
	
	
	public void insertTwitterStatuses(List<Status> timeline) throws SQLException {
		List<Long> idStatuses = selectTwitterStatuses();
		
		for (int i=0;i<timeline.size();i++){
			Status post = timeline.get(i);
			
			if(!idStatuses.contains(post.getId())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", post.getId());
				map.put("user_id", post.getUser().getId());
				map.put("created_at", post.getCreatedAt().toString());
				map.put("in_reply_to_user_id", post.getInReplyToUserId());
				map.put("retweet_count", post.getRetweetCount());
				map.put("in_reply_to_status_id", post.getInReplyToStatusId());
				map.put("text_", post.getText().replace("'"," "));
				map.put("in_reply_to_screen_name", post.getInReplyToScreenName());
				map.put("place", post.getPlace());
				map.put("source", post.getSource());
				map.put("full_content", null);
				map.put("created", post.getCreatedAt().toString());
				map.put("modified", null);
				if (client.insert("twitter_statuses", map) == 1) {
					System.out.println("Record added");
				}
			}
		}
	}
	
	
	public void insertTwitterFollowingUsers(PagableResponseList<User> list) throws SQLException {
		
		java.util.Date date= new java.util.Date();
		List<Map<String, Object>> relationships = selectTwitterRelationships();
		 
		for (int i=0;i<list.size();i++){
			User user = list.get(i);
			insertTwitterUser(user);
			
			// Store relationship in database
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", user.getId());
			map.put("friend", id_twitter);
			map.put("still_friend", true);
			map.put("created", format.format(date));
			map.put("modified", null);

			if(!testIfRelationshipAlreadyExist(relationships,map)){
				if (client.insert("twitter_friendships", map) == 1) {
					System.out.println("Record added");
				}
			}
		}
	}
	
	
	public void insertTwitterFriendUsers(PagableResponseList<User> list) throws SQLException {
		
		java.util.Date date= new java.util.Date();
		List<Map<String, Object>> relationships = selectTwitterRelationships();
		
		
		for (int i=0;i<list.size();i++){
			User user = list.get(i);
			insertTwitterUser(user);
			
			// Store relationship in database if not already exist
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", id_twitter);
			map.put("friend", user.getId());
			map.put("still_friend", true);
			map.put("created", format.format(date));
			map.put("modified", null);
			
			if(!testIfRelationshipAlreadyExist(relationships,map)){
				if (client.insert("twitter_friendships", map) == 1) {
					System.out.println("Record added");
				}
			}
		}
	}
	
	
	public List<Long> selectTwitterUsers() throws SQLException {
		ResultSet rs = client.execQuery("SELECT id FROM twitter_users");
		List<Long> liste = new ArrayList<Long>();
		while(rs.next()) {
			liste.add(rs.getLong(1));
		}
		return liste;
	}
	
	public List<Long> selectTwitterStatuses() throws SQLException {
		ResultSet rs = client.execQuery("SELECT id FROM twitter_statuses");
		List<Long> liste = new ArrayList<Long>();
		while(rs.next()) {
			liste.add(rs.getLong(1));
		}
		return liste;
	}
	
	public List<Map<String, Object>> selectTwitterRelationships() throws SQLException {
		ResultSet rs = client.execQuery("SELECT * FROM twitter_friendships");
		List<Map<String, Object>> liste = new ArrayList<Map<String, Object>>();
		while(rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", rs.getLong(1));
			map.put("friend", rs.getLong(2));
			map.put("still_friend", rs.getBoolean(3));
			map.put("created", rs.getTimestamp(4));
			map.put("modified", rs.getTimestamp(5));
			liste.add(map);
		}
		return liste;
	}
	
	public boolean testIfRelationshipAlreadyExist(List<Map<String, Object>> relationships, Map<String, Object> map){
		boolean res=false;
		for (int i=0;i<relationships.size();i++){
			Map<String, Object> relation = relationships.get(i);
			if(relation.get("user_id").equals(map.get("user_id")) && relation.get("friend").equals(map.get("friend"))){
				res=true;
			}
		}
		return res;
	}
	
	
}
