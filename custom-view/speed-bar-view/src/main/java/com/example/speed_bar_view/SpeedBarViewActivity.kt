package com.example.speed_bar_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants.SPEED_BAR_VIEW

/**
 * @author 89hnim
 * @since 19/03/2022
 */
@AppDeepLink(SPEED_BAR_VIEW)
internal class SpeedBarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_bar_view)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.fragment_container_view, SpeedBarViewFragment())
        }
    }

}