package jp.co.kotakestudio.godot_android_firebase_message

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo

class GodotAndroidFirebaseMessage(godot: Godot?) : GodotPlugin(godot) {

    companion object {
        var TAG = "GodotAndroidFirebaseMessage"
        var PLUGIN_NAME = "GodotAndroidFirebaseMessage"
        val SIGNAL_RECEIVE_TOKEN = SignalInfo("_did_receive_token", String::class.java)
        val SIGNAL_RECEIVE_MESSAGE = SignalInfo("_did_receive_message", Dictionary::class.java)
        var instance: GodotAndroidFirebaseMessage? = null
    }

    init {
        instance = this
    }

    override fun getPluginName(): String {
        return PLUGIN_NAME
    }

    override fun getPluginMethods(): MutableList<String> {
        return mutableListOf(
            "getToken",
        )
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf(
            SIGNAL_RECEIVE_TOKEN,
        )
    }

    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            emitSignal(SIGNAL_RECEIVE_TOKEN.name, token)
        })
    }

    fun callSignalReceiveToken(token: String) {
        emitSignal(SIGNAL_RECEIVE_TOKEN.name, token)
    }

    fun callSignalReceiveMessage(message: RemoteMessage) {
        var _message = Dictionary()
        _message.put("from", message.getFrom())
        _message.put("to", message.getTo())
        _message.put("senderId", message.getSenderId())
        _message.put("messageId", message.getMessageId())
        _message.put("messageType", message.getMessageType())
        message.notification.let {
            val notif = Dictionary()
            it?.body.let {
                notif["body"] = it
            }
            it?.title.let {
                notif["title"] = it
            }
            _message.put("notification", notif)
        }

        if (message.data.size > 0) {
            val data = Dictionary()
            for (key in message.data.keys) {
                data[key] = message.data.get(key)
            }
            _message.put("data", data)
        }
        emitSignal(SIGNAL_RECEIVE_MESSAGE.name, _message)
    }

}