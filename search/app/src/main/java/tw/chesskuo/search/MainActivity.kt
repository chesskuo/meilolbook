package tw.chesskuo.search

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.*
import java.util.*
import androidx.fragment.app.Fragment
import android.widget.ListView





const val proto = "https://"
var region = ""
const val host = ".api.riotgames.com"
val mode = mapOf("summoner" to "/lol/summoner/v4/summoners/by-name/",
                "league" to "/lol/league/v4/entries/by-summoner/",
                "match" to "/lol/match/v4/matchlists/by-account/"
            )
const val api_key = "?api_key=" + "RGAPI-9d877f40-5f36-4ce3-b3bb-f0830e54977d"





class MainActivity : AppCompatActivity()
{
    private var id = ""
    private var accountId = ""

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
                val tmp_layout = layoutInflater.inflate(R.layout.summoner_profile, null)

                val imgReq = ImageRequest("http://ddragon.leagueoflegends.com/cdn/9.3.1/img/profileicon/${it["profileIconId"]}.png",
                    Response.Listener<Bitmap> {
                        (tmp_layout.findViewById(R.id.profile_icon) as ImageView).setImageBitmap(it)
                    }, 0, 0, Bitmap.Config.RGB_565, Response.ErrorListener {})
                queue.add(imgReq)

                id = it["id"].toString()
                accountId = it["accountId"].toString()

//                Toast.makeText(applicationContext, id, Toast.LENGTH_SHORT).show()

                (tmp_layout.findViewById(R.id.profile_name) as TextView).text = it["name"].toString()
                (tmp_layout.findViewById(R.id.profile_level) as TextView).text = it["summonerLevel"].toString()
                (tmp_layout.findViewById(R.id.profile_revision) as TextView).text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(Date(it["revisionDate"].toString().toLong()))

                val listview = tmp_layout.findViewById(R.id.match_list) as ListView

                addToList(listview)

                scroll_list.addView(tmp_layout)
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

    fun addToList(listview: ListView)
    {
        var info :ArrayList<ListView_save> = ArrayList()
        info.add(ListView_save("冰鳥",R.mipmap.ic_launcher,"VICTORY","6/6/6","145","18000","2019/10/10","45:00"))
        info.add(ListView_save("刮哥",R.mipmap.ic_launcher,"DEFEAT","0/6/0","0","20000","2019/10/4","20:00"))
        info.add(ListView_save("乞丐大劍",R.mipmap.ic_launcher,"VICTORY","4/2/1","76","10000","2019/10/3","36:00"))
        info.add(ListView_save("桶子",R.mipmap.ic_launcher,"VICTORY","1/0/6","127","5000","2019/10/12","38:00"))
        info.add(ListView_save("卡撒",R.mipmap.ic_launcher,"VICTORY","7/7/7","300","7800","2019/10/5","30:34"))
        info.add(ListView_save("滑起來",R.mipmap.ic_launcher,"VICTORY","6/6/6","0","6666","2019/10/6","30:43"))
        info.add(ListView_save("李星",R.mipmap.ic_launcher,"VICTORY","6/6/6","60","8888","2019/10/8","30:55"))
        info.add(ListView_save("雷歐娜",R.mipmap.ic_launcher,"VICTORY","6/6/6","99","6666","2019/10/30","31:00"))
        info.add(ListView_save("娜米",R.mipmap.ic_launcher,"VICTORY","6/6/6","0","7777","2019/10/21","10:00"))
        info.add(ListView_save("賽恩",R.mipmap.ic_launcher,"VICTORY","9/9/9","2","1","2019/10/11","20:00"))

        listview.adapter = CustomAdapter(this, info)
    }
}



















data class ListView_save(var name: String,var image: Int,var matchEnd:String,var Scroe:String,var cs:String,var money:String,var date:String,var time:String)

class CustomAdapter(var context: Context, var champion:ArrayList<ListView_save>):BaseAdapter() {

    private class ViewHolder(row:View?){
        var txtName : TextView = row?.findViewById(R.id.championName) as TextView
        var ivImage : ImageView = row?.findViewById(R.id.championPic) as ImageView
        var txtMatchend : TextView = row?.findViewById(R.id.matchEnd_id) as TextView
        var txtScore : TextView = row?.findViewById(R.id.Scroe_id) as TextView
        var txtCs : TextView = row?.findViewById(R.id.cs_id) as TextView
        var txtMoney : TextView = row?.findViewById(R.id.money_id) as TextView
        var txtDate : TextView = row?.findViewById(R.id.date_id) as TextView
        var txtTime : TextView = row?.findViewById(R.id.time_id) as TextView

    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if(convertView==null){
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.match_list_row,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var cham: ListView_save = getItem(position) as ListView_save
        viewHolder.txtName.text = cham.name
        viewHolder.ivImage.setImageResource(cham.image)
        viewHolder.txtMatchend.text = cham.matchEnd
        viewHolder.txtScore.text = cham.Scroe
        viewHolder.txtCs.text = cham.cs
        viewHolder.txtMoney.text = cham.money
        viewHolder.txtDate.text = cham.date
        viewHolder.txtTime.text = cham.time

        return  view as View
    }

    override fun getItem(position: Int): Any {
        return champion.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return champion.count()
    }
}