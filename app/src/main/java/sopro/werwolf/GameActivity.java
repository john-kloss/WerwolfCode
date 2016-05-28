package sopro.werwolf;

import android.app.Dialog;
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

        //get the roles
        Intent intent = getIntent();
        String[] roles = intent.getStringArrayExtra("roles");

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //creates rows (Linear Layouts) and playerbuttons
        createObjects(roles.length);

        Snackbar.make(findViewById(R.id.gameView), "Willkommen im Spiel", Snackbar.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPhase();
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

        //make all buttons transparent
        ViewGroup gameView =(ViewGroup) findViewById(R.id.gameView);
        for (int i=0; i < gameView.getChildCount(); i++){
            LinearLayout row = (LinearLayout) gameView.getChildAt(i);
            for (int j=0; j < row.getChildCount(); j++){
                Button currentButton = (Button) row.getChildAt(j);
                currentButton.setBackgroundColor(0);
            }
        }

        //if button was already selected, unselect it
        if(button.equals(currentlySelectedButton)) {
            currentlySelectedButton = null;
        }
        //otherwise select it
        else{
            currentlySelectedButton = button;
            Snackbar.make(view, "Du hast "+ button.getText().toString() +" ausgewählt", Snackbar.LENGTH_LONG).show();
            button.setBackgroundColor(getResources().getColor(R.color.button_material_dark));
        }
    }

    private void nextPhase(){
        //next phase modulo the number of phases
        phasecounter = (phasecounter+1) % phase.length;

        //action based on phase
        switch (phase[phasecounter]){
            case "tag":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.tag));
                Snackbar.make(findViewById(R.id.gameView), "Es ist Tag - Wähle eine Person, die du hängen möchtest", Snackbar.LENGTH_LONG).show();
                break;

            case "dieb":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.dieb));
                Snackbar.make(findViewById(R.id.gameView), "Dieb - wähle eine neue Identität", Snackbar.LENGTH_LONG).show();
                break;

            case "amor":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.amor));
                Snackbar.make(findViewById(R.id.gameView), "Amor - wähle zwei Personen, die sich ineinander verlieben", Snackbar.LENGTH_LONG).show();
                break;

            case "werwoelfe":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.werwoelfe));
                Snackbar.make(findViewById(R.id.gameView), "Werwolf - wähle eine Person, die du töten möchtest", Snackbar.LENGTH_LONG).show();
                break;

            case "seherin":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.seherin));
                Snackbar.make(findViewById(R.id.gameView), "Seherin - wähle eine Person, deren Identität du erfahren möchtest", Snackbar.LENGTH_LONG).show();
                // TODO:  show identity
                break;

            case "hexe":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.hexe));
                Snackbar.make(findViewById(R.id.gameView), "Hexe - du siehst nun das Opfer der Nacht. Möchtest du deinen Heiltrank benutzen?", Snackbar.LENGTH_LONG).show();
                // TODO: show victim 
                // TODO: two selection buttons
                // TODO: save choice
                Snackbar.make(findViewById(R.id.gameView), "Hexe - möchtest du deinen Gifttrank verwenden?", Snackbar.LENGTH_LONG).show();
                // TODO: two selection buttons
                // TODO: save choice
                break;

        }


    }
}
