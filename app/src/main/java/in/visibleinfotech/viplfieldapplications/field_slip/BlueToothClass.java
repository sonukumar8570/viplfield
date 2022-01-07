package in.visibleinfotech.viplfieldapplications.field_slip;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BlueToothClass {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String msg;
    boolean connected;

    public BlueToothClass(Context context) {

    }

    void findBT(String name) {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                msg = "No bluetooth adapter available";
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
            Log.d("myname", text);
            byte[] format = {27, 33, 0};
            format[2] = ((byte) (0x1 | arrayOfByte1[2]));
            mmOutputStream.write(format);

                mmOutputStream.write(text.getBytes(), 0,text.getBytes().length);

        } catch (Exception e) {
            Log.d("myname", e.getMessage());
        }
    }

    public void sendHeight(String text) {
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

        } catch (Exception e) {
            Log.d("myname", e.getMessage());
        }
    }
}
