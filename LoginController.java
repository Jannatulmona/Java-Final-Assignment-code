package com.example.banglaquizapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField idField;
    @FXML
    private TextField emailField;
    @FXML
    private Button startQuizButton;

    private String name, email;

    @FXML
    public void startQuiz(ActionEvent event) {
        // Retrieve user input
        name = idField.getText();
        email = emailField.getText();

        // Check if ID or email is empty
        if (name.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ভুল ইনপুট");
            alert.setHeaderText("অনুপস্থিত তথ্য");
            alert.setContentText("অনুগ্রহ করে ID এবং ইমেইল পূর্ণ করুন।");
            alert.showAndWait();
        } else {
            // Proceed to the quiz screen
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz.fxml"));
                AnchorPane quizRoot = loader.load();
                QuizController controller = loader.getController();
                controller.setUserInfo(name, email); // Pass name and email to quiz controller
                Scene quizScene = new Scene(quizRoot);
                Stage currentStage = (Stage) startQuizButton.getScene().getWindow();
                currentStage.setScene(quizScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
