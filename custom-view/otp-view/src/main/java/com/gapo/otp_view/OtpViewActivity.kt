package com.gapo.otp_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants.OTP_VIEW

/**
 * @author 89hnim
 * @since 05/11/2021
 */
@AppDeepLink(OTP_VIEW)
internal class OtpViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_view)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.fragment_container_view, OtpViewFragment())
        }
    }

}