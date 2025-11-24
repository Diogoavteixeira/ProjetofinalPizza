package com.example.projetopizza

sealed class DestinoPizza(
    val route: String,
    val title: String,
    val icon: Int?
) {

    object Utilizadores : DestinoPizza(
        route = "utilizadores",
        title = "Cliente",
        icon = R.drawable.ic_cliente
    )
    object Tamanho : DestinoPizza(
        route = "tamanho",
        title = "tamanho",
        icon = R.drawable.ic_pizza
    )
    object Ingredientes : DestinoPizza(
        route = "ingredientes",
        title = "Ingredientes",
        icon = R.drawable.ic_ingredientes
    )
    object Complementos : DestinoPizza(
        route = "complementos",
        title = "complementos",
        icon = R.drawable.ic_bebidas
    )
    object Resumo : DestinoPizza(
        route = "resumo",
        title = "resumo",
        icon = R.drawable.ic_carrinho
    )
}

