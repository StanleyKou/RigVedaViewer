package com.jeremyfeinstein.slidingmenu.lib;

import android.graphics.Canvas;
import android.view.animation.Interpolator;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.kou.android.RigVedaViewer.utils.LogWrapper;


public class CanvasTransformerBuilder {
	private final static String TAG = CanvasTransformerBuilder.class.getSimpleName();
	
	private CanvasTransformer mTrans;

	private static Interpolator lin = new Interpolator() {
		public float getInterpolation(float t) {
			LogWrapper.d(TAG, "Interpolator::getInterpolation()");
			return t;
		}
	};

	private void initTransformer() {
		LogWrapper.d(TAG, "initTransformer()");
		if (mTrans == null)
			mTrans = new CanvasTransformer() {
			public void transformCanvas(Canvas canvas, float percentOpen) {	}
		};
	}

	public CanvasTransformer zoom(final int openedX, final int closedX, final int openedY, final int closedY, final int px, final int py) {
		LogWrapper.d(TAG, "zoom()");
		return zoom(openedX, closedX, openedY, closedY, px, py, lin);
	}

	public CanvasTransformer zoom(final int openedX, final int closedX, final int openedY, final int closedY, final int px, final int py, final Interpolator interp) {
		LogWrapper.d(TAG, "zoom(), with Interpolator");
		initTransformer();
		mTrans = new CanvasTransformer() {
			public void transformCanvas(Canvas canvas, float percentOpen) {
				mTrans.transformCanvas(canvas, percentOpen);
				float f = interp.getInterpolation(percentOpen);
				canvas.scale((openedX - closedX) * f + closedX, (openedY - closedY) * f + closedY, px, py);
			}			
		};
		return mTrans;
	}

	public CanvasTransformer rotate(final int openedDeg, final int closedDeg, final int px, final int py) {
		LogWrapper.d(TAG, "rotate()");
		return rotate(openedDeg, closedDeg, px, py, lin);
	}

	public CanvasTransformer rotate(final int openedDeg, final int closedDeg, final int px, final int py, final Interpolator interp) {
		LogWrapper.d(TAG, "rotate(), with Interpolator");
		initTransformer();
		mTrans = new CanvasTransformer() {
			public void transformCanvas(Canvas canvas, float percentOpen) {
				mTrans.transformCanvas(canvas, percentOpen);
				float f = interp.getInterpolation(percentOpen);
				canvas.rotate((openedDeg - closedDeg) * f + closedDeg, px, py);
			}			
		};
		return mTrans;
	}

	public CanvasTransformer translate(final int openedX, final int closedX, final int openedY, final int closedY) {
		LogWrapper.d(TAG, "translate()");
		return translate(openedX, closedX, openedY, closedY, lin);
	}

	public CanvasTransformer translate(final int openedX, final int closedX, final int openedY, final int closedY, final Interpolator interp) {
		LogWrapper.d(TAG, "translate(), with Interpolator");
		
		initTransformer();
		mTrans = new CanvasTransformer() {
			public void transformCanvas(Canvas canvas, float percentOpen) {
				mTrans.transformCanvas(canvas, percentOpen);
				float f = interp.getInterpolation(percentOpen);
				canvas.translate((openedX - closedX) * f + closedX, (openedY - closedY) * f + closedY);
			}			
		};
		return mTrans;
	}

	public CanvasTransformer concatTransformer(final CanvasTransformer t) {
		LogWrapper.d(TAG, "concatTransformer()");
		
		initTransformer();
		mTrans = new CanvasTransformer() {
			public void transformCanvas(Canvas canvas, float percentOpen) {
				mTrans.transformCanvas(canvas, percentOpen);
				t.transformCanvas(canvas, percentOpen);
			}			
		};
		return mTrans;
	}

}
