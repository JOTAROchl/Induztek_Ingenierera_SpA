package cl.duocuc.induztekspa.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cl.duocuc.induztekspa.ui.styles.EstilosBoton

@Composable
fun BotonPrincipal(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icono: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.then(EstilosBoton.modifierPrincipal),
        shape = EstilosBoton.formaBoton,
        colors = EstilosBoton.coloresPrincipal(),
        enabled = enabled
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icono != null) {
                Icon(
                    imageVector = icono, 
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = text)
        }
    }
}