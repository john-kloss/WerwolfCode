package sopro.werwolf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Random;

public class GameSetupActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setEnabled(false);  -> enabled for debugging


        //set values for NumberPicker
        final NumberPicker players = (NumberPicker) findViewById(R.id.numberPicker);
        players.setMinValue(8);
        players.setMaxValue(20);

        //if value of the NumberPicker changes
        players.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setRecommendedNumberOfWer(picker.getValue());
                calculateNumberDor(players);
            }
        });

        //if value of the Spinner changes, recalculate numberDor
        final Spinner spinnerWer = (Spinner) findViewById(R.id.spinnerWer);
        spinnerWer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateNumberDor(spinnerWer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //initiate values
        calculateNumberDor(players);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setRecommendedNumberOfWer(int players) {

        if (players < 12)
            ((Spinner) findViewById(R.id.spinnerWer)).setSelection(1, true);
        else if (players < 17)
            ((Spinner) findViewById(R.id.spinnerWer)).setSelection(2, true);
        else
            ((Spinner) findViewById(R.id.spinnerWer)).setSelection(3, true);
    }


    // TODO: put calculateNumberDor and startGame into one function..

    public void calculateNumberDor(View view) {
        Spinner spinnerWer = (Spinner) findViewById(R.id.spinnerWer);

        int numberDor = ((NumberPicker) findViewById(R.id.numberPicker)).getValue();

        //transform String to int and
        switch ((String) spinnerWer.getSelectedItem()) {
            case "1":
                numberDor -= 1;
                break;
            case "2":
                numberDor -= 2;
                break;
            case "3":
                numberDor -= 3;
                break;
            case "4":
                numberDor -= 4;
                break;
            case "5":
                numberDor -= 5;
                break;
        }

        // TODO: Optimize this part...
        //calculate number of extra roles
        int numberExtra = 0;
        if (((CheckBox) (findViewById(R.id.checkBoxAmo))).isChecked())
            numberDor--;
        if (((CheckBox) (findViewById(R.id.checkBoxDie))).isChecked())
            numberDor++;
        if (((CheckBox) (findViewById(R.id.checkBoxHex))).isChecked())
            numberDor--;
        if (((CheckBox) (findViewById(R.id.checkBoxMaed))).isChecked())
            numberDor--;
        if (((CheckBox) (findViewById(R.id.checkBoxSeh))).isChecked())
            numberDor--;

        ((TextView) findViewById(R.id.numberDor)).setText(" " + numberDor);
    }

    public void startGame(View view) {

        String[] cards;
        //array containing the cards, Dieb -> two more cards
        if (((CheckBox) findViewById(R.id.checkBoxDie)).isChecked()) {
            cards = new String[((NumberPicker) findViewById(R.id.numberPicker)).getValue() + 2];
        } else {
            cards = new String[((NumberPicker) findViewById(R.id.numberPicker)).getValue()];
        }
        int i;

        //insert werewolves
        int numberWer = 0;
        switch ((String) ((Spinner) findViewById(R.id.spinnerWer)).getSelectedItem()) {
            case "1":
                numberWer = 1;
                break;
            case "2":
                numberWer = 2;
                break;
            case "3":
                numberWer = 3;
                break;
            case "4":
                numberWer = 4;
                break;
            case "5":
                numberWer = 5;
                break;
        }
        for (i = 0; i < numberWer; i++) {
            cards[i] = "werwolf";
        }

        if (((CheckBox) (findViewById(R.id.checkBoxAmo))).isChecked())
            cards[i++] = "amor";
        if (((CheckBox) (findViewById(R.id.checkBoxDie))).isChecked())
            cards[i++] = "dieb";
        if (((CheckBox) (findViewById(R.id.checkBoxHex))).isChecked())
            cards[i++] = "hexe";
        if (((CheckBox) (findViewById(R.id.checkBoxMaed))).isChecked())
            cards[i++] = "maedchen";
        if (((CheckBox) (findViewById(R.id.checkBoxSeh))).isChecked())
            cards[i++] = "seherin";

        for (i = i; i < cards.length; i++) {
            cards[i] = "dorfbewohner";
        }

        String[] cardsShuffled = new String[i];
        //shuffle roles
        for (int j = 0; j<i ; j++){
            //pick a random number in array
            Random random = new Random();
            int value = random.nextInt(i);
            //move on till an empty slot is found
            while(cardsShuffled[value]!=null)
                value=(value+1)%i;

            cardsShuffled[value] = cards[j];
        }

        // TODO: JSON - initialize_table.php  


        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("cards", cardsShuffled);
        startActivity(intent);
    }
    
}
