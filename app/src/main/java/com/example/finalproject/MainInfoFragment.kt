package com.example.finalproject

import android.content.res.AssetManager
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import kotlinx.android.synthetic.main.fragment_main_info.*
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

/**
 * A simple [Fragment] subclass.
 */
class MainInfoFragment : Fragment() {
    lateinit var adapter: MyAdapter
    var pageNo = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var regionname =""

        if(arguments!=null){
            regionname = arguments!!.getString("regionName").toString()
        }

        init(regionname)
        startXMLTask(regionname)

    }
    fun startXMLTask(regionname:String){
        val task = MyAsyncTask(this,regionname)
        task.execute(
            URL("http://openapi.cpf.go.kr/openapi/service/rest/ChildPlyFcltInfoService/getFcltInfo?"+
                "&pageNo="+pageNo+"&numOfRows=700&ServiceKey="+
                "Yoh1wQzCRaF%2Bsce%2F90HUUZx3Lt9XrST%2B%2Bf5wfjlT5QZJ%2FTTBYgXP1I3PnkMO1sTzUraFL3Aa72Tfq1aTkIVsZw%3D%3D")
        )
    }
    fun init(regionname:String){

        // Amazon Cognito 인증 공급자를 초기화합니다
        val credentialsProvider =
            CognitoCachingCredentialsProvider(
                activity?.applicationContext,
                "us-east-1:6af2b07e-6630-4e6e-b4ce-9903a8009e0c",  // 자격 증명 풀 ID
                Regions.US_EAST_1 // 리전
            )
        val s3: AmazonS3 = AmazonS3Client(credentialsProvider)
        val transferUtility = TransferUtility(s3, activity?.applicationContext)

        s3.setRegion(Region.getRegion(Regions.US_EAST_1))
        s3.setEndpoint("s3.us-east-1.amazonaws.com")

        val filePath = activity?.getApplicationContext()?.getFilesDir()?.getPath().toString() + "/file.txt";
        val file = File(filePath);

       /* val observer = transferUtility.upload(
            "2020-cws-s3test",   //업로드 할 버킷 이름
            file.name,   //버킷에 저장할 파일의 이름
            file  //버킷에 저장할 파일
        )
        observer*/
        val reviewpath = activity?.getApplicationContext()?.getFilesDir()?.getPath().toString() + "/PlaygroundInfo.json";
        val review = File(reviewpath);
        val readjson = transferUtility.download(
            "2020-cws-s3test",
            review.name,
            review
        )
        val reviewfile = File(readjson.absoluteFilePath)
        //val sd = reviewfile.readText()



        val regionname = regionname
        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true
            startXMLTask(regionname)
        }
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        adapter = MyAdapter(ArrayList<MyData>())
        recyclerView.adapter = adapter
        adapter.itemClickListener = object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(
                holder: MyAdapter.MyViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
                try {
                    val address =
                        Geocoder(context).getFromLocationName(holder.ciaddr.text.toString(), 1)
                    val mapfragment = MapFragment()
                    val latitude = address[0].latitude
                    val longitude = address[0].longitude
                    val ciname = holder.ciname.text.toString()
                    val ciaddr = holder.ciaddr.text.toString()
                    var name1 = data.name1.toString()
                    var name21 = data.name21.toString()
                    var name2 = data.name2.toString()
                    var name4 = data.name4

                    var args = Bundle()
                    args.putString("ciname", ciname)
                    args.putDouble("latitude", latitude)
                    args.putDouble("longitude", longitude)
                    args.putString("ciaddr", ciaddr)
                    args.putString("name1", name1)
                    args.putString("name21", name21)
                    args.putString("name2",name2)
                    args.putString("name4",name4)

                    mapfragment.arguments = args

                    fragmentManager?.beginTransaction()?.replace(R.id.frame, mapfragment)
                        ?.commit()
                }catch(e:IOException){
                    Log.e("MapsActivity",e.localizedMessage)
                }
            }

        }
    }
    class MyAsyncTask(context:MainInfoFragment,regionName:String): AsyncTask<URL, Unit, Unit>(){
        val fragmentreference = WeakReference(context)
        val regionname = regionName
        override fun doInBackground(vararg params: URL?): Unit {
            val fragment = fragmentreference.get()
            fragment?.adapter?.items?.clear()

            val doc = Jsoup.connect(params[0].toString()).parser(Parser.xmlParser()).get()
            val ciName = doc.select("item")
            for(info in ciName){
                if(regionname == "") {
                    if (info.select("ciRaddr1").toString() == "") {
                        fragment?.adapter?.items?.add(
                            MyData(
                                info.select("ciName").text(),
                                info.select("ciNaddr2").text(),
                                info.select("name1").text(),
                                info.select("name21").text(),
                                info.select("name2").text(),
                                info.select("name4").text()
                            )
                        )
                    } else {
                        fragment?.adapter?.items?.add(
                            MyData(
                                info.select("ciName").text(),
                                info.select("ciRaddr1").text(),
                                info.select("name1").text(),
                                info.select("name21").text(),
                                info.select("name2").text(),
                                info.select("name4").text()
                            )
                        )
                    }
                }else{
                    if (info.select("name3").text().toString().indexOf(regionname)>=0) {
                        if (info.select("ciRaddr1").toString() == "") {
                            fragment?.adapter?.items?.add(
                                MyData(
                                    info.select("ciName").text(),
                                    info.select("ciNaddr2").text(),
                                    info.select("name1").text(),
                                    info.select("name21").text(),
                                    info.select("name2").text(),
                                    info.select("name4").text()
                                )
                            )
                        } else {
                            fragment?.adapter?.items?.add(
                                MyData(
                                    info.select("ciName").text(),
                                    info.select("ciRaddr1").text(),
                                    info.select("name1").text(),
                                    info.select("name21").text(),
                                    info.select("name2").text(),
                                    info.select("name4").text()
                                )
                            )
                        }
                    }
                }
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            val fragment = fragmentreference.get()
            fragment?.swiperefresh?.isRefreshing = false
            fragment?.adapter?.notifyDataSetChanged()
        }
    }
}

