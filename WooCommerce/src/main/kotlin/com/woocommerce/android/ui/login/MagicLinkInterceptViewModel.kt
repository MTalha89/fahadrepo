package com.woocommerce.android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.woocommerce.android.R
import com.woocommerce.android.model.RequestResult
import com.woocommerce.android.viewmodel.ScopedViewModel
import com.woocommerce.android.viewmodel.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MagicLinkInterceptViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val magicLinkInterceptRepository: MagicLinkInterceptRepository
) : ScopedViewModel(savedState) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isAuthTokenUpdated = MutableLiveData<Boolean>()
    val isAuthTokenUpdated: LiveData<Boolean> = _isAuthTokenUpdated

    private val _showSnackbarMessage = SingleLiveEvent<Int>()
    val showSnackbarMessage: LiveData<Int> = _showSnackbarMessage

    private val _showRetryOption = SingleLiveEvent<Boolean>()
    val showRetryOption: LiveData<Boolean> = _showRetryOption

    fun updateMagicLinkAuthToken(authToken: String) {
        launch {
            _isLoading.value = true
            handleRequestResultResponse(magicLinkInterceptRepository.updateMagicLinkAuthToken(authToken))
        }
    }

    fun fetchAccountInfo() {
        launch {
            _isLoading.value = true
            _showRetryOption.value = false
            handleRequestResultResponse(magicLinkInterceptRepository.fetchAccountInfo())
        }
    }

    private fun handleRequestResultResponse(requestResult: RequestResult) {
        _isLoading.value = false
        when (requestResult) {
            RequestResult.SUCCESS -> _isAuthTokenUpdated.value = true

            // Errors can occur if the auth token was not updated to the FluxC cache
            // or if the user is not logged in
            // Either way, display error message and redirect user to login screen
            RequestResult.ERROR -> {
                _isAuthTokenUpdated.value = false
                _showSnackbarMessage.value = R.string.magic_link_update_error
            }

            // If magic link update is successful, but account & site info could not be fetched,
            // display error message and provide option for user to retry the request
            RequestResult.RETRY -> {
                _showSnackbarMessage.value = R.string.magic_link_fetch_account_error
                _showRetryOption.value = true
            }
            RequestResult.NO_ACTION_NEEDED -> { }
            RequestResult.API_ERROR -> Unit // Do nothing
        }
    }

    override fun onCleared() {
        super.onCleared()
        magicLinkInterceptRepository.onCleanup()
    }
}
