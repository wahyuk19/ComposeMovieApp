package com.dev.composemovie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.dev.composemovie.R
import com.dev.composemovie.navigation.MovieScreens

@Composable
fun MovieAppLogo(modifier: Modifier = Modifier) {
    Image(
        painter = rememberImagePainter(data = R.drawable.tmdb),
        contentDescription = "pokemon image"
    )
}

@Composable
fun MovieAppBar(
    title: String,
    icon: ImageVector? = null,
    showReviews: Boolean = true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {}
) {

    TopAppBar(
        title = {
            if (showReviews) {
                Image(painter = rememberImagePainter(data = R.drawable.baseline_reviews_24),
                    contentDescription = "Logo Icon",
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp)
                        .clickable {
                            navController.navigate(MovieScreens.ReviewScreen.name)
                        }
                )

            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = "arrow back",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.clickable { onBackArrowClicked.invoke() })
                }
                Spacer(modifier = Modifier.width(40.dp))
                Text(
                    text = title,
                    color = Color.Red.copy(alpha = 0.7f),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )


            }


        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )

}