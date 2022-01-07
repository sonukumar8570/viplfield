package in.visibleinfotech.viplfieldapplications.field_survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.ImportActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_admin);
    }



    public void advanceSetting(View view) {
        Intent i = new Intent(this, AdvanceSetting.class);
        startActivity(i);
    }
}
