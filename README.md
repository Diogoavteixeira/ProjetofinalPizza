# 🍕 Pizza Ordering App (Android)

Uma aplicação nativa Android desenvolvida com **Kotlin** e **Jetpack Compose** para a gestão de encomendas de uma pizzaria. O projeto foca-se numa experiência de utilizador fluida, gestão de estado eficiente e persistência de dados local.

## 📱 Funcionalidades Principais

* **Sistema de Login Inteligente:** Verifica se o número de telemóvel já existe na base de dados.
    * *Cliente Recorrente:* Login automático e carregamento de dados.
    * *Novo Cliente:* Redirecionamento para o formulário de registo.
* **Gestão de Carrinho de Compras:** Adicionar, remover e atualizar quantidades de produtos em tempo real.
* **Navegação Híbrida:** * Fluxo de Entrada (Login/Registo) sem barras de navegação.
    * Fluxo de Loja com `BottomNavigationBar` para navegação rápida entre categorias.
* **Persistência de Dados:** Utilização do **Room Database** para guardar histórico de clientes e endereços.
* **Cálculo Automático:** O resumo do pedido calcula o total a pagar dinamicamente.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **Interface (UI):** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Navegação:** Navigation Compose
* **Base de Dados:** Room Database (SQLite)
* **Arquitetura:** MVVM Simplificado (State Hoisting)

## 📂 Estrutura do Projeto

O projeto está organizado da seguinte forma:

| Ficheiro | Descrição |
| :--- | :--- |
| **`MainActivity.kt`** | O "cérebro" da app. Gere o estado global (`carrinho`, `dadosCliente`), a navegação principal e a lógica de verificação de login. |
| **`BaseDados.kt`** | Configuração do Room Database (Entity, DAO e Database). Permite ler e gravar utilizadores. |
| **`Destinos.kt`** | Sealed Classes para gestão segura das rotas de navegação. |
| **`Login.kt`** | Ecrã inicial. Valida o número de telemóvel e consulta a BD. |
| **`Utilizadores.kt`** | Ecrã de registo para novos clientes (Nome, Telefone, Morada). |
| **`Tamanho.kt`** | Seleção de Pizzas (Pequena, Média, Grande). |
| **`Ingredientes.kt`** | Seleção de ingredientes extra. |
| **`Complementos.kt`** | Seleção de bebidas e sobremesas. |
| **`Resumo.kt`** | Ecrã final com a lista de itens escolhidos e o total a pagar. |
| **`Utils.kt`** | Componentes reutilizáveis (ex: Botões de `+` e `-` para quantidades). |

## 🧠 Lógica de Autenticação

A aplicação implementa uma verificação lógica simples mas eficaz para a entrada:

1.  O utilizador insere o telemóvel no ecrã `Login`.
2.  A app executa uma query SQL: `SELECT * FROM utilizadores WHERE telefone = input`.
3.  **Se existir:** Recupera o Nome/Morada e avança direto para o pedido (`Tamanho`).
4.  **Se não existir:** Guarda o número e avança para o registo (`Utilizadores`) para completar os dados.

## 📸 Como Executar

1.  Clone este repositório.
2.  Abra o projeto no **Android Studio**.
3.  Aguarde a sincronização do Gradle.
4.  Execute num Emulador ou Dispositivo Físico Android.

---
**Desenvolvido no âmbito da disciplina de Computaçao Móvel.**
