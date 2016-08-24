package sopro.werwolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import sopro.werwolf.DatabaseConnection.databaseCon;


public class GameActivity extends AppCompatActivity {

    Button currentlySelectedPlayer;

    //check frequently if limit is reached during the day
    Handler timerHandler = new Handler();
    Runnable timerRunnable= new Runnable() {

        @Override
        public void run() {
            Snackbar.make(findViewById(R.id.gameView), "Checking for results...", Snackbar.LENGTH_SHORT).show();
            // TODO: JSON - check for changes

            timerHandler.postDelayed(this, 2000);
        }
    };


    //string contains phases, counter keeps track of current phase
    String[] phase = new String[10];
    int phasecounter = 0;

    String lover1, lover2, victimWer, victimDor, victimHex, victimSeh, decisDieb;
    Boolean decisHexHeil, decisHexGift;
    String[] cards;

    // TODO: save game status in data base

    boolean gifttrank = true;
    boolean heiltrank = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get the cards
        Intent intent = getIntent();
        cards = intent.getStringArrayExtra("cards");


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

    /*
    * >createObjects<
    *
    * This method creates all objects in the GameView.
    * Like rows, button, ...
    *
    */
    private void createObjects(int numOfPlayers){
        //create Linear Layouts in gameView
        LinearLayout row1 = (LinearLayout) findViewById(R.id.row1);
        LinearLayout row2 = (LinearLayout) findViewById(R.id.row2);
        LinearLayout row3 = (LinearLayout) findViewById(R.id.row3);
        LinearLayout row4 = (LinearLayout) findViewById(R.id.row4);

        //create playerbuttons
        for (int i = 0; i < numOfPlayers; i++){
            Button button = new Button(this);
            button.setText("player"+i);
            button.setBackgroundColor(0);
            // TODO: JSON - getAllPlayer.php
            //button.setText(player[i]);
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

        //set up the phases
        phase[0] = "tag";
        String[] searchFor = {"tag", "dieb", "amor", "werwolf", "seherin", "hexe"};
        for (int j=1; j<searchFor.length;) {
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].equals(searchFor[j])) {
                    phase[j] = cards[i];
                }
            }
            j++;
        }

    }

    /*
    * >playerSelected<
    *
    * This method is called if a playerButton gets selected.
    * It toggles the background transparency and
    * updates currentlySelectedPlayer.
    *
    */

    public void playerSelected(View view){
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

    /*
    * >confirm<
    *
    * This method is the onClick method of the FloatingActionButton (fab)
    * which is clicked if a player wants to confirm the selection.
    *
    * It saves the selection and runs the following commands waiting for the next confirm.
    * In the end it always calls nextPhase();
    *
    *
     */

    private void confirm(){

        databaseCon con = new databaseCon();

        //check for dead players
        //con.checkDeadPlayers(gameID);

        if (currentlySelectedPlayer != null) {
            switch (phase[phasecounter]) {
                case "tag":

                    timerHandler.removeCallbacks(timerRunnable);

                    nextPhase();
                    break;

                case "dieb":
                    decisDieb = "amor";
                    // TODO: JSON - udpate dieb
                    // TODO: update phase array (see below)
                    switch (decisDieb){
                        case "amor":
                           //- playerID = own playerID
                            con.Role("Dieb", "Amor", 1, "1");
                            phase[2] = "amor";
                            break;
                        case "werwolf":
                            phase[3] = "werwolf";
                            //con.Role("Dieb", "Werwolf", playerID, gameID);
                            break;
                        case "seherin":
                            phase[4] = "seherin";
                            //con.Role("Dieb", "Werwolf", playerID, gameID);
                            break;
                        case "hexe":
                            phase[5] = "hexe";
                            //con.Role("Dieb", "Hexe", playerID, gameID);
                            break;
                        case "dorfbewohner":
                            //con.Role("Dieb", "Dorfbewohner", playerID, gameID);
                            break;
                    }

                    //remove from phase array
                    phase[phasecounter] = "";
                    nextPhase();
                    break;

                case "amor":

                    //select lover1 first, then lover2 and go to nextPhase (!)afterwards(!)
                    if (lover1 == null) {
                        lover1 = currentlySelectedPlayer.getText().toString();
                        Snackbar.make(currentlySelectedPlayer, "Du hast "+lover1+" ausgewählt",Snackbar.LENGTH_LONG).show();

                        con.Role("Amor", "lover1", 1, "0"); // ToDo: set playerId and gameID

                        popupinfo("Wähle nun die zweite Person");
                    }
                    else if (!lover1.equals(currentlySelectedPlayer.getText())){

                        lover2 = currentlySelectedPlayer.getText().toString();

                        con.Role("Amor", "lover2", 2, "0");

                        Snackbar.make(currentlySelectedPlayer, lover1 + " hat sich in " + lover2 + " verliebt", Snackbar.LENGTH_LONG).show();

                        //disable fab till next phase is created
                        findViewById(R.id.fab).setEnabled(false);

                        //make the system wait for 3 seconds before starting new phase
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                findViewById(R.id.fab).setEnabled(true);
                                //remove from phase array
                                phase[phasecounter] = "";
                                nextPhase();
                            }
                        }, 500);
                    }
                    // TODO: JSON - set lover
                    // entweder wie oben oder hier gesammelt; dann müssten aber die jeweiligen playerIDs gespeichert werden
                    break;


                case "werwolf":

                    victimWer = currentlySelectedPlayer.getText().toString();
                    Snackbar.make(currentlySelectedPlayer, "Du hast " + victimWer + " ausgewählt", Snackbar.LENGTH_LONG).show();

                    // TODO: check for decision of others and show votes
                    // TODO: JSON - set victimWer
                    //con.Role("Werwolf", null, victimWerID, gameID);

                    nextPhase();
                    break;

                case "seherin":
                    if (victimSeh == null) {
                        victimSeh = currentlySelectedPlayer.getText().toString();
                        // TODO: JSON - get the role
                        String GoB = null;
                        GoB = con.Role("Seherin", null, 1, "1"); // ToDO: change numbers to playerId and gameID
                        popupinfo(victimSeh + " ist " + GoB);

                    }
                    else {
                        victimSeh = null;
                        nextPhase();
                    }
                    break;

                case "hexe":

                    //execute if there is no decision on the 'heiltrank' yet

                    gifttrank = Boolean.parseBoolean(con.Role("Hexe", "Gifttrank", 0, "0")); // ToDo: change number to playerID/ gameID
                    heiltrank = Boolean.parseBoolean(con.Role("Hexe", "Heiltrank", 0, "0")); // ToDo: change number to playerID/ gameID

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
                                // TODO: JSON - save to database
                                //con.Role("Hexe", "TrankVerwendenHeilen", 0, gameID);
                            }
                        }

                        if (gifttrank){
                            if(decisHexGift) {
                                //gifttrank used
                                gifttrank = false;
                                victimHex = currentlySelectedPlayer.getText().toString();
                                //so you have to choose a new player
                                currentlySelectedPlayer = null;
                                //to enter the else part below the next time this method is called
                                decisHexGift = false;
                                // TODO: JSON - save to database
                                //con.Role("Hexe", "TrankVerwendenGift", victimHexID, gameID);
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

    /*
    * >nextPhase<
    *
    * Is executed if the game enters a new phase.
    * Increases phasecounter by one.
    * Resets the buttons.
    *
     */

    private void nextPhase(){

        //next phase modulo the number of phases
        phasecounter = (phasecounter+1) % phase.length;
        while(phase[phasecounter] == null)
            phasecounter = (phasecounter+1) % phase.length;

        //make all buttons transparent
        final ViewGroup gameView =(ViewGroup) findViewById(R.id.gameView);
        for (int i=0; i < gameView.getChildCount(); i++){
            LinearLayout row = (LinearLayout) gameView.getChildAt(i);
            for (int j=0; j < row.getChildCount(); j++){
                Button currentButton = (Button) row.getChildAt(j);
                currentButton.setBackgroundColor(0);
            }
        }

        //leave selection in this case
        if (!(phase[phasecounter].equals("hexe") || phase[phasecounter].equals("dieb")))
            currentlySelectedPlayer = null;

        //action based on phase
        switch (phase[phasecounter]){
            case "tag":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.tag));
                Snackbar.make(findViewById(R.id.gameView), "Es ist Tag - Wähle eine Person, die du hängen möchtest", Snackbar.LENGTH_LONG).show();

                timerHandler.postDelayed(timerRunnable, 0);

                break;

            case "dieb":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.dieb));
                Snackbar.make(findViewById(R.id.gameView), "Dieb - wähle eine neue Identität", Snackbar.LENGTH_LONG).show();
                popupdecision("Dieb - wähle deine neue Rolle", "dieb");
                break;

            case "amor":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.amor));
                popupinfo("Amor - bestimme das Liebespaar. Wähle nun die erste Person");


                break;

            case "werwolf":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.werwoelfe));
                popupinfo("Werwölfe - wählt eine Person, die ihr töten möchtet");

                break;

            case "seherin":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.seherin));
                Snackbar.make(findViewById(R.id.gameView), "Seherin - wähle eine Person, deren Identität du erfahren möchtest", Snackbar.LENGTH_LONG).show();
                break;

            case "hexe":
                findViewById(R.id.activityGame).setBackgroundColor(getResources().getColor(R.color.hexe));
                //show
                // TODO: JSON - get victimWer
                databaseCon con = new databaseCon();
                // String victimWer = con.Role("Hexe", "show", 0, "0");
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

    /* creates a popup letting you choose between two cards (Dieb) */
    public void popupdecision(String info, final String toBeDecided){

        ViewGroup gameView =(ViewGroup) findViewById(R.id.gameView);
        //inflate the popupdecision.xml
        View popupView = getLayoutInflater().inflate(R.layout.popupdecision, null);
        final PopupWindow pw = new PopupWindow(popupView, gameView.getWidth(), gameView.getHeight(), true);

        TextView textViewPopup = (TextView) popupView.findViewById(R.id.textViewPopupChoice);
        Button buttonLeft = (Button) popupView.findViewById(R.id.buttonPopupLeft);
        Button buttonRight = (Button) popupView.findViewById(R.id.buttonPopupRight);

        buttonLeft.setHeight(pw.getHeight());
        buttonRight.setHeight(pw.getHeight());
        buttonLeft.setWidth(pw.getWidth()/2);
        buttonRight.setWidth(pw.getWidth()/2);
        buttonLeft.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_info_black_24dp, null), null, null);
        buttonRight.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_info_black_24dp, null), null, null);
        buttonLeft.setText(cards[cards.length-2]);
        buttonRight.setText(cards[cards.length-1]);

        textViewPopup.setText(info);

        pw.showAtLocation(findViewById(R.id.activityGame), Gravity.CENTER, 0 ,0);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                popup_callback(true, toBeDecided);

            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
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

            case "dieb":
                if(choice)
                    decisDieb = cards[cards.length-2];
                else
                    decisDieb = cards[cards.length-1];
        }
        confirm();
    }

}
