package cl.duocuc.induztekspa.ui.styles

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duocuc.induztekspa.ui.theme.Dimens

object EstilosTarjeta {
    val modifierLista: Modifier
        get() = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.paddingSmall)

    val formaTarjeta = RoundedCornerShape(Dimens.cornerRadius)

    @Composable
    fun coloresTarjeta(): CardColors {
        return CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}