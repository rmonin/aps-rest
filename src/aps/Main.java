package aps;

public class Main {

	/**
	 * 
	 * APS Server
	 * 
	 * Collect Facebook and Twitter informations
	 * Store users, informations, posts and statuses in DataBase  
	 *
	 */
	    
	public static void main(String[] args) {
		
		CollectFeed collectFeed = new CollectFeed();
		CollectTweets collectTweets = new CollectTweets();
		ClassifyTweets classifyTweets = new ClassifyTweets();
		ClassifyFeed classifyFeed = new ClassifyFeed();
	}

}
