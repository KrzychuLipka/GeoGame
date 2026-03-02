package pl.lipov.geogame.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pl.lipov.geogame.domain.model.FactionType

class MainViewModel : ViewModel() {

    var selectedFactionType by mutableStateOf<FactionType?>(null)
        private set

    fun onFactionSelected(
        factionType: FactionType
    ) {
        selectedFactionType = factionType
    }
}