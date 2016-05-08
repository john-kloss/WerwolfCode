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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    Button currentlySelectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void playerSelected(View view){
        boolean alreadyClicked = false;
        Button button = (Button) view;

        //get all buttons and make them transparent
        ViewGroup parentView =(ViewGroup) findViewById(R.id.gameView);
        for (int i=0; i < parentView.getChildCount(); i++){
            Button currentButton = (Button) (parentView.getChildAt(i));
            currentButton.setBackgroundColor(Color.TRANSPARENT);
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
