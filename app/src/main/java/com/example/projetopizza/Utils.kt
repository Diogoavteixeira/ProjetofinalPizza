package com.example.projetopizza

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Este ficheiro controla os butoes de aumentar e diminuir a quantidade de itens no carrinho, assim ivita se de colocar este codigo em todos os ecras

//Professor, reparei que ia ter de escrever o código dos botões + e - três vezes (no Tamanho, nos Ingredientes e nos Complementos).
//Para não repetir código e manter os ecrãs limpos, criei esta pequena função auxiliar.
@Composable
fun ContadorQuantidade(
    quantidade: Int,
    onAumentar: () -> Unit,
    onDiminuir: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = onDiminuir, enabled = quantidade > 0) { Text("-") }
        Text(
            text = "$quantidade",
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(onClick = onAumentar) { Text("+") }
    }
}

