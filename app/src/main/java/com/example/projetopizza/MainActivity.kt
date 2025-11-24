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

@Composable
fun ProgramaPrincipalPizza() {

    val carrinho: MutableList<ItemCarrinho> = remember { mutableStateListOf() }
    val dadosCliente = remember { mutableStateOf(DadosUtilizador()) }

    val navControllerApp = rememberNavController()

    NavHost(navController = navControllerApp, startDestination = "inicio_flow") {

        composable("inicio_flow") {
            Inicio(
                onIniciarClick = {
                    navControllerApp.navigate("main_app_flow") {
                        popUpTo("inicio_flow") { inclusive = true }
                    }
                }
            )
        }

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

    val ecransDaBarra = listOf(
        DestinoPizza.Utilizadores,
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

    val context = LocalContext.current

    val db = remember { PizzaDatabase.getDatabase(context) }
    val utilizadorDao = db.utilizadorDao()


    val listaUtilizadoresSalvos by utilizadorDao.getAllUtilizadores().observeAsState(initial = emptyList())

    NavHost(
        navController = navController,
        startDestination = DestinoPizza.Utilizadores.route
    ) {

        composable(DestinoPizza.Utilizadores.route) {
            Utilizadores(
                dados = dadosCliente,

                historicoUtilizadores = listaUtilizadoresSalvos,


                onUtilizadorSelecionado = { userBd ->

                    dadosCliente.value = DadosUtilizador(
                        nome = userBd.nome,
                        telefone = userBd.telefone,
                        morada = userBd.morada
                    )
                },
                onApagarClick = { userBd ->
                    utilizadorDao.deleteUtilizador(userBd)
                },


                onProximoClick = {

                    if (dadosCliente.value.nome.isNotBlank()) {
                        utilizadorDao.insertUtilizador(
                            UtilizadorBd(
                                nome = dadosCliente.value.nome,
                                telefone = dadosCliente.value.telefone,
                                morada = dadosCliente.value.morada
                            )
                        )
                    }

                    navController.navigate(DestinoPizza.Tamanho.route)
                },

                onVoltarClick = {
                    navControllerApp.navigate("inicio_flow") {
                        popUpTo("main_app_flow") { inclusive = true }
                    }
                }
            )
        }

        composable(DestinoPizza.Tamanho.route) {
            Tamanho(
                carrinho = carrinho,
                onProximoClick = { navController.navigate(DestinoPizza.Ingredientes.route) },
                onVoltarClick = { navController.navigate(DestinoPizza.Utilizadores.route) }
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