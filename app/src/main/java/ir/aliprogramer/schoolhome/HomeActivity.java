package ir.aliprogramer.schoolhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import ir.aliprogramer.schoolhome.Fragments.CourseFragment;


public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView title;
    String nationalCode;
    FragmentManager manager;
    ProgressDialog progressDialog = null;
    AppPreferenceTools appPreferenceTools;
    static Context  context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.home_toolbar);
        title=toolbar.findViewById(R.id.home_title);
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        context=this.getApplicationContext();
        setSupportActionBar(toolbar);
        manager=getSupportFragmentManager();
        initList();
    }

    public void changeToolBarText(String txt){
        title.setText(txt);
    }

    /* Show progress dialog. */
    public void showProgressDialog()
    {
        // Set progress dialog display message.
        progressDialog.setMessage("لطفا منتظر بمانید...");

        // The progress dialog can not be cancelled.
        progressDialog.setCancelable(false);

        // Show it.
        progressDialog.show();
    }

    /* Hide progress dialog. */
    public void hideProgressDialog()
    {
        // Close it.
        progressDialog.hide();
    }
    private void initList() {
        appPreferenceTools=new AppPreferenceTools(this);

        CourseFragment courseFragment=new CourseFragment();
        courseFragment.setData(manager);

        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.frame_layout,courseFragment,"courseFragment");
        transaction.commit();

    }


    public static Context getContext() {
        return context;
    }

    @Override
    public void onBackPressed() {
        if(manager.getBackStackEntryCount()>0) {
            manager.popBackStack();
        }else{
            super.onBackPressed();
            appPreferenceTools.removeAllPrefs();
            finish();
        }
    }
}
