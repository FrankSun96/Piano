package frank.example.com.piano;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    protected SoundPool keySound;
    protected int []keyId = {R.raw.key1, R.raw.key2, R.raw.key3, R.raw.key4, R.raw.key5, R.raw.key6, R.raw.key7};
    protected int []poolId;
    protected int []buttonId = {R.id.button1,R.id.button2, R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7};
    protected Button button[] = new Button[7];
    protected ArrayList<Integer> record;
    protected boolean isRecord = false;
    protected int now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            keySound = new SoundPool.Builder().setMaxStreams(7).build();
        }else {
            keySound = new SoundPool(7, AudioManager.STREAM_SYSTEM, 0);
        }
        poolId = new int[7];
        record = new ArrayList<Integer>();
        for(int i = 0; i < 7; i++) {
            button[i] = (Button) findViewById(buttonId[i]);
            poolId[i] = keySound.load(this, keyId[i],1);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button key = (Button)view;
                    int current;
                    current = Integer.parseInt(key.getText().toString());
                    if(isRecord){
                        record.add(current - 1);
                    }
                    keySound.play(poolId[current - 1], 5, 5, 5, 0, 1);
                }
            });
        }
    }
    public void onShareClick(View view){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "share#");
        share.putExtra(Intent.EXTRA_TEXT, "piano is so fun! come and play with me!");
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(share,getTitle()));
    }

    public void onPlayClick(View view){
        if(record.isEmpty()){
            Toast.makeText(MainActivity.this, "You don't have any record. Please record your music firstly", Toast.LENGTH_LONG).show();
        }else{
            for(int i = 0; i < record.size(); i++){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                now = keySound.play(poolId[record.get(i)], 5, 5, 5, 0, 1);
            }
        }
    }

    public void onRecClick(View view){
        Button record = (Button)findViewById(R.id.btnRec);
        if(!isRecord){
            record.setText("PAUSE");
            isRecord = true;
        }else{
            record.setText("RECORD");
            isRecord = false;
        }
    }

    public void onNewRec(View view){
        record.clear();
    }

    protected void onPause(){
        super.onPause();
        keySound.pause(now);
    }

    protected  void onResume(){
        super.onResume();
        keySound.resume(now);
    }

}
