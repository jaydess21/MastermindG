
/**
 * @author Sahara Aracki-Maurer, Grace Hechavarria, and Jayde Shaw
 * 
 * MastermindGame.java
 * 
 * This program will prompt a user with a menu choice and it will display
 * rules of the game, it will play the mastermind game with the user, and
 * it will store the highest score in a file and display it when option
 * is chosen in the menu
 * 
 * Input:           user's menu choice, user's guess for the codebreaking, user's name
 * 
 * Processing:      1. Display Menu Choice
 *                      "1) See Rules"
 *                      "2) Play Game"
 *                      "3) See Highest Score"
 *                      "4) Quit"
 *                  2. Process input
 *                      -if choice is 1
 *                          1. Display rules
 *                      -if choice is 2
 *                          1. Prompt for user's name
 *                          2. Prompt for code
 *                          3. Compare to master code
 *                          4. If the code it not broken show colors and pins used
 *                             and prompt the player for another guess
 *                          5. If the code is broken prompt check if there is a new highscore
 *                          	- Create highestScore.txt if does not exist
 *                          	- Read file for name and highest score
 *                          	- Evaluate which highest score is greater 
 *                          	- Replace highest score
 *                          6. Prompt to play again once the code is broken or after 10 tries
 *                      -if choice is 3
 *                          if there is not a highestScore.txt file
 *                              1. Display "there is no highscore"
 *                          if there the highestScore.txt file exists
 *                              2. Display name and highest score from file
 *                      -if choice is 4
 *                          1. Display Outro
 *                          2. Quit the game
 * 
 * Output:         if choice 1
 *                      -Output rules
 *                 if choice 2
 *                 		-Output previous colors and pins
 *                      -Output win or lose
 *                 if choice 3
 *                      -Output high score
 *                 if choice 4
 *                      -Output Outro
 */


// Import Classes
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.*;
import java.util.Random;
import java.util.Arrays;


public class mastermindGame {
	
	//Scanner object for standard input
	static Scanner stdIn = new Scanner(System.in);
	
	//Declare and initialize constants and global variables / arrays
	static final int TRIES = 10;
	static final int CODE = 3;
	static final String[] COLORS = {"R", "B", "W", "Y", "G"};
	static String [][] guesses = new String [TRIES][CODE];
	static String [][] hints = new String [TRIES][CODE];
	
	public static void main(String[] args) throws IOException
	{
		// Declare and initialize local Variables
		int inGameScore;
		int choice = 0;
		int guessNum = 0; //to keep track of the guess
		String[] masterCode = new String[CODE];
		boolean brokeCode = false;
		boolean playAgain = true;
		String playerName;
		
		// Intro
		System.out.println("\nWelcome to the Mastermind Game!");
		do {
			// Display menu and prompt user to choose an option
			choice = gameChoice();
			
            switch(choice){
                case 1:
                	//display game rules
                    gameRules();
                    break;

                case 2:
                	//play game
                    System.out.println("\nPlay Game ...");
                    
                    //reset playAgain
                    playAgain = true;
                    
                    //while playAgain == true 
                    while(playAgain == true) {                    	
                    	
                    	//reset variables
                    	guessNum = 0;
                    	inGameScore = 0;
                    	brokeCode = false;
                    	
                    	//reset guesses and hints array
                    	guesses = new String[TRIES][CODE];
                    	hints = new String[TRIES][CODE];
                    	
                    	//generate random code
                    	masterCode = generateMasterCode();
		          	
	                    //prompt for player name
	                    playerName = promptName();
                    	
                    	//while guessNum < 10
                    	while(guessNum < 10) {               
                    		
	                    	//prompt code guess
                    		promptCodeGuess(guesses, guessNum);                    		

                    		//check if broke code or hints
                    		brokeCode = checkCode(guesses, masterCode, guessNum);
                    		
                    		//if broke the code
                    		if (brokeCode == true) {    
                    			System.out.println("\nCongratulations, you win! (*^-^*)");
                    			//see if new highscore
                    			highscoreFileCheck(playerName, inGameScore+1);
                    			//exit the game
                    			break;
                    		}
                    		
	                    	//add one to inGameScore
                    		inGameScore ++;
                    		//add one to guessNum
                    		guessNum ++;
                    		
                    	}
                    	//if code was not broken / tries ran out  
                    	if(brokeCode == false)
                    		System.out.println("\nSorry, you lose! <('.'<)");
                    	
                    	//prompt play again
                    	playAgain = promptPlayAgain();
                    }
                    break;

                case 3:
                	//show game high score
                    System.out.println("\nGame High Score ...");
                    gameHighscore();
                    break;
                case 4:
                	//Outro
                	System.out.println("\nGood Bye ...");
                	System.exit(0);
                	break;

                default:
                	//Display menu choice error
                    System.out.println("Error...must be a choice between 1-4");
            }

		} while (choice != 4);
		
		// Close Scanner
		stdIn.close();
		
	}
	
/**
 * Displays menu and prompts the user for a menu choice
 * and returns the choice
 * @return - menu choice
 */
	static int gameChoice() 
	{
		//Declare Local variables
		int choice;
		//display menu
		System.out.println("\nPlease choose one of the following:");
		System.out.println("\t1) See Rules");
		System.out.println("\t2) Play Game");
		System.out.println("\t3) See Highest Score");
		System.out.println("\t4) Quit");
		System.out.print("Option: ");
		//prompt for choice
        choice = stdIn.nextInt();
       return choice;
	}

/**
 * Displays game rules
 */
	static void gameRules() 
	{
		//Display game rules
		System.out.println("\nMastermind Game Rules ...\n");
		System.out.println("Objective of the Game: ");
		System.out.println("In ten tries, guess the correct color and placement of pins in order to best the Mastermind");
		System.out.println("You have these options: R, B, W, Y, G\n");
		System.out.println("Computer Responses: R, W, or O");
		System.out.println("R = Right color and right placement");
		System.out.println("W = Right color but wrong placement");
		System.out.println("O = Wrong color and wrong placement\n");
		
		System.out.println("1. Guess color placements such as 'R G B'");
		System.out.println("2. Look at the Mastermind's pins to solve the code: 'R W O'");
		System.out.println("\nBeat the Mastermind in under 10 tries and you win!");
		System.out.println("The player with the least amount of tries has the highest score!!");
	}
	
/**
 * Reads highscore from the highscore.txt file if it exist
 * and displays it to the user
 * 
 * @throws IOException
 */
	static void gameHighscore() throws IOException
	{
		//Declare and initialize local variables
		String fileNameScore;
		int fileHighscore;
		
		//Open highscore.txt for input (reading)
		File scoreFile = new File("highscore.txt");
		
		//If file does not exist
		if(!scoreFile.exists())
		{
			//no highscore
			System.out.println("No current Highscore (0.0)...");
		} else {
			//compare score
			//Scanner object to read the File object
			Scanner inFile = new Scanner(scoreFile);
			//Read highscore data
			fileNameScore = inFile.nextLine();
			fileHighscore = inFile.nextInt();
			//close inFile
			inFile.close();
			//Display player and highscore
			System.out.println("\nPlayer: " + fileNameScore);
			System.out.println("Highscore: " + fileHighscore);
		}
	}

/**
 * Randomly generates the masterCode from the constant COLORS array
 * and returns it
 * @return - master code
 */
	static String[] generateMasterCode()
	{
		//random generator
		Random rand = new Random();
		//declare and initialize variables and arrays
		String[] code = new String[CODE];
		int colorCode;
		//randomly fill the code array with the COLORS array subscripts
		for(int i=0; i<CODE; i++)
		{
			colorCode = rand.nextInt(5);
			code[i]=COLORS[colorCode];
		}
		
		return code;
	}
	
/**
 * Prompts name of the player and returns it
 * 
 * @return - player name
 */
	static String promptName()
	{
		//Declare and initialize local variables
		String playerName;
		//Prompt for playerName
		System.out.print("\nEnter player name: ");
		playerName = stdIn.next();
		
		return playerName;		
	}
	
/**
 * Prompts code guess from user and validates
 * 
 * @param guessCode
 * @param guessNum
 */
	static void promptCodeGuess(String[][] guessCode, int guessNum)
	{
		//Declare and initialize local variables
		String guess;
		String letter;
		String[]code;
		boolean wrongColor = false;
		
		do {
			//Prompt for guess
			System.out.print("\nEnter code guess: ");
			guess = stdIn.next();
			
			//make the string guess all upppercase
			guess = guess.toUpperCase();
			
			//Initialize array size
			code = new String[guess.length()];
			
			//make guess an array called code
			for(int i = 0; i <guess.length(); i++) {
				letter = Character.toString(guess.charAt(i));
				code[i] = letter;
			}
			
			//check if only COLORS are used in the code array 
			for(int i = 0; i < code.length; i++) {
				if(!Arrays.asList(COLORS).contains(code[i])) {
					wrongColor = true;
					break;
				}else
					wrongColor = false;
			}
			
			//display error if code length is not three or code does not contain valid colors from COLORS array
			if(code.length < 3 || code.length > 3 || wrongColor == true)
				System.out.println("Error ... code must be 3 letters long and must contain R, B, W, Y, G. Try Again! :)");
			
		}while(code.length < 3 || code.length > 3 || wrongColor == true);
		
		//place code[] into the guessCode[][] array column of guessNum
		for (int i =0; i<CODE; i++)
		{
			guessCode[guessNum][i] = code[i];
		}
		
	}

/**
 * Displays hints if code was not broken and returns whether or not
 * the code was broken
 * 
 * @param guess
 * @param masterCode
 * @param row
 * @return - true if code was broke, false if code was not broken
 */
	static boolean checkCode(String[][] guess, String[] masterCode, int row)
	{
		//Declare and initialize local variables and arrays
		boolean codeBreak = false;
		int[] check = new int[CODE]; //1 true, 0 false
		String hintArray[] = new String[CODE];
		int checkArray = 0;
		String hint = "";
		String temp;
		
		
		for(int i=0; i<CODE; i++)
		{
			//add R if guess[row][i] is the right color and right place
			if((guess[row][i]).equals(masterCode[i])) {
				hint += "R ";
			//add W if guess[row][i] is the right color and wrong place
			}else if(Arrays.asList(masterCode).contains(guess[row][i])) {
				hint += "W ";
			//add O if guess[row][i] is the wrong color and wrong place
			}else if (!Arrays.asList(masterCode).contains(guess[row][i])) {
				hint += "O ";
			//else error
			}else {
				System.out.print("Error... ");
			}
		}
		

		//make hint an array
		hintArray = hint.split(" ");
		
		//rearrange hints alphabetically
		hintArray = Stream.of(hintArray).sorted().toArray(String[]::new);
		
		
		//Bubble sort hintArray to move O's to the end of the array
		for (int i = 0 ; i < hintArray.length ; i ++){
            for (int j = i+1; j < hintArray.length ; j ++){
            	//if hintArray[i] equals "O" and hintArray[j] does not equal "O"
                if ( hintArray[i].equals("O") && !hintArray[j].equals("O")){
                	//switch places
                    temp = hintArray[i];
                    hintArray[i] = hintArray[j];
                    hintArray[j] = temp;
                }
            }

        }
        for (int i = 0 ; i < hintArray.length ; i ++){
            for (int j = i+1; j < hintArray.length ; j ++){
            	//if hintArray[i] equals "O" and hintArray[j] equal "O"
                if ( hintArray[i].equals("O") && hintArray[j].equals("O")){
                	//switch places
                    temp = hintArray[i];
                    hintArray[i] = hintArray[j];
                    hintArray[j] = temp;
                }
            }

        }
		
		
		//place hintArray into the row in constant array hints[]
		for (int i=0; i<CODE; i++) {
			hints[row][i] = hintArray[i];
		}
		
		
		//check if guess subscript is equal to the subscript of mastercode
		for(int x=0; x<CODE; x++) {
			if ((guess[row][x]).equals(masterCode[x])) {
				check[x] = 1; //true
			}else {
				check[x] = 0; //false
			}
		}

		//check if any colors are not in the mastercode
		for(int i = 0; i<CODE; i++) {
			if(check[i] == 0) //0 = false
				checkArray ++;
		}
		
		//if there were no wrong colors user wins
		if(checkArray == 0) {
			codeBreak = true;
		}else { //else codeBreak is false
			codeBreak = false;
			
			//show previous color guesses and hints
			System.out.printf("\n %6s | %3s\n", "Colors", "Pins");
			System.out.println("-----------------");
			for(int r=0; r<row+1; r++)
			{
				System.out.printf("%2s", " ");
				
				for(int c=0; c<CODE; c++)
					//display guess
					System.out.printf("%1s ", guesses[r][c]);
				
				System.out.print("| ");
				
				for(int c=0; c<CODE; c++)
					//display hints
					System.out.printf("%1s ", hints[r][c]);
				
				System.out.println();
			}
				
		}
		
		return codeBreak;
	}
	
/**
 * Reads highschore.txt if exists and checks if there is a new
 * highscore. Also writes playerName and new highscore if it is
 * smaller than the previous highscore or the file does not exist.
 * 
 * @param playerName
 * @param score
 * @throws IOException
 */
	static void highscoreFileCheck(String playerName, int score)throws IOException
	{
		//Declare and initialize local variables
		String fileNameScore;
		int fileHighscore;
		
		//Open highscore.txt for input (reading)
		File scoreFile = new File("highscore.txt");
		
		//If file does not exist
		if(!scoreFile.exists())
		{
			//Open highscore.txt for output
			PrintWriter outFile = new PrintWriter("highscore.txt");
			//write new score to file
			outFile.println(playerName);
			outFile.println(score);
			outFile.close();
			//Display New Highscore
			System.out.println("New Highscore!! ~*0o0*~");
			
		}else {
			//compare score
			
			//Scanner object to read the File object
			Scanner inFile = new Scanner(scoreFile);
			
			//Read highscore data
			fileNameScore = inFile.nextLine();
			fileHighscore = inFile.nextInt();
			
			if(fileHighscore > score) {
				//Open highscore.txt for output
				PrintWriter outFile = new PrintWriter("highscore.txt");
				//write new score to file
				outFile.println(playerName);
				outFile.println(score);
				outFile.close();
				
				System.out.println("New Highscore!! ~*0o0*~");
			}else
				inFile.close(); //close file
		}
	}

/**
 * Prompts if the player wants to play again
 * and returns the boolean for the answer
 * 
 * @return - true if wants to play again, false if not
 */
	static boolean promptPlayAgain()
	{
		//Declare and initialize local variables
		boolean playAgain = true;
		int option = 0;
		
		//Flush the buffer
		stdIn.nextLine();
		
		do {
			//Prompt if the user wants to play the game
			System.out.println("\nDo you want to play again?");
			System.out.println("\t1) Yes");
			System.out.println("\t2) No");
			System.out.print("Option: ");
			option = stdIn.nextInt();
			
			//display error if input is not 1 or 2
			if (option < 1 || option > 2)
				System.out.println("Error ...must answer with either 1 or 2");
			else
				//if option is 1 then set playAgain to true
				if(option == 1)
					playAgain = true;
				//else is 2 and set playAgain to false
				else
					playAgain = false;
			
		}while(option < 1 || option > 2);
		
		return playAgain;
	}
}