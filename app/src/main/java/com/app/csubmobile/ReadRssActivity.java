package com.app.csubmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app.csubmobile.adapter.MyAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by qpngd on 3/26/2017.
 */

public class ReadRssActivity extends AsyncTask<Void, Void, Void> {

    Context context;
    String address = "http://25livepub.collegenet.com/calendars/university.rss?mixin=15119%2c14957";
    ProgressDialog progressDialog;
    ArrayList<FeedItem> feedItems;
    RecyclerView recyclerView;
    URL url;

    public ReadRssActivity(Context context, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        MyAdapter adapter = new MyAdapter(context, feedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Void doInBackground(Void... params) {
        ProcessXml(Getdata());
        return null;
    }

    private void ProcessXml(Document data) {
        if (data != null) {
//            Log.d("Root", data.getDocumentElement().getNodeName());
            feedItems = new ArrayList<>();
            // element object that stores the root element
            Element root = data.getDocumentElement();
            // node channel (first child)
            Node channel = root.getChildNodes().item(1);
            // Nodelist object that will store all child elements of channel node
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                // node that hold current child
                Node currentchild = items.item(i);
                // check that node is "item" node
                if (currentchild.getNodeName().equalsIgnoreCase("item")) {
                    FeedItem item = new FeedItem();
                    // nodelist holding item childs
                    NodeList itemchilds = currentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node current = itemchilds.item(j);
//                        Log.d("textcontent", current.getTextContent());
                        if (current.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("description")) {
                            String editText = current.getTextContent();
                            String patternRegex1 = "(?i)<br\\p{javaSpaceChar}*(?:/>|>)";
                            String patternRegex2 = "(?i)<b\\p{javaSpaceChar}*(?:/>|>)";
                            String patternRegex3 = "(?i)</b\\p{javaSpaceChar}*(?:/>|>)";
                            String patternRegex4 = "(?i)<p\\p{javaSpaceChar}*(?:/>|>)";
                            String patternRegex5 = "(?i)</p\\p{javaSpaceChar}*(?:/>|>)";
                            String patternRegex6 = "(?i)<a.*?>";
                            String patternRegex7 = "(?i)</a*(?:/>|>)";
                            String patternRegex8 = "(?i)<span.*?>";
                            String patternRegex9 = "(?i)</span*(?:/>|>)";
                            String newline = "\n";
                            String empty = "";
                            String newDescription = editText.replaceAll(patternRegex1, newline);
                            newDescription = newDescription.replaceAll(patternRegex2, empty);
                            newDescription = newDescription.replaceAll(patternRegex3, empty);
                            newDescription = newDescription.replaceAll(patternRegex4, newline);
                            newDescription = newDescription.replaceAll(patternRegex5, newline);
                            newDescription = newDescription.replaceAll(patternRegex6, empty);
                            newDescription = newDescription.replaceAll(patternRegex7, empty);
                            newDescription = newDescription.replaceAll(patternRegex8, empty);
                            newDescription = newDescription.replaceAll(patternRegex9, empty);

                            newDescription = newDescription.replace("&nbsp;", " ");
                            newDescription = newDescription.replace("&ndash;", "-");
                            newDescription = newDescription.replace("&#8211;", "-");
                            newDescription = newDescription.replace("&#39;", "'");
                            newDescription = newDescription.replace("&#160;", " ");
                            newDescription = newDescription.replace("&#8217;", "'");
                            newDescription = newDescription.replace("&#233;", "");
                            newDescription = newDescription.replace("&#8230;", "...");
                            newDescription = newDescription.replace("&amp;", "&");

                            Log.d("description before", newDescription);
                            item.setDescription(newDescription);
                        } else if (current.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("x-trumba:ealink")) {
                            item.setTrumbaLink(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("category")) {
                            item.setCatagory(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(current.getTextContent());
                        }
                    }
                    feedItems.add(item);
                    /*Log.d("itemTitle", item.getTitle());
                    Log.d("itemDescription", item.getDescription());
                    Log.d("itemLink", item.getLink());
                    Log.d("itemTrumaLink", item.getTrumbaLink());
                    Log.d("itemCategory", item.getCatagory());
                    Log.d("itemPubDate", item.getPubDate());*/
                }
            }
        }
    }

    public Document Getdata() {
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
