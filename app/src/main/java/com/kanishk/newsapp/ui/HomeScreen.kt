package com.kanishk.newsapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.kanishk.newsapp.ui.dataclass.Article
import com.kanishk.newsapp.ui.theme.RobotoFont
import com.kanishk.newsapp.ui.theme.primaryColor
import com.kanishk.newsapp.ui.theme.whiteBasic
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NewsScreen(onArticleClick: (Article) -> Unit) {
    val selectedCategory = remember { mutableStateOf("general") }
    val viewModel: NewsViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteBasic)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){
            Text("Newzify", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = primaryColor, fontFamily = FontFamily.Monospace, letterSpacing = 0.7.sp)

            Spacer(Modifier.height(16.dp))

            CategoryRow(
                categories = listOf("general","business", "entertainment", "health", "science","sports","technology"),
                selectedCategory = selectedCategory.value,
                onCategorySelected = { selectedCategory.value = it }
            )

            Spacer(Modifier.height(16.dp))

            RandomNews(viewModel)

            Spacer(Modifier.height(24.dp))

            Text(selectedCategory.value.uppercase(), fontWeight = FontWeight.SemiBold, color = Color.Gray, fontFamily = RobotoFont)

            Spacer(Modifier.height(12.dp))

            NewsScreen(viewModel, selectedCategory.value,onArticleClick)


        }

    }
}

@Composable
fun NewsScreen(viewModel: NewsViewModel,selectedCategory: String,onArticleClick: (Article) -> Unit) {



    // Automatically fetch news when country changes
    LaunchedEffect(selectedCategory) {
        viewModel.fetchNews(selectedCategory)
    }

    Box(Modifier.fillMaxSize()) {
        if (viewModel.articles.isEmpty()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center), color = primaryColor)
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.articles) { article ->
                    NewsListItem(article,onArticleClick)
                }
            }
        }
    }

//    Column(modifier = Modifier.fillMaxSize()) {
//        // Country selector
//
//
//        // Article list
//        LazyColumn(
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            items(viewModel.articles) { article ->
//                NewsListItem(article,onArticleClick)
//            }
//        }
//
//    }
}



@Composable
fun RandomNews(viewModel: NewsViewModel) {

    val currentArticle by viewModel.currentArticle.collectAsState()

    LaunchedEffect(Unit) {

//        viewModel.fetchRandNews()
        viewModel.startAutoLifeCycle()


    }


    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Center) {
        currentArticle?.let { article ->
            currentArticle!!.urlToImage?.let {
                FeaturedNewsItem(
                    imageUrl = it,
                    title = currentArticle!!.title,
                    time = formatIsoTimestamp(currentArticle!!.publishedAt),
                    category = currentArticle!!.source.name
                )
            }
        } ?: Text(
            "News Loading....",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = primaryColor,
            fontFamily = RobotoFont,
            modifier = Modifier.padding(10.dp)
        )
    }


}


@Composable
fun CategoryRow(categories: List<String>, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        categories.forEach { category ->
            val selected = category == selectedCategory
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selected) Color(0xFF007BFF) else Color.LightGray.copy(alpha = 0.2f))
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category.substringBefore(" ").replaceFirstChar { it.uppercase() } +
                            category.substringAfter(" ", missingDelimiterValue = "").let { if (it.isNotEmpty()) " $it" else "" },
                    color = if (selected) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun FeaturedNewsItem(imageUrl: String, title: String, time: String, category: String) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Image",
                loading = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp), color = primaryColor
                        )
                    }
                },
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("$time  •  $category", color = Color.Gray, fontSize = 14.sp)
    }
}



fun formatIsoTimestamp(iso: String): String {
    val zonedDateTime = ZonedDateTime.parse(iso)
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm a", Locale.getDefault())
    return zonedDateTime.format(formatter)
}

@Composable
fun NewsListItem(news:Article,onArticleClick: (Article) -> Unit) {
//    val newsItems = listOf(
//        Triple("https://your_image_url_1.com", "New Regulations in Baseball", "3h ago  •  Sports"),
//        Triple("https://your_image_url_2.com", "The Impact of Climate Change", "4h ago  •  Politics"),
//        Triple("https://your_image_url_3.com", "Apple Unveils New iPhone", "6h ago  •  Technology")
//    )


            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = { onArticleClick(news) })
            ) {

//                news.urlToImage?.let { ImageWithLoaderManual(it) }

                SubcomposeAsyncImage(
                    model = news.urlToImage,
                    contentDescription = "Image",
                    loading = { CircularProgressIndicator(Modifier.align(Alignment.Center), color = primaryColor) },
                    modifier =Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
//
//                Image(
//                    painter = rememberAsyncImagePainter(news.urlToImage),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(RoundedCornerShape(8.dp)),
//                    contentScale = ContentScale.Crop
//                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(news.title, fontWeight = FontWeight.SemiBold, color = Color.Black, fontFamily = RobotoFont)
                    Text("${news.source.name} . ${formatIsoTimestamp(news.publishedAt)}", color = Color.Gray, fontSize = 12.sp)
                }

//                Icon(
//                    imageVector = Icons.Default.FavoriteBorder,
//                    contentDescription = "Bookmark"
//                )

    }
}



