package com.ecml;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class YoutubeActivity extends Activity {


	/** Called when the activity is first created. */
	
	/** Ne lance pas youtube mais ne plante pas et affiche un message d'erreur concernant le réseau */
	/*
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
       
        // Let's display the progress in the activity title bar, like the
        // browser app does.
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        WebView webview = new WebView(this);
        setContentView(webview);
       
        webview.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
        public void onProgressChanged(WebView view, int progress) {
             // Activities and WebViews measure progress with different scales.
             // The progress meter will automatically disappear when we reach 100%
             activity.setProgress(progress * 1000);
        }
      });
        
        webview.setWebViewClient(new WebViewClient() {
            
        	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        	          //Users will be notified in case there's an error (i.e. no internet connection)
        	          Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
        	}
        	 });
        	       //This will load the webpage that we want to see
        	        webview.loadUrl("http://youtube.com/");
        	 
        	     }
	
	*/
	
	/** Lance pas youtube mais MidiSheetMusic ne plante pas */
	/*
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView w = new WebView(this);
		w.loadUrl("http://www.youtube.com"); 
		setContentView(w);
	}
	*/
	
	/** Lance pas youtube mais MidiSheetMusic ne plante pas */
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        WebView view = (WebView) findViewById(R.id.help_webview);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("http://www.youtube.com");
	}
	*/
		
		/** Lance youtube mais MidiSheetMusic plante */ 
	    /*
		Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
		myWebLink.setData(Uri.parse("http://www.youtube.com"));
			startActivity(myWebLink);
		*/
		
	 	/** Lance youtube mais MidiSheetMusic plante */
		/*
	    Uri uri = Uri.parse("http://www.youyube.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		*/
		
	    // TODO Auto-generated method stub

		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Uri uri = Uri.parse("http://www.youtube.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
		 
	/*
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);
		 setContentView(webview);
		 WebView myWebView = (WebView) findViewById(R.id.webview);
		 myWebView.loadUrl("http://www.example.com");
		 */
	
}
