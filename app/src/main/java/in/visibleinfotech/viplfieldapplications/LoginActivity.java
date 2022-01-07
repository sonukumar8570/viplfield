package in.visibleinfotech.viplfieldapplications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaDrm;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    EditText mobileEt;
    MainConstant constant;
    String ip, db, DBUserNameStr, DBPasswordStr, username, deviceId;
    Connection connect;
    String mobile;
    String eCode;
    String dbIp;
    String dbName;
    String dbUser;
    String dbPass;
    String instance;
    float K_SurveyAccuracy;
    String ftpPath;
    String ftpUser;
    String ftpServer;
    String ftpPass;
    String siteCode;
    String companyName;
    String K_site_code;
    String plantName, plantCode, registeringUser;
    int k_surveyArea;
    AlertDialog dialog;
    TextView employeeNameEt, edtOTP;
    FirebaseAuth mAuth;
    private String verificationId;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    boolean otpVerified = false;

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mobileEt = findViewById(R.id.mobileEt);
        constant = new MainConstant(this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (constant.getUsername() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationPermission();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    122);
        }
    }

    @SuppressLint("MissingPermission")
    public void login(View view) {
        ip = "43.255.152.26";
        db = "vipl";
        DBUserNameStr = "Saini";
        DBPasswordStr = "vipl12345";
        mobile = mobileEt.getText().toString();
        if (mobile.isEmpty() || mobile.length() < 10) {
            Toast.makeText(this, "Zone code or Username is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
       /* for (int i = 0; i < pas.length(); i++) {
            password = password + ((int) pas.charAt(i));
        }*/

        UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
        try {
            MediaDrm wvDrm = new MediaDrm(wideVineUuid);
            byte[] wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            deviceId = bytesToHex(wideVineId);

        } catch (Exception e) {
            // Inspect exception
            Log.d("myname", e.getMessage());
        }

        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute();


    }

    public void iiii(View view) {

        companyName = null;
        showRegistrationDialog();
    }

    TextView companyNameEt;

    void showRegistrationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.device_registration, null);
        final EditText companyCodeEt = view.findViewById(R.id.companySpinner);
        employeeNameEt = view.findViewById(R.id.nameET);
        edtOTP = view.findViewById(R.id.idEdtOtp);
        final EditText employeeMobileEt = view.findViewById(R.id.mobileEtr);
        companyNameEt = view.findViewById(R.id.companyNameEt);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        companyCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String companyCode = companyCodeEt.getText().toString();
                if (companyCode.length() == 15) {
                    plantCode = companyCode;
                    new VerifyCompany().execute();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        employeeMobileEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNUm = employeeMobileEt.getText().toString();
                if (mobileNUm.length() == 10) {
                    mobile = mobileNUm;
                    if (dbIp != null) {
                        new VerifyMobile().execute();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String otp = edtOTP.getText().toString();
                if (otp.length() == 6) {
                  //  verifyCode(otp);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (!otpVerified) {
                    Toast.makeText(LoginActivity.this, "OTP NOT VERIFIED", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if (companyName == null) {
                    Toast.makeText(LoginActivity.this, "Company code is either invalid or inactive.", Toast.LENGTH_SHORT).show();
                    return;
                }
                UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
                try {
                    MediaDrm wvDrm = new MediaDrm(wideVineUuid);
                    byte[] wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
                    deviceId = bytesToHex(wideVineId);

                } catch (Exception e) {
                    // Inspect exception
                    Log.d("myname", e.getMessage());
                }

                if (deviceId == null) {
                    Toast.makeText(LoginActivity.this, "Device Id not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                registeringUser = employeeNameEt.getText().toString();

                if (registeringUser.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                mobile = employeeMobileEt.getText().toString();

                if (mobile.isEmpty() || mobile.length() < 10) {
                    Toast.makeText(LoginActivity.this, "Please enter your valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                String companyCode = companyCodeEt.getText().toString();
                if (companyCode.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your Company Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                plantCode = companyCode;

                new RegisterUserImei().execute();

            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    void closeRegistrationDialog() {
        dialog.dismiss();
    }

    public class VerifyCompany extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;
        ArrayList<LoginDetail> loginDetails;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();
            loginDetails = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                connect = conStr.connectionclasss("Saini", "vipl12345", "vipl", "43.255.152.26", null);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "select k_server,k_uid,k_pwd,k_database,k_dataInstance,k_company_name,K_site_code from  vipl_key where k_key='" + plantCode + "' and k_active='Yes'";
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {

                        flag = true;

                        dbIp = rs.getString("k_server");
                        dbUser = rs.getString("k_uid");
                        dbPass = rs.getString("k_pwd");
                        dbName = rs.getString("k_database");
                        instance = rs.getString("k_dataInstance");
                        companyName = rs.getString("k_company_name");
                        K_site_code = rs.getString("K_site_code");

                        msg = "Access Granted!!!";
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
            if (flag) {
                companyNameEt.setText(companyName);
            } else {
                companyNameEt.setText("code mismatched or not found");

            }
        }
    }

    public class VerifyMobile extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false, result;
        ArrayList<LoginDetail> loginDetails;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();
            loginDetails = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                connect = conStr.connectionclasss(dbUser, dbPass, dbName, dbIp, instance);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    flag = true;
                    String query = "select account_id,account_name from Vipl_Accounts_master where G_enable=1 and G_MobileNo=" + mobile;
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {

                        result = true;
                        username = rs.getString("account_name");
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
            if (!flag) {
                Toast.makeText(LoginActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
            } else {
                if (result) {
                    employeeNameEt.setText(username);
                    Toast.makeText(LoginActivity.this, "Mobile number found", Toast.LENGTH_SHORT).show();
                    //requestOtp();
                } else {
                    employeeNameEt.setText("mobile not found");
                    Toast.makeText(LoginActivity.this, "Mobile number is not in database.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void requestOtp() {
        String phone = "+91" + mobile;
        sendVerificationCode(phone);
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(LoginActivity.this, "Verifying", Toast.LENGTH_SHORT).show();
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Log.d("myname", e.getMessage());
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            otpVerified = true;
                            Toast.makeText(LoginActivity.this, "Opt Verified", Toast.LENGTH_SHORT).show();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public class CheckLogin extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;
        ArrayList<LoginDetail> detailArrayList;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();
            detailArrayList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                connect = conStr.connectionclasss(DBUserNameStr, DBPasswordStr, db, ip, instance);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "select K_SurveyAccuracy,k_surveyArea, k_key,e_code,e_active,e_name,user_mobile,k_server,k_uid,k_pwd,k_database,k_dataInstance,k_ftpIp,k_ftppath,k_ftpUID,k_ftpPWD,k_site_code,k_company_name\n" +
                            " from vipl_imei a inner join vipl_key b on b.k_key=a.e_key where  E_imei1='" + deviceId + "' and E_App = 'field'";
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String k_key = null;
                    flag = false;
                    msg = "device not registered";

                    while (rs.next()) {
                        flag = true;
                        msg = "Device registered";
                        eCode = rs.getString("e_code");
                        String value = rs.getString("e_active").trim();
                        if (!value.equals("Yes")) {
                            msg = "Device is not activated by Admin, value = " + value + " code = " + eCode;
                            continue;
                        }

                        if (!rs.getString("user_mobile").equals(mobile)) {
                            msg = "Device and mobile mismatched, " + eCode + "\n device id = " + deviceId;
                            continue;
                        }

                        k_key = rs.getString("k_key");
                        k_surveyArea = rs.getInt("k_surveyArea");

                        username = rs.getString("e_name");
                        mobile = rs.getString("user_mobile");
                        dbIp = rs.getString("k_server");
                        dbUser = rs.getString("k_uid");
                        dbPass = rs.getString("k_pwd");
                        dbName = rs.getString("k_database");
                        instance = rs.getString("k_dataInstance");
                        ftpServer = rs.getString("k_ftpIp");
                        ftpUser = rs.getString("k_ftpUID");
                        ftpPass = rs.getString("k_ftpPWD");
                        ftpPath = rs.getString("k_ftppath");
                        siteCode = rs.getString("k_site_code");
                        companyName = rs.getString("k_company_name");
                        K_SurveyAccuracy  = rs.getFloat("K_SurveyAccuracy");

                        detailArrayList.add(new LoginDetail(username, mobile, dbIp, dbUser, dbPass, dbName, instance, ftpServer, ftpUser, ftpPass, ftpPath, siteCode, companyName,k_surveyArea,K_SurveyAccuracy));

                    }
                    if (flag) {
                        query = "Select k_active from vipl_key where k_key = '" + k_key + "'";
                        rs = stmt.executeQuery(query);
                        String value;
                        if (rs.next()) {
                            value = rs.getString(1);
                            flag = value.equalsIgnoreCase("Yes");
                        } else {
                            flag = false;
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                msg = e.toString();
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            if (flag) {
                if (detailArrayList.size() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Mobile number registered with multiple companies");
                    builder.setMessage("Select One from below to Continue with : ");
                    Spinner listView = new Spinner(LoginActivity.this);
                    ArrayList<String> mulit = new ArrayList<>();
                    mulit.add("Select Company : ");
                    for (LoginDetail detail : detailArrayList) {
                        mulit.add(detail.getCompanyName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, R.layout.spinner_layout, R.id.spinnerTv, mulit);
                    listView.setAdapter(adapter);

                    builder.setView(listView);
                    listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                LoginDetail loginDetail = detailArrayList.get(position - 1);
                                username = loginDetail.getUsername();
                                mobile = loginDetail.getMobile();
                                dbIp = loginDetail.getDbIp();
                                dbUser = loginDetail.getDbUser();
                                dbPass = loginDetail.getDbPass();
                                dbName = loginDetail.getDbName();
                                instance = loginDetail.getInstance();
                                ftpServer = loginDetail.getFtpServer();
                                ftpUser = loginDetail.getFtpUser();
                                ftpPass = loginDetail.getFtpPass();
                                ftpPath = loginDetail.getFtpPath();
                                siteCode = loginDetail.getSiteCode();
                                companyName = loginDetail.getCompanyName();
                                k_surveyArea=loginDetail.k_surveyArea;
                                K_SurveyAccuracy =loginDetail.K_SurveyAccuracy ;

                                new CheckLogin2().execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    builder.create();
                    builder.show();
                } else {

                    new CheckLogin2().execute();
                }
            } else {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }


        }
    }

    public class CheckLogin2 extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;
        boolean flag = false, record = false;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... voids) {
            ConnectionStr conStr = new ConnectionStr();

            Connection connect = conStr.connectionclasss(dbUser, dbPass, dbName, dbIp, instance);

            if (connect == null) {
                flag = true;
                msg = "Settings are not correct";
            } else {
                Statement stmt = null;
                try {
                    stmt = connect.createStatement();
                    stmt.setQueryTimeout(5000);
                    String q = "select account_id,account_name from Vipl_Accounts_master where G_FactoryCode=" + siteCode + " and G_MobileNo='" + mobile + "'";
                    Log.d("myname", q);
                    msg = "Record Not found";
                    ResultSet rs = stmt.executeQuery(q);
                    record = false;
                    if (rs.next()) {
                        record = true;
                        eCode = rs.getString(1);
                        username = rs.getString(2);
                        msg = "Record Found";
                    }
                } catch (SQLException e) {
                    Log.d("myname", "" + e);
                    msg = e.getMessage();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String v) {
            super.onPostExecute(v);

            progress.dismiss();
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (!flag) {
                if (record) {
                    constant.saveLogin(username, mobile, eCode, dbIp, dbName, dbUser, dbPass, instance, ftpPath, ftpUser, ftpServer, ftpPass, siteCode, companyName,k_surveyArea,K_SurveyAccuracy );

                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    public class RegisterUserImei extends AsyncTask<String, String, String> {
        String msg = "null";
        boolean saved, previous, flag, licenseOver, mobileUsed;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss("Saini", "vipl12345", "vipl", "43.255.152.26", null);

                if (connect == null) {
                    flag = true;
                    return "not connected to internet";
                } else {
                    String query = "select * from VIPL_IMEI where (E_IMEI1 = '" + deviceId + "' or E_IMEI2 = '" + deviceId + "') and E_App = 'field' and E_KEY = '" + plantCode + "'";
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        String value = rs.getString("E_Active");
                        previous = (value.trim()).equalsIgnoreCase("yes");

                    }

                    query = "select * from vipl_imei where user_mobile='" + mobile + "' and E_App = 'field' and E_KEY = '" + plantCode + "'";
                    rs = stmt.executeQuery(query);
                    mobileUsed = rs.next();
                    int k_applicence = 0, count = 0;
                    query = "select isnull(k_applicence,0) k_applicence from vipl_key where K_key='" + plantCode + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        k_applicence = rs.getInt(1);
                    }

                    query = "select  count(*) count from vipl_imei where e_key='" + plantCode + "' and E_active='Yes'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }

                    if (k_applicence == 0 || k_applicence <= count) {
                        licenseOver = true;
                    }
                    if (!previous && !licenseOver && !mobileUsed) {
                        String query2 = "Insert into VIPL_IMEI(E_IMEI1,E_IMEI2,E_name,E_ACTIVE,E_APP,e_key,user_mobile,E_PlantName,E_plantCode)" +
                                "values('" + deviceId + "','" + deviceId + "','" + registeringUser + "','No','field','" + plantCode + "','" + mobile + "','" + companyName + "','" + K_site_code + "')";
                        Log.d("myname", query2);
                        int i = stmt.executeUpdate(query2);
                        saved = i > 0;
                    }
                }
            } catch (
                    Exception e) {
                Log.d("myname", e.toString());
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
            progress.dismiss();

            if (saved) {
                Toast.makeText(LoginActivity.this, "Device has been registered successfully", Toast.LENGTH_SHORT).show();
            } else {
                if (previous) {
                    Toast.makeText(LoginActivity.this, "Your device is already registered and Active", Toast.LENGTH_SHORT).show();
                }
                if (licenseOver) {
                    Toast.makeText(LoginActivity.this, "License Over", Toast.LENGTH_SHORT).show();
                }
                if (mobileUsed) {
                    Toast.makeText(LoginActivity.this, "Mobile number is already registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Already Registered. Contact Admin", Toast.LENGTH_SHORT).show();
                }
            }
            closeRegistrationDialog();
        }
    }
}
