package tw.chesskuo.search

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val spinner = findViewById(R.id.region_spinner) as Spinner
        val nAdapter = ArrayAdapter.createFromResource(this, R.array.region_array, android.R.layout.simple_spinner_item)
        nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(nAdapter)
    }



    private var cnt: Int = 0

    fun searchSummoner(view: View) {
        scroll_list.removeAllViews()

        val region = region_spinner.getSelectedItem().toString();
        val host = "https://${region}.api.riotgames.com"
        val mode = mapOf("summoner" to "/lol/summoner/v4/summoners/by-name/",
                            "league" to "/lol/league/v4/entries/by-summoner/",
                            "match" to "/lol/match/v4/matchlists/by-account/"
                        )
        val api_key = "?api_key=RGAPI-9d877f40-5f36-4ce3-b3bb-f0830e54977d"

        Toast.makeText(applicationContext, host, Toast.LENGTH_SHORT).show()


        val queue = Volley.newRequestQueue(this)
        val url = host + mode["summoner"] + summonerName.text + api_key

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {

                val tmp = layoutInflater.inflate(R.layout.summoner_profile, null)

                val imageRequest = ImageRequest("http://ddragon.leagueoflegends.com/cdn/9.3.1/img/profileicon/${it["profileIconId"]}.png",
                    Response.Listener<Bitmap> {

                        (tmp.findViewById(R.id.profile_icon) as ImageView).setImageBitmap(it)

                    }, 0, 0, Bitmap.Config.RGB_565, Response.ErrorListener {})

                queue.add(imageRequest)

                (tmp.findViewById(R.id.profile_name) as TextView).text = it["name"].toString()
                (tmp.findViewById(R.id.profile_level) as TextView).text = it["summonerLevel"].toString()
                (tmp.findViewById(R.id.profile_revision) as TextView).text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(Date(it["revisionDate"].toString().toLong()))

                scroll_list.addView(tmp)
            },
            Response.ErrorListener {

                val tmp = TextView(this)

                tmp.text = "Summoner Not Found!"
                tmp.textSize = 30f
                tmp.textAlignment = View.TEXT_ALIGNMENT_CENTER

                scroll_list.addView(tmp)
            }
        )

        queue.add(jsonObjectRequest)
    }
}
