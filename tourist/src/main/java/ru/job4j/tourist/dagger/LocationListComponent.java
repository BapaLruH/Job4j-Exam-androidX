package ru.job4j.tourist.dagger;

import dagger.Component;
import ru.job4j.tourist.adapter.LocationAdapter;

@LocationListScope
@Component(modules = {AdapterModule.class})
public interface LocationListComponent {
    LocationAdapter getLocationAdapter();
}
