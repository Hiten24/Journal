package com.hcapps.journal.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hcapps.journal.util.Constants.CLIENT_ID
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

private const val TAG = "AuthenticationScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapSignInState: OneTapSignInState,
    onButtonClicked: () -> Unit
) {

    val context = LocalContext.current

    Scaffold {
        AuthenticationContent(
            loadingState = loadingState,
            onButtonClicked = onButtonClicked
        )
    }

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d(TAG, "AuthenticationScreen: $tokenId")
            Toast.makeText(context, "Successfully Authenticated!", Toast.LENGTH_SHORT).show()
        },
        onDialogDismissed = { message ->
            Log.d(TAG, "AuthenticationScreen: $message")
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    )

}