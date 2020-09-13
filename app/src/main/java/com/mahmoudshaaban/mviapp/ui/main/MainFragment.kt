package com.mahmoudshaaban.mviapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mahmoudshaaban.mviapp.R
import com.mahmoudshaaban.mviapp.model.User
import com.mahmoudshaaban.mviapp.ui.DataStateListener
import com.mahmoudshaaban.mviapp.ui.main.state.MainStateEvent
import com.mahmoudshaaban.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_blog_list_item.*
import java.lang.ClassCastException


class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    lateinit var dataStateHandler: DataStateListener

    lateinit var mainReyclerAdapter : MainRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.let {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        subscribeObservers()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            mainReyclerAdapter = MainRecyclerAdapter()
            adapter = mainReyclerAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            println("DEBUG: DataState: ${dataState}")
            // that's mean okay we have datastate coming in and
            // 2- handle loading and message
            dataStateHandler.onDataStateChanged(dataState)
            // 3 -  we know that data handle it fragment it self
            dataState.data?.let { it ->

                it.getContentIfNotHandled()?.let {
                    it.blogPost?.let {
                        // set BlogPosts data
                        viewModel.setBLogListData(it)
                    }

                    it.user?.let {
                        // set User data
                        viewModel.setUser(it)
                    }
                }

            }



        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            // Handle the data
            viewState.blogPost?.let {
                // set BlogPosts to RecyclerView
                mainReyclerAdapter.submitList(it)
                println("DEBUG: Setting blog posts to RecyclerView: ${viewState.blogPost}")

            }

            viewState.user?.let {
                // set User data to widgets
                println("DEBUG: Setting User data: ${viewState.user}")
                setUserProperites(it)
            }


        })
    }

    private fun setUserProperites(user : User){
        email.setText(user.email)
        username.setText(user.username)
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
    }

    fun triggerGetUserEvent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }

    fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostEvent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_blogs -> triggerGetBlogsEvent()

            R.id.action_get_user -> triggerGetUserEvent()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            // that's how we init Datastate listener
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            // if we forgot to implement the interface in MainActivity
            println("DEBUG: $context must implement DataStateListener")
        }
    }
}

