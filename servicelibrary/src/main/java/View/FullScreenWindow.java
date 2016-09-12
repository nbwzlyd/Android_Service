package View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.servicelibrary.R;


public class FullScreenWindow {
    private InnerDialog mDialog;
    
    private static int DEFAULT_DIM_COLOR = R.drawable.bg_box;
    private Context mContext;
    private View mContentView;
    
    /**
     * 动画是否开启
     */
    private boolean isAnimationable = false;
    /**
     * 背景是否也有动画
     */
    private boolean isAnimationWithBackground = false;
    /**
     * 进入动画，作用于不带背景的动画效果
     */
    private Animation mContentInAnimation;
    /**
     * 退出动画，作用于不带背景的动画效果
     */
    private Animation mContentOutAnimation;
    /**
     * 动画style，包括了进入退出动画，作用于带背景的动画效果
     * 定义style如下
     *  <style name="popwin_anim_style">  
     *      <item name="android:windowEnterAnimation">@anim/in</item>  
     *      <item name="android:windowExitAnimation">@anim/out</item> 
     *  </style> 
     */
    private int mWindowAnimationStyle;
    
    private boolean isDoingAnimation = false;
    
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 状态栏高度
     */
    private int mStatusBarHeight = -1;

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public enum LOCATION {
        FULL, TOP, BOTTOM, LEFT, RIGHT
    }
    
    public FullScreenWindow(Context context) {
        mContext = context;
        mDialog = new InnerDialog(mContext, R.style.Dialog_Fullscreen);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawableResource(DEFAULT_DIM_COLOR);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.setBackListener(new BackListener() {
            
            @Override
            public void onBackPress() {
                dismiss();
            }
        });
        
        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
    }
    
    public void setBackgroundRes(int colorRes) {
        mDialog.getWindow().setBackgroundDrawableResource(colorRes);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        mDialog.getWindow().setBackgroundDrawable(drawable);
    }

    public void setContentView(View view) {
        if(view != null) {
            mContentView = view;
            mContentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isDoingAnimation) {
                        dismiss();
                    }
                }
            });
            mDialog.setContentView(mContentView);
        }
    }
    
    public void setAnimationable(boolean value) {
        isAnimationable = value;
    }
    
    public void setAnimationWithBackground(boolean value) {
        isAnimationWithBackground = value;
    }
    
    public void setOutsideTouchDismiss(boolean value) {
        mDialog.setCanceledOnTouchOutside(value);
    }
    
    public void setContentInAnimation(Animation inAnimation) {
        if(inAnimation != null) {
            mContentInAnimation = inAnimation;
            mContentInAnimation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isDoingAnimation = true;
                }
                
                @Override
                public void onAnimationRepeat(Animation animation) {
                    isDoingAnimation = true;
                }
                
                @Override
                public void onAnimationEnd(Animation animation) {
                    isDoingAnimation = false;
                }
            });
        }
    }
    
    public void setContentInAnimation(int animation) {
        mContentInAnimation = AnimationUtils.loadAnimation(mContext, animation);
        mContentInAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isDoingAnimation = true;
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                isDoingAnimation = true;
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                isDoingAnimation = false;
            }
        });
    }
    
    public void setContentOutAnimation(Animation outAnimation) {
        if(outAnimation != null) {
            mContentOutAnimation = outAnimation;
            mContentOutAnimation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isDoingAnimation = true;
                }
                
                @Override
                public void onAnimationRepeat(Animation animation) {
                    isDoingAnimation = true;
                }
                
                @Override
                public void onAnimationEnd(Animation animation) {
                    isDoingAnimation = false;
                    mDialog.dismiss();
                }
            });
        }
    }
    
    public void setContentOutAnimation(int animation) {
        mContentOutAnimation = AnimationUtils.loadAnimation(mContext, animation);
        mContentOutAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isDoingAnimation = true;
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                isDoingAnimation = true;
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                isDoingAnimation = false;
                mDialog.dismiss();
            }
        });
    }
    
    /**
     * 设置带背景的动画，包括进入，退出动画
     * @param style
     */
    public void setWindowAnimationStyle(int style) {
        mWindowAnimationStyle = style;
    }
    
    public void setDismissListener(final DismissListener listener) {
        mDialog.setOnDismissListener(new OnDismissListener() {
            
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(listener != null) {
                    listener.onDissmiss();
                }
            }
        });
    }
    
    /**
     * 全屏展示，ActionBar也会覆盖，不建议使用该方法，该方法的参数view没用到，可能会误导，建议使用不带参数的showFullScreen
     * @param view
     */
    @Deprecated
    public void showFullScreen(View view) {
        if(view == null) {
            return;
        }
        initAnimation();
        computeStatusBarHeight(view);
        setLocation(view, LOCATION.FULL);
        mDialog.show();
        if(isAnimationable && !isAnimationWithBackground && mContentInAnimation != null && mContentView != null) {
            mContentView.startAnimation(mContentInAnimation);
        }
    }
    
    /**
     * 全屏展示，ActionBar也会覆盖
     */
    public void showFullScreen() {
        initAnimation();
        mDialog.show();
        if(isAnimationable && !isAnimationWithBackground && mContentInAnimation != null && mContentView != null) {
            mContentView.startAnimation(mContentInAnimation);
        }
    }
    
    public void showAbove(View view) {
        if(view == null) {
            return;
        }
        initAnimation();
        computeStatusBarHeight(view);
        setLocation(view, LOCATION.TOP);
        mDialog.show();
        if(isAnimationable && !isAnimationWithBackground && mContentInAnimation != null && mContentView != null) {
            mContentView.startAnimation(mContentInAnimation);
        }
    }
    
    public void showBelow(View view) {
        if(view == null) {
            return;
        }
        initAnimation();
        computeStatusBarHeight(view);
        setLocation(view, LOCATION.BOTTOM);
        mDialog.show();
        if(isAnimationable && !isAnimationWithBackground && mContentInAnimation != null && mContentView != null) {
            mContentView.startAnimation(mContentInAnimation);
        }
    }
    
    public void showLeft(View view) {
        if(view == null) {
            return;
        }
        initAnimation();
        computeStatusBarHeight(view);
        setLocation(view, LOCATION.LEFT);
        mDialog.show();
        if(isAnimationable && !isAnimationWithBackground && mContentInAnimation != null && mContentView != null) {
            mContentView.startAnimation(mContentInAnimation);
        }
    }
    
    public void showRight(View view) {
        if(view == null) {
            return;
        }
        initAnimation();
        computeStatusBarHeight(view);
        setLocation(view, LOCATION.RIGHT);
        mDialog.show();
        if(isAnimationable && !isAnimationWithBackground && mContentInAnimation != null && mContentView != null) {
            mContentView.startAnimation(mContentInAnimation);
        }
    }
    
    private void initAnimation() {
        Window window = mDialog.getWindow();
        if(isAnimationable) {
            if(isAnimationWithBackground) {
                window.setWindowAnimations(mWindowAnimationStyle);
            } else {
                window.setWindowAnimations(0);
            }
        } else {
            window.setWindowAnimations(0);
        }
    }
    
    private void computeStatusBarHeight(View view) {
        if(mStatusBarHeight <= 0) {
            Rect rect = new Rect();
            view.getWindowVisibleDisplayFrame(rect);
             mStatusBarHeight = rect.top;
        }
    }
    
    private void setLocation(View view, LOCATION location) {
        if(mDialog == null || view == null) {
            return;
        }
        int[] point = new int[2];  
        view.getLocationOnScreen(point);
        
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if(location == LOCATION.FULL) {
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.TOP);
        } else if(location == LOCATION.TOP) {
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = point[1];
            window.setGravity(Gravity.TOP);
        } else if(location == LOCATION.BOTTOM) {
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = mScreenHeight - point[1] - view.getHeight();
            window.setGravity(Gravity.BOTTOM);
        } else if(location == LOCATION.LEFT) {
            lp.width = point[0];
            lp.height = LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.LEFT);
        } else if(location == LOCATION.RIGHT) {
            lp.width = mScreenWidth - point[0] - view.getWidth();
            lp.height = LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.RIGHT);
        }
        window.setAttributes(lp);
    }
    
    public void dismiss() {
        initAnimation();
        if(isAnimationable && !isAnimationWithBackground && mContentView != null && mContentOutAnimation != null) {
            mContentView.clearAnimation();
            mContentView.startAnimation(mContentOutAnimation);
        } else {
            mDialog.dismiss();
        }
    }
    
    public interface DismissListener {
        void onDissmiss();
    }

    /**
     * 主要用于解决物理返回键不会跑退出动画问题
     */
    public class InnerDialog extends Dialog {
        private BackListener mBackListener;
        public InnerDialog(Context context) {
            super(context);
        }

        protected InnerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        public InnerDialog(Context context, int theme) {
            super(context, theme);
        }
        
        public void setBackListener (BackListener listener) {
            mBackListener = listener;
        }
        
        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                    && !event.isCanceled()) {
                if(mBackListener != null) {
                    mBackListener.onBackPress();
                } else {
                    dismiss();
                }
                return true;
            }
            return false;
        }
        
    }
    
    public interface BackListener {
        void onBackPress();
    }
}
