package com.kou.android.RigVedaViewer.test;

import android.app.Application;
import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;

import com.kou.android.RigVedaViewer.activity.WebViewFragmentHolderActivity;
import com.kou.android.RigVedaViewer.utils.Utils;

/**
 * 
 * http://www.androidpub.com/546499
 * 
 * Make sure that the main launcher activity opens up properly, which will be verified by {@link #testActivityTestCaseSetUpProperly}.
 */
public class WebViewFragmentHolderActivityTest extends ActivityInstrumentationTestCase2<WebViewFragmentHolderActivity> {

	public WebViewFragmentHolderActivity mActivity;

	/**
	 * Creates an {@link ActivityInstrumentationTestCase2} for the {@link Snake} activity.
	 */
	public WebViewFragmentHolderActivityTest() {
		super(WebViewFragmentHolderActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(true);
		mActivity = getActivity();
	}

	/**
	 * Verifies that the activity under test can be launched.
	 */
	public void testActivityTestCaseSetUpProperly() {
		assertNotNull("activity should be launched successfully", getActivity());
	}

	public void testAsset() {
		Application ap = mActivity.getApplication();
		AssetManager am = null;
		am = ap.getAssets();

		assertNotNull("AssetManager must be not null", am);

	}

	public void testActivity() {
		assertNotNull("activity must be not null", mActivity);

	}

	public void testAlwaysSuccess() {
		assertTrue(true);
	}

	public void testAlwaysFail() {
		assertTrue(false);
	}
	
	public void test_Utils_getRomanNumber() {
		assertEquals("i", Utils.getRomanNumber(1));
        assertEquals("ii", Utils.getRomanNumber(2));
        assertEquals("iii", Utils.getRomanNumber(3));        
        assertEquals("iv", Utils.getRomanNumber(4));        
        assertEquals("v", Utils.getRomanNumber(5));        
        assertEquals("vi", Utils.getRomanNumber(6));
        assertEquals("vii", Utils.getRomanNumber(7));
        assertEquals("viii", Utils.getRomanNumber(8));        
        assertEquals("ix", Utils.getRomanNumber(9));        
        assertEquals("x", Utils.getRomanNumber(10));
        assertEquals("xi", Utils.getRomanNumber(11));
        assertEquals("xii", Utils.getRomanNumber(12));
        assertEquals("xiii", Utils.getRomanNumber(13));        
        assertEquals("xiv", Utils.getRomanNumber(14));        
        assertEquals("xv", Utils.getRomanNumber(15));        
        assertEquals("xix", Utils.getRomanNumber(19));        
        assertEquals("xx", Utils.getRomanNumber(20));
        assertEquals("xxx", Utils.getRomanNumber(30));        
        assertEquals("xl", Utils.getRomanNumber(40));        
        assertEquals("l", Utils.getRomanNumber(50));        
        assertEquals("lx", Utils.getRomanNumber(60));
        assertEquals("lxx", Utils.getRomanNumber(70));
        assertEquals("lxxx", Utils.getRomanNumber(80));        
        assertEquals("xc", Utils.getRomanNumber(90));        
        assertEquals("c", Utils.getRomanNumber(100));
        assertEquals("cc", Utils.getRomanNumber(200));
        assertEquals("cd", Utils.getRomanNumber(400));
        assertEquals("d", Utils.getRomanNumber(500));
        assertEquals("dclxvi", Utils.getRomanNumber(666));
        assertEquals("cm", Utils.getRomanNumber(900));
        assertEquals("m", Utils.getRomanNumber(1000));
        assertEquals("mcmxlv", Utils.getRomanNumber(1945));
        assertEquals("mcmxcix", Utils.getRomanNumber(1999));
        assertEquals("mm", Utils.getRomanNumber(2000));
        assertEquals("mmm", Utils.getRomanNumber(3000));
        assertEquals("f", Utils.getRomanNumber(5000));
        
        assertEquals("mmmcccxxxiii", Utils.getRomanNumber(3333));
        assertEquals("fm", Utils.getRomanNumber(4444));
        assertEquals("f", Utils.getRomanNumber(5555));
        assertEquals("f", Utils.getRomanNumber(6666));
        assertEquals("f", Utils.getRomanNumber(9999));
	}

}
