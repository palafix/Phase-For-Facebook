package nl.palafix.phase.facebook.requests

import nl.palafix.phase.facebook.FB_URL_BASE

/**
 * Created by Allan Wang on 29/12/17.
 **/
fun RequestAuth.markNotificationRead(notifId: Long): PhaseRequest<Boolean> {

    val body = listOf(
            "click_type" to "notification_click",
            "id" to notifId,
            "target_id" to "null",
            "fb_dtsg" to fb_dtsg,
            "__user" to userId
    ).withEmptyData("m_sess", "__dyn", "__req", "__ajax__")

    return phaseRequest(::executeForNoError) {
        url("${FB_URL_BASE}a/jewel_notifications_log.php")
        post(body.toForm())
    }
}

fun RequestAuth.markNotificationsRead(vararg notifId: Long) =
        notifId.toTypedArray().zip<Long, Boolean, Boolean>(
                { it.all { it } },
                { markNotificationRead(it).invoke() })