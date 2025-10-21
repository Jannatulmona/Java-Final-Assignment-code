package com.example.banglaquizapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ScoreController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label scoreLabel;

    public void setScoreInfo(String name, String email, int[] scores, int total) {
        nameLabel.setText("নাম: " + name);
        emailLabel.setText("ইমেইল: " + email);
        scoreLabel.setText("মোট স্কোর: " + total);
    }
}
