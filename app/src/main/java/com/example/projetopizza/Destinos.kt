package com.example.projetopizza

import com.example.projetopizza.R

sealed class DestinoPizza(
    val route: String,
    val title: String,
    val icon: Int?
) {
    object Login : DestinoPizza("login", "Login", null)

    object Utilizadores : DestinoPizza(
        route = "utilizadores",
        title = "Cliente",
        icon = R.drawable.ic_cliente
    )
    object Tamanho : DestinoPizza(
        route = "tamanho",
        title = "Pizzas",
        icon = R.drawable.ic_pizza
    )
    object Ingredientes : DestinoPizza(
        route = "ingredientes",
        title = "Ingredientes",
        icon = R.drawable.ic_ingredientes
    )
    object Complementos : DestinoPizza(
        route = "complementos",
        title = "Bebidas",
        icon = R.drawable.ic_bebidas
    )
    object Resumo : DestinoPizza(
        route = "resumo",
        title = "Carrinho",
        icon = R.drawable.ic_carrinho
    )
}