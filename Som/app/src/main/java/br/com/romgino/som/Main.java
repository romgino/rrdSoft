package br.com.romgino.som;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.*;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.romgino.som.dao.RegistroDAO;
import br.com.romgino.som.modelo.Registro;


public class Main extends ActionBarActivity{
    // The sampling rate for the audio recorder.
    private static final int SAMPLING_RATE = 44100;

    private WaveformView mWaveformView;
    private TextView mDecibelView;
    private TextView mDecibel;

    private RecordingThread mRecordingThread;
    private int mBufferSize;
    private short[] mAudioBuffer;
    private String mDecibelFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mWaveformView = (WaveformView) findViewById(R.id.waveform_view);
        mDecibelView = (TextView) findViewById(R.id.decibel_view);
        mDecibel = (TextView)findViewById(R.id.resultado);

        // Compute the minimum required audio buffer size and allocate the buffer.
        mBufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mAudioBuffer = new short[mBufferSize / 2];

        mDecibelFormat = getResources().getString(R.string.decibel_format);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRecordingThread = new RecordingThread();
        mRecordingThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mRecordingThread != null) {
            mRecordingThread.stopRunning();
            mRecordingThread = null;
        }
    }

    /**
     * A background thread that receives audio from the microphone and sends it to the waveform
     * visualizing view.
     */
    private class RecordingThread extends Thread {

        private boolean mShouldContinue = true;

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

            AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, mBufferSize);
            record.startRecording();

            while (shouldContinue()) {
                record.read(mAudioBuffer, 0, mBufferSize / 2);
                mWaveformView.updateAudioData(mAudioBuffer);

                updateDecibelLevel();


            }

            record.stop();
            record.release();
        }

        /**record.getRecordingState()
         * Gets a value indicating whether the thread should continue running.
         *
         * @return true if the thread should continue running or false if it should stop
         */
        private synchronized boolean shouldContinue() {
            return mShouldContinue;
        }

        /**
         * Notifies the thread that it should stop running at the next opportunity.
         */
        public synchronized void stopRunning() {
            mShouldContinue = false;
        }

        /**
         * Computes the decibel level of the current sound buffer and updates the appropriate text
         * view.
         */
        private void updateDecibelLevel() {
            // Compute the root-mean-squared of the sound buffer and then apply the formula for
            // computing the decibel level, 20 * log_10(rms). This is an uncalibrated calculation
            // that assumes no noise in the samples; with 16-bit recording, it can range from
            // -90 dB to 0 dB.   -->32768.0
             final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            double sum = 0;

            for (short rawSample : mAudioBuffer) {
                double sample = rawSample / 32768.0;
                sum += sample * sample;
            }

            double rms = Math.sqrt(sum / mAudioBuffer.length);
            final double db = 20 * Math.log10(rms);
            final double ap = (1 - db / -90) * 100;



            // Update the text view on the main thread.
            mDecibelView.post(new Runnable() {
                @Override
                public void run() {
                    mDecibelView.setText(String.format(mDecibelFormat, db));

                }
            });
            mDecibel.post(new Runnable() {
                @Override
                public void run() {
                    //mDecibel.setText(String.valueOf(ap));
                    mDecibel.setText(String.format("%.2f"+"dB", ap));
                    if (ap > 80){
                        vibe.vibrate(500);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
                        String dataFormatada = sdf.format(hora);

                        //gravar no banco
                        //Registro rg = new Registro();
                        //rg.setData(hora);
                        //rg.setDecibel((float) ap);
                        //RegistroDAO dao = new RegistroDAO(getApplicationContext());
                        //if (dao.adicionar(rg)){
                        //    Toast.makeText(getApplicationContext(),"ok    -  "+ dataFormatada ,Toast.LENGTH_SHORT).show();

                        //}else {
                        //    Toast.makeText(getApplicationContext(),"erro ",Toast.LENGTH_SHORT).show();
                        //}



                    }

                }
            });
        }

/*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }*/
    }
}
