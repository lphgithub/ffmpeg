package com.cairh.ffmpeg;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private EditText etInput;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btn_start);
        etInput = (EditText) findViewById(R.id.et_input);
        dialog = new ProgressDialog(this);
        final FFmpeg fFmpeg = FFmpeg.getInstance(this);
        try {
            fFmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onSuccess() {
                    super.onSuccess();
                    String cmd = etInput.getText().toString();
                    final String[] cmds = cmd.split(" ");
                    Log.e(TAG, cmd);
                    btnStart.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                fFmpeg.execute(cmds, new FFmpegExecuteResponseHandler() {
                                    @Override
                                    public void onSuccess(String s) {
                                        Log.e(TAG, ">>>>onSuccess" + s);
                                        dialog.setMessage("处理成功");
                                    }

                                    @Override
                                    public void onProgress(String s) {
                                        Log.e(TAG, ">>>>onProgress" + s);
                                        dialog.setMessage(s);
                                    }

                                    @Override
                                    public void onFailure(String s) {
                                        Log.e(TAG, ">>>>onFailure" + s);
                                        dialog.dismiss();
                                        dialog.setMessage("处理失败");
                                    }

                                    @Override
                                    public void onStart() {
                                        Log.e(TAG, ">>>>onStart");
                                        dialog.show();
                                        dialog.setMessage("开始处理");
                                    }

                                    @Override
                                    public void onFinish() {
                                        Log.e(TAG, ">>>>onFinish");

                                    }
                                });
                            } catch (FFmpegCommandAlreadyRunningException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }


}
