package sopro.werwolf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    Button currentlySelectedPlayer;


    //string contains phases, counter keeps track of current phase
    String[] phase = {"tag","dieb","amor","werwoelfe","seherin","hexe"};
    int phasecounter = 0;

    String lover1, lover2, victimWer, victimHex, victimSeh;
    Boolean decisHexHeil, decisHexGift;

    // TODO: save game status in data base
    boolean gifttrank = true;
    boolean heiltrank = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get the cards
        Intent intent = getIntent();
        String[] cards = intent.getStringArrayExtra("cards");

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //creates rows (Linear Layouts) and playerbuttons
        createObjects(cards.length);

        Snackbar.make(findViewById(R.id.gameView), "Willkommen im Spiel", Snackbar.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
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
            //button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);

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
        if(button.equals(currentlySelectedPlayer)) {
            currentlySelectedPlayer = null;
        }
        //otherwise select it
        else{
            currentlySelectedPlayer = button;
            button.setBackgroundColor(getResources().getColor(R.color.button_material_dark));
        }
    }

    private void confirm(){
        // TODO: check for dead players
        if (currentlySelectedPlayer != null) {
            switch (phase[phasecounter]) {
                case "tag":

                    // TODO: implement day

                    nextPhase();
                    break;

                case "dieb":
                    //get selected card

                    //remove from phase array
                    phase[phasecounter] = "";
                    nextPhase();
                    break;

                case "amor":

                    //select lover1 first, then lover2 and go to nextPhase (!)afterwards(!)
                    if (lover1 == null) {
                        lover1 = currentlySelectedPlayer.getText().toString();
                        Snackbar.make(currentlySelectedPlayer, "Du hast " + lover1 + " ausgewählt", Snackbar.LENGTH_LONG).show();
                    }
                    else if (!lover1.equals(currentlySelectedPlayer.getText())){
                        lover2 = currentlySelectedPlayer.getText().toString();
                        Snackbar.make(currentlySelectedPlayer, lover1 + " hat sich in " + lover2 + " verliebt", Snackbar.LENGTH_LONG).show();

                        //disable fab till next phase is created
                        ((FloatingActionButton) findViewById(R.id.fab)).setEnabled(false);

                        //make the system wait for 3 seconds before starting new phase
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                ((FloatingActionButton) findViewById(R.id.fab)).setEnabled(true);
                                //remove from phase array
                                phase[phasecounter] = "";
                                nextPhase();
                            }
                        }, 500);
                    }
                    break;


                case "werwoelfe":

                    victimWer = currentlySelectedPlayer.getText().toString();
                    Snackbar.make(currentlySelectedPlayer, "Du hast " + victimWer + " ausgewählt", Snackbar.LENGTH_LONG).show();

                    nextPhase();
                    break;

                case "seherin":
                    if (victimSeh == null) {
                        victimSeh = currentlySelectedPlayer.getText().toString();
                        // TODO: get the role...
                        popupinfo(victimSeh + " ist ein ...");
                    }
                    else {
                        victimSeh = null;
                        nextPhase();
                    }
                    break;

                case "hexe":

                    //execute if there is no decision on the 'heiltrank' yet
                    if (decisHexHeil == null && heiltrank){
                        popupchoice("Möchtest du das Opfer retten?", "decisHexHeil");
                    }
                    //otherwise ask if the 'gifttrank' should be used
                    else if (decisHexGift == null && gifttrank) {
                        popupchoice("Möchtest du deinen Gifttrank verwenden?", "decisHexGift");
                    }
                    else {
                        if (heiltrank){
                            if (decisHexHeil){
                                victimWer = null;
                                heiltrank = false;
                            }
                        }

                        if (gifttrank){
                            if(decisHexGift) {
                                //gifttrank used
                                gifttrank = false;
                                //so you have to choose a new player
                                currentlySelectedPlayer = null;
                                //to enter the else part below the next time this method is called
                                decisHexGift = false;
                            }

                            else{
                                decisHexGift = null;
                                decisHexHeil = null;
                                nextPhase();
                            }
                        }
                        //no 'gifttrank' available
                        else{
                            decisHexGift = null;
                            decisHexHeil = null;
                            nextPhase();
                        }

                    }

                    break;
            }
        }
    }

    private void nextPhase(){


        //next phase modulo the number of phases
        phasecounter = (phasecounter+1) % phase.length;

        //make all buttons transparent
        ViewGroup gameView =(ViewGroup) findViewById(R.id.gameView);
        for (int i=0; i < gameView.getChildCount(); i++){
            LinearLayout row = (LinearLayout) gameView.getChildAt(i);
            for (int j=0; j < row.getChildCount(); j++){
                Button currentButton = (Button) row.getChildAt(j);
                currentButton.setBackgroundColor(0);
            }
        }

        //leave selection in this case
        if (!phase[phasecounter].equals("hexe"))
            currentlySelectedPlayer = null;

        //action based on phase
        switch (phase[phasecounter]){
            case "tag":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.tag));
                Snackbar.make(findViewById(R.id.gameView), "Es ist Tag - Wähle eine Person, die du hängen möchtest", Snackbar.LENGTH_LONG).show();
                // TODO: wait for pecentage range to be reached
                break;

            case "dieb":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.dieb));
                Snackbar.make(findViewById(R.id.gameView), "Dieb - wähle eine neue Identität", Snackbar.LENGTH_LONG).show();
                // TODO: show to cards
                break;

            case "amor":
                // TODO: select two players somehow
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.amor));
                Snackbar.make(findViewById(R.id.gameView), "Amor - wähle (nacheinander) zwei Personen, die sich ineinander verlieben", Snackbar.LENGTH_LONG).show();

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
                //show
                popupinfo("Das Opfer der Werwölfe in dieser Nacht ist ..." + victimWer);
                Snackbar.make(findViewById(R.id.gameView), "Hexe - du siehst nun das Opfer der Nacht.", Snackbar.LENGTH_LONG).show();

                break;

            default:

                nextPhase();

        }
    }

    /* creates a popup containing information */
    public void popupinfo(String info){

        ViewGroup gameView =(ViewGroup) findViewById(R.id.gameView);
        //inflate the popupinfo.xml
        View popupView = getLayoutInflater().inflate(R.layout.popupinfo, null);
        final PopupWindow pw = new PopupWindow(popupView, gameView.getWidth()/2, gameView.getHeight()/2, true);

        TextView textViewPopup = (TextView) popupView.findViewById(R.id.textViewPopupInfo);
        Button button = (Button) popupView.findViewById(R.id.buttonPopup);
        textViewPopup.setText(info);

        pw.showAtLocation(findViewById(R.id.activityGame), Gravity.CENTER, 0 ,0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                confirm();
            }
        });
    }

    /* creates a popup offering you a choice */
    public void popupchoice(String info, final String toBeDecided){

        ViewGroup gameView =(ViewGroup) findViewById(R.id.gameView);
        //inflate the popupchoice.xml
        View popupView = getLayoutInflater().inflate(R.layout.popupchoice, null);
        final PopupWindow pw = new PopupWindow(popupView, gameView.getWidth()/2, gameView.getHeight()/2, true);

        TextView textViewPopup = (TextView) popupView.findViewById(R.id.textViewPopupChoice);
        Button buttonYes = (Button) popupView.findViewById(R.id.buttonPopupYes);
        Button buttonNo = (Button) popupView.findViewById(R.id.buttonPopupNo);
        textViewPopup.setText(info);

        pw.showAtLocation(findViewById(R.id.activityGame), Gravity.CENTER, 0 ,0);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                popup_callback(true, toBeDecided);

            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                popup_callback(false, toBeDecided);

            }
        });

    }


    /* action based on chosen value in popup */
    public void popup_callback(Boolean choice, String toBeDecided){
        switch (toBeDecided){
            case "decisHexHeil":
                decisHexHeil = choice;
                break;

            case "decisHexGift":
                decisHexGift = choice;
                break;
        }
        confirm();
    }


}
