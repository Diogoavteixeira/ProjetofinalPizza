package com.example.projetopizza

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Utilizadores(
    dados: MutableState<DadosUtilizador>,
    historicoUtilizadores: List<UtilizadorBd>,
    onUtilizadorSelecionado: (UtilizadorBd) -> Unit,
    onApagarClick: (UtilizadorBd) -> Unit,
    onProximoClick: () -> Unit,
    onVoltarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dados de Entrega",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = dados.value.nome,
            onValueChange = { novoNome ->
                if (novoNome.all { it.isLetter() || it.isWhitespace() }) {
                    dados.value = dados.value.copy(nome = novoNome)
                }
            },
            label = { Text("Nome Completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dados.value.telefone,
            onValueChange = { novoTelefone ->
                if (novoTelefone.length <= 9 && novoTelefone.all { it.isDigit() }) {
                    dados.value = dados.value.copy(telefone = novoTelefone)
                }
            },
            label = { Text("Telefone (Max 9)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dados.value.morada,
            onValueChange = { novaMorada ->
                dados.value = dados.value.copy(morada = novaMorada)
            },
            label = { Text("Morada de Entrega") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (historicoUtilizadores.isNotEmpty()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Text(
                    text = "Usar dados anteriores:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            historicoUtilizadores.take(3).forEach { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onUtilizadorSelecionado(user) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = user.nome, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "Tel: ${user.telefone}", fontSize = 14.sp, color = Color.DarkGray)
                            Text(text = user.morada, fontSize = 14.sp, color = Color.DarkGray, maxLines = 1)
                        }

                        IconButton(onClick = { onApagarClick(user) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Apagar",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

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
                modifier = Modifier.weight(1f),
                enabled = dados.value.nome.isNotBlank() &&
                        dados.value.morada.isNotBlank() &&
                        dados.value.telefone.length == 9
            ) {
                Text("Próximo")
            }
        }
    }
}

