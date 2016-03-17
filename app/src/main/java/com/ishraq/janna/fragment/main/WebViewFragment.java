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
            webViewNumber = 1;
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
            case 2:
                // Load Our web site
                webView.loadUrl("http://beta.html5test.com/");
                break;

            case 3:
                // Load Facebook page
                webView.loadUrl("http://beta.html5test.com/");
                break;

            case 4:
                // Load Twitter page
                webView.loadUrl("http://beta.html5test.com/");
                break;

            case 5:
                // Load linked in page
                webView.loadUrl("http://beta.html5test.com/");
                break;
        }

        return view;
    }

    @Override
    public void refreshContent() {
    }
}
