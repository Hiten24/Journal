package com.hcapps.journal.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.hcapps.journal.model.GalleryState
import com.hcapps.journal.model.Journal
import com.hcapps.journal.model.Mood
import java.time.ZonedDateTime

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: UiState,
    moodName: () -> String,
    galleryState: GalleryState,
    pagerState: PagerState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeletedConfirmed: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onBackPressed: () -> Unit,
    onSaveClicked: (Journal) -> Unit,
    onImageSelect: (Uri) -> Unit
) {
    // Update the Mood when selecting an existing Journal
    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedJournal = uiState.selectedJournal,
                moodName = moodName,
                onDeleteConfirmed = onDeletedConfirmed,
                onBackPressed = onBackPressed,
                onDateTimeUpdated = onDateTimeUpdated
            )
        },
        content = {
            WriteContent(
                uiState = uiState,
                pagerState = pagerState,
                galleryState = galleryState,
                title = uiState.title,
                onTitleChanged = onTitleChanged,
                description = uiState.description,
                onDescriptionChanged = onDescriptionChanged,
                paddingValues = it,
                onSaveClicked = onSaveClicked,
                onImageSelect = onImageSelect
            )
        }
    )
}