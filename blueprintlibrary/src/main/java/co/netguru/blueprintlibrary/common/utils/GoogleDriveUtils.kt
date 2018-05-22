package co.netguru.blueprintlibrary.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.support.v4.app.Fragment
import android.util.Log
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveFile
import com.google.android.gms.drive.DriveId
import com.google.android.gms.drive.OpenFileActivityOptions
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import java.util.*


object GoogleDriveUtils {

    lateinit var showGoogleDrivePickerDialogTask: TaskCompletionSource<DriveId>
    private val tag = GoogleDriveUtils::class.java.simpleName

    @SuppressLint("RestrictedApi")
    fun getLastSignedInAccount(context: Context): GoogleSignInAccount? =
            GoogleSignIn.getLastSignedInAccount(context)


    fun checkIfScopesGranted(scopes: Set<Scope>): Boolean {
        val requiredScopes = HashSet<Scope>(2)
        requiredScopes.add(Drive.SCOPE_FILE)
        requiredScopes.add(Drive.SCOPE_APPFOLDER)
        return scopes.containsAll(requiredScopes)
    }

    @SuppressLint("RestrictedApi")
    fun signInGoogleAccount(activity: Activity) {
        activity.startActivityForResult(
                GoogleSignIn.getClient(activity, getGoogleSignInOptions()).signInIntent,
                Constants.REQUEST_CODE_GOOGLE_DRIVE_SIGN_IN)
    }

    @SuppressLint("RestrictedApi")
    fun signInGoogleAccount(fragment: Fragment) {
        fragment.startActivityForResult(
                GoogleSignIn.getClient(fragment.activity!!, getGoogleSignInOptions()).signInIntent,
                Constants.REQUEST_CODE_GOOGLE_DRIVE_SIGN_IN)
    }

    @SuppressLint("RestrictedApi")
    fun getSignedInAccountFromIntent(data: Intent?): GoogleSignInAccount? {
        var googleSignInAccount: GoogleSignInAccount? = null
        val getAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (getAccountTask.isSuccessful) {
            googleSignInAccount = getAccountTask.result
        }
        return googleSignInAccount
    }

    fun showGoogleDrivePickerDialog(activity: Activity,
                                    account: GoogleSignInAccount?,
                                    listener: PickGoogleDriveFileListener) {
        if (isGoogleSignInAccountValid(account, listener)) {
            showGoogleDrivePickerDialogTask = TaskCompletionSource()
            setupDriveClient(activity, account!!)
            setShowGoogleDrivePickerDialogTaskListeners(activity, account, listener)
        }
    }

    fun showGoogleDrivePickerDialog(fragment: Fragment,
                                    account: GoogleSignInAccount?,
                                    listener: PickGoogleDriveFileListener) {
        if (isGoogleSignInAccountValid(account, listener)) {
            showGoogleDrivePickerDialogTask = TaskCompletionSource()
            setupDriveClient(fragment, account!!)
            setShowGoogleDrivePickerDialogTaskListeners(fragment.activity!!, account, listener)
        }
    }

    private fun isGoogleSignInAccountValid(account: GoogleSignInAccount?,
                                           listener: PickGoogleDriveFileListener): Boolean {
        val valid = account != null
        if (!valid) {
            Log.e(tag, "googleSignInAccount is null")
            listener.onFailure()
        }
        return valid
    }

    private fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build()
    }

    private fun setupDriveClient(activity: Activity, account: GoogleSignInAccount) {
        Drive.getDriveClient(activity, account)
                .newOpenFileActivityIntentSender(getOpenFileActivityOptions(activity))
                .continueWith({ task: Task<IntentSender> ->
                    startIntentSenderForDrivePickerResult(activity, task)
                })
    }

    private fun setupDriveClient(fragment: Fragment, account: GoogleSignInAccount) {
        Drive.getDriveClient(fragment.activity!!, account)
                .newOpenFileActivityIntentSender(getOpenFileActivityOptions(fragment.context!!))
                .continueWith({ task: Task<IntentSender> ->
                    startIntentSenderForDrivePickerResult(fragment, task)
                })
    }

    private fun getOpenFileActivityOptions(context: Context): OpenFileActivityOptions {
        return OpenFileActivityOptions.Builder()
                .setActivityTitle(context.getString(R.string.select_file))
                .build()
    }

    private fun startIntentSenderForDrivePickerResult(activity: Activity, task: Task<IntentSender>) {
        activity.startIntentSenderForResult(
                task.result, Constants.REQUEST_CODE_GOOGLE_DRIVE_PICK_FILE,
                null, 0, 0, 0, null)
    }

    private fun startIntentSenderForDrivePickerResult(fragment: Fragment, task: Task<IntentSender>) {
        fragment.startIntentSenderForResult(
                task.result, Constants.REQUEST_CODE_GOOGLE_DRIVE_PICK_FILE,
                null, 0, 0, 0, null)
    }

    private fun setShowGoogleDrivePickerDialogTaskListeners(context: Context, account: GoogleSignInAccount,
                                                            listener: PickGoogleDriveFileListener) {
        showGoogleDrivePickerDialogTask.task
                .addOnSuccessListener {
                    retrieveFileInfo(context, account, it.asDriveFile(), listener)
                }
                .addOnFailureListener({
                    Log.e(tag, "No file selected.", it)
                    listener.onFailure()
                })
    }

    private fun retrieveFileInfo(context: Context, googleSignInAccount: GoogleSignInAccount,
                                 driveFile: DriveFile, listener: PickGoogleDriveFileListener) {
        Drive.getDriveResourceClient(context, googleSignInAccount)
                .getMetadata(driveFile)
                .addOnSuccessListener({ metadata ->
                    listener.onSuccess(metadata.webContentLink, metadata.title, metadata.mimeType)
                })
                .addOnFailureListener({ e ->
                    Log.e(tag, "error: " + e.message)
                    listener.onFailure()
                })
    }

    interface PickGoogleDriveFileListener {

        fun onSuccess(fileUrl: String, fileName: String, fileType: String)

        fun onFailure()

    }

}