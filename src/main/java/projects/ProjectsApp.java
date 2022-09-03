package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	
	private ProjectService projectService = new ProjectService(); 
	private Scanner sc = new Scanner(System.in);

	// @formatter:off
	// creates the list for options the user can select from
			private List<String> operations = List.of(
					"1) Add a project"
					);
			// @formatter:on

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}
	
	/*
	 * decides whether the user input is valid, 
	 * or if they want to terminate the app
	 */
	private void processUserSelections() {
		boolean done = false;
		
		while (!done) {
			try {
				int selection = getUserSelection();
				
				switch (selection) { 
				case -1:
					done = exitMenu();
					break;	
				case 1:
					createProject();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid"
							+ " selection, please try again.");
			}
				
			} catch (Exception e) {
				System.out.println("\nError: " + e + ": Please enter a valid number.");
			}
		}

	}
	

	// method to terminate app
	private boolean exitMenu() {
	System.out.println("Exiting the menu. Goodbye.");
		return true;
	}
	
	/*
	 * method that is called within switch of getUserSelection, that creates
	 * project table
	 */
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated length of project, in hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual length of the project, in hours");
		Integer difficulty = getIntInput("Enter the difficulty level (1-5)");
		String notes = getStringInput("Enter any projects notes you may have");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}


	/*
	 * method that requests int input from user, then checks to see
	 *  if input is null
	 */
	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
	}

	/*
	 * method that converts string input to integer, 
	 * if invalid throws numberformatexception
	 */
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number. Try again");
		}
	}

	// method to convert decimal input to BigDecimal
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}
	
		// lowest level input, just recieves input from user
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = sc.nextLine();
		return input.isBlank() ? null : input.trim();
	}

	// method that prints choices for user to choose from
	private void printOperations() {
		System.out.println("\nThese are the available selections. " + "Press the Enter key to quit: ");
		operations.forEach(line -> System.out.println("   " + line));

	}

}
