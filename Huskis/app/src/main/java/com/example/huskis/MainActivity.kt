package com.example.huskis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huskis.data.Todo
import com.example.huskis.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private var USER_ID:String = "Anonymous"
private var DISPLAY_NAME:String = "Anon"
private var EMAIL:String = "anonymous@email.com"
private val TAG: String = "Huskis:MainActivity"
class ListHolder {

    companion object {
        var PickedTodo: Todo? = null
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardListing.layoutManager = LinearLayoutManager(this)
        binding.cardListing.adapter = ListRecyclerAdapter(emptyList<Todo>(), this::onListClicked)

        //Signing in with google user, using firebase with realtime database
        signInGoogle()
        ListDepositoryManager.instance.onList = {
            (binding.cardListing.adapter as ListRecyclerAdapter).updateCollection(it)
        }


        //Header list spacing
        binding.cardListing.addItemDecoration(HeaderDecoration(50, 50))

        //Floating action button
        binding.fabAdd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle(getString(R.string.input))
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            builder.setView(dialogLayout)
            builder.setNeutralButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, i -> addList(
                Todo(
                    inputText.text.toString(),
                    mutableListOf()
                )
            ) }
            builder.show()
        }
    }

    private fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 200)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 200) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        lateinit var auth: FirebaseAuth
        auth = Firebase.auth
        auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null)).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                Log.d(TAG, "signInWithCredential:success")
                USER_ID = auth.currentUser?.uid.toString()
                EMAIL = auth.currentUser?.email.toString()
                DISPLAY_NAME = auth.currentUser?.displayName.toString()
                binding.loginStatus.text = "Logged in as '$DISPLAY_NAME' \n($EMAIL)"
                ListDepositoryManager.instance.load(USER_ID, EMAIL, "https://huskis-79721-default-rtdb.europe-west1.firebasedatabase.app/", this)

            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)

            }
        }
    }

    private fun addList(item: Todo) {
        ListDepositoryManager.instance.addTodo(item)

    }

    override fun onResume() {
        super.onResume()
        binding.cardListing.adapter?.notifyDataSetChanged()
    }

    private fun onListClicked(todo: Todo): Unit {
        ListHolder.PickedTodo = todo
        Log.e(TAG, "Pushed list : >${todo.id}")
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

}