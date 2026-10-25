package cl.duocuc.induztekspa.ui.styles

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duocuc.induztekspa.ui.theme.Dimens

object EstilosBoton {
    val modifierPrincipal: Modifier
        get() = Modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight)

    val formaBoton = RoundedCornerShape(Dimens.cornerRadius)

    @Composable
    fun coloresPrincipal(): ButtonColors {
        return ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}