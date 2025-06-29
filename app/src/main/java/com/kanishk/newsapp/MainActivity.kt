package com.kanishk.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kanishk.newsapp.ui.ArticleScreen
import com.kanishk.newsapp.ui.NewsScreen
import com.kanishk.newsapp.ui.theme.NewsAPPTheme
import com.kanishk.newsapp.ui.Screen
import com.kanishk.newsapp.ui.dataclass.Article

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            NewsAPPTheme {
                SetStatusBarColor(color = MaterialTheme.colorScheme.primary)
                Scaffold(modifier = Modifier.safeDrawingPadding().fillMaxSize(),contentWindowInsets = WindowInsets.systemBars) { innerPadding ->
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }




}


@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color)
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.News.route) {
        composable(Screen.News.route) {
            NewsScreen(onArticleClick = {article ->
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("article", article)
                navController.navigate(Screen.Article.route){
                    popUpTo(Screen.News.route) {
                        inclusive = false
                    }
                }
            })
        }
        composable(Screen.Article.route) {
            val article = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Article>("article")

            article?.let {
                ArticleScreen(article = it, onNewsScreen = { navController.navigate(Screen.News.route){
                    popUpTo(Screen.Article.route) {
                        inclusive = true
                    }
                } })
            }
        }
    }
}

