package com.aminano.githubsample.presentation.view


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminano.githubsample.R
import com.aminano.githubsample.data.remote.api.base.Status
import com.aminano.githubsample.data.remote.api.models.GitHubAPIRepoResponse
import com.aminano.githubsample.databinding.UsersListFragmentBinding
import com.aminano.githubsample.presentation.datasource.RepoListAdapter
import com.aminano.githubsample.presentation.viewmodel.RepoListViewModel
import kotlinx.android.synthetic.main.custom_dialog.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepoListFragment : Fragment(), RepoListAdapter.RepoListAdapterInteraction {

    private val repoListViewModel: RepoListViewModel by viewModel()
    private lateinit var itemViewer: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: UsersListFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.users_list_fragment, container, false)

        binding.usersListViewModel = repoListViewModel

        itemViewer = binding.root.findViewById(R.id.itemViewer)
        initAdapterAndObserve()

        return binding.root
    }

    private fun initAdapterAndObserve() {
        val repoListAdapter = RepoListAdapter(this)

        itemViewer.layoutManager = LinearLayoutManager(context)
        itemViewer.adapter = repoListAdapter

        Transformations.switchMap(repoListViewModel.dataSource) { dataSource -> dataSource.loadStateLiveData }
            .observe(this, Observer {
                when (it) {
                    Status.LOADING -> {
                        repoListViewModel.isWaiting.set(true)
                        repoListViewModel.errorMessage.set(null)
                    }
                    Status.SUCCESS -> {
                        repoListViewModel.isWaiting.set(false)
                        repoListViewModel.errorMessage.set(null)
                    }
                    Status.EMPTY -> {
                        repoListViewModel.isWaiting.set(false)
                        repoListViewModel.errorMessage.set(getString(R.string.msg_users_list_is_empty))
                    }
                    else -> {
                        repoListViewModel.isWaiting.set(false)
                        repoListViewModel.errorMessage.set(getString(R.string.msg_fetch_users_list_has_error))
                    }
                }
            })

        Transformations.switchMap(repoListViewModel.dataSource) { dataSource -> dataSource.totalCount }
            .observe(this, Observer { totalCount ->
                totalCount?.let { repoListViewModel.totalCount.set(it) }
            })

        repoListViewModel.usersLiveData.observe(this, Observer {
            repoListAdapter.submitList(it)
        })
    }

    override fun onUserItemClick(gitHubAPIRepoResponse: GitHubAPIRepoResponse) {
        showMessageBox(gitHubAPIRepoResponse)
    }

    fun showMessageBox(gitHubAPIRepoResponse: GitHubAPIRepoResponse) {
        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.custom_dialog, null)
        val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)
        messageBoxView.repo_url_button.setOnClickListener {
            openInBrowser(gitHubAPIRepoResponse.url)
        }
        messageBoxView.owner_url_button.setOnClickListener {
            openInBrowser(gitHubAPIRepoResponse.items.url)
        }
        messageBoxBuilder.show()
    }

    private fun openInBrowser(pageUrl: String?) {
        pageUrl?.let {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
    }

}

