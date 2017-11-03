package com.takeaway.assignment.gameofthree.util;

/**
 * 
 * @author vaibhav
 * Class containing all the utilities method of the application
 */
public class ApplicationUtils {
	
	
	/**
	 * The method processes the number received, prints appropriate messages
	 * and returns the next number to be sent to another player
	 * 
	 * @param conn - WebSocket connection
	 * @param message - number received
	 * @return - next number to be sent
	 */
	public static String processNextNumber(String message) {
		
		if (message.equals(Constants.DONE_KEYWORD)) {
			System.out.println(Constants.GAME_OVER);
			return Constants.SUCCESS_STR;
		}

		int current = Integer.parseInt(message);

		System.out.println("Received number: " + current);

		if (current == 1) {
			System.out.println(Constants.WINNER);
			return Constants.DONE_KEYWORD;
		} else if (current <= 0) {
			System.out.println(Constants.ERROR);
			return Constants.FAILURE_STR;
		}
		
		String operation = ApplicationUtils.getOperationPerformed(current);
		System.out.println(operation);
		
		String next = Long.toString(Math.round(((double) current) / 3));
		System.out.println("Sending number: " + next);
		
		return next;
	}
	
	/**
	 * Returns the operation performed on the number received by the player
	 * @param current
	 */
	public static String getOperationPerformed(int current) {
		
		String operation = "";
		if((current+1)%3 == 0){
			operation = "Added 1";
		} else if((current-1)%3==0){
			operation = "Subtracted 1";
		} else if(current%3==0){
			operation = "Added 0";
		}
		
		return operation;
	}


}
