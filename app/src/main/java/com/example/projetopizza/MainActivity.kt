package com.example.projetopizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetopizza.ui.theme.ProjetoPizzaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetoPizzaTheme {
                ProgramaPrincipalPizza()
            }
        }
    }
}

// --- CLASSES DE DADOS ---

enum class TipoItem {
    TAMANHO,
    INGREDIENTE,
    BEBIDA,
    EXTRA
}

data class DadosUtilizador(
    var nome: String = "",
    var telefone: String = "",
    var morada: String = ""
)

data class ItemPedido(
    val nome: String,
    val preco: Double,
    val tipo: TipoItem
)

data class ItemCarrinho(
    val item: ItemPedido,
    var quantidade: Int
)

// --- PROGRAMA PRINCIPAL ---

@Composable
fun ProgramaPrincipalPizza() {

    // Dados partilhados
    val carrinho: MutableList<ItemCarrinho> = remember { mutableStateListOf() }
    val dadosCliente = remember { mutableStateOf(DadosUtilizador()) }
    val navControllerApp = rememberNavController()

    // --- CONFIGURAÇÃO DA BASE DE DADOS ---
    val context = LocalContext.current
    val db = remember { PizzaDatabase.getDatabase(context) }
    val utilizadorDao = db.utilizadorDao()
    // Lista para o histórico (opcional, caso queiras usar no registo)
    val listaUtilizadoresSalvos by utilizadorDao.getAllUtilizadores().observeAsState(initial = emptyList())

    // --- NAVHOST PRINCIPAL (SEM BARRA DE NAVEGAÇÃO) ---
    NavHost(navController = navControllerApp, startDestination = "inicio_flow") {

        // 1. Ecrã Início
        composable("inicio_flow") {
            Inicio(
                onIniciarClick = {
                    navControllerApp.navigate(DestinoPizza.Login.route)
                }
            )
        }

        // 2. Ecrã Login (Verifica se existe na BD)
        composable(DestinoPizza.Login.route) {
            Login(
                onVoltarClick = {
                    navControllerApp.navigate("inicio_flow") { popUpTo("inicio_flow") { inclusive = true } }
                },
                onVerificarNumero = { telefoneInserido ->
                    // Procura na BD
                    val utilizadorEncontrado = utilizadorDao.findByTelefone(telefoneInserido)

                    if (utilizadorEncontrado != null) {
                        // LOGIN SUCESSO: Carrega dados e entra na App
                        dadosCliente.value = DadosUtilizador(
                            nome = utilizadorEncontrado.nome,
                            telefone = utilizadorEncontrado.telefone,
                            morada = utilizadorEncontrado.morada
                        )
                        navControllerApp.navigate("main_app_flow") {
                            popUpTo("inicio_flow") { inclusive = true }
                        }
                    } else {
                        // NOVO CLIENTE: Preenche telefone e vai para Registo
                        dadosCliente.value = DadosUtilizador(telefone = telefoneInserido)
                        navControllerApp.navigate(DestinoPizza.Utilizadores.route)
                    }
                }
            )
        }

        // 3. Ecrã Registo (Utilizadores)
        composable(DestinoPizza.Utilizadores.route) {
            Utilizadores(
                dados = dadosCliente,
                historicoUtilizadores = listaUtilizadoresSalvos,

                onUtilizadorSelecionado = { userBd ->
                    dadosCliente.value = DadosUtilizador(userBd.nome, userBd.telefone, userBd.morada)
                },
                onApagarClick = { userBd ->
                    utilizadorDao.deleteUtilizador(userBd)
                },

                onProximoClick = {
                    // Guarda na BD e entra na App
                    if (dadosCliente.value.nome.isNotBlank()) {
                        utilizadorDao.insertUtilizador(
                            UtilizadorBd(
                                nome = dadosCliente.value.nome,
                                telefone = dadosCliente.value.telefone,
                                morada = dadosCliente.value.morada
                            )
                        )
                    }
                    navControllerApp.navigate("main_app_flow") {
                        popUpTo("inicio_flow") { inclusive = true }
                    }
                },

                onVoltarClick = {
                    navControllerApp.navigate(DestinoPizza.Login.route)
                }
            )
        }

        // 4. A APP PRINCIPAL (Aqui é que entra a Barra)
        composable("main_app_flow") {
            AppComNavegacao(navControllerApp, carrinho, dadosCliente)
        }
    }
}

@Composable
fun AppComNavegacao(
    navControllerApp: NavHostController,
    carrinho: MutableList<ItemCarrinho>,
    dadosCliente: MutableState<DadosUtilizador>
) {
    val navControllerBarra = rememberNavController()

    // Lista da Barra (Só os ecrãs de pedido)
    val ecransDaBarra = listOf(
        DestinoPizza.Tamanho,
        DestinoPizza.Ingredientes,
        DestinoPizza.Complementos,
        DestinoPizza.Resumo
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navControllerBarra.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                ecransDaBarra.forEach { ecra ->
                    NavigationBarItem(
                        label = { Text(text = ecra.title) },
                        icon = { Icon(painterResource(id = ecra.icon!!), contentDescription = ecra.title) },
                        selected = currentRoute == ecra.route,
                        onClick = {
                            navControllerBarra.navigate(ecra.route) {
                                launchSingleTop = true
                                restoreState = true
                                navControllerBarra.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) { saveState = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigationPizza(
                navController = navControllerBarra,
                navControllerApp = navControllerApp,
                carrinho = carrinho,
                dadosCliente = dadosCliente
            )
        }
    }
}

@Composable
fun AppNavigationPizza(
    navController: NavHostController,
    navControllerApp: NavHostController,
    carrinho: MutableList<ItemCarrinho>,
    dadosCliente: MutableState<DadosUtilizador>
) {
    NavHost(
        navController = navController,
        startDestination = DestinoPizza.Tamanho.route
    ) {

        composable(DestinoPizza.Tamanho.route) {
            Tamanho(
                carrinho = carrinho,
                onProximoClick = { navController.navigate(DestinoPizza.Ingredientes.route) },
                onVoltarClick = {
                    // Voltar sai da app (Logout)
                    navControllerApp.navigate(DestinoPizza.Login.route) {
                        popUpTo("main_app_flow") { inclusive = true }
                    }
                }
            )
        }

        composable(DestinoPizza.Ingredientes.route) {
            Ingredientes(
                carrinho = carrinho,
                onProximoClick = { navController.navigate(DestinoPizza.Complementos.route) },
                onVoltarClick = { navController.navigate(DestinoPizza.Tamanho.route) }
            )
        }

        composable(DestinoPizza.Complementos.route) {
            Complementos(
                carrinho = carrinho,
                onProximoClick = { navController.navigate(DestinoPizza.Resumo.route) },
                onVoltarClick = { navController.navigate(DestinoPizza.Ingredientes.route) }
            )
        }

        composable(DestinoPizza.Resumo.route) {
            Resumo(
                carrinho = carrinho,
                dadosCliente = dadosCliente.value,
                onVoltarClick = { navController.navigate(DestinoPizza.Complementos.route) },
                onConfirmarClick = {
                    carrinho.clear()
                    // Volta ao início (Logout)
                    navControllerApp.navigate("inicio_flow") {
                        popUpTo("main_app_flow") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProjetoPizzaTheme {
        ProgramaPrincipalPizza()
    }
}