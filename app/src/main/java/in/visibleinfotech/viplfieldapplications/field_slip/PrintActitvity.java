package in.visibleinfotech.viplfieldapplications.field_slip;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class PrintActitvity extends AppCompatActivity {
    TextView myLabel;

    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    EditText plotEt;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String msg = null;
    Button sendButton;
    SharedPreferences sharedPreferences;
    String indentNum, user, tagNo, harvesterCode, harvesterName, growercode, growerName, date, transporterCode, transporterName, caneId, cropId, plotId, mode, villName, villCode;
    String z_code;
    TextView indentTv, farmer_codeTv, date_timeTv, vehicleTv, harvesterTv, villageTv, areaTv,caneTv, cropTv, modeTv, plotNumTv;
    String tokenNum;
    boolean connected;
    String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slip_print_actitvity);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // text label and input box
        myLabel = findViewById(R.id.label);
        farmer_codeTv = findViewById(R.id.farmerCodeTv);
        date_timeTv = findViewById(R.id.dateTv);
        vehicleTv = findViewById(R.id.vehicleTv);
        harvesterTv = findViewById(R.id.harvesterTV);
        caneTv = findViewById(R.id.caneTV);
        cropTv = findViewById(R.id.varietyTv);
        villageTv = findViewById(R.id.villageTv);
        modeTv = findViewById(R.id.modeTv);
        plotNumTv = findViewById(R.id.plotNumTvv);
        indentTv = findViewById(R.id.slipNoTv);
        plotEt = findViewById(R.id.plotET);
        areaTv = findViewById(R.id.areatTV);


      /*  sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        preferences = getSharedPreferences("bluetooth", MODE_PRIVATE);
        editor = preferences.edit();
        String name = preferences.getString("name", null);

        if (null != name) {
            // open bluetooth connection
            new InitBluetooth().execute(name);
        } else {
            showBluetoothDevices();
        }

    }

    @Override
    public void onBackPressed() {

        try {
            closeBT();
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "-1");
        setResult(101, intent);
        super.onBackPressed();
    }

    void findBT(String name) {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                msg = "No bluetooth adapter available";
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals(name)) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            msg = "Bluetooth device found.";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();
            msg = "Bluetooth device connected";
            connected = true;
        } catch (Exception e) {
            msg = "Bluetooth not Conncted";
            connected = false;
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

// this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

// specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        msg = data;

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAndPrint(View view) {
        StringBuffer buffer=new StringBuffer();
        if (mode == null) return;
        MainConstant c = new MainConstant(PrintActitvity.this);
        sendHeight(c.getCompanyName()+"\n");
        buffer.append(" Indent Num : ").append(indentTv.getText().toString()).append("\n").append("Date : ").append(date).append("\nMode: ").append(mode).append("\n");
        String name;
        try {
            name = growerName.substring(0, 22);
        } catch (StringIndexOutOfBoundsException e) {
            name = growerName;
        }
        buffer.append(name);
        //sendBold(name + "\n");

        buffer.append("\nId : ").append(plotId).append(", Ar ").append(area).append("\n").append("Variety : ").append(caneId).append(",Crop: ").append(cropId).append("\n").append("Village").append(villName).append("\n");
        String t;
        try {
            t = transporterName.substring(0, 22);
        } catch (StringIndexOutOfBoundsException e) {
            t = transporterName;
        }
        String h;
        try {
            h = harvesterName.substring(0, 22);
        } catch (StringIndexOutOfBoundsException e) {
            h = harvesterName;
        }
        buffer.append("Harv : ").append(harvesterCode).append("\n").append(h).append("\nTrans : ").append(transporterCode).append("\n").append(t).append("\n");
        sendHeight(buffer.toString());
       // sendBarcode(indentNum);
        sendBold("\n\n---------------------\n\n\n ");


    }

    byte[] arrayOfByte1 = {27, 33, 0};

    void sendBold(String text) {
        try {
            byte[] format = {27, 33, 0};


            format[2] = ((byte) (0x8 | arrayOfByte1[2]));
            mmOutputStream.write(format);

            mmOutputStream.write(text.getBytes(), 0, text.getBytes().length);
        } catch (Exception e) {
            Log.d("myname", e.getMessage());
        }
    }

    void sendSmall(String text) {
        try {
            byte[] format = {27, 33, 0};
            format[2] = ((byte) (0x1 | arrayOfByte1[2]));
            mmOutputStream.write(format);
            try {
                String t = text.substring(0, 22);
                mmOutputStream.write(t.getBytes(), 0, t.getBytes().length);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d("myname", e.getMessage());
                mmOutputStream.write(text.getBytes(), 0, text.getBytes().length);
            }
        } catch (Exception e) {
            Log.d("myname", e.getMessage());
        }
    }

    void sendHeight(String text) {
        try {
            byte[] format = {27, 33, 0};
            format[2] = ((byte) (0x10 | arrayOfByte1[2]));
            mmOutputStream.write(format);
            mmOutputStream.write(text.getBytes(), 0, text.getBytes().length);
        } catch (Exception e) {
            Log.d("myname", e.getMessage());
        }
    }

    void sendBarcode(String text) {
        try {

            int GS = 29;
            int k = 107;
            int m = 73;   //73 code128
            int n = text.length(); //code length
            int HH = 72;
            int Hn = 2;
            byte[] codebytes = text.getBytes();

            mmOutputStream.write((byte) GS);
            mmOutputStream.write((byte) HH);
            mmOutputStream.write((byte) Hn);
            mmOutputStream.write((byte) GS);
            mmOutputStream.write((byte) k);
            mmOutputStream.write((byte) m);
            mmOutputStream.write((byte) n);
            mmOutputStream.write(codebytes);
            sendBold("-------------------------");


        } catch (Exception e) {
            Log.d("myname", e.getMessage());
        }
    }

    public void searchPlot(View view) {
        String plotNum = plotEt.getText().toString();
        if (plotNum.isEmpty()) {
            Toast.makeText(this, "Enter Plot Num", Toast.LENGTH_SHORT).show();
            return;
        }
        new CheckLogin().execute(plotNum);
    }


    class InitBluetooth extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(PrintActitvity.this, "Connecting to Bluetooth Device", "Please wait");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                findBT(strings[0]);
                openBT();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            myLabel.setText(msg);


        }
    }

    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blue_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bluetooth) {
            showBluetoothDevices();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBluetoothDevices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select new Blueetooth Device");
        final ListView listView = new ListView(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        builder.setView(listView);
        if (mBluetoothAdapter == null) {
            builder.setTitle("No bluetooth adapter available");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 2);
        }

        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        ArrayList<String> bluetoothList = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            bluetoothList.add(device.getName());

        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = listView.getItemAtPosition(position).toString();
                editor.putString("name", name).commit();
                new InitBluetooth().execute(name);
            }
        });
        builder.create();
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            showBluetoothDevices();
        }
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;
        boolean flag = false, success = false;
        String deviceId;
        int count;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(PrintActitvity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();
                MainConstant c = new MainConstant(PrintActitvity.this);
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());

                if (connect == null) {
                    success = false;
                } else {
                    String query = "select I_no,I_PlotNumber,I_GrowerCode,I_GrName,I_CreatedOn,I_LandName,I_HarvCode,I_HarvName,I_TransCode,I_TransName,I_CropName,I_VarName," +
                            "I_ModeName,I_PlotVillName,I_area from VIPL_Indent where I_PlotNumber=" + strings[0] +" order by I_no desc";
                    Statement stmt = connect.createStatement();
                    Log.d("mynane", query);
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        flag = true;
                        indentNum = rs.getString("I_no");
                        plotId = rs.getString("I_PlotNumber") + " - " + rs.getString("I_LandName");
                        growercode = rs.getString("I_GrowerCode");
                        growerName = rs.getString("I_GrName");
                        date = rs.getString("I_CreatedOn");
                        harvesterCode = rs.getString("I_HarvCode");
                        harvesterName = rs.getString("I_HarvName");
                        transporterCode = rs.getString("I_TransCode");
                        transporterName = rs.getString("I_TransName");
                        cropId = rs.getString("I_CropName");
                        caneId = rs.getString("I_VarName");
                        mode = rs.getString("I_ModeName");
                        villName = rs.getString("I_PlotVillName");
                        area = rs.getString("I_area");
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {

            Toast.makeText(PrintActitvity.this, msg + "", Toast.LENGTH_LONG).show();
            if (flag) {
                indentTv.setText(indentNum);
                farmer_codeTv.setText(growercode + " - " + growerName);
                date_timeTv.setText(date);
                vehicleTv.setText(transporterCode + " - " + transporterName);
                harvesterTv.setText(harvesterCode + " - " + harvesterName);
                cropTv.setText("" + cropId);
                areaTv.setText("" + area);
                caneTv.setText("" + caneId);
                plotNumTv.setText("" + plotId);
                modeTv.setText(mode);
                villageTv.setText(villName);
            }
            progress.dismiss();
        }
    }
}