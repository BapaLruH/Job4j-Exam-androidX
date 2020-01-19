package ru.job4j.tourist.dagger;

import dagger.Module;
import dagger.Provides;
import ru.job4j.tourist.adapter.LocationAdapter;

@Module
public class AdapterModule {
    private final LocationAdapter.SelectItemListener listener;

    public AdapterModule(LocationAdapter.SelectItemListener listener) {
        this.listener = listener;
    }

    @LocationListScope
    @Provides
    LocationAdapter provideLocationAdapter() {
        return new LocationAdapter(listener);
    }
}
