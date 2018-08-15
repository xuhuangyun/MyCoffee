package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.utils.SizeUtil;

import java.lang.reflect.Method;
/*
 *键盘view
 */

/**
 * 取货码界面
 */
public class CalView extends RelativeLayout {
    CallBack callBack;

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface  CallBack{
        void onOk(String code);
    }
    private MyButton btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btna,btnb,btne,btnc,btnok;
    private MyEditText edt_code;//显示输入的数字

    /**
     *
     */
    private void keyPressed(int keyCode)
    {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        edt_code.onKeyDown(keyCode, event);
        //键按下时被调用，
    }
    //跟据被选择按钮的id设置监听器
    private OnClickListener lisenter = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub


            try{

                switch(v.getId())
                {
                    case R.id.button0:
                    {
                        keyPressed(KeyEvent.KEYCODE_0);
                        break;
                    }
                    case R.id.button1:
                    {
                        keyPressed(KeyEvent.KEYCODE_1);
                        break;
                    }
                    case R.id.button2:
                    {
                        keyPressed(KeyEvent.KEYCODE_2);
                        break;
                    }
                    case R.id.button3:
                    {
                        keyPressed(KeyEvent.KEYCODE_3);
                        break;
                    }
                    case R.id.button4:
                    {
                        keyPressed(KeyEvent.KEYCODE_4);
                        break;
                    }
                    case R.id.button5:
                    {
                        keyPressed(KeyEvent.KEYCODE_5);
                        break;
                    }
                    case R.id.button6:
                    {
                        keyPressed(KeyEvent.KEYCODE_6);
                        break;
                    }
                    case R.id.button7:
                    {
                        keyPressed(KeyEvent.KEYCODE_7);
                        break;
                    }
                    case R.id.button8:
                    {
                        keyPressed(KeyEvent.KEYCODE_8);
                        break;
                    }
                    case R.id.button9:
                    {
                        keyPressed(KeyEvent.KEYCODE_9);
                        break;
                    }
                    case R.id.buttona://*
                    {
                        keyPressed(KeyEvent.KEYCODE_STAR);
                        break;
                    }
                    case R.id.buttonb://#
                    {
                        keyPressed(KeyEvent.KEYCODE_POUND);
                        break;
                    }
                    case R.id.buttone://删除
                    {
                        keyPressed(KeyEvent.KEYCODE_DEL);
                        break;
                    }
                    case R.id.buttonc://清空
                    {
                        edt_code.setText("");
                        break;
                    }
                    case R.id.buttonok:
                    {
                    callBack.onOk(edt_code.getText().toString());
                        break;
                    }

                }
            }catch(Exception e){}
        }
    };
    public void disableShowInput(){
        edt_code.setLongClickable(false);  //禁止长按事件
        edt_code.setMaxEms(10);//返回TextView的最大像素宽度，或如果使用setMaxEms(int)或setEms(int)设置的最大宽度，则返回-1。
        edt_code.setMaxLines(1);//设置为TextView的最大高度1
        edt_code.setTextIsSelectable(false);//设置用户是否可以选择此视图的内容。
        if (android.os.Build.VERSION.SDK_INT <= 10){
            edt_code.setInputType(InputType.TYPE_NULL);//设置文本的输入类型
        }else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus",boolean.class);
                //返回一个 Method 对象，它反映此 Class 对象所表示的类或接口的指定公共成员方法。
                //name 参数是一个 String，用于指定所需方法的简称。
                //parameterTypes 参数是按声明顺序标识该方法形参类型的 Class 对象的一个数组。如果 parameterTypes 为 null，则按空数组处理。
                method.setAccessible(true);//将此对象的 accessible 标志设置为true
                method.invoke(edt_code,false);//对带有指定参数的指定对象调用由此 Method 对象表示的底层方法。
                //edt_code从中调用底层方法的对象, false用于方法调用的参数
            }catch (Exception e) {//TODO: handle exception
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus",boolean.class);
                method.setAccessible(true);
                method.invoke(edt_code,false);
            }catch (Exception e) {//TODO: handle exception
            } } }
    public CalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.cal_view,this);
        edt_code = (MyEditText)findViewById(R.id.edt_code);
        disableShowInput();
        //获取按钮的id
        btn0= (MyButton) findViewById(R.id.button0);
        btn1 = (MyButton) findViewById(R.id.button1);
        btn2 = (MyButton) findViewById(R.id.button2);
        btn3 = (MyButton) findViewById(R.id.button3);
        btn4 = (MyButton) findViewById(R.id.button4);
        btn5 = (MyButton) findViewById(R.id.button5);
        btn6= (MyButton) findViewById(R.id.button6);
        btn7 = (MyButton) findViewById(R.id.button7);
        btn8 = (MyButton) findViewById(R.id.button8);
        btn9 = (MyButton) findViewById(R.id.button9);
        btna = (MyButton) findViewById(R.id.buttona);
        btnb = (MyButton) findViewById(R.id.buttonb);
        btne = (MyButton) findViewById(R.id.buttone);
        btnc = (MyButton) findViewById(R.id.buttonc);
        btnok = (MyButton) findViewById(R.id.buttonok);
        int size=SizeUtil.screenHeight()/20;//1920/20=96
        int size2=SizeUtil.screenHeight()/40;//1920/40=48
        btn0.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn1.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn2.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn3.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn4.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn5.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn6.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn7.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn8.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn9.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btna .setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btnb.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btne.setTextSize(TypedValue.COMPLEX_UNIT_PX,size2);
        btnc .setTextSize(TypedValue.COMPLEX_UNIT_PX,size2);
        btnok .setTextSize(TypedValue.COMPLEX_UNIT_PX,size2);
        edt_code.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        btn0.setOnClickListener(lisenter);
        btn1.setOnClickListener(lisenter);
        btn2.setOnClickListener(lisenter);
        btn3.setOnClickListener(lisenter);
        btn4.setOnClickListener(lisenter);
        btn5.setOnClickListener(lisenter);
        btn6.setOnClickListener(lisenter);
        btn7.setOnClickListener(lisenter);
        btn8.setOnClickListener(lisenter);
        btn9.setOnClickListener(lisenter);
        btna .setOnClickListener(lisenter);
        btnb.setOnClickListener(lisenter);
        btne.setOnClickListener(lisenter);
        btnc .setOnClickListener(lisenter);
        btnok .setOnClickListener(lisenter);
        edt_code.setPadding(size2,size2,size2,size2);//设置内边距
        int pading=size2;  //48
        btn0.setPadding(0,pading/2,0,pading/2); //0,24,0,24
        btn1.setPadding(0,pading/2,0,pading/2);
        btn2.setPadding(0,pading/2,0,pading/2);
        btn3.setPadding(0,pading/2,0,pading/2);
        btn4.setPadding(0,pading/2,0,pading/2);
        btn5.setPadding(0,pading/2,0,pading/2);
        btn6.setPadding(0,pading/2,0,pading/2);
        btn7.setPadding(0,pading/2,0,pading/2);
        btn8.setPadding(0,pading/2,0,pading/2);
        btn9.setPadding(0,pading/2,0,pading/2);
        btna.setPadding(0,pading/2,0,pading/2);
        btnb.setPadding(0,pading/2,0,pading/2);
    }

    public CalView(Context context) {
        super(context);
        init(context);

    }

}