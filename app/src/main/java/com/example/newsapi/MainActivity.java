package com.example.newsapi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> title;
    ArrayList<String> con;
    ArrayList<String> img;
    AsyncHttpClient client;
    RequestParams params;
    LayoutInflater inflater;
    String url="https://thecity247.com/api/get_posts/";
    ListView listView;
    TextView titletxt,contenttxt;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=new ArrayList<>();
        con=new ArrayList<>();
        img=new ArrayList<>();
        client=new AsyncHttpClient();
        params=new RequestParams();

        listView=findViewById(R.id.list_view);


        client.get(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject jsonObject=new JSONObject(content);
                    String s=jsonObject.getString("status");
                    Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray=jsonObject.getJSONArray("posts");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String t=jsonObject1.getString("title");
                        title.add(t);
                        String c=jsonObject1.getString("content");
                        con.add(c);
                        JSONArray jsonArray1=jsonObject1.getJSONArray("attachments");
                        for (int a=0;a<jsonArray1.length();a++){
                            JSONObject jsonObject2=jsonArray1.getJSONObject(a);
                            String im=jsonObject2.getString("url");
                            img.add(im);


                        }
                    }

                    adapter adapt=new adapter();
                    listView.setAdapter(adapt);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    class adapter extends BaseAdapter{
        @Override
        public int getCount() {
            return title.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_view_layout,null);
            titletxt=(TextView)convertView.findViewById(R.id.textview_title);
            contenttxt=(TextView)convertView.findViewById(R.id.content_text);
            imageView=(ImageView) convertView.findViewById(R.id.images);

            titletxt.setText(title.get(position));
            contenttxt.setText(con.get(position));

            Glide.with(MainActivity.this).asBitmap().load(img.get(position)).into(imageView);



            return convertView;
        }
    }


}
