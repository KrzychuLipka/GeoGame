package pl.lipov.geogame.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pl.lipov.geogame.R
import pl.lipov.geogame.domain.model.Faction
import pl.lipov.geogame.ui.theme.AppTheme
import pl.lipov.geogame.ui.theme.Dimens

//import pl.lipov.geogame.ui.factionSelection.FactionSelectionScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold { padding ->
                    FactionSelectionScreen(
                        modifier = Modifier.padding(padding),
                        onFactionSelected = {
                            // TODO
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TestScreen() {
    AppTheme {
        Scaffold { padding ->
            FactionSelectionScreen(
                modifier = Modifier.padding(padding),
                onFactionSelected = {
                    // TODO
                }
            )
        }
    }
}

@Composable
fun FactionSelectionScreen(
    modifier: Modifier,
    onFactionSelected: (Faction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.select_faction)
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            FactionImage(
                faction = Faction.POLANDIA,
                imageResId = R.drawable.polandia,
                contentDescription = "polandia",
                onFactionSelected = {
                    // TODO
                }
            )
            Spacer(Modifier.height(Dimens.PaddingSmall))
            FactionImage(
                faction = Faction.AFRYKANIA,
                imageResId = R.drawable.arykania,
                contentDescription = "arykania",
                onFactionSelected = {
                    // TODO
                }
            )
        }
    }
}

@Composable
fun FactionImage(
    faction: Faction,
    @DrawableRes imageResId: Int,
    contentDescription: String,
    onFactionSelected: (Faction) -> Unit
) {
    Image(
        painter = painterResource(imageResId),
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onFactionSelected(faction)
            }
    )
}
