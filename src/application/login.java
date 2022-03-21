package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage; 
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class login extends Application{
	
	private TextField tfUsername = new TextField();
	private TextField tfPassword = new TextField();
	private Button btnConfirm = new Button ("Confirm");
	private Button btnCancel = new Button ("Cancel");
	
	
	
	@Override
	public void start(Stage loginStage) {
		
		GridPane loginGrid = new GridPane();
		loginGrid.setHgap(4);
		loginGrid.setVgap(4);
		//loginGrid.add(new Label("User Login"), 0, 0);
		loginGrid.add(new Label("Username"), 0, 0);
		loginGrid.add(tfUsername, 1, 0);
		loginGrid.add(new Label("Password"), 0, 1);
		loginGrid.add(tfPassword, 1, 1);
		loginGrid.add(btnCancel, 1, 2);
		loginGrid.add(btnConfirm, 2, 2);
		
		//Set properties for UI
		loginGrid.setAlignment(Pos.CENTER);
		tfUsername.setAlignment(Pos.BOTTOM_RIGHT);
		tfPassword.setAlignment(Pos.BOTTOM_RIGHT);
		
		//events
		
		//Create Scene
		Scene loginScene = new Scene(loginGrid, 400, 500);
		loginStage.setTitle("User Login");
		loginStage.setScene(loginScene);
		loginStage.show();
		
		
	}
	private void Authenticatioun() {
		//text values from user input
		String UserNameInput = tfUsername.getText();
		String UserPasswordInput = tfUsername.getText();
		

		//Authentication object
		AuthenticationInfo userAuthentication = new AuthenticationInfo(UserNameInput, UserPasswordInput);
	}
	
	
	public static void main(String[] args) {
	    launch(args);
  }
	
}
//https://www.dataquest.io/blog/sql-commands/
