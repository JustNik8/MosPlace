package com.justnik.mosplace.presentation.account

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentAccountContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountContainerFragment : Fragment(R.layout.fragment_account_container),
    NavController.OnDestinationChangedListener {

    private val binding: FragmentAccountContainerBinding by viewBinding()
    private val viewModel: AccountViewModel by viewModels()

    private val navHostFragment by lazy {
        childFragmentManager.findFragmentById(R.id.account_container) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }

    private val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.account)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStartDestination()
        setHasOptionsMenu(true)
        navController.addOnDestinationChangedListener(this)
    }

    private fun setStartDestination() {
        if (viewModel.isAuthorized()) {
            navGraph.setStartDestination(R.id.accountFragment)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
        }
        navController.graph = navGraph
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val isDestinationLogin = destination.id == R.id.loginFragment
        val isDestinationAccount = destination.id == R.id.accountFragment
        val notDisplayUpButton = isDestinationLogin || isDestinationAccount
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(!notDisplayUpButton)
    }

}