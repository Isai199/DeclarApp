package com.isadev.declarapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BDdeclarations {
    private lateinit var collection: String
    constructor(collection: String) {
        this.collection = collection
    }

    fun setDeclaration(declaration: HashMap<String, Any>, docName: String) {
        var dbDeclarations = FirebaseFirestore.getInstance()
        dbDeclarations.collection(this.collection).document(docName).set(declaration)
    }

    fun getDeclarations(field: String, order: Query.Direction, year: Int? = null, callback: (ArrayList<Declaration>) -> Unit) {
        var declarationsArray = ArrayList<Declaration>()
        var dbDeclarations = FirebaseFirestore.getInstance()
        if (year != null) {
            dbDeclarations.collection("declarations")
                .whereEqualTo("year", year)
                .orderBy(field, order)
                .get()
                .addOnSuccessListener { declarations ->
                    for (declaration in declarations)
                        if (declaration.data.isNotEmpty()) declarationsArray.add(declaration.toObject(Declaration::class.java))
                    callback(declarationsArray)
                }.addOnFailureListener {
                    Log.e("Error", it.message.toString())
                }
        } else {
            dbDeclarations.collection("declarations")
                .orderBy(field, order)
                .get()
                .addOnSuccessListener { declarations ->
                    for (declaration in declarations)
                        if (declaration.data.isNotEmpty()) declarationsArray.add(declaration.toObject(Declaration::class.java))
                    callback(declarationsArray)
                }.addOnFailureListener {
                    Log.e("Error", it.message.toString())
                }
        }
    }

    fun updateDeclaration(field: String, newValue: Any, docName: String) {
        var dbDeclarations = FirebaseFirestore.getInstance()
        dbDeclarations.collection(this.collection)
            .document(docName)
            .update(field, newValue)
    }

    fun deleteDeclaration(docName: String) {
        var dbDeclarations = FirebaseFirestore.getInstance()
        dbDeclarations.collection(this.collection).document(docName).delete()
    }


}