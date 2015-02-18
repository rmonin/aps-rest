package aps;

import java.sql.SQLException;

import twitter4j.TwitterException;

public class CollectTweets extends Thread {

	public CollectTweets () {
		this.start() ;
	}
	
	
	public void run () {   
		Twitter twitter = new Twitter();
//		try {
//			twitter.getTimeline();
//		} catch (TwitterException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			twitter.main();
		} catch (TwitterException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
