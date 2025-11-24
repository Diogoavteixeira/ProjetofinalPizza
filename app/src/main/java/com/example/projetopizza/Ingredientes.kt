package com.example.projetopizza

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val listaIngredientes = listOf(
    ItemPedido("Queijo Extra", 1.50, TipoItem.INGREDIENTE),
    ItemPedido("Fiambre", 1.00, TipoItem.INGREDIENTE),
    ItemPedido("Cogumelos", 1.00, TipoItem.INGREDIENTE),
    ItemPedido("Ovo", 1.00, TipoItem.INGREDIENTE),
    ItemPedido("Ananás", 1.20, TipoItem.INGREDIENTE),
    ItemPedido("Pimentos", 0.80, TipoItem.INGREDIENTE),
    ItemPedido("Azeitonas", 0.70, TipoItem.INGREDIENTE),
    ItemPedido("Tomate", 0.50, TipoItem.INGREDIENTE),
    ItemPedido("Cebola", 1.00, TipoItem.INGREDIENTE)
)

@Composable
fun Ingredientes(
    carrinho: MutableList<ItemCarrinho>,
    onProximoClick: () -> Unit,
    onVoltarClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Escolha os Ingredientes", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // --- A LISTA COMEÇA AQUI ---
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(listaIngredientes) { item ->
                val itemNoCarrinho = carrinho.find { it.item.nome == item.nome }
                val quantidade = itemNoCarrinho?.quantidade ?: 0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${item.nome} (+ ${item.preco}0 €)", fontSize = 18.sp)

                    // Contar quantidade de itens no carrinho
                    ContadorQuantidade(
                        quantidade = quantidade,
                        onAumentar = {
                            if (itemNoCarrinho == null) {
                                carrinho.add(ItemCarrinho(item = item, quantidade = 1))
                            } else {
                                val index = carrinho.indexOf(itemNoCarrinho)
                                if (index != -1) {
                                    carrinho[index] =
                                        itemNoCarrinho.copy(quantidade = itemNoCarrinho.quantidade + 1)
                                }
                            }
                        },
                        onDiminuir = {
                            if (itemNoCarrinho != null) {
                                if (itemNoCarrinho.quantidade > 1) {
                                    val index = carrinho.indexOf(itemNoCarrinho)
                                    if (index != -1) {
                                        carrinho[index] =
                                            itemNoCarrinho.copy(quantidade = itemNoCarrinho.quantidade - 1)
                                    }
                                } else {
                                    carrinho.remove(itemNoCarrinho)
                                }
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


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
                onClick = onProximoClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Próximo")
            }
        }
    }
}