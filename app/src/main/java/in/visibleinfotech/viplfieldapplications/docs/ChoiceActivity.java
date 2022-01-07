package in.visibleinfotech.viplfieldapplications.docs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import in.visibleinfotech.viplfieldapplications.R;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_doc_activity_choice);
    }

    public void registered(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void unregistered(View view) {
        startActivity(new Intent(this, in.visibleinfotech.viplfieldapplications.docs.unregistered.MainActivity.class));
    }
}
