package sopro.werwolf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

public class GameSetupActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setEnabled(false);  -> enabled for debugging
        final EditText editText = (EditText) findViewById(R.id.editText);


        //set values for NumberPicker
        NumberPicker players = (NumberPicker) findViewById(R.id.numberPicker);
        players.setMinValue(8);
        players.setMaxValue(20);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().length()>0) {
                    fab.setEnabled(true);
                }
                else {
                    fab.setEnabled(false);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);

    }

    public void startGame(View view){

        NumberPicker players = (NumberPicker) findViewById(R.id.numberPicker);
        EditText name = (EditText) findViewById(R.id.editText);

        players.getValue();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("players", players.getValue());
        intent.putExtra("name", name.getText().toString());
        startActivity(intent);
    }

}
