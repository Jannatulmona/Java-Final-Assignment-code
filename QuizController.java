package com.example.banglaquizapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class QuizController {

    @FXML private Label questionLabel;
    @FXML private Button optionA;
    @FXML private Button optionB;
    @FXML private Button optionC;
    @FXML private Button optionD;

    private String name, email;
    private int[] scores = new int[5];
    private int currentQ = 0;

    // প্রশ্নের অ্যারে: প্রশ্ন, অপশন A, B, C, D, সঠিক উত্তর (A/B/C/D)
    private String[][] questions = {
            {"বাংলাদেশের রাজধানী কোথা?", "ঢাকা", "চট্টগ্রাম", "খুলনা", "রাজশাহী", "A"},
            {"সর্বোচ্চ পর্বতমালা?", "হিমালয়", "আলপস", "ক্যাচক", "অ্যাপেনাইন", "A"},
            {"বাংলা ভাষার জন্মসন কত?", "১২৫০", "১৩৫০", "১৪৫০", "১৫৫০", "B"},
            {"বাংলাদেশের জাতীয় ফুল?", "শাপলা", "রজনীগন্ধা", "গোলাপ", "কামেলিয়া", "A"},
            {"পৃথিবীর বৃহত্তম মহাসাগর?", "প্রশান্ত", "অ্যাটলান্টিক", "ইন্ডিয়ান", "আর্কটিক", "A"}
    };

    public void setUserInfo(String name, String email){
        this.name = name;
        this.email = email;
        loadQuestion();
    }

    private void loadQuestion(){
        if(currentQ >= questions.length){
            saveScore();
            showScore();
            return;
        }

        questionLabel.setText(questions[currentQ][0]);
        optionA.setText(questions[currentQ][1]);
        optionB.setText(questions[currentQ][2]);
        optionC.setText(questions[currentQ][3]);
        optionD.setText(questions[currentQ][4]);
    }

    @FXML
    private void handleAnswer(ActionEvent event){
        Button btn = (Button) event.getSource();
        String selectedOption = btn.getText();  // অপশন A, B, C, D পাঠানো হচ্ছে

        if(selectedOption.equals(questions[currentQ][5])) {
            scores[currentQ] = 1;  // সঠিক উত্তর হলে স্কোর ১
        } else {
            scores[currentQ] = 0;  // ভুল উত্তর হলে স্কোর ০
        }

        currentQ++;  // পরবর্তী প্রশ্নে চলে যান
        loadQuestion();  // পরবর্তী প্রশ্ন লোড করুন
    }

    private void saveScore(){
        int total = 0;
        for(int s : scores) total += s;  // স্কোর গননা

        try (Connection conn = DBConnection.getConnection()) {  // ডাটাবেস সংযোগ
            // SQL ইনসার্ট কোয়েরি
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO scores(name, email, q1, q2, q3, q4, q5, total) VALUES(?,?,?,?,?,?,?,?)"
            );

            // ইউজারের নাম ও ইমেইল
            ps.setString(1, name);
            ps.setString(2, email);

            // প্রতিটি প্রশ্নের জন্য স্কোর
            for (int i = 0; i < 5; i++) {
                ps.setInt(3 + i, scores[i]);
            }

            // মোট স্কোর
            ps.setInt(8, total);

            // SQL এক্সিকিউট করা
            ps.executeUpdate();
            System.out.println("Score saved successfully!");  // স্কোর সফলভাবে সেভ হয়েছে

        } catch (Exception e) {
            e.printStackTrace();  // কোনো ত্রুটি হলে এখানে দেখাবে
        }
    }

    private void showScore(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/score.fxml"));
            Stage stage = (Stage) questionLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));

            ScoreController controller = loader.getController();
            int total = 0;
            for(int s : scores) total += s;  // মোট স্কোর গননা
            controller.setScoreInfo(name, email, scores, total);  // স্কোর দেখানো
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
