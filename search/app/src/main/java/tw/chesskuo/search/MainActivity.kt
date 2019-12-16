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


const val proto = "https://"
var region = ""
const val host = ".api.riotgames.com"
val mode = mapOf("summoner" to "/lol/summoner/v4/summoners/by-name/",
                "league" to "/lol/league/v4/entries/by-summoner/",
                "match" to "/lol/match/v4/matchlists/by-account/"
            )
const val api_key = "?api_key=" + "RGAPI-9d877f40-5f36-4ce3-b3bb-f0830e54977d"

var id = ""
var accountId = ""





class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nAdapter = ArrayAdapter.createFromResource(this, R.array.region_arr, android.R.layout.simple_spinner_item)
        nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        region_spinner.adapter = nAdapter
    }



    fun searchSummoner(view: View)
    {
        scroll_list.removeAllViews()

        val queue = Volley.newRequestQueue(this)

        region = region_spinner.selectedItem.toString()
        val url = proto + region + host + mode["summoner"] + summonerName.text + api_key

        val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val tmp = layoutInflater.inflate(R.layout.summoner_profile, null)

                val imgReq = ImageRequest("http://ddragon.leagueoflegends.com/cdn/9.3.1/img/profileicon/${it["profileIconId"]}.png",
                    Response.Listener<Bitmap> {
                        (tmp.findViewById(R.id.profile_icon) as ImageView).setImageBitmap(it)
                    }, 0, 0, Bitmap.Config.RGB_565, Response.ErrorListener {})
                queue.add(imgReq)

                id = it["id"].toString()
                accountId = it["accountId"].toString()

//                Toast.makeText(applicationContext, id, Toast.LENGTH_SHORT).show()

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
        queue.add(jsonReq)
    }
}