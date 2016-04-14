package com.ishraq.janna.fragment.main;

import android.graphics.PointF;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.ishraq.janna.R;

/**
 * Created by Ahmed on 3/17/2016.
 */
public class WebViewFragment extends MainCommonFragment implements View.OnTouchListener {

    private WebView webView;
    private Integer webViewNumber;
    ImageView internal_map;


    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            webViewNumber = bundle.getInt("webViewNumber");
        } else {
            webViewNumber = 1111;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getMainActivity().getSwipeRefreshLayout().setEnabled(false);
        getMainActivity().stopLoadingAnimator();
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        internal_map = (ImageView) view.findViewById(R.id.internal_map);
        internal_map.setOnTouchListener(this);

        switch (webViewNumber) {

            case 1:
                // Load Our web site
                webView.loadUrl("https://www.google.com.eg/maps/place/%D8%AC%D8%B1%D8%A7%D9%86%D8%AF+%D9%86%D8%A7%D9%8A%D9%84+%D8%AA%D8%A7%D9%88%D8%B1%E2%80%AD/@30.0346311,31.2281593,17z/data=!4m2!3m1!1s0x0000000000000000:0x201285387107863a");
                internal_map.setVisibility(View.VISIBLE);
                break;
            case 2:
                // Load Our web site
                webView.loadUrl("http://alinany-clinic.com/hesham-clinic/%D9%85%D8%B3%D8%AA%D8%B4%D9%81%D9%89-%D8%AC%D9%86%D8%A9.html");
                break;

            case 3:
                // Load Facebook page
                webView.loadUrl("https://www.facebook.com/groups/314911565200565/");
                break;

            case 4:
                // Load Twitter page
                webView.loadUrl("https://twitter.com/kaainih");
                break;

            case 5:
                // Load linked in page
                webView.loadUrl("https://www.linkedin.com/in/hesham-elenani-21b6202b?trk=hp-identity-name");
                break;
            case 6:
                // Load linked in page
                webView.loadUrl("https://www.cookmedical.com/");
                break;
            case 7:
                // Load linked in page
                webView.loadUrl("http://www.pfizer.com/");
                break;
            case 8:
                // Load linked in page
                webView.loadUrl("http://www.ccd-lab.com/en/");
                break;
            case 9:
                // Load linked in page
                webView.loadUrl("https://www.facebook.com/ApexMultiApexPharmaMap/");
                break;
            case 10:
                // Load linked in page
                webView.loadUrl("https://www.ferring.com/en/home/");
                break;
            case 11:
                // Load linked in page
                webView.loadUrl("http://www.ibsa-international.com/home/");
                break;
            case 12:
                // Load linked in page
                webView.loadUrl("http://www.msd-uk.com/index.xhtml");
                break;
            case 13:
                // Load linked in page
                webView.loadUrl("http://www.nerhadou.com/");
                break;

        }

        return view;
    }

    @Override
    public void refreshContent() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                } else if (mode == ZOOM) {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Show an event in the LogCat view, for debugging
     */
    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }
}
