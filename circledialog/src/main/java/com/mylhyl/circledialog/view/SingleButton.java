package com.mylhyl.circledialog.view;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.mylhyl.circledialog.CircleParams;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.res.drawable.SelectorBtn;
import com.mylhyl.circledialog.res.values.CircleColor;

/**
 * 对话框单个按钮的视图
 * Created by hupei on 2017/3/30.
 */
class SingleButton extends ScaleTextView {
    private CircleParams mCircleParams;
    private ButtonParams mButtonParams;

    public SingleButton(Context context, CircleParams params) {
        super(context);
        init(params);
    }

    private void init(CircleParams params) {
        mCircleParams = params;
        mButtonParams = params.negativeParams != null ? params.negativeParams : params
                .positiveParams;

        setTextSize(mButtonParams.textSize);
        setHeight(mButtonParams.height);
        handleStyle();

        //如果取消按钮没有背景色，则使用默认色
        int backgroundColor = mButtonParams.backgroundColor != 0 ? mButtonParams.backgroundColor
                : CircleColor.bgDialog;

        int radius = params.dialogParams.radius;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(new SelectorBtn(backgroundColor, 0, 0, radius, radius));
        } else {
            setBackgroundDrawable(new SelectorBtn(backgroundColor, 0, 0, radius, radius));
        }

        regOnClickListener();
    }

    private void handleStyle() {
        setText(mButtonParams.text);
        setEnabled(!mButtonParams.disable);
        //禁用按钮则改变文字颜色
        setTextColor(mButtonParams.disable ? mButtonParams.textColorDisable : mButtonParams.textColor);
    }

    private void regOnClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleParams.dismiss();
                if (mCircleParams.clickPositiveListener != null)
                    mCircleParams.clickPositiveListener.onClick(v);
                else if (mCircleParams.clickNegativeListener != null)
                    mCircleParams.clickNegativeListener.onClick(v);
            }
        });
    }

    public void regOnInputClickListener(final EditText input) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if (!TextUtils.isEmpty(text))
                    mCircleParams.dismiss();
                if (mCircleParams.inputListener != null)
                    mCircleParams.inputListener.onClick(text, v);
            }
        });
    }

    public void refreshText() {
        if (mButtonParams == null) return;
        post(new Runnable() {
            @Override
            public void run() {
                handleStyle();
            }
        });
    }
}
