package com.kou.android.RigVedaViewer.base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.kou.android.RigVedaViewer.R;

public class ScrollerLinearLayout extends LinearLayout {

	private final String TAG = ScrollerLinearLayout.class.getSimpleName();
	private float touchDownPointY = 0;

	public enum PositionState {
		POSITION_UP, POSITION_DOWN
	};

	private PositionState positionState;

	private Scroller scroller;
	private Context mContext;
	private ViewGroup upperView;
	private ViewGroup lowerView;

	private Animation animDownIn;
	private Animation animDownInFast;
	private Animation animUpOut;

	private boolean isTimerReady = true;

	private long scrollRunnedTime = 0;
	private long currentTime = 0;

	private int scrollDuration = 900;
	private int animDuration = 300;
	private int upperViewHeight = 0;

	public ScrollerLinearLayout(Context context) {
		super(context);
		init(context);
	}

	public ScrollerLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

		mContext = context;

		scroller = new Scroller(context);
		positionState = PositionState.POSITION_DOWN;

		animDownIn = AnimationUtils.loadAnimation(mContext, R.anim.sort_up_to_down_in);
		animDownIn.setDuration(animDuration);
		animDownIn.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				if (upperView != null) {
					upperView.setVisibility(View.VISIBLE);
				}
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
			}
		});

		animDownInFast = AnimationUtils.loadAnimation(mContext, R.anim.sort_up_to_down_in);
		animDownInFast.setDuration(0);
		animDownInFast.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				if (upperView != null) {
					upperView.setVisibility(View.VISIBLE);
				}
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
			}
		});

		animUpOut = AnimationUtils.loadAnimation(mContext, R.anim.sort_down_to_up_out);
		animUpOut.setDuration(animDuration);
		animUpOut.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (upperView != null) {
					upperView.setVisibility(View.GONE);
				}
			}
		});

		//setOnTouchListener(this);

	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	//
	// // if (isFirst == true) {
	// // pullDownInstant();
	// // isFirst = false;
	// // }
	//
	// super.onDraw(canvas);
	// }

	// @Override
	// protected void onLayout(boolean changed, int l, int t, int r, int b) {
	// super.onLayout(changed, l, t, r, b);
	// upperView = (ViewGroup) getChildAt(0); // first child is upper menu.
	// upperViewHeight = upperView.getHeight();
	//
	// lowerView = (ViewGroup) getChildAt(2);
	// LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lowerView.getLayoutParams();
	// lp.height = lp.height + upperViewHeight;
	// lowerView.setLayoutParams(lp);
	// }

	public void pullDown() {
		Log.d(TAG, "pullDown()");
		currentTime = System.currentTimeMillis();
		if (currentTime - 200 > scrollRunnedTime) {
			isTimerReady = true;
		}

		if (true == isTimerReady && PositionState.POSITION_UP == positionState && upperView != null) {
			upperView.startAnimation(animDownIn);

			// FIXING
			// �� �༮�� ��ġ�� ��Ȯ�� �´´�. �׷��� �ι�° �䰡 ����������� �ʴ´�.
			// scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), 0, 0, scrollDuration);

			// �� �༮�� ��ġ�� �ι� �� ���� �����δ�. �׷��� �ι�° �䰡 ��¦ ���� �����δ�.
			// scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), 0, -upperViewHeight, scrollDuration);

			scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), 0, 0, scrollDuration);

			positionState = PositionState.POSITION_DOWN;

			isTimerReady = false;
			scrollRunnedTime = System.currentTimeMillis();
		}
	}

	// public void pullDownInstant() {
	// Log.d(TAG, "pullDownInstant()");
	// if (PositionState.POSITION_UP == positionState && upperView != null) {
	// scrollTo(0, -upperViewHeight);
	// positionState = PositionState.POSITION_DOWN;
	//
	// isTimerReady = false;
	// scrollRunnedTime = System.currentTimeMillis();
	// }
	// }

	public void pullUp() {
		Log.d(TAG, "pullUp()");
		currentTime = System.currentTimeMillis();
		if (currentTime - 200 > scrollRunnedTime) {
			isTimerReady = true;
		}

		if (true == isTimerReady && PositionState.POSITION_DOWN == positionState && upperView != null) {
			upperView.startAnimation(animUpOut);
			scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), 0, -1 * scroller.getCurrY(), scrollDuration);
			positionState = PositionState.POSITION_UP;

			isTimerReady = false;
			scrollRunnedTime = System.currentTimeMillis();
		}
	}

//	@Override
//	public void computeScroll() {
//		if (scroller.computeScrollOffset()) {
//			scrollTo(scroller.getCurrX(), scroller.getCurrY());
//			invalidate();
//		}
//	}

	public void pullMove(int x, int y) {
		// Log.d(TAG, "pullMove top:" + getTop() + " y:" + y);

		// Log.d(TAG, "screenHeight :" + screenHeight);
		// Log.d(TAG, "upperView.getHeight() :" + upperView.getHeight());

		// scrollTo(0, screenHeight - y); // ��ġ ���� ��ũ��

		// scrollTo(0, 0); // ��Ȯ�� ó�� ����. (������ ��� �޴��� ��¦ �� ������ ����)
		// scrollTo(0, screenHeight); // �޴��� ������ �ʴ� ����

		// ��ġ ����

		// ���� ���� : ù ��ġ ���� - ���� ��ġ < upperViewHeight �� �� ����

		// �Ʒ��� ���� : upperViewHeight

		int moveTo = -y + (int) touchDownPointY;
		Log.d(TAG, "moveTo:" + moveTo);

		// Down ����
		// �о�÷��� �� �Ѱ��� : 96 == upperViewHeight
		// �о���� �� �Ѱ��� : 0

		// Up ����
		// �о�÷��� �� �Ѱ��� : 0
		// �о���� �� �Ѱ��� : -96 == -upperViewHeight

		// if (positionState == PositionState.POSITION_DOWN) {
		// if (moveTo <= upperViewHeight && moveTo >= 0) {
		// scrollTo(0, moveTo);
		// }
		//
		// } else {
		// if (moveTo <= 0 && moveTo >= -upperViewHeight) {
		// scrollTo(0, moveTo);
		// }
		// }

		scrollTo(0, moveTo);
	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	//
	// switch (event.getAction() & MotionEvent.ACTION_MASK) {
	// case MotionEvent.ACTION_DOWN:
	// Log.d(TAG, "ACTION_DOWN");
	// touchDownPointY = event.getY();
	// return true;
	//
	// case MotionEvent.ACTION_UP:
	// Log.d(TAG, "ACTION_UP :" + (touchDownPointY - event.getY()));
	// // if (touchDownPointY - event.getY() > 0) {
	// // pullUp();
	// // } else if (touchDownPointY - event.getY() < 0) {
	// // pullDown();
	// // }
	// break;
	// case MotionEvent.ACTION_MOVE:
	// Log.d(TAG, "ACTION_MOVE");
	// pullMove(0, (int) event.getY());
	// return false;
	//
	// }
	//
	// return false;
	// }
}
