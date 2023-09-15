package com.dev.composemovie.screens.genres

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.composemovie.data.DataOrException
import com.dev.composemovie.data.Resource
import com.dev.composemovie.model.GenreX
import com.dev.composemovie.model.Result
import com.dev.composemovie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieGenreViewModel @Inject constructor(
    private val repository: MovieRepository
):ViewModel(){
    var list: List<GenreX> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    init {
        loadGenres()
    }

    private fun loadGenres() {
        loadList()
    }

    private fun loadList() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                when(val response = repository.getGenres()){
                    is Resource.Success ->{
                        list = response.data!!
                        if(list.isNotEmpty()) isLoading = false
                    }
                    is Resource.Error -> {
                        isLoading = false
                    }
                    else -> {isLoading = false}
                }
            }catch (e: Exception){
                isLoading = false
            }
        }
    }
}