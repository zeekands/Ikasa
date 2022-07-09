package com.zeekands.ikasa.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zeekands.ikasa.ui.login.MainActivity
import com.zeekands.ikasa.R
import com.zeekands.ikasa.db.LoginHelper
import com.zeekands.ikasa.ui.admin.FormIkanActivity
import com.zeekands.ikasa.ui.home.main.HomeActivity
import com.zeekands.ikasa.ui.home.pesanan.PesananProsesFragment
import com.zeekands.ikasa.ui.home.pesanan.PesananSelesaiFragment
import com.zeekands.ikasa.ui.home.pesanan.PesananSemuaFragment

class SplashScreen : AppCompatActivity() {
    private lateinit var loginHelper: LoginHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash_screen)
        loginHelper = LoginHelper.getInstance(this)
        loginHelper.open()
        cekLogin()
    }
    private fun cekLogin(){
        val cursor = loginHelper.queryAll()
        MappingHelper.mapLoginCursorToArrayList(cursor).also {
            if (it.size > 0) {
                if (it[0].role == 0) {
                    intent = Intent(this, HomeActivity::class.java)
                    intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    PesananSelesaiFragment.ID_USER = it[0].id_user.toString()
                    PesananSemuaFragment.ID_USER = it[0].id_user.toString()
                    PesananProsesFragment.ID_USER = it[0].id_user.toString()
                    startActivity(intent)
                }else{
                    intent = Intent(this, FormIkanActivity::class.java)
                    intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(intent)
                }
            }
            else{
                intent = Intent(this, MainActivity::class.java)
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
                startActivity(intent)
            }
        }
    }
}