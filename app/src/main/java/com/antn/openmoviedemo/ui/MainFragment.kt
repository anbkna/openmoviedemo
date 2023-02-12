package com.antn.openmoviedemo.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antn.openmoviedemo.R
import com.antn.openmoviedemo.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.fragment_main) {
    private val viewModel by viewModels<MainViewModel>()
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        val movieAdapter = MovieAdapter()
        val footerAdapter = MovieLoadStateAdapter {
            movieAdapter.retry()
        }

        movieAdapter.addLoadStateListener {
            val error = when {
                it.append is LoadState.Error -> it.append as LoadState.Error
                it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                else -> null
            }
            if (error != null && movieAdapter.itemCount == 0) {
                binding.tvEmpty.apply {
                    isVisible = true
                    text = error.error.message
                }
            } else {
                if (it.append.endOfPaginationReached) {
                    binding.tvEmpty.isVisible = movieAdapter.itemCount == 0
                } else {
                    binding.tvEmpty.isVisible = false
                }
            }
        }

        binding.rcvMovie.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    if (parent.getChildAdapterPosition(view) % 2 == 0) {
                        outRect.set(16, 8, 8, 8)
                    } else {
                        outRect.set(8, 8, 16, 8)
                    }
                }
            })
            adapter = movieAdapter.withLoadStateFooter(footerAdapter)
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            movieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        val menuSearch = menu.findItem(R.id.menu_search)
        val searchView = menuSearch.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.rcvMovie.scrollToPosition(0)
                    viewModel.searchMovies(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}