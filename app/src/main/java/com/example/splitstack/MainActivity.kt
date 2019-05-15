package com.example.splitstack


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.blockstack.android.sdk.*


private const val username = "dev_android_sdk.id.blockstack"
private var usersId = ""
private var decentralizedIdForUser = ""
@SuppressLint("SetTextI18n")

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName


    private var _blockstackSession: BlockstackSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //    setSupportActionBar(toolbar)
        val config = "https://flamboyant-darwin-d11c17.netlify.com/"
            .toBlockstackConfig(kotlin.arrayOf(org.blockstack.android.sdk.Scope.StoreWrite))

        _blockstackSession = BlockstackSession(this@MainActivity, config)
        signInButton.isEnabled = true
        returnToDashBoard.isEnabled = false
        signInButton.setOnClickListener { _: View ->
            blockstackSession().redirectUserToSignIn { errorResult ->
                if (errorResult.hasErrors) {
                    Toast.makeText(this, "error: " + errorResult.error, Toast.LENGTH_SHORT).show()
                }
            }
        }


        if (intent?.action == Intent.ACTION_VIEW) {
            handleAuthResponse(intent)

//            val intent = Intent(this,MainActivity::class.java)
//            println("This is not working")
//            intent.putExtra("userId", usersId)
//            intent.putExtra("decentralizedIdForUser", decentralizedIdForUser)
//            startActivity(intent)
        }

    }

    private fun onSignIn(userData: UserData) {
        userDataTextView.text = "Welcome ${userData.json.getString("username")}"
       usersId = userData.json.getString("username")
        decentralizedIdForUser = userData.decentralizedID
        println("This is not being run")
        //  showUserAvatar(userData.profile?.avatarImage)
        signInButton.isEnabled = false
        signInButton.visibility = INVISIBLE
        returnToDashBoard.isEnabled = true
        returnToDashBoard.visibility = VISIBLE
        userDataTextView.text = "Signed in"
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent")

        if (intent?.action == Intent.ACTION_MAIN) {
            val userData = blockstackSession().loadUserData()
            if (userData != null) {
                runOnUiThread {
                    System.out.print("this Working" + usersId)
                    onSignIn(userData)
                }
            } else {
                Toast.makeText(this, "no user data", Toast.LENGTH_SHORT).show()
            }
        } else if (intent?.action == Intent.ACTION_VIEW) {
            handleAuthResponse(intent)
        }
    }

    private fun handleAuthResponse(intent: Intent) {
        val response = intent.data.query
        Log.d(TAG, "response ${response}")
        if (response != null) {
            val authResponseTokens = response.split('=')

            if (authResponseTokens.size > 1) {
                val authResponse = authResponseTokens[1]
                Log.d(TAG, "authResponse: ${authResponse}")
                blockstackSession().handlePendingSignIn(authResponse) { userDataResult: Result<UserData> ->
                    if (userDataResult.hasValue) {
                        val userData = userDataResult.value!!
                        Log.d(TAG, "signed in!")
                        System.out.print("This is working")
                        onSignIn(userData)
                        runOnUiThread {
                        }
                    } else {
                        Toast.makeText(this, "error: " + userDataResult.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun blockstackSession(): BlockstackSession {
        val session = _blockstackSession
        if (session != null) {
            return session
        } else {
            throw IllegalStateException("No session.")
        }
    }
fun returnToDashboard(view: View){
    val intent = Intent(this,DashboardActivity::class.java)
    intent.putExtra("userId", usersId)
    intent.putExtra("decentralizedIdForUser", decentralizedIdForUser)
    startActivity(intent)
}
}
