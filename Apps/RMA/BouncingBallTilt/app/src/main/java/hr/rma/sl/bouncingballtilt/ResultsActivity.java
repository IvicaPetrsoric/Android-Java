package hr.rma.sl.bouncingballtilt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ResultsActivity extends AppCompatActivity {

    WebView myWebView;
    boolean startPage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        startPage = true;

        getSupportActionBar().setTitle("Bouncing Ball ++ Highscores");

        myWebView = (WebView)findViewById(R.id.webView);
        myWebView.loadUrl("http://rijeka.riteh.hr/~sljubic/bounce/highscorelist.php");

        myWebView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startPage = false;
                //view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                if (startPage) {
                    view.getSettings().setLoadWithOverviewMode(false);
                    view.getSettings().setUseWideViewPort(false);
                } else {
                    view.getSettings().setLoadWithOverviewMode(true);
                    view.getSettings().setUseWideViewPort(true);
                }
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.results_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_backtogame:
                //this.finish();
                if (myWebView.canGoBack()) {
                    startPage = true;
                    myWebView.goBack();
                }
                else {
                    this.finish();
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            startPage = true;
            myWebView.goBack();
        }
        else {
            //super.onBackPressed();
            this.finish();
        }
    }

}
