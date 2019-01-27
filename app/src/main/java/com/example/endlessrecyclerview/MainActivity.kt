package com.example.endlessrecyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val numberList: MutableList<String> = ArrayList()

    var page = 0
    var isLoading = false
    val limit = 10

    lateinit var adapter: NumberAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        getPage()


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

//                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if (!isLoading) {

                        if ((visibleItemCount + pastVisibleItem) >= total) {
                            page++
                            getPage()
                        }

                    }
//                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    fun getPage() {
        isLoading = true
        progressBar.visibility = View.VISIBLE
        val start = ((page) * limit) + 1
        val end = (page + 1) * limit

        for (i in start..end) {
            numberList.add("Item " + i.toString())
        }
        Handler().postDelayed({
            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged()
            } else {
                adapter = NumberAdapter(this)
                recyclerView.adapter = adapter
            }
            isLoading = false
            progressBar.visibility = View.GONE
        }, 5000)

    }


    class NumberAdapter(val activity: MainActivity) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NumberViewHolder {
            return NumberViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_number, p0, false))
        }

        override fun getItemCount(): Int {
            return activity.numberList.size
        }

        override fun onBindViewHolder(p0: NumberViewHolder, p1: Int) {
            p0.tvNumber.text = activity.numberList[p1]
        }

        class NumberViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvNumber = v.findViewById<TextView>(R.id.tv_number)
        }
    }

}
