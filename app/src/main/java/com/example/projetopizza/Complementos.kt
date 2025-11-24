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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

val listaBebidas = listOf(
    ItemPedido("Água 50cl", 1.00, TipoItem.BEBIDA),
    ItemPedido("Coca-Cola 33cl", 1.50, TipoItem.BEBIDA),
    ItemPedido("Coca-Cola Zero", 1.50, TipoItem.BEBIDA),
    ItemPedido("7Up 33cl", 1.50, TipoItem.BEBIDA),
    ItemPedido("cerveja 33cl", 1.50, TipoItem.BEBIDA),
    ItemPedido("Sumo Laranja", 1.40, TipoItem.BEBIDA)
)

val listaExtras = listOf(
    ItemPedido("Molho de Alho", 0.50, TipoItem.EXTRA),
    ItemPedido("Pão de Alho", 2.00, TipoItem.EXTRA),
    ItemPedido("Mousse Chocolate", 2.50, TipoItem.EXTRA)
)

@Composable
fun Complementos(
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

        Text("Bebidas e Extras", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Text("Bebidas", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            listaBebidas.forEach { item ->
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

            Spacer(modifier = Modifier.height(16.dp))

            Text("Extras", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            listaExtras.forEach { item ->
                val itemNoCarrinho = carrinho.find { it.item.nome == item.nome }
                val quantidade = itemNoCarrinho?.quantidade ?: 0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${item.nome} (+ ${item.preco}0 €)", fontSize = 16.sp)
                    ContadorQuantidade(
                        quantidade = quantidade,
                        onAumentar = {
                            if (itemNoCarrinho == null) {
                                carrinho.add(ItemCarrinho(item = item, quantidade = 1))
                            } else {val index = carrinho.indexOf(itemNoCarrinho)
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
                Text("Proximo")
            }
        }
    }
}

