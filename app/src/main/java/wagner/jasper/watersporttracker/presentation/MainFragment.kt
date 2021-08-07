package wagner.jasper.watersporttracker.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import wagner.jasper.watersporttracker.databinding.MainFragmentBinding

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
//        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.screenOrientation.observe(viewLifecycleOwner, {
            activity?.requestedOrientation = it.identifier
        })
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}