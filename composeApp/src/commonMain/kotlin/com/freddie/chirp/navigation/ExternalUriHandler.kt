package com.freddie.chirp.navigation

object ExternalUriHandler {
    // 콜드 스타트에서는 iOS가 Compose 리스너 등록보다 먼저 URL을 전달할 수 있다. 등록 전에는 마지막 URI 하나를 보관한다.
    private var cached: String? = null

    var listener: ((uri: String) -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                cached?.let {
                    value.invoke(it)
                }
                cached = null
            }
        }

    fun onNewUri(uri: String) {
        cached = uri
        listener?.let {
            it.invoke(uri)
            cached = null
        }
    }
}