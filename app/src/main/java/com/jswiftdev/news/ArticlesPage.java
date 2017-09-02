package com.jswiftdev.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jswiftdev.news.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Handles urls from articles by showing the pages the point to
 * to use it calling intent should contain {@link Constants#KEY_URL}
 */
public class ArticlesPage extends AppCompatActivity {
    /**
     * the web view used to display the content of the page
     */
    @BindView(R.id.wv_article_content)
    WebView wvArticleSourceContent;

    /**
     * bar showing the progress of the page being loaded
     */
    @BindView(R.id.page_loading_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_page);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra(Constants.KEY_URL);

        if (url != null) {
            WebSettings settings = wvArticleSourceContent.getSettings();
            settings.setJavaScriptEnabled(true);
            wvArticleSourceContent.loadUrl(url);

            wvArticleSourceContent.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressBar.setProgress(newProgress);

                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else
                        progressBar.setVisibility(View.VISIBLE);
                }
            });

            wvArticleSourceContent.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }
}
