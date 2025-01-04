package cu.lt.joe.mc.lyrics.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cu.lt.joe.mc.lyrics.databinding.AdditionalInformationDialogLayoutBinding

open class BaseActivity : AppCompatActivity() {
    @JvmField
    var actionBarTintColor = 0

    @JvmField
    var foregroundColor = 0

    @JvmField
    var cardForegroundColor = 0

    @JvmField
    var cardBackgroundTintColor = 0

    @JvmField
    var navigateToHome = false
    open fun onMenuExpanderClick(menuExpanderView: View) {}

    fun openExternalLink(link: String?) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)))
        } catch (ignored: Exception) {
        }
    }

    fun showGeneralInfoDialog() {
        val dialogLayoutBinding = AdditionalInformationDialogLayoutBinding.inflate(layoutInflater)
        dialogLayoutBinding.activity = this
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogLayoutBinding.root)
            .create()
        dialogLayoutBinding.aidlOk.setOnClickListener { _ -> dialog.dismiss() }
        dialog.show()
    }

    companion object {
        const val ALPHA_VALUE = 150

        @JvmStatic
        @BindingAdapter("android:backgroundColor")
        fun setBackgroundColor(view: View, color: Int) {
            view.backgroundTintList = ColorStateList.valueOf(color)
        }

        @JvmStatic
        @BindingAdapter("android:foregroundColor")
        fun setForegroundColor(textView: TextView, color: Int) {
            textView.setTextColor(color)
        }

        @JvmStatic
        @BindingAdapter("android:foregroundColor")
        fun setForegroundColor(imageView: ImageView, color: Int) {
            imageView.imageTintList = ColorStateList.valueOf(color)
        }

        @JvmStatic
        @BindingAdapter("android:cardBackgroundColor")
        fun setCardBackgroundTintColor(view: View, backgroundTintColor: Int) {
            view.backgroundTintList = ColorStateList.valueOf(backgroundTintColor)
        }
    }
}