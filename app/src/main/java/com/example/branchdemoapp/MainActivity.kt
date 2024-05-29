package com.example.branchdemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.branchdemoapp.ui.theme.BranchDemoAppTheme
import io.branch.referral.Branch
import io.branch.referral.validators.IntegrationValidator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BranchDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        IntegrationValidator.validate(this)
        Branch.sessionBuilder(this).withCallback { branchUniversalObject, linkProperties, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", "Branch init failed. Caused by -" + error.message)
            } else {
                Log.i("BranchSDK_Tester", "Branch init complete!")
                branchUniversalObject?.let {
                    Log.i("BranchSDK_Tester", "Title: ${it.title}")
                    Log.i("BranchSDK_Tester", "Canonical Identifier: ${it.canonicalIdentifier}")
                    Log.i("BranchSDK_Tester", "Metadata: ${it.contentMetadata.convertToJson()}")
                }
                linkProperties?.let {
                    Log.i("BranchSDK_Tester", "Channel: ${it.channel}")
                    Log.i("BranchSDK_Tester", "Control Params: ${it.controlParams}")
                }
            }
        }.withData(this.intent.data).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        intent?.let {
            if (it.hasExtra("branch_force_new_session") && it.getBooleanExtra("branch_force_new_session", false)) {
                Branch.sessionBuilder(this).withCallback { referringParams, error ->
                    if (error != null) {
                        Log.e("BranchSDK_Tester", error.message)
                    } else {
                        referringParams?.let { params ->
                            Log.i("BranchSDK_Tester", params.toString())
                        }
                    }
                }.reInit()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BranchDemoAppTheme {
        Greeting("Android")
    }
}
