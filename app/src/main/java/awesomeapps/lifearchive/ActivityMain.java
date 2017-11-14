package awesomeapps.lifearchive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import awesomeapps.lifearchive.adapters.AdapterDrops;
import awesomeapps.lifearchive.beans.Drop;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

//TODO add a layout manager for the RecyclerView
public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;
    Toolbar mToolbar;
    Button mBtnAdd;
    RecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    AdapterDrops mAdapter;

    private View.OnClickListener mBtnAddListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            Log.d(TAG, "onChange: was called");
            mAdapter.update(mResults);
        }
    };

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();

        mToolbar = findViewById(R.id.toolbar);
        mBtnAdd = findViewById(R.id.btn_add);
        mRecycler = findViewById(R.id.rv_drops);

        mAdapter = new AdapterDrops(this, mResults);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);

        mRecycler.setAdapter(mAdapter);

        mBtnAdd.setOnClickListener(mBtnAddListener);
        setSupportActionBar(mToolbar);
        initBackgroundImage();
    }

    private void initBackgroundImage() {
        ImageView background = findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .into(background);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListener);
    }
}