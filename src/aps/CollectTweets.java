package aps;

import java.sql.SQLException;

import twitter4j.TwitterException;

public class CollectTweets extends Thread {

	public CollectTweets () {
		this.start() ;
	}
	
	
	public void run () {   
		Twitter twitter = new Twitter();
		long timeRemaining=0;

		try {
			timeRemaining = twitter.rateLimit();
			if(timeRemaining==0){
				twitter.collectTweets();
			}
			else {
				System.out.println("CollectTweets sleeping");
				System.out.println("Time remaining = " + (timeRemaining * 1.66666667) / 100000 + " minutes");
				CollectTweets.sleep(timeRemaining);
				System.out.println("CollectTweets awaken");
				twitter.collectTweets();
			}
		} catch (TwitterException | SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String error = e.getMessage();
			System.out.println(error.contains("code - 88"));
			System.out.println(e.getLocalizedMessage());
		}
		
		this.run();
	}
}
