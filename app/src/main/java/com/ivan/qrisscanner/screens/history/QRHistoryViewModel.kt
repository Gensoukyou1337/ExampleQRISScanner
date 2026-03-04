package com.ivan.qrisscanner.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.domain.displayentity.TransactionDisplayableEntity
import com.ivan.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QRHistoryViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _transactionsList: MutableStateFlow<List<TransactionDisplayableEntity>> =
        MutableStateFlow(emptyList())
    val transactionsList: StateFlow<List<TransactionDisplayableEntity>> get() = _transactionsList

    fun getAllTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            _transactionsList.value = repository.queryAllTransactionData()
        }
    }
}