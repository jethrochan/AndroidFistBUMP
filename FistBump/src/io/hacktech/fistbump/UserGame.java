package io.hacktech.fistbump;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class UserGame extends Activity {
	public String name;
	public String joke;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
 
       
		setContentView(R.layout.game_play);
		//bindFinishedButton();
    }

}
