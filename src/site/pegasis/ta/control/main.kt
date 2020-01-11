package site.pegasis.ta.control

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request.Builder
import okhttp3.RequestBody
import org.json.simple.JSONArray
import java.io.IOException
import java.util.concurrent.TimeUnit




fun main(a: Array<String>) {
    val args = JSONArray()
    args.addAll(a)
    var target = "http://localhost:5006/"
    if (args.size >= 2 && (args.first() == "-t" || args.first() == "--target")) {
        target = args[1].toString()
        args.removeAt(0)
        args.removeAt(0)
    }
    if (args.size == 0) {
        args.add("-h")
    }

    val request = Builder()
        .url(target)
        .post(args)
        .build()

    val res = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.MINUTES)
        .build()
        .newCall(request)
        .execute()

    if (!res.isSuccessful) throw IOException("Unexpected code $res")
    println(res.body().string())
}

fun Builder.post(jsonArray: JSONArray): Builder {
    return this.post(RequestBody.create(MediaType.parse("application/json"), jsonArray.toJSONString()))
}
