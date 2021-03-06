package nl.palafix.phase.web


import android.webkit.JavascriptInterface
import nl.palafix.phase.activities.MainActivity
import nl.palafix.phase.contracts.VideoViewHolder
import nl.palafix.phase.facebook.FbCookie
import nl.palafix.phase.utils.*
import nl.palafix.phase.views.PhaseWebView
import io.reactivex.subjects.Subject


/**
 * Created by Allan Wang on 2017-06-01.
 */
class PhaseJSI(val web: PhaseWebView) {

    private val context = web.context
    private val activity = context as? MainActivity
    private val header: Subject<String>? = activity?.headerBadgeObservable
    private val refresh: Subject<Boolean> = web.parent.refreshObservable
    private val cookies = activity?.cookies() ?: arrayListOf()

    /**
     * Attempts to load the url in an overlay
     * Returns {@code true} if successful, meaning the event is consumed,
     * or {@code false} otherwise, meaning the event should be propagated
     */
    @JavascriptInterface
    fun loadUrl(url: String?): Boolean = if (url == null) false else web.requestWebOverlay(url)

    @JavascriptInterface
    fun loadVideo(url: String?, isGif: Boolean): Boolean =
            if (url != null && Prefs.enablePip) {
                web.post {
                    (context as? VideoViewHolder)?.showVideo(url, isGif)
                            ?: L.e { "Could not load video; contract not implemented" }
                }
                true
            } else {
                false
            }

    @JavascriptInterface
    fun reloadBaseUrl(animate: Boolean) {
        L.d { "FrostJSI reload" }
        web.post {
            web.stopLoading()
            web.reloadBase(animate)
        }
    }

    @JavascriptInterface
    fun contextMenu(url: String, text: String?) {
        if (!text.isIndependent) return
        //url will be formatted through webcontext
        web.post { context.showWebContextMenu(WebContext(url, text)) }
    }

    /**
     * Get notified when a stationary long click starts or ends
     * This will be used to toggle the main activities viewpager swipe
     */
    @JavascriptInterface
    fun longClick(start: Boolean) {
        activity?.viewPager?.enableSwipe = !start
    }

    /**
     * Allow or disallow the pull down to refresh action
     */
    @JavascriptInterface
    fun disableSwipeRefresh(disable: Boolean) {
        web.post { web.parent.swipeEnabled = !disable }
    }

    @JavascriptInterface
    fun loadLogin() {
        FbCookie.logout(context)
    }

    /**
     * Launch image overlay
     */
    @JavascriptInterface
    fun loadImage(imageUrl: String, text: String?) {
        context.launchImageActivity(imageUrl, text)
    }

    @JavascriptInterface
    fun emit(flag: Int) {
        web.post { web.phaseWebClient.emit(flag) }
    }

    @JavascriptInterface
    fun isReady() {
        refresh.onNext(false)
        }

    @JavascriptInterface
    fun handleHtml(html: String?) {
        html ?: return
        web.post { web.phaseWebClient.handleHtml(html) }
    }

    @JavascriptInterface
    fun handleHeader(html: String?) {
        html ?: return
        header?.onNext(html)
    }

}