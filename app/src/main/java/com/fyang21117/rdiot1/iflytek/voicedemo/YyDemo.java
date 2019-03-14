package com.fyang21117.rdiot1.iflytek.voicedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.fyang21117.rdiot1.R;
import com.fyang21117.rdiot1.iflytek.speech.setting.IatSettings;
import com.fyang21117.rdiot1.iflytek.speech.util.FucUtil;
import com.fyang21117.rdiot1.iflytek.speech.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.sunflower.FlowerCollector;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class YyDemo extends Activity  implements View.OnClickListener{
    private static String TAG = YyDemo.class.getSimpleName();

    // 语音听写对象
    private SpeechRecognizer mYy;
    // 用HashMap存储听写结果
    private HashMap<String, String> mYyResults = new LinkedHashMap<>();
    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable = true;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    String voicer = "vixy";
    String texts = "";
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    private int mVolume = 0;

    StringBuffer resultBuffer = new StringBuffer();
    String cmd = resultBuffer.toString();
    String words[]=new String[]{
            "打开空气净化器","打开净化器","净化器",
            "打开风扇","打开智能风扇","风扇",
            "打开智能电饭煲","打开电饭煲","电饭煲",
            "打开微信","微信","open WeChat",
            "打开QQ","打开腾讯QQ","QQ",
            "打开设置","打开手机设置","设置",
            "打开摄像头","打开拍照","摄像头",
            "打开电话","打电话","电话",
            "打开信息","信息","发信息"
    };

    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yydemo);
        texts = getResources().getString(R.string.text_tts_source);
        initLayout();

        // 初始化识别无UI识别对象
        mYy = SpeechRecognizer.createRecognizer(YyDemo.this, mInitListener);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(YyDemo.this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mResultText = findViewById(R.id.yy_text);

        // 上传联系人和热词表
        ContactManager mgr = ContactManager.createManager(YyDemo.this, mContactListener);
        mgr.asyncQueryAllContactsName();
        String contents = FucUtil.readFile(YyDemo.this, "userwords","utf-8");
        mYy.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        ret = mYy.updateLexicon("userword", contents, mLexiconListener);
        if (ret != ErrorCode.SUCCESS)
            showTip("上传热词失败,错误码：" + ret);
    }

    private void initLayout() {
        findViewById(R.id.yy_start).setOnClickListener(YyDemo.this);
        findViewById(R.id.yy_answer).setOnClickListener(YyDemo.this);
    }
    int ret = 0; // 函数调用返回值

    @Override
    public void onClick(View view) {
        if( null == mYy || null ==mTts){
             this.showTip( "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化" );
            return;
        }
        setParam();  // 设置参数

        switch (view.getId()) {
            // 开始听写
            case R.id.yy_start: {
               // new Thread(new Runnable() { @Override
                //                    public void run() {}}).start();

                        FlowerCollector.onEvent(YyDemo.this, "iat_recognize");
                        mResultText.setText(null);// 清空显示内容
                        mYyResults.clear();
                        ret = mYy.startListening(mRecognizerListener);
                        if (ret != ErrorCode.SUCCESS)
                            showTip("听写失败,错误码：" + ret);
                        else
                            showTip(getString(R.string.text_begin));


            }break;

            case R.id.yy_answer:
                // 移动数据分析，收集开始合成事件
                FlowerCollector.onEvent(YyDemo.this, "tts_play");
                //设置文本源，进行合成。加个模糊算法判断关键词，作出回复
                String text = ((EditText) findViewById(R.id.yy_text)).getText().toString();
                int code = mTts.startSpeaking(text, mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code);
                }break;
            default:
                break;
        }
    }
    /**
     * 初始化监听器。初始化单例对象时，通过此回调接口，获取初始化状态。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 上传联系人/词表监听器。
     */
    private LexiconListener mLexiconListener = new LexiconListener() {
        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                showTip(error.toString());
            } else {
                showTip(getString(R.string.text_upload_success));
            }
        }
    };

    /**
     * 听写监听器。
     *通过实现此接口，获取当前识别的状态和结果
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
            //如何持续使用录音机进行录音判断？
        }
        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }
        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            if (isLast) {
                FlowerCollector.onEvent(YyDemo.this, "iat_recognize");
                String text_speak = ((EditText) findViewById(R.id.yy_text)).getText().toString();
                int code;

               if (text_speak.equals(words[0])||text_speak.equals(words[1])||text_speak.equals(words[2]))
                    code = mTts.startSpeaking("空净已打开，have a good day", mTtsListener);
               else if (text_speak.equals(words[3])||text_speak.equals(words[4])||text_speak.equals(words[5]))
                   code = mTts.startSpeaking("智能风扇已打开，have a good day", mTtsListener);
               else if (text_speak.equals(words[6])||text_speak.equals(words[7])||text_speak.equals(words[8]))
                    code = mTts.startSpeaking("电饭煲已启动，等我为你做一顿晚饭吧~", mTtsListener);
               else if (text_speak.equals(words[9])||text_speak.equals(words[10])||text_speak.equals(words[11]))
                    code = mTts.startSpeaking("微信已启动，您需要找谁？", mTtsListener);
               else if (text_speak.equals(words[12])||text_speak.equals(words[13])||text_speak.equals(words[14]))
                    code = mTts.startSpeaking("QQ已启动，您想要找哪位好友？", mTtsListener);
               else if (text_speak.equals(words[15])||text_speak.equals(words[16])||text_speak.equals(words[17]))
                    code = mTts.startSpeaking("您已进入手机设置，下一步是什么？", mTtsListener);
               else if (text_speak.equals(words[18])||text_speak.equals(words[19])||text_speak.equals(words[20]))
                    code = mTts.startSpeaking("您已打开手机摄像头，拍照可以和我说一声茄子哦", mTtsListener);
               else if (text_speak.equals(words[21])||text_speak.equals(words[22])||text_speak.equals(words[23]))
                    code = mTts.startSpeaking("已打开电话拨号，您想打给谁？", mTtsListener);
               else if (text_speak.equals(words[24])||text_speak.equals(words[25])||text_speak.equals(words[26]))
                    code = mTts.startSpeaking("已打开信息，您想发送给哪位？", mTtsListener);
               else
                   code = mTts.startSpeaking("请你慢点说，我听得不太清楚~", mTtsListener);

               mResultText.setText("正在回复，请稍等！");
                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code);
                }
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            mVolume = volume;
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {

        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mYyResults.put(sn, text);

        for (String key : mYyResults.keySet()) {
            resultBuffer.append(mYyResults.get(key));
        }
        mResultText.setText(cmd);
        mResultText.setSelection(mResultText.length());
        //语音转为文字输出
        //判断字符属于指令，跳转执行
  /*      if (cmd.equals(words[0])||cmd.equals(words[1])||cmd.equals(words[2])){
                Toast.makeText(YyDemo.this, String.valueOf(R.string.text_testactivity), Toast.LENGTH_SHORT).show();
                testActivity.actionStart(YyDemo.this);
            }
        if (cmd.equals(words[3])||cmd.equals(words[4])||cmd.equals(words[5])){
                Toast.makeText(YyDemo.this, String.valueOf(R.string.text_test2activity), Toast.LENGTH_SHORT).show();
                test2Activity.actionStart(YyDemo.this);
            }
        if (cmd.equals(words[6])||cmd.equals(words[7])||cmd.equals(words[8])){
            Toast.makeText(YyDemo.this, String.valueOf(R.string.text_test4activity), Toast.LENGTH_SHORT).show();
            test4Activity.actionStart(YyDemo.this);
        }
        if (cmd.equals(words[9])||cmd.equals(words[10])||cmd.equals(words[11])) {
            String url = "weixin://";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        if (cmd.equals(words[12])||cmd.equals(words[13])||cmd.equals(words[14])) {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=123456";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        if (cmd.equals(words[15])||cmd.equals(words[16])||cmd.equals(words[17])) {
            FingerprintUtil.openFingerPrintSettingPage(YyDemo.this);
        }
        if (cmd.equals(words[18])||cmd.equals(words[19])||cmd.equals(words[20])) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        }
        if (cmd.equals(words[21])||cmd.equals(words[22])||cmd.equals(words[23])) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + "119");
            intent.setData(data);
            startActivity(intent);
        }
        if (cmd.equals(words[24])||cmd.equals(words[25])||cmd.equals(words[26])) {
            Uri uri2 = Uri.parse("smsto:" + 10086);
            Intent intentMessage = new Intent(Intent.ACTION_VIEW, uri2);
            startActivity(intentMessage);
        }*/
    }


    /**
     * 听写UI监听器
     * 通过实现此接口，获取识别对话框识别过程的结果和错误信息。
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if( mTranslateEnable ){
                printTransResult( results );
            }else{
                printResult(results);
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

    };

    /**
     * 获取联系人监听器。
     */
    private ContactManager.ContactListener mContactListener = new ContactManager.ContactListener() {
        @Override
        public void onContactQueryFinish(final String contactInfos, boolean changeFlag) {
            runOnUiThread(new Runnable() {
                public void run() {
                    showTip("上传联系人成功");
                    //showContacts.setText(contactInfos);
                }
            });
            mYy.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);
            mYy.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            ret = mYy.updateLexicon("contact", contactInfos, mLexiconListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("上传联系人失败：" + ret);
            }
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /*
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mYy.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mYy.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mYy.setParameter(SpeechConstant.RESULT_TYPE, "json");
        this.mTranslateEnable = mSharedPreferences.getBoolean(this.getString(R.string.pref_key_translate), false );
        if( mTranslateEnable ){
            Log.i( TAG, "translate enable" );
            mYy.setParameter( SpeechConstant.ASR_SCH, "1" );
            mYy.setParameter( SpeechConstant.ADD_CAP, "translate" );
            mYy.setParameter( SpeechConstant.TRS_SRC, "its" );
        }
        String lag = mSharedPreferences.getString("Yy_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mYy.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mYy.setParameter(SpeechConstant.ACCENT, null);
            if( mTranslateEnable ){
                mYy.setParameter( SpeechConstant.ORI_LANG, "en" );
                mYy.setParameter( SpeechConstant.TRANS_LANG, "cn" );
            }
        } else {
            // 设置语言
            mYy.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mYy.setParameter(SpeechConstant.ACCENT, lag);
            if( mTranslateEnable ){
                mYy.setParameter( SpeechConstant.ORI_LANG, "cn" );
                mYy.setParameter( SpeechConstant.TRANS_LANG, "en" );
            }
        }
        // 超时处理
        mYy.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("Yy_vadbos_preference", "4000"));
        //  自动停止录音
        mYy.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("Yy_vadeos_preference", "1000"));
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mYy.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("Yy_punc_preference", "1"));
        // 设置音频保存路径
        mYy.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mYy.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/Yy.wav");

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }

    private void printTransResult (RecognizerResult results) {
        String trans  = JsonParser.parseTransResult(results.getResultString(),"dst");
        String oris = JsonParser.parseTransResult(results.getResultString(),"src");

        if( TextUtils.isEmpty(trans)||TextUtils.isEmpty(oris) ){
            showTip( "解析结果失败，请确认是否已开通翻译功能。" );
        }else{
            mResultText.setText( "原始语言:\n"+oris+"\n目标语言:\n"+trans );
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( null != mYy ){
            // 退出时释放连接
            mYy.cancel();
            mYy.destroy();
        }
    }
    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(YyDemo.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }
    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(YyDemo.this);
        super.onPause();
    }



    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };
    /**
     * tts合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }
        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }
        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));

            SpannableStringBuilder style=new SpannableStringBuilder(texts);
            Log.e(TAG,"beginPos = "+beginPos +"  endPos = "+endPos);
            style.setSpan(new BackgroundColorSpan(Color.RED),beginPos,endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.e("MscSpeechLog", "buf is =" + buf);
            }
        }
    };
}
