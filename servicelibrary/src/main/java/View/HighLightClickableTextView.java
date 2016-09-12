package View;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.servicelibrary.R;

/**
 * Created by lyd10892 on 2016/6/21.
 */
//高亮指定字符串，并且高亮字符串点击可跳转
public class HighLightClickableTextView extends TextView {

    private Activity mActivity = null;

    private String mTotalStr = null;
    private String mChangeStr = null;

    private boolean mIsShowUnderLine = false;
    private int mColorId = R.color.main_red;
    private OnClickListener listener;


    public HighLightClickableTextView(Context context) {
        this(context, null);
    }

    public HighLightClickableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighLightClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        this.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setHighLightText(String totalStr, String highLightStr) {
        mTotalStr = totalStr;
        mChangeStr = highLightStr;
    }

    public void setUnderLineShow(boolean isShowUnderLine) {
        mIsShowUnderLine = isShowUnderLine;
    }

    /**
     * 传id 例如R.id.main_red
     *
     * @param colorId
     */
    public void setHighLightColor(int colorId) {
        mColorId = colorId;
    }

    /**
     * 显示最终的效果
     */
    public void showText() {
        this.setText(changeStr());
    }

    private SpannableStringBuilder changeStr() {
        SpannableStringBuilder spannableString = null;
        if (!TextUtils.isEmpty(mChangeStr) && !TextUtils.isEmpty(mTotalStr)) {

            int length = mChangeStr.length();
            int start = mTotalStr.indexOf(mChangeStr);
            spannableString = new SpannableStringBuilder(mTotalStr);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onClick();
                    }

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(mIsShowUnderLine);//设置下划线
                }
            }, start, start + length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(mColorId)), start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
