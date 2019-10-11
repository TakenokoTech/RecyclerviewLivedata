package tech.takenoko.recyclerviewlivedata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private lateinit var recyclerFragment: RecyclerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerFragment = RecyclerFragment()
        supportFragmentManager.commit  {
            replace(R.id.fragment_container, recyclerFragment)
        }
    }

    interface MainViewAction
}
