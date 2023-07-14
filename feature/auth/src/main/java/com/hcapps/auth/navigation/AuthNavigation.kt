package com.hcapps.auth.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hcapps.util.Screen
import com.stevdzasan.onetap.rememberOneTapSignInState

fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit) {
    composable(route = Screen.Authentication.route) {

        val viewModel: com.hcapps.auth.AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val context = LocalContext.current

        com.hcapps.auth.AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTapSignInState = oneTapState,
            onSuccessfulFirebaseSignIn = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId,
                    onSuccess = {
                        Toast.makeText(context, "Successfully Authenticated!", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.setLoading(false)
                    },
                    onError = { exception ->
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                        Log.e("google_auth", "authenticationRoute: ${exception.message}", )
                        viewModel.setLoading(false)
                    }
                )
            },
            onFailedFirebaseSignIn = {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                viewModel.setLoading(false)
            },
            onDialogDismissed = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.setLoading(false)
            },
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            navigateToHome = navigateToHome
        )

    }
}