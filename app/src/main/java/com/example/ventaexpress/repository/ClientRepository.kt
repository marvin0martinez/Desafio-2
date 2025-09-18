package com.example.ventaexpress.repository
import com.example.ventaexpress.model.Client

interface ClientRepository {
    fun addClient(client: Client, onComplete: (Boolean, String?) -> Unit)
    fun updateClient(client: Client, onComplete: (Boolean, String?) -> Unit)
    fun deleteClient(clientId: String, onComplete: (Boolean, String?) -> Unit)
    fun fetchClients(listener: (List<Client>) -> Unit)
}