package in.visibleinfotech.viplfieldapplications.bill_entry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class BillEntry extends AppCompatActivity {
    MainConstant constant;
    Calendar calendar;
    Spinner dayBookSpinner, serviceSpinner;
    String fromDate;
    String rate;
    TextView amountTv;
    EditText rateEt, areaEt, hourEt, minEt, remarkEt;
    String partyDebit, partyCredit, dayBook, service, remark;
    double hour, min, area, amount;
    AppCompatAutoCompleteTextView partyDebitEt, partyCreditEt, villageCredtEt, villageDebitEt;
    int choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_entry);
        calendar = Calendar.getInstance();
        constant = new MainConstant(this);
        dayBookSpinner = findViewById(R.id.dayBookSpinner);
        serviceSpinner = findViewById(R.id.serviceSpinner);
        rateEt = findViewById(R.id.rateET);
        areaEt = findViewById(R.id.areaEt);
        hourEt = findViewById(R.id.hourEt);
        minEt = findViewById(R.id.minEt);
        amountTv = findViewById(R.id.amountTv);
        partyDebitEt = findViewById(R.id.partyDebitET);
        partyCreditEt = findViewById(R.id.partyCreditEt);
        villageCredtEt = findViewById(R.id.villageCreditEt);
        villageDebitEt = findViewById(R.id.vilageDebitET);
        remarkEt = findViewById(R.id.remarkEt);
        new DouwnloadDetail().execute();

        minEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amountTv.setText("Calculate Amount");
                amount = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        areaEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amountTv.setText("Calculate Amount");
                amount = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        hourEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amountTv.setText("Calculate Amount");
                amount = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rateEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amountTv.setText("Calculate Amount");
                amount = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void calender(final View view) {

        DatePickerDialog pickerDialog = new DatePickerDialog(BillEntry.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker vie, int year, int month, int dayOfMonth) {
                String d, m;
                month = month + 1;
                if (month < 10) {
                    m = "0" + month;
                } else {
                    m = "" + month;
                }
                if (dayOfMonth < 10) {
                    d = "0" + dayOfMonth;
                } else {
                    d = "" + dayOfMonth;
                }
                fromDate = year + "-" + m + "-" + d;
                ((TextView) view).setText(d + "/" + m + "/" + year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();

    }

    public void save(View view) {
        service = serviceSpinner.getSelectedItem().toString();
        dayBook = dayBookSpinner.getSelectedItem().toString();
        partyCredit = partyCreditEt.getText().toString();
        partyDebit = partyDebitEt.getText().toString();
        if (amount == 0.0) {
            Toast.makeText(this, "Calcuate Amount first", Toast.LENGTH_SHORT).show();
            return;
        }
        partyDebit = partyDebit.split(":")[0].trim();
        if (service.isEmpty()) {
            showErrorAlert("Service name Missing");
            return;
        }
        service = service.split(":")[0].trim();
        if (partyDebit.isEmpty()) {
            showErrorAlert("Debited party name Missing");
            return;
        }
        if (partyCredit.isEmpty()) {
            showErrorAlert("Credited party name Missing");
            return;
        }
        partyCredit = partyCredit.split(":")[0].trim();
        dayBook = dayBook.split(":")[0].trim();
        remark = remarkEt.getText().toString();
        new SaveDetail().execute();
    }

    public void clearAll(View view) {
        clear();
    }

    public void calculateAmount(View view) {
        String h = hourEt.getText().toString();
        if (h.isEmpty()) {
            showErrorAlert("Enter Hour Time");
            return;
        }
        String m = minEt.getText().toString();
        if (m.isEmpty()) {
            showErrorAlert("Enter minute Time");
            return;
        }
        String a = areaEt.getText().toString();
        if (a.isEmpty()) {
            showErrorAlert("Enter Area");
            return;
        }
        hour = Double.parseDouble(h);
        min = Double.parseDouble(m);
        if (min < 0 || min > 60) {
            showErrorAlert("Enter valid Minute value");
            return;
        }
        area = Double.parseDouble(a);
        service = serviceSpinner.getSelectedItem().toString();
        new Calculate().execute();

    }

    public class SaveDetail extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(BillEntry.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), null);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    Statement stmt = connect.createStatement();


                    String query = "insert into Vipl_CaneDevApp (C_DrParty,C_CrParty,C_SerCode,C_Area,C_TimeHr,C_TimeMi,C_Rate,C_Amount,C_CreateBy,C_Date,C_Remark,C_SiteCode,C_Status,C_BookCode,C_EntDate) \n" +
                            "values(" + partyDebit + "," + partyCredit + "," + service + "," + area + "," + hour + "," + min + "," + rate + "," + amount + ",'" + constant.getUsername() + "','" + fromDate + "','" + remark + "'," + constant.getSiteCode() + ",4," + dayBook + ",getDate())";
                    Log.d("myname", "doInBackground: " + query);
                    stmt.executeUpdate(query);
                    flag = true;

                }
            } catch (Exception e) {
                flag = false;
                msg = e.toString();
                Log.d("myname", e.toString());
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            if (flag) {
                clear();
                Toast.makeText(BillEntry.this, "Saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BillEntry.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    void clear() {
        areaEt.setText("");
        area = 0;
        partyDebitEt.setText("");
        partyCreditEt.setText("");
        remarkEt.setText("");
        hourEt.setText("");
        minEt.setText("");
        villageCredtEt.setText("");
        villageDebitEt.setText("");
        hour = min = area = 0;
        partyCredit = partyDebit = rate = null;
    }

    void showErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public class DouwnloadDetail extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;
        ArrayList<String> dayBookList, placeList, serviceList;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(BillEntry.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                dayBookList = new ArrayList<>();
                placeList = new ArrayList<>();
                serviceList = new ArrayList<>();
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), null);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "Select Acc_Book_Id, acc_book_description from Vipl_Book_Master where Book_Type='CD'  and sitecode= " + constant.getSiteCode() + " order by acc_book_description asc";
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        dayBookList.add(rs.getString(1) + " : " + rs.getString(2));
                    }
                    query = "select s_code,s_name  from Vipl_CaneDevService where s_sitecode=" + constant.getSiteCode();
                    rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        serviceList.add(rs.getString(1) + " : " + rs.getString(2));
                    }
                    query = "select place_id,place_Name from vipl_place_master where sitecode=" + constant.getSiteCode();
                    rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        placeList.add(rs.getString(1) + " : " + rs.getString(2));
                    }


                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            ArrayAdapter itemAdapter = new ArrayAdapter<String>(BillEntry.this, R.layout.spinner_layout, R.id.spinnerTv, dayBookList);
            dayBookSpinner.setAdapter(itemAdapter);
            ArrayAdapter serviceAdapter = new ArrayAdapter<String>(BillEntry.this, R.layout.spinner_layout, R.id.spinnerTv, serviceList);
            serviceSpinner.setAdapter(serviceAdapter);
            ArrayAdapter partyAdapter = new ArrayAdapter<String>(BillEntry.this, R.layout.spinner_layout, R.id.spinnerTv, placeList);
            villageCredtEt.setAdapter(partyAdapter);
            villageDebitEt.setAdapter(partyAdapter);
            villageCredtEt.setThreshold(1);
            villageDebitEt.setThreshold(1);
            villageDebitEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    choice = 1;
                    new DownloadParties().execute(villageDebitEt.getText().toString().split(":")[0].trim());
                }
            });
            villageCredtEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    choice = 2;
                    new DownloadParties().execute(villageCredtEt.getText().toString().split(":")[0].trim());

                }
            });
            serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    new DownloadRates().execute(serviceSpinner.getSelectedItem().toString().split(":")[0].trim());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public class DownloadParties extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;
        ArrayList<String> partyList;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(BillEntry.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                partyList = new ArrayList<>();
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), null);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "select account_id,account_name from Vipl_Accounts_master where place_id = " + strings[0] + " and G_FactoryCode=" + constant.getSiteCode();
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        partyList.add(rs.getString(1) + " : " + rs.getString(2));
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();

            ArrayAdapter partyAdapter = new ArrayAdapter<String>(BillEntry.this, R.layout.spinner_layout, R.id.spinnerTv, partyList);
            if (choice == 1) {
                partyDebitEt.setAdapter(partyAdapter);
            }
            if (choice == 2) {
                partyCreditEt.setAdapter(partyAdapter);
            }
            partyDebitEt.setThreshold(1);
            partyCreditEt.setThreshold(1);

        }
    }

    public class DownloadRates extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;
        ArrayList<String> partyList;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(BillEntry.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                partyList = new ArrayList<>();
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), null);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "select s_rate from Vipl_CaneDevService where s_code=" + strings[0] + " and s_sitecode=" + constant.getSiteCode();
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        rate = rs.getString(1);
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            rateEt.setText(rate);
            rateEt.setEnabled(false);
            amount = 0;
            amountTv.setText("Calculate Amount :");
        }
    }

    public class Calculate extends AsyncTask<String, String, String> {
        ProgressDialog progress;
        boolean flag = false;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(BillEntry.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), null);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    Statement stmt = connect.createStatement();
                    String query = "select s_rate from Vipl_CaneDevService where s_code=" + service.split(":")[0].trim() + " and s_sitecode = " + constant.getSiteCode();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        rate = rs.getString(1);
                    }


                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            rateEt.setText(rate);
            rateEt.setEnabled(false);
            amount = Math.round(Math.round(hour) * Double.parseDouble(rate) + Math.round(min) * 0.0167 * Double.parseDouble(rate));
            amountTv.setText("Amount = " + amount);
        }
    }
}

