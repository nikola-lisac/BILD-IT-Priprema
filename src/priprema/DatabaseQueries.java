package priprema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DatabaseQueries {


	/**
	 * Method which connects with the database, executes defined query and
	 * prints results
	 * 
	 * @param query
	 *            defined query to execute on the datebase
	 * @param args
	 *            list of columns to display with the results
	 */
	public static void databaseOutput(String query, String... args) {
		try {
			// connection with the datebase
			Connection myCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "nikola");

			// statement
			Statement myStat = myCon.createStatement();

			// query
			ResultSet myResult = myStat.executeQuery(query);

			// print results
			while (myResult.next()) {
				// print fields from table depending on number of columns that need to be displayed
				for(int i = 0;i<args.length;i++){
					System.out.print(String.format("%-20s ",myResult.getString(args[i])));
				}
				System.out.println();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Protected input of an integer The number must be in range of number of
	 * options When input is valid, method returns the number
	 * 
	 * @return number
	 */
	public static int inputOptionNumber() {
		Scanner input = null;
		boolean isOk = false; // loop control variable
		int number = 0; // variable which will store user's input

		// loops until user enters an integer
		while (!isOk) {
			input = new Scanner(System.in);
			isOk = true;

			try {
				number = input.nextInt(); // users input
				if(number<0 || number>5){
					isOk = false;
				}

			} catch (InputMismatchException e) { // if user enters something
													// other than integer
				System.out.println("Wrong input, try again: ");
				input.nextLine();
				isOk = false;
			}
		}

		// return entered integer
		return number;

	}

	/**
	 * Protected input of a letter Method returns the value of the letter when
	 * input is valid Valid input is any letter of the alphabet
	 * 
	 * @return letter
	 */
	public static char inputChar() {
		Scanner input = null;
		char character = ' ';
		try {

			input = new Scanner(System.in);
			boolean isOk = false;
			while (!isOk) {
				isOk = true;
				// user enters a char
				character = input.next().charAt(0);
				character = Character.toUpperCase(character);
				// if the char isn't a letter, ask user to try again
				if (character < 'A' || character > 'Z') {
					System.out.println("Try again: ");
					isOk = false;
				}
			}
		} catch (Exception e) {
			System.out.println("Wrong input, try again: ");
			inputChar();
		}

		return character;
	}
	
	/**
	 * Getting input string from scanner
	 * 
	 * @return entered string
	 */
	public static String inputNextLine() {
		Scanner input = null;
		String inputString = "";
		try {
		input = new Scanner(System.in);
		 inputString = input.nextLine();
		} catch (Exception a){
			System.out.println("Wrong input, try again: ");
			inputNextLine();
		}
		return inputString;

	}

	public static void main(String[] args) {

		// print list of options
		System.out.println("[1] List of 20 most populated countries");
		System.out.println("[2] Number of countries on every continent");
		System.out.println("[3] List of countries that start with specific letter");
		System.out.println("[4] List of cities that start with specific letter");
		System.out.println("[5] View informations about specific country");
		System.out.println("\n[0] Exit");
		System.out.println("Choose an option by entering the number: ");
		// user select an option by entering a number
		int optionNumber = inputOptionNumber();
		String query = ""; // string to hold the query instruction

		// depanding on the entered number, call database method with specific
		// arguments
		switch (optionNumber) {

		case 1: { // print name and the continent of the countries with the largest population
			// sql query instruction
			query = "SELECT Name, Continent, Population FROM country ORDER BY Population DESC LIMIT 20;";
			// print header
			System.out.println(String.format("%-20s %-20s %s", "Country", "Continent", "Population"));
			System.out.println("---------------------------------------------------------------");
			// execute query on the datebase
			databaseOutput(query, "Name", "Continent", "Population");
			break;
		}

		case 2: { // print number of countries on every continent
			// query instruction
			query = "SELECT Continent, COUNT(*) FROM country GROUP BY Continent;";
			// print header
			System.out.println(String.format("%-20s %s", "Continent", "Countries"));
			System.out.println("-------------------------------------");
			// execute query
			databaseOutput(query, "Continent", "COUNT(*)");
			break;

		}

		case 3: { // print all countries which start with specified letter
			System.out.println("Enter the starting letter: ");
			// user enters the starting letter
			char character = inputChar();
			// print header
			System.out.println(String.format("%-20s %s", "Name", "Code"));
			System.out.println("-------------------------------------");

			// query instruction
			query = "SELECT Code,Name FROM country WHERE Name LIKE '" + character + "%'";
			// execute query
			databaseOutput(query, "Code", "Name");
			break;
		}

		case 4: { // print all cities which start with specified letter
			System.out.println("Enter the starting letter: ");
			// user enters the starting letter
			char character = inputChar();
			// print header
			System.out.println(String.format("%-20s %s", "Country Code", "Name"));
			System.out.println("---------------------------------------------");
			// query
			query = "SELECT CountryCode,Name FROM city WHERE Name LIKE '" + character + "%'";
			// execute query
			databaseOutput(query, "CountryCode", "Name");
			break;
		}
		
		case 5: { // view informations about entered country
			System.out.println("Enter the country name: ");
			// user enters a country
			String country = inputNextLine();
			// get Code,Name,Region,SurfaceArea,Population for entered country from database
			query = "SELECT Code,Name,Region,SurfaceArea,Population FROM country WHERE Name = '"+country+"';";
			System.out.println(String.format("%-20s %-20s %-20s %-20s %-20s","Code","Name","Region","SurfaceArea","Population"));
			System.out.println("-----------------------------------------------------------------------------------------------");
			// execute query
			databaseOutput(query, "Code","Name","Region","SurfaceArea","Population");
			break;
		}
		
		// exit the program
		default: {
			System.out.println("Bye!");
			System.exit(0);
		}
		}
		
	}

}
