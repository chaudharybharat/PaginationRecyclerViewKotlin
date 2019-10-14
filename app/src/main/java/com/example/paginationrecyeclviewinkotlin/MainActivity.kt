package com.example.paginationrecyeclviewinkotlin

import android.os.Bundle
import android.os.Handler
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import com.example.paginationrecyeclviewinkotlin.PaginationListener.Companion.PAGE_START



class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    var mRecyclerView: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    private var adapter: PostRecyclerAdapter? = null
    private var currentPage = PAGE_START
    private var isLastPage = false
    private val totalPage = 10
    internal var isLoading = false
    internal var itemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView=findViewById(R.id.recyclerView) as RecyclerView
        swipeRefresh=findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh!!.setOnRefreshListener(this)
        mRecyclerView!!.setHasFixedSize(true)
        // use a linear layout manager
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        adapter = PostRecyclerAdapter(ArrayList())
        mRecyclerView!!.adapter = adapter
        doApiCall()

        /**
         * add scroll listener while user reach in bottom load more will call
         */

        var paginationListener :PaginationListener;
        paginationListener =object :PaginationListener(layoutManager){
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                 Log.e("test","Loadmore")
                isLoading = true
                currentPage++
                doApiCall()
            }



        }
        mRecyclerView!!.addOnScrollListener(paginationListener)
    }

    /**
     * do api call here to fetch data from server
     * In example i'm adding data manually
     */
    private fun doApiCall() {
        val items = ArrayList<PostItem>()
        Handler().postDelayed({
            for (i in 0..9) {
                itemCount++
                val postItem = PostItem()
                postItem.title = getString(R.string.text_title) + itemCount
                postItem.description = getString(R.string.text_description)
                items.add(postItem)
            }
            // do this all stuff on Success of APIs response
            /**
             * manage progress view
             */
            /**
             * manage progress view
             */
            if (currentPage != PAGE_START) adapter!!.removeLoading()
            adapter!!.addItems(items)
            swipeRefresh!!.isRefreshing = false

            // check weather is last page or not
            if (currentPage < totalPage) {
                adapter!!.addLoading()
            } else {
                isLastPage = true
            }
            isLoading = false
        }, 1500)
    }

    override fun onRefresh() {
        itemCount = 0
        currentPage = PAGE_START
        isLastPage = false
        adapter!!.clear()
        doApiCall()
    }


}
