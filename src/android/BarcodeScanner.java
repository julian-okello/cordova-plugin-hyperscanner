package cordova.plugin.hyperscanner;

import android.app.Activity;
import android.os.AsyncTask;

import com.zebra.adc.decoder.Barcode2DWithSoft;

public class BarcodeScanner {

    private Barcode2DWithSoft barcode2DWithSoft;
    private Activity activity;
    private OnScannerCallback scanCallback;
    private boolean scanning = false;

    private cordova.plugin.hyperscanner.BeepHelper beepHelper;

    public interface OnScannerCallback {
        void success(String barcode);

        void error(String error);
    }

    public BarcodeScanner(Activity activity, OnScannerCallback scanCallback){
        this.activity = activity;
        this.scanCallback = scanCallback;
        new InitTask().execute();
    }

    public void startScan(){
        if(barcode2DWithSoft!=null) {
            barcode2DWithSoft.scan();
            barcode2DWithSoft.setScanCallback(scanBack);
            scanning = true;
        }
    }

    public void stopScan() {
        if (barcode2DWithSoft != null && scanning){
            barcode2DWithSoft.stopScan();
            scanning = false;
        }
    }

    public void destroy(){
        if (barcode2DWithSoft != null){
            barcode2DWithSoft.stopScan();
            barcode2DWithSoft.close();
        }
    }

    private Barcode2DWithSoft.ScanCallback scanBack = new Barcode2DWithSoft.ScanCallback(){
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length < 1) {
                destroy();
                if (length == -1) {
                    // Scan cancel
                    scanCallback.error("Scan cancelled");

                } else if (length == 0) {
                    // Scan TimeOut
                    scanCallback.error("Scan time out");
                } else {
                    // Scan fail
                    scanCallback.error("Scan failed");
                }
            } else{
                beepHelper = new BeepHelper();
                beepHelper.beep();

                String barCode = new String(bytes, 0, length);
                scanCallback.success(barCode);
            }
        }
    };

    private class InitTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;

            try {
                if(barcode2DWithSoft == null){
                    barcode2DWithSoft = Barcode2DWithSoft.getInstance();
                }

                if(barcode2DWithSoft != null) {
                    result = barcode2DWithSoft.open(activity);
                }
            } catch(Exception e){

            }


            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result){
                barcode2DWithSoft.setParameter(324, 1);
                barcode2DWithSoft.setParameter(300, 0); // Snapshot Aiming
                barcode2DWithSoft.setParameter(361, 0); // Image Capture Illumination

                // interleaved 2 of 5
                barcode2DWithSoft.setParameter(6, 1);
                barcode2DWithSoft.setParameter(22, 0);
                barcode2DWithSoft.setParameter(23, 55);

                barcode2DWithSoft.setTimeOut(30000);

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }


}