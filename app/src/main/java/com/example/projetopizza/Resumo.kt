package com.example.projetopizza

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun formatarPreco(preco: Double): String {
    val precoFormatado = String.format("%.2f", preco)
    return "$precoFormatado €"
}

@Composable
fun Resumo(
    carrinho: List<ItemCarrinho>,
    dadosCliente: DadosUtilizador,
    onConfirmarClick: () -> Unit,
    onVoltarClick: () -> Unit
) {
    val context = LocalContext.current
    val precoTotal = carrinho.sumOf { it.item.preco * it.quantidade }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {


        Text(text = "Resumo do seu Pedido", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {


            Text(text = "Dados de Entrega", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Nome: ${dadosCliente.nome}", fontSize = 16.sp)
            Text(text = "Telefone: ${dadosCliente.telefone}", fontSize = 16.sp)
            Text(text = "Morada: ${dadosCliente.morada}", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))


            Text(text = "Itens no Carrinho", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            carrinho.forEach { itemNoCarrinho ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${itemNoCarrinho.quantidade}x ${itemNoCarrinho.item.nome}",
                        fontSize = 16.sp, // Letra ajustada para caber bem
                        modifier = Modifier.weight(0.6f)
                    )
                    Text(
                        text = formatarPreco(itemNoCarrinho.item.preco * itemNoCarrinho.quantidade),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(0.4f),
                        textAlign = TextAlign.End
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TOTAL: ${formatarPreco(precoTotal)}",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )


            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onVoltarClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.weight(1f)
            ) {
                Text("Voltar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    Toast.makeText(context, "Pedido confirmado para ${dadosCliente.nome}!", Toast.LENGTH_LONG).show()
                    onConfirmarClick()
                },
                modifier = Modifier.weight(1f),
                enabled = carrinho.isNotEmpty()
            ) {
                Text("Confirmar")
            }
        }
    }
}