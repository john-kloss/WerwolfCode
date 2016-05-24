package sopro.werwolf;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class GameActivity extends AppCompatActivity {

    Button currentlySelectedButton;

    //string contains phases, counter keeps track of current phase
    String[] phase = {"tag","dieb","amor","werwoelfe","seherin","hexe"};
    int phasecounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get the number of Players
        Intent intent = getIntent();
        int players = intent.getIntExtra("players", 8);
        String name = intent.getStringExtra("name");

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //creates rows (Linear Layouts) and playerbuttons
        createObjects(players);

        Snackbar.make(findViewById(R.id.gameView), "Willkommen im Spiel "+name, Snackbar.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //next phase modulo the number of phases
                phasecounter = phasecounter++ % phase.length;
            }
        });
    }


    private void createObjects(int players){
        //create Linear Layouts in gameView
        LinearLayout row1 = (LinearLayout) findViewById(R.id.row1);
        LinearLayout row2 = (LinearLayout) findViewById(R.id.row2);
        LinearLayout row3 = (LinearLayout) findViewById(R.id.row3);
        LinearLayout row4 = (LinearLayout) findViewById(R.id.row4);

        //create playerbuttons
        for (int i = 0; i < players; i++){
            Button button = new Button(this);
            button.setText("player"+i);
            button.setBackgroundColor(0);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerSelected(v);
                }
            };
            button.setOnClickListener(onClickListener);
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);

            //insert into rows (Linear Layouts)
            if (i < 5)
                row1.addView(button);
            else if (i < 10)
                row2.addView(button);
            else if (i < 15)
                row3.addView(button);
            else
                row4.addView(button);
        }
    }

    public void playerSelected(View view){
        boolean alreadyClicked = false;
        Button button = (Button) view;

        //get all buttons and make them transparent
        ViewGroup gameView =(ViewGroup) findViewById(R.id.gameView);
        for (int i=0; i < gameView.getChildCount(); i++){
            LinearLayout row = (LinearLayout) gameView.getChildAt(i);
            for (int j=0; j < row.getChildCount(); j++){
                Button currentButton = (Button) row.getChildAt(i);
                currentButton.setBackgroundColor(0);
            }

        }

        if(button.equals(currentlySelectedButton)) {
            currentlySelectedButton = null;
        }
        else{
            currentlySelectedButton = button;
            Snackbar.make(view, "Du hast "+ button.getText().toString() +" ausgewählt", Snackbar.LENGTH_LONG).show();
            button.setBackgroundColor(getResources().getColor(R.color.button_material_dark));
        }
    }
}
