package cl.duocuc.induztekspa.ui.styles

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import cl.duocuc.induztekspa.ui.theme.Dimens

object EstilosCampo {
    val modifierFormulario: Modifier
        get() = Modifier.fillMaxWidth()

    val formaCampo = RoundedCornerShape(Dimens.cornerRadius)
}