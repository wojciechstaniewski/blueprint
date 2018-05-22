package co.netguru.blueprintlibrary.common.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class FilesUtils @Inject constructor(val context: Context) {

    private val tag = FilesUtils::class.java.simpleName

    fun getFileFromUri(uri: Uri): File? {
        val path = getFilePathFromUri(uri)
        return if (path != null) {
            try {
                File(path)
            } catch (e: Exception) {
                Log.e(tag, "getFileFromUri", e)
                null
            }
        } else {
            null
        }
    }

    fun getFilePathFromUri(uri: Uri) =
            when {
                DocumentsContract.isDocumentUri(context, uri) ->
                    getFilePathBasedOnUriAuthority(uri)
                uri.scheme == Constants.SCHEME_CONTENT ->
                    getPathFromDocumentContentUriScheme(uri)
                else -> uri.path
            }

    fun getFileName(path: String): String =
            if (path.contains(Constants.FILE_PATH_SEPARATOR)) {
                path.substring(path.lastIndexOf(Constants.FILE_PATH_SEPARATOR) + 1)
            } else {
                ""
            }

    fun getFileAttachmentIconResId(fileName: String, isMineMessage: Boolean): Int {
        val typedArray = if (isMineMessage) {
            context.resources.obtainTypedArray(R.array.mine_file_attachment_icons)
        } else {
            context.resources.obtainTypedArray(R.array.incoming_file_attachment_icons)
        }
        return when {
            fileName.endsWith(Constants.FILE_EXTENSION_DOC) -> {
                getResourceId(typedArray, Constants.FILE_DOCUMENT_ICON_RES_ID_INDEX)
            }
            fileName.endsWith(Constants.FILE_EXTENSION_PDF) -> {
                getResourceId(typedArray, Constants.FILE_PDF_ICON_RES_ID_INDEX)
            }
            fileName.endsWith(Constants.FILE_EXTENSION_JPG) ||
                    fileName.endsWith(Constants.FILE_EXTENSION_PNG) -> {
                getResourceId(typedArray, Constants.FILE_IMAGE_ICON_RES_ID_INDEX)
            }
            else -> Constants.FILE_DOCUMENT_ICON_RES_ID_INDEX
        }
    }

    private fun getResourceId(typedArray: TypedArray, arrayIndex: Int) =
            typedArray.getResourceId(arrayIndex, Constants.FILE_DOCUMENT_ICON_RES_ID_INDEX)

    fun getFileFromIntent(intent: Intent) =
            if (intent.hasExtra(Constants.PHOTO_BITMAP_KEY)) {
                createTemporaryFileFromBitmap(intent.getParcelableExtra(Constants.PHOTO_BITMAP_KEY))
            } else {
                getFileFromBundle(intent.extras)
            }

    fun isImageTypeAttachment(fileName: String?) =
            fileName != null
                    && (fileName.endsWith(Constants.FILE_EXTENSION_JPG)
                    || fileName.endsWith(Constants.FILE_EXTENSION_PNG))

    fun isAttachment(fileName: String?) = fileName != null
            && (fileName.endsWith(Constants.FILE_EXTENSION_PDF)
            || fileName.endsWith(Constants.FILE_EXTENSION_DOC)
            || fileName.endsWith(Constants.FILE_EXTENSION_JPG)
            || fileName.endsWith(Constants.FILE_EXTENSION_PNG))

    fun isImageTypeFile(fileType: String) =
            arrayListOf(Constants.IMAGE_TYPE_JPG, Constants.IMAGE_TYPE_JPEG,
                    Constants.IMAGE_TYPE_PNG).contains(fileType)

    private fun createTemporaryFileFromBitmap(fileBitmap: Bitmap): File? {
        var outputStream: FileOutputStream? = null
        var file: File?
        try {
            file = File.createTempFile(
                    Constants.TEMPORARY_FILE_PREFIX, Constants.TEMPORARY_FILE_SUFFIX)
            file?.deleteOnExit()
            outputStream = FileOutputStream(file)
            fileBitmap.compress(
                    Bitmap.CompressFormat.JPEG, Constants.IMAGE_COMPRESSION_QUALITY, outputStream)
        } catch (e: Exception) {
            Log.e(tag, "createTemporaryFileFromBitmap()", e)
            file = null
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                Log.e(tag, "createTemporaryFileFromBitmap()", e)
            }
        }
        return file
    }

    fun openFile(fileUri: Uri, mimeType: String, activity: Activity) {
        try {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(fileUri, mimeType)
            activity.startActivity(intent)
        } catch (e: Exception) {
            Log.e(tag, "openFile", e)
        }
    }

    private fun getFileFromBundle(bundle: Bundle?): File? {
        return when {
            bundle == null -> null
            bundle.getString(Constants.FILE_PATH) != null -> File(bundle.getString(Constants.FILE_PATH))
            else -> File(Uri.parse(bundle.getString(Constants.FILE_URI)).path)
        }
    }

    private fun getFilePathBasedOnUriAuthority(uri: Uri) =
            when (uri.authority) {
                Constants.URI_AUTHORITY_EXTERNAL_STORAGE -> getPathFromExternalStorageDir(uri)
                Constants.URI_AUTHORITY_DOWNLOADS -> getPathFromDownloadsDir(uri)
                Constants.URI_AUTHORITY_MEDIA -> getPathFromMediaDir(uri)
                Constants.URI_AUTHORITY_GOOGLE_STORAGE -> uri.path
                else -> null
            }

    private fun getPathFromDocumentContentUriScheme(uri: Uri) =
            if (uri.authority == Constants.URI_AUTHORITY_PHOTOS) uri.lastPathSegment
            else getValueFromDataColumn(uri, null, null)

    private fun getPathFromExternalStorageDir(uri: Uri): String? {
        val docInfo = getDocInfo(uri)
        return if (docInfo?.size == Constants.DOC_INFO_SIZE) {
            String.format("%s/%s",
                    Environment.getExternalStorageDirectory().toString(),
                    docInfo[Constants.DOC_INFO_FILE_NAME_INDEX])
        } else {
            null
        }
    }

    private fun getPathFromDownloadsDir(uri: Uri) =
            getValueFromDataColumn(ContentUris.withAppendedId(
                    Uri.parse(Constants.DOWNLOADS_DIR),
                    DocumentsContract.getDocumentId(uri).toLong()),
                    null, null)

    private fun getPathFromMediaDir(uri: Uri): String? {
        val docInfo = getDocInfo(uri)
        return if (docInfo?.size == Constants.DOC_INFO_SIZE) {
            val contentUri =
                    when (docInfo[Constants.DOC_INFO_FIRST_ROW_ID]) {
                        Constants.DOC_INFO_IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        Constants.DOC_INFO_VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        Constants.DOC_INFO_AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
            getValueFromDataColumn(contentUri,
                    String.format("%s=?", Constants.DOC_INFO_COLUMN_ID),
                    arrayOf(docInfo[Constants.DOC_INFO_SECOND_ROW_ID]))
        } else {
            null
        }
    }

    private fun getValueFromDataColumn(uri: Uri?, selection: String?,
                                       selectionArgs: Array<String>?): String? {
        var value: String?
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, arrayOf(Constants.DOC_INFO_COLUMN_DATA),
                    selection, selectionArgs, null)
            value =
                    if (cursor.moveToFirst())
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.DOC_INFO_COLUMN_DATA))
                    else
                        null
        } catch (e: Exception) {
            Log.e(tag, e.message)
            value = null
        } finally {
            cursor?.close()
        }
        return value
    }

    private fun getDocInfo(uri: Uri): Array<String>? =
            DocumentsContract.getDocumentId(uri).split(Constants.DOC_INFO_SPLIT_SEPARATOR.toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()

}