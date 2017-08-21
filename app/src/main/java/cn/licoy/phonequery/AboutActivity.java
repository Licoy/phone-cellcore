package cn.licoy.phonequery;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private Button go_github;
    private Button go_blog;
    private Button go_bug;
    private static final String github = "https://github.com/Licoy";
    private static final String blog = "https://www.licoy.cn";
    private static final String bug = "https://github.com/Licoy/phone-query/issues";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        go_blog = (Button) findViewById(R.id.go_blog);
        go_github = (Button) findViewById(R.id.go_github);
        go_bug = (Button) findViewById(R.id.go_bug);

        go_blog.setOnClickListener(this);
        go_github.setOnClickListener(this);
        go_bug.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_github : startBrowser(github) ;break;
            case R.id.go_blog : startBrowser(blog) ;break;
            case R.id.go_bug : startBrowser(bug) ;break;
        }
    }

    private void startBrowser(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }
}
