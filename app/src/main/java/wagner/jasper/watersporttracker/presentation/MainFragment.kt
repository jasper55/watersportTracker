package wagner.jasper.watersporttracker.presentation

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import wagner.jasper.watersporttracker.R
import wagner.jasper.watersporttracker.databinding.MainFragmentBinding

@InternalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var dataBinding: MainFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = MainFragmentBinding.inflate(inflater,container,false)
        dataBinding?.lifecycleOwner = viewLifecycleOwner
        dataBinding?.viewmodel = viewModel
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.screenOrientation.observe(viewLifecycleOwner, {
            activity?.requestedOrientation = it.identifier
        })
        viewModel.initializing.observe(viewLifecycleOwner, {
            if (it) {
                animateSignalIcon(dataBinding?.loadingIndicatior?.drawable as? AnimatedVectorDrawable?)
                animateDots(dataBinding?.loadingMessage)
            }
        })
    }

    private fun animateSignalIcon(iconAnimation: AnimatedVectorDrawable?) {
        iconAnimation ?: return
        iconAnimation.start()
        iconAnimation.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                (drawable as AnimatedVectorDrawable).start()
            }
        })
    }

    private fun animateDots(loadingMessage: TextView?) {
        val handler = Looper.myLooper()?.let { Handler(it) }
        val runnable: Runnable = object : Runnable {
            var count = 0
            override fun run() {
                count++
                when (count) {
                    1 -> loadingMessage?.text = resources.getText(R.string.initializing_gps_1)
                    2 -> loadingMessage?.text = resources.getText(R.string.initializing_gps_2)
                    3 -> loadingMessage?.text = resources.getText(R.string.initializing_gps_3)
                }
                if (count == 3) count = 0
                handler?.postDelayed(this,  333)
            }
        }
        handler?.postDelayed(runnable, 0)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startCountDown()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}