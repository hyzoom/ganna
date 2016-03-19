package com.ishraq.janna.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ishraq.janna.R;

/**
 * Created by Ahmed on 3/17/2016.
 */
public class WebViewFragment extends MainCommonFragment {

    private WebView webView;
    private Integer webViewNumber;

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

        switch (webViewNumber) {

            case 1:
                // Load Our web site
                webView.loadUrl("https://www.google.com.eg/maps/place/%D8%AC%D8%B1%D8%A7%D9%86%D8%AF+%D9%86%D8%A7%D9%8A%D9%84+%D8%AA%D8%A7%D9%88%D8%B1%E2%80%AD/@30.0346311,31.2281593,17z/data=!4m2!3m1!1s0x0000000000000000:0x201285387107863a");
                break;
            case 2:
                // Load Our web site
                webView.loadUrl("http://alinany-clinic.com/hesham-clinic/%D9%85%D8%B3%D8%AA%D8%B4%D9%81%D9%89-%D8%AC%D9%86%D8%A9.html");
                break;

            case 3:
                // Load Facebook page
                webView.loadUrl("https://www.facebook.com/");
                break;

            case 4:
                // Load Twitter page
                webView.loadUrl("https://www.twitter.com/");
                break;

            case 5:
                // Load linked in page
                webView.loadUrl("https://www.linked-in.com/");
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
}
