package com.hcapps.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.hcapps.ui.GalleryImage
import com.hcapps.ui.GalleryState
import com.hcapps.util.GalleryUploader
import com.hcapps.util.model.Journal
import com.hcapps.util.model.Mood
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WriteContent(
    uiState: UiState,
    pagerState: PagerState,
    title: String,
    galleryState: GalleryState,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    paddingValues: PaddingValues,
    onSaveClicked: (Journal) -> Unit,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (GalleryImage) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = scrollState)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalPager(
                state = pagerState,
                count = Mood.values().size
            ) { page ->
                AsyncImage(
                    modifier = Modifier.size(120.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Mood.values()[page].icon)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Mood Image"
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = onTitleChanged,
                placeholder = { Text(text = "Title") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        scope.launch {
                            focusManager.moveFocus(FocusDirection.Down)
                            scrollState.animateScrollTo(Int.MAX_VALUE)
                        }
                    }
                ),
                maxLines = 1,
                singleLine = true
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = onDescriptionChanged,
                placeholder = { Text(text = "Tell me about it.") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                )
            )
        }

        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(12.dp))
            GalleryUploader(
                galleryState = galleryState,
                onAddClicked = { focusManager.clearFocus() },
                onImageSelect = onImageSelect,
                onImageClicked = onImageClicked
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                onClick = {
                    if (uiState.title.isNotEmpty() && uiState.description.isNotEmpty()) {
                        onSaveClicked(
                            Journal().apply {
                                this.title = uiState.title
                                this.description = uiState.description
                                this.images = galleryState.images.map { it.remoteImagePath }.toRealmList()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Fields cannot be empty.", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                shape = Shapes().small
            ) {
                Text(text = "Save")
            }
        }

    }
}