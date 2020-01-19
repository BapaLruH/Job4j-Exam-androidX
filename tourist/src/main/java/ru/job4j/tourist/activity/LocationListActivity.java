package ru.job4j.tourist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.job4j.tourist.App;
import ru.job4j.tourist.R;
import ru.job4j.tourist.adapter.LocationAdapter;
import ru.job4j.tourist.dagger.AdapterModule;
import ru.job4j.tourist.dagger.DaggerLocationListComponent;
import ru.job4j.tourist.dagger.LocationListComponent;
import ru.job4j.tourist.db.dao.LocationDao;
import ru.job4j.tourist.db.model.ModelLocation;

public class LocationListActivity extends AppCompatActivity implements LocationAdapter.SelectItemListener {

    private static final String TAG = LocationListActivity.class.getSimpleName();
    private LocationListComponent component;
    @Inject
    public LocationDao locationDao;
    public LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        App.getComponent().inject(this);
        component = DaggerLocationListComponent.builder()
                .adapterModule(new AdapterModule(this))
                .build();
        locationAdapter = component.getLocationAdapter();

        RecyclerView recyclerView = findViewById(R.id.location_rw);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(locationAdapter);
        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> onBackPressed());
        fillLocations();
    }

    private void fillLocations() {
        locationDao.getAllLocation().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<ModelLocation>>() {
                    @Override
                    public void onSuccess(List<ModelLocation> modelLocations) {
                        locationAdapter.setItems(modelLocations);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
    }


    @Override
    public void onItemSelect(ModelLocation location) {
        Intent intent = new Intent();
        intent.putExtra("location", location);
        setResult(MapsActivity.REQUEST_CODE, intent);
        finish();
    }
}
