package com.example.ventaexpress.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.ventaexpress.model.Client
import com.example.ventaexpress.repository.ClientRepository

class ClientController : ClientRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // 📌 referencia al nodo "clientes" dentro del usuario autenticado
    private fun userRef(): DatabaseReference {
        val uid = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
        return database.child("users").child(uid).child("clientes")
    }

    // ➕ Crear cliente
    override fun addClient(client: Client, onComplete: (Boolean, String?) -> Unit) {
        val ref = userRef().push()
        client.id = ref.key ?: ""
        ref.setValue(client).addOnCompleteListener {
            onComplete(it.isSuccessful, it.exception?.message)
        }
    }

    // ✏️ Editar cliente
    override fun updateClient(client: Client, onComplete: (Boolean, String?) -> Unit) {
        userRef().child(client.id).setValue(client)
            .addOnCompleteListener { onComplete(it.isSuccessful, it.exception?.message) }
    }

    // ❌ Eliminar cliente
    override fun deleteClient(clientId: String, onComplete: (Boolean, String?) -> Unit) {
        userRef().child(clientId).removeValue()
            .addOnCompleteListener { onComplete(it.isSuccessful, it.exception?.message) }
    }

    // 📥 Listar clientes en tiempo real
    override fun fetchClients(listener: (List<Client>) -> Unit) {
        userRef().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val clients = snapshot.children.mapNotNull { it.getValue(Client::class.java) }
                listener(clients)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}