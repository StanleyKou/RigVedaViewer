package com.jeremyfeinstein.slidingmenu.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.utils.LogWrapper;

public class CustomViewBehind extends ViewGroup {

	private static final String TAG = CustomViewBehind.class.getSimpleName();

	private static final int MARGIN_THRESHOLD = 48; // dips
	private int mTouchMode = SlidingMenu.TOUCHMODE_MARGIN;

	private CustomViewAbove mViewAbove;

	private View mContent;
	private View mSecondaryContent;
	private int mMarginThreshold;
	private int mWidthOffset;
	private CanvasTransformer mTransformer;
	private boolean mChildrenEnabled;

	public CustomViewBehind(Context context) {
		this(context, null);
		LogWrapper.d(TAG, "Constructor::CustomViewBehind()");
		setContentDescription(CustomViewBehind.class.getSimpleName());
	}

	public CustomViewBehind(Context context, AttributeSet attrs) {
		super(context, attrs);
		mMarginThreshold = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_THRESHOLD, getResources().getDisplayMetrics());
		LogWrapper.d(TAG, "Constructor::CustomViewBehind(), with AttributeSet, mMarginThreshold(%s)", mMarginThreshold);
		setContentDescription(CustomViewBehind.class.getSimpleName());
	}

	public void setCustomViewAbove(CustomViewAbove customViewAbove) {
		LogWrapper.d(TAG, "setCustomViewAbove()");
		mViewAbove = customViewAbove;
	}

	public void setCanvasTransformer(CanvasTransformer t) {
		LogWrapper.d(TAG, "setCanvasTransformer()");
		mTransformer = t;
	}

	public void setWidthOffset(int i) {
		LogWrapper.d(TAG, "setWidthOffset(%s)", i);
		mWidthOffset = i;
		requestLayout();
	}

	public void setMarginThreshold(int marginThreshold) {
		LogWrapper.d(TAG, "setMarginThreshold(%s)", marginThreshold);
		mMarginThreshold = marginThreshold;
	}

	public int getMarginThreshold() {
		LogWrapper.d(TAG, "getMarginThreshold()");
		return mMarginThreshold;
	}

	public int getBehindWidth() {
		return mContent.getWidth();
	}

	public void setContent(View v) {
		LogWrapper.d(TAG, "setContent()");
		if (mContent != null)
			removeView(mContent);
		mContent = v;
		addView(mContent);
	}

	public View getContent() {
		LogWrapper.d(TAG, "getContent()");
		return mContent;
	}

	/**
	 * Sets the secondary (right) menu for use when setMode is called with SlidingMenu.LEFT_RIGHT.
	 * 
	 * @param v
	 *            the right menu
	 */
	public void setSecondaryContent(View v) {
		LogWrapper.d(TAG, "setSecondaryContent(), with view");
		if (mSecondaryContent != null) {
			removeView(mSecondaryContent);
		}

		mSecondaryContent = v;
		addView(mSecondaryContent);
	}

	public View getSecondaryContent() {
		LogWrapper.d(TAG, "getSecondaryContent()");
		return mSecondaryContent;
	}

	public void setChildrenEnabled(boolean enabled) {
		LogWrapper.d(TAG, "setChildrenEnabled(%s)", enabled);
		mChildrenEnabled = enabled;
	}

	@Override
	public void scrollTo(int x, int y) {
		LogWrapper.d(TAG, "scrollTo(%s, %s)", x, y);
		super.scrollTo(x, y);
		if (mTransformer != null) {
			invalidate();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		LogWrapper.d(TAG, "onInterceptTouchEvent()");
		return !mChildrenEnabled;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		LogWrapper.d(TAG, "onTouchEvent()");
		return !mChildrenEnabled;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		LogWrapper.d(TAG, "dispatchDraw()");
		if (mTransformer != null) {
			canvas.save();
			mTransformer.transformCanvas(canvas, mViewAbove.getPercentOpen());
			super.dispatchDraw(canvas);
			canvas.restore();
		} else {
			super.dispatchDraw(canvas);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		LogWrapper.d(TAG, "onLayout(), changed(%s)", changed);
		final int width = r - l;
		final int height = b - t;
		mContent.layout(0, 0, width - mWidthOffset, height);
		if (mSecondaryContent != null) {
			mSecondaryContent.layout(0, 0, width - mWidthOffset, height);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		LogWrapper.d(TAG, "onMeasure(), widthMeasureSpec(%s), heightMeasureSpec(%s)", widthMeasureSpec, heightMeasureSpec);
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);

		final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width - mWidthOffset);
		final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
		mContent.measure(contentWidth, contentHeight);
		if (mSecondaryContent != null) {
			mSecondaryContent.measure(contentWidth, contentHeight);
		}
	}

	private int mMode;
	private boolean mFadeEnabled;
	private final Paint mFadePaint = new Paint();
	private float mScrollScale;
	private Drawable mShadowDrawable;
	private Drawable mSecondaryShadowDrawable;
	private int mShadowWidth;
	private float mFadeDegree;

	public void setMode(int mode) {
		LogWrapper.d(TAG, "setMode(%s)", mode);
		if (mode == SlidingMenu.LEFT || mode == SlidingMenu.RIGHT) {
			if (mContent != null) {
				mContent.setVisibility(View.VISIBLE);
			}

			if (mSecondaryContent != null) {
				mSecondaryContent.setVisibility(View.INVISIBLE);
			}
		}
		mMode = mode;
	}

	public int getMode() {
		LogWrapper.d(TAG, "getMode()");
		return mMode;
	}

	public void setScrollScale(float scrollScale) {
		LogWrapper.d(TAG, "setScrollScale(%s)", scrollScale);
		mScrollScale = scrollScale;
	}

	public float getScrollScale() {
		LogWrapper.d(TAG, "getScrollScale()");
		return mScrollScale;
	}

	public void setShadowDrawable(Drawable shadow) {
		LogWrapper.d(TAG, "setShadowDrawable()");
		mShadowDrawable = shadow;
		invalidate();
	}

	public void setSecondaryShadowDrawable(Drawable shadow) {
		LogWrapper.d(TAG, "setSecondaryShadowDrawable()");
		mSecondaryShadowDrawable = shadow;
		invalidate();
	}

	public void setShadowWidth(int width) {
		LogWrapper.d(TAG, "setShadowWidth(), width(%s)", width);
		mShadowWidth = width;
		invalidate();
	}

	public void setFadeEnabled(boolean b) {
		LogWrapper.d(TAG, "setFadeEnabled(%s)", b);
		mFadeEnabled = b;
	}

	public void setFadeDegree(float degree) {
		LogWrapper.d(TAG, "setFadeDegree(), degree(%s)", degree);
		if (degree > 1.0f || degree < 0.0f) {
			throw new IllegalStateException("The BehindFadeDegree must be between 0.0f and 1.0f");
		}
		mFadeDegree = degree;
	}

	public int getMenuPage(int page) {
		LogWrapper.d(TAG, "getMenuPage(), page(%s)", page);
		page = (page > 1) ? 2 : ((page < 1) ? 0 : page);
		if (mMode == SlidingMenu.LEFT && page > 1) {
			return 0;
		} else if (mMode == SlidingMenu.RIGHT && page < 1) {
			return 2;
		} else {
			return page;
		}
	}

	public void scrollBehindTo(View content, int x, int y) {
		LogWrapper.d(TAG, "scrollBehindTo(%s, %s)", x, y);
		int vis = View.VISIBLE;
		if (mMode == SlidingMenu.LEFT) {
			if (x >= content.getLeft())
				vis = View.INVISIBLE;
			scrollTo((int) ((x + getBehindWidth()) * mScrollScale), y);

		} else if (mMode == SlidingMenu.RIGHT) {
			if (x <= content.getLeft())
				vis = View.INVISIBLE;
			scrollTo((int) (getBehindWidth() - getWidth() + (x - getBehindWidth()) * mScrollScale), y);

		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			mContent.setVisibility(x >= content.getLeft() ? View.INVISIBLE : View.VISIBLE);
			mSecondaryContent.setVisibility(x <= content.getLeft() ? View.INVISIBLE : View.VISIBLE);
			vis = x == 0 ? View.INVISIBLE : View.VISIBLE;
			if (x <= content.getLeft()) {
				scrollTo((int) ((x + getBehindWidth()) * mScrollScale), y);
			} else {
				scrollTo((int) (getBehindWidth() - getWidth() + (x - getBehindWidth()) * mScrollScale), y);
			}
		}

		if (vis == View.INVISIBLE) {
			LogWrapper.d(TAG, "scrollBehindTo(), behind INVISIBLE");
		}
		setVisibility(vis);
	}

	public int getMenuLeft(View content, int page) {
		LogWrapper.d(TAG, "getMenuLeft(), page(%s)", page);
		if (mMode == SlidingMenu.LEFT) {
			switch (page) {
			case 0:
				return content.getLeft() - getBehindWidth();
			case 2:
				return content.getLeft();
			}

		} else if (mMode == SlidingMenu.RIGHT) {
			switch (page) {
			case 0:
				return content.getLeft();
			case 2:
				return content.getLeft() + getBehindWidth();
			}

		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			switch (page) {
			case 0:
				return content.getLeft() - getBehindWidth();
			case 2:
				return content.getLeft() + getBehindWidth();
			}
		}
		return content.getLeft();
	}

	public int getAbsLeftBound(View content) {
		LogWrapper.d(TAG, "getAbsLeftBound()");
		if (mMode == SlidingMenu.LEFT || mMode == SlidingMenu.LEFT_RIGHT) {
			return content.getLeft() - getBehindWidth();
		} else if (mMode == SlidingMenu.RIGHT) {
			return content.getLeft();
		}
		return 0;
	}

	public int getAbsRightBound(View content) {
		LogWrapper.d(TAG, "getAbsRightBound()");
		if (mMode == SlidingMenu.LEFT) {
			return content.getLeft();
		} else if (mMode == SlidingMenu.RIGHT || mMode == SlidingMenu.LEFT_RIGHT) {
			return content.getLeft() + getBehindWidth();
		}
		return 0;
	}

	public boolean marginTouchAllowed(View content, int x) {
		LogWrapper.d(TAG, "marginTouchAllowed(%s)", x);
		int left = content.getLeft();
		int right = content.getRight();
		if (mMode == SlidingMenu.LEFT) {
			return (x >= left && x <= mMarginThreshold + left);
		} else if (mMode == SlidingMenu.RIGHT) {
			return (x <= right && x >= right - mMarginThreshold);
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			return (x >= left && x <= mMarginThreshold + left) || (x <= right && x >= right - mMarginThreshold);
		}
		return false;
	}

	public void setTouchMode(int i) {
		LogWrapper.d(TAG, "setTouchMode(%s)", i);
		mTouchMode = i;
	}

	public boolean menuOpenTouchAllowed(View content, int currPage, float x) {
		LogWrapper.d(TAG, "menuOpenTouchAllowed(), currPage(%s), x(%s)", currPage, x);

		switch (mTouchMode) {
		case SlidingMenu.TOUCHMODE_FULLSCREEN:
			return true;
		case SlidingMenu.TOUCHMODE_MARGIN:
			return menuTouchInQuickReturn(content, currPage, x);
		}
		return false;
	}

	public boolean menuTouchInQuickReturn(View content, int currPage, float x) {
		LogWrapper.d(TAG, "menuTouchInQuickReturn(), currPage(%s), x(%s)", currPage, x);
		if (mMode == SlidingMenu.LEFT || (mMode == SlidingMenu.LEFT_RIGHT && currPage == 0)) {
			return x >= content.getLeft();
		} else if (mMode == SlidingMenu.RIGHT || (mMode == SlidingMenu.LEFT_RIGHT && currPage == 2)) {
			return x <= content.getRight();
		}
		return false;
	}

	public boolean menuClosedSlideAllowed(float dx) {
		LogWrapper.d(TAG, "menuClosedSlideAllowed(), dx(%s)", dx);
		if (mMode == SlidingMenu.LEFT) {
			return dx > 0;
		} else if (mMode == SlidingMenu.RIGHT) {
			return dx < 0;
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			return true;
		}
		return false;
	}

	public boolean menuOpenSlideAllowed(float dx) {
		LogWrapper.d(TAG, "menuOpenSlideAllowed(), dx(%s)", dx);
		if (mMode == SlidingMenu.LEFT) {
			return dx < 0;
		} else if (mMode == SlidingMenu.RIGHT) {
			return dx > 0;
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			return true;
		}
		return false;
	}

	public void drawShadow(View content, Canvas canvas) {
		if (mShadowDrawable == null || mShadowWidth <= 0) {
			return;
		}

		int left = 0;
		if (mMode == SlidingMenu.LEFT) {
			left = content.getLeft() - mShadowWidth;
		} else if (mMode == SlidingMenu.RIGHT) {
			left = content.getRight();
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			if (mSecondaryShadowDrawable != null) {
				left = content.getRight();
				mSecondaryShadowDrawable.setBounds(left, 0, left + mShadowWidth, getHeight());
				mSecondaryShadowDrawable.draw(canvas);
			}
			left = content.getLeft() - mShadowWidth;
		}
		mShadowDrawable.setBounds(left, 0, left + mShadowWidth, getHeight());
		mShadowDrawable.draw(canvas);
	}

	public void drawFade(View content, Canvas canvas, float openPercent) {
		LogWrapper.d(TAG, "drawFade(), openPercent(%s)", openPercent);
		if (!mFadeEnabled) {
			return;
		}

		final int alpha = (int) (mFadeDegree * 255 * Math.abs(1 - openPercent));
		mFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
		int left = 0;
		int right = 0;
		if (mMode == SlidingMenu.LEFT) {
			left = content.getLeft() - getBehindWidth();
			right = content.getLeft();
		} else if (mMode == SlidingMenu.RIGHT) {
			left = content.getRight();
			right = content.getRight() + getBehindWidth();
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			left = content.getLeft() - getBehindWidth();
			right = content.getLeft();
			canvas.drawRect(left, 0, right, getHeight(), mFadePaint);
			left = content.getRight();
			right = content.getRight() + getBehindWidth();
		}
		canvas.drawRect(left, 0, right, getHeight(), mFadePaint);
	}

	private boolean mSelectorEnabled = true;
	private Bitmap mSelectorDrawable;
	private View mSelectedView;

	public void drawSelector(View content, Canvas canvas, float openPercent) {
		LogWrapper.d(TAG, "drawSelector(), openPercent(%s)", openPercent);
		if (!mSelectorEnabled) {
			return;
		}

		if (mSelectorDrawable != null && mSelectedView != null) {
			String tag = (String) mSelectedView.getTag(R.id.selected_view);
			if (tag.equals("CustomViewBehindSelectedView")) {
				canvas.save();
				int left, right, offset;
				offset = (int) (mSelectorDrawable.getWidth() * openPercent);
				if (mMode == SlidingMenu.LEFT) {
					right = content.getLeft();
					left = right - offset;
					canvas.clipRect(left, 0, right, getHeight());
					canvas.drawBitmap(mSelectorDrawable, left, getSelectorTop(), null);
				} else if (mMode == SlidingMenu.RIGHT) {
					left = content.getRight();
					right = left + offset;
					canvas.clipRect(left, 0, right, getHeight());
					canvas.drawBitmap(mSelectorDrawable, right - mSelectorDrawable.getWidth(), getSelectorTop(), null);
				}
				canvas.restore();
			}
		}
	}

	public void setSelectorEnabled(boolean b) {
		LogWrapper.d(TAG, "setSelectorEnabled(%s)", b);
		mSelectorEnabled = b;
	}

	public void setSelectedView(View v) {
		LogWrapper.d(TAG, "setSelectedView()");
		if (mSelectedView != null) {
			mSelectedView.setTag(R.id.selected_view, null);
			mSelectedView = null;
		}

		if (v != null && v.getParent() != null) {
			mSelectedView = v;
			mSelectedView.setTag(R.id.selected_view, "CustomViewBehindSelectedView");
			invalidate();
		}
	}

	private int getSelectorTop() {
		LogWrapper.d(TAG, "getSelectorTop()");
		int y = mSelectedView.getTop();
		y += (mSelectedView.getHeight() - mSelectorDrawable.getHeight()) / 2;
		return y;
	}

	public void setSelectorBitmap(Bitmap b) {
		LogWrapper.d(TAG, "setSelectorBitmap()");
		mSelectorDrawable = b;
		refreshDrawableState();
	}

	public View getContents() {
		return mContent;
	}

}
