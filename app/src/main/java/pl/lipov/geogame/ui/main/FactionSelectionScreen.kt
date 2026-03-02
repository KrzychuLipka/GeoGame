package pl.lipov.geogame.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import pl.lipov.geogame.R
import pl.lipov.geogame.domain.model.FactionType
import pl.lipov.geogame.ui.theme.Dimens

@Composable
fun FactionSelectionScreen(
    modifier: Modifier = Modifier,
    onFactionSelected: (FactionType) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.select_faction)
        )
        Row(
            modifier = Modifier.padding(top = Dimens.PaddingStandard)
        ) {
            FactionImage(
                modifier = Modifier.weight(1f),
                alignment = Alignment.CenterEnd,
                factionType = FactionType.POLANDIA,
                imageResId = R.drawable.polandia,
                onFactionSelected = onFactionSelected
            )
            Spacer(Modifier.width(Dimens.PaddingSmall))
            FactionImage(
                modifier = Modifier.weight(1f),
                alignment = Alignment.CenterStart,
                factionType = FactionType.AFRYKANIA,
                imageResId = R.drawable.afrykania,
                onFactionSelected = onFactionSelected
            )
        }
    }
}

@Composable
fun FactionImage(
    modifier: Modifier = Modifier,
    alignment: Alignment,
    factionType: FactionType,
    @DrawableRes imageResId: Int,
    onFactionSelected: (FactionType) -> Unit
) {
    Image(
        painter = painterResource(imageResId),
        contentDescription = factionType.name,
        modifier = modifier
            .fillMaxSize()
            .clickable { onFactionSelected(factionType) },
        alignment = alignment
    )
}
