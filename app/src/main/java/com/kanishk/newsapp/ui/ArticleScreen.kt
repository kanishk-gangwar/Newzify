package com.kanishk.newsapp.ui

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kanishk.newsapp.ui.dataclass.Article
import com.kanishk.newsapp.ui.theme.RobotoFont
import com.kanishk.newsapp.ui.theme.primaryColor
import java.net.URI


@Composable
fun ArticleScreen(article: Article,onNewsScreen:() -> Unit) {

//onNewsScreen:() -> Unit
    val imageURl: String? = article.urlToImage
    val description: String? = article.content
    val title: String = article.title
    var Author: String? = article.author
    var Date: String? = formatIsoTimestamp(article.publishedAt)
    val newsUrl: String = article.url
    val context = LocalContext.current
    Column(Modifier.fillMaxSize().background(Color.White)) {




            Image(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "BackButton",
                modifier = Modifier.size(50.dp).clickable(onClick = onNewsScreen).padding(13.dp))

        Spacer(Modifier.height(10.dp))

        title?.let {

            Text(
                it,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontFamily = RobotoFont,
                letterSpacing = 0.7.sp,
                modifier = Modifier.padding(10.dp)
            )
        }


        Image(

            painter = rememberAsyncImagePainter(imageURl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp,10.dp,20.dp,10.dp), horizontalArrangement = Arrangement.SpaceBetween) {

            Date?.let {
                Text(
                    it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor,
                    fontFamily = RobotoFont
                )
            }

            Author?.let {
                Text(
                    it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor,
                    fontFamily = RobotoFont
                )
            }

        }

        description?.let {
            Text(
                article.description + "\n\n" +it,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray,
                fontFamily = RobotoFont,
                modifier = Modifier.padding(10.dp)
            )
        }


        Text(
            text = "Read More....",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Red,
            fontFamily = RobotoFont,
            letterSpacing = 0.7.sp,
            modifier = Modifier.padding(20.dp).align(alignment = Alignment.End).clickable {
                val uri = Uri.parse(newsUrl)
                val intentBuilder = CustomTabsIntent.Builder()
                val customTabsIntent = intentBuilder.build()
                customTabsIntent.launchUrl(context, uri)
            }
        )


        }

    }


@Preview
@Composable
fun testArticle(){
    val imageURl: String? = "null"
    var Author: String? = "CNN"
    var Date: String? = "21 Aug 2025"
    var description: String? = "Experimental Data description"
    var title: String? = "Experimental Data title"
    var newsUrl: String? = "url"
    val context = LocalContext.current
    Column(Modifier.fillMaxSize().background(Color.White)) {



        Spacer(Modifier.height(10.dp))

        Image(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "BackButton",
            modifier = Modifier.size(40.dp).clickable{})

        Spacer(Modifier.height(10.dp))

        title?.let {

            Text(
                it,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontFamily = RobotoFont,
                letterSpacing = 0.7.sp,
                modifier = Modifier.padding(10.dp)
            )
        }


        Image(

            painter = rememberAsyncImagePainter(imageURl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp,10.dp,20.dp,10.dp), horizontalArrangement = Arrangement.SpaceBetween) {

            Date?.let {
                Text(
                    it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Blue,
                    fontFamily = RobotoFont
                )
            }

            Author?.let {
                Text(
                    it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor,
                    fontFamily = RobotoFont
                )
            }

        }

        description?.let {
            Text(
                it,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray,
                fontFamily = RobotoFont,
                modifier = Modifier.padding(10.dp)
            )
        }


            Text(
                text = "Read More....",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red,
                fontFamily = RobotoFont,
                letterSpacing = 0.7.sp,
                modifier = Modifier.padding(20.dp).align(alignment = Alignment.End).clickable {
                    val uri = Uri.parse(newsUrl)
                    val intentBuilder = CustomTabsIntent.Builder()
                    val customTabsIntent = intentBuilder.build()
                    customTabsIntent.launchUrl(context, uri)
                }
            )



    }
}
