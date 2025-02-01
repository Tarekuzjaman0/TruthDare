package com.example.truthanddare;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView bottle;
    private TextView resultTextView;
    private Random random;
    private int lastDirection;
    private boolean spinning;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottle = findViewById(R.id.bottle);
        resultTextView = findViewById(R.id.resultTextView);
        random = new Random();

        mediaPlayer = MediaPlayer.create(this, R.raw.spin); // Add spin_sound.mp3 to res/raw

        bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spinning) {
                    spinBottle();
                }
            }
        });
    }

    private void spinBottle() {
        int newDirection = random.nextInt(3600) + 360;
        float pivotX = bottle.getWidth() / 2;
        float pivotY = bottle.getHeight() / 2;

        Animation rotate = new RotateAnimation(lastDirection, newDirection, pivotX, pivotY);
        rotate.setDuration(2000);
        rotate.setFillAfter(true);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                spinning = true;
                mediaPlayer.start(); // Play sound
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                spinning = false;
                determinePlayer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        lastDirection = newDirection;
        bottle.startAnimation(rotate);
    }

    private void determinePlayer() {
        int degrees = lastDirection % 360;
        String selectedPlayer;

        if (degrees >= 0 && degrees < 90) {
            selectedPlayer = "Player 1";
        } else if (degrees >= 90 && degrees < 180) {
            selectedPlayer = "Player 2";
        } else if (degrees >= 180 && degrees < 270) {
            selectedPlayer = "Player 3";
        } else {
            selectedPlayer = "Player 4";
        }

        resultTextView.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
