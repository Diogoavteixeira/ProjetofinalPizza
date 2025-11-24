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

val listaTamanhos = listOf(
    ItemPedido("Pizza Pequena", 8.50, TipoItem.TAMANHO),
    ItemPedido("Pizza Média", 10.50, TipoItem.TAMANHO),
    ItemPedido("Pizza Grande", 12.50, TipoItem.TAMANHO)
)

@Composable
fun Tamanho(
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

        Text("Escolha as suas Pizzas", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))


        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(listaTamanhos) { item ->

                val itemNoCarrinho = carrinho.find { it.item.nome == item.nome }
                val quantidade = itemNoCarrinho?.quantidade ?: 0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${item.nome} (${item.preco}0 €)", fontSize = 18.sp)

                    ContadorQuantidade(
                        quantidade = quantidade,
                        onAumentar = {
                            if (itemNoCarrinho == null) {

                                carrinho.add(ItemCarrinho(item = item, quantidade = 1))
                            } else {

                                val index = carrinho.indexOf(itemNoCarrinho)
                                if (index != -1) {
                                    carrinho[index] = itemNoCarrinho.copy(quantidade = itemNoCarrinho.quantidade + 1)
                                }
                            }
                        },
                        onDiminuir = {
                            if (itemNoCarrinho != null) {
                                if (itemNoCarrinho.quantidade > 1) {

                                    val index = carrinho.indexOf(itemNoCarrinho)
                                    if (index != -1) {
                                        carrinho[index] = itemNoCarrinho.copy(quantidade = itemNoCarrinho.quantidade - 1)
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

