package in.visibleinfotech.viplfieldapplications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectable_layout);

        TextView queriesTV=findViewById(R.id.queriesTV);
        queriesTV.setText(getIntent().getStringExtra("log"));
    }
}