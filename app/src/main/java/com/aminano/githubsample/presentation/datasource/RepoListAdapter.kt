package com.aminano.githubsample.presentation.datasource

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.aminano.githubsample.R
import com.aminano.githubsample.data.remote.api.models.GitHubAPIRepoResponse

class RepoListAdapter(private val  listener: RepoListAdapterInteraction): PagedListAdapter<GitHubAPIRepoResponse, RepoListAdapter.RepoListViewHolder>(usersDiffCallback) {

    lateinit var context: Context

    interface RepoListAdapterInteraction {
        fun onUserItemClick(gitHubAPIRepoResponse: GitHubAPIRepoResponse)
    }

    inner class RepoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userItem: LinearLayout = itemView.findViewById(R.id.userItem)
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val txtName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        val authName: AppCompatTextView = itemView.findViewById(R.id.authorName)
        val txtHomeUrl: AppCompatTextView = itemView.findViewById(R.id.txtHomeUrl)
    }

    override fun onBindViewHolder(holder: RepoListViewHolder, position: Int) {
        val gitHubAPIRepoResponse = getItem(position)
        gitHubAPIRepoResponse?.let {
            Glide.with(context)
                .load(it.items.avatar_url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgAvatar)

            holder.txtName.text = it.repoName
            holder.authName.text = "@"+it.items.login
            if (it.repoDescription!=null){
                holder.txtHomeUrl.text = it.repoDescription
            }else{
                holder.txtHomeUrl.text = "No description"
            }

            holder.userItem.setOnClickListener {
                listener.onUserItemClick(gitHubAPIRepoResponse)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.list_github_user, parent, false)
        return RepoListViewHolder(view)
    }

    companion object {
        val usersDiffCallback = object : DiffUtil.ItemCallback<GitHubAPIRepoResponse>() {
            override fun areItemsTheSame(oldItem: GitHubAPIRepoResponse, newItem: GitHubAPIRepoResponse): Boolean {

                return oldItem.url == newItem.url
            }
            override fun areContentsTheSame(oldItem: GitHubAPIRepoResponse, newItem: GitHubAPIRepoResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}