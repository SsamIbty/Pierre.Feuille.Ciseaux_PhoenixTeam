package com.example.shifumi;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button rockButton, paperButton, scissorsButton;
    private ImageView computerChoiceImageView, playerChoiceImageView, playerChoiceImageView2;
    private TextView resultTextView, scoreTextView, countdownTextView;
    private int playerScore, computerScore, turnCount;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        rockButton = findViewById(R.id.rockButton);
        paperButton = findViewById(R.id.paperButton);
        scissorsButton = findViewById(R.id.scissorsButton);
        computerChoiceImageView = findViewById(R.id.computerChoiceImageView);
        playerChoiceImageView = findViewById(R.id.playerChoiceImageView);
        playerChoiceImageView2 = findViewById(R.id.playerChoiceImageView2);
        resultTextView = findViewById(R.id.resultTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        // Initialize scores
        playerScore = 0;
        computerScore = 0;
        turnCount = 0;


        // Set button onClick listeners
        rockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTurn("rock");
            }
        });

        paperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTurn("paper");
            }
        });

        scissorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTurn("scissors");
            }
        });

        updateScore();
    }

    private void playTurn(String playerChoice) {

        // Create an AlertDialog for the countdown
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.dialog_countdown);

        final AlertDialog countdownDialog = builder.create();

        Window window = countdownDialog.getWindow();
        window.setGravity(Gravity.TOP); // Positionnement en haut de l'écran

        countdownDialog.show();

        // Get the countdown TextView from the dialog
        TextView countdownTextView = countdownDialog.findViewById(R.id.countdownTextView);

        // Create a CountDownTimer for the countdown
        CountDownTimer countdownTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText("Time remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                countdownDialog.dismiss();
                // Your existing playTurn() code goes here
            }
        };
        countdownTimer.start();

        // Generate computer's choice
        Random random = new Random();
        int computerChoiceIndex = random.nextInt(3);
        String[] choices = {"rock", "paper", "scissors"};
        String computerChoice = choices[computerChoiceIndex];

        // Update computer's choice image
        if (computerChoice.equals("rock")) {
            computerChoiceImageView.setImageResource(R.drawable.button_rock);
        } else if (computerChoice.equals("paper")) {
            computerChoiceImageView.setImageResource(R.drawable.button_paper);
        } else if (computerChoice.equals("scissors")) {
            computerChoiceImageView.setImageResource(R.drawable.button_cisor);
        }

        // Update player's choice image
        if (playerChoice.equals("rock")) {
            playerChoiceImageView.setImageResource(R.drawable.button_rock);
        } else if (playerChoice.equals("paper")) {
            playerChoiceImageView.setImageResource(R.drawable.button_paper);
        } else if (playerChoice.equals("scissors")) {
            playerChoiceImageView.setImageResource(R.drawable.button_cisor);
        }

        // Determine winner
        if (playerChoice.equals(computerChoice)) {
            // Tie
            resultTextView.setText("Tie!");
            playerChoiceImageView2.setImageResource(R.drawable.fair);

        } else if (playerChoice.equals("rock") && computerChoice.equals("scissors") ||
                playerChoice.equals("paper") && computerChoice.equals("rock") ||
                playerChoice.equals("scissors") && computerChoice.equals("paper")) {
            // Player wins
            turnCount++;
            playerScore++;
            resultTextView.setText("I lost!");
            playerChoiceImageView2.setImageResource(R.drawable.phone_sad);
        } else {
            // Computer wins
            computerScore++;
            turnCount++;
            resultTextView.setText("I win!");
            playerChoiceImageView2.setImageResource(R.drawable.phone_happy);


        }

        // Update scores
        updateScore();


        if (turnCount == 7) {
            // arrêter le jeu après le 10ème tour
            rockButton.setEnabled(false);
            paperButton.setEnabled(false);
            scissorsButton.setEnabled(false);

            String winner;
            int imageResourceId;

            if (computerScore > playerScore) {
                winner = "I win!";
                imageResourceId = R.drawable.phone_happy;
                playSound(R.raw.looser);
            } else if (computerScore < playerScore) {
                winner = "You win!";
                imageResourceId = R.drawable.phone_sad;
                playSound(R.raw.winner);
            } else {
                winner = "Tie!";
                imageResourceId = R.drawable.fair;
            }

            showWinnerDialog(winner, imageResourceId);
        }


    }

    private void updateScore() {
        scoreTextView.setText("Player: " + playerScore + "                     Device: " + computerScore);

    }

    private void playSound(int soundResourceId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResourceId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    private void showWinnerDialog(String winner, int imageResourceId) {
        // create a custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.winner_dialog);
        dialog.setTitle("Game Over");

        // set the winner text and image in the dialog
        TextView winnerTextView = dialog.findViewById(R.id.winnerText);
        winnerTextView.setText(winner);
        ImageView winnerImageView = dialog.findViewById(R.id.winnerImageView);
        winnerImageView.setImageResource(imageResourceId);

        // set up the exit and replay buttons
        Button exitButton = dialog.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // exit the app
                finish();
            }
        });

        Button replayButton = dialog.findViewById(R.id.replayButton);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset the scores and turn count and enable the buttons
                playerScore = 0;
                computerScore = 0;
                turnCount = 0;
                updateScore();
                rockButton.setEnabled(true);
                paperButton.setEnabled(true);
                scissorsButton.setEnabled(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}