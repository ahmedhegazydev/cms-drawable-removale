import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

    private static final String
            HTTPS_BETA_VODAFONE_DE_CONFIG_ANDROID_V_1220_VERSION_JSON
            = "https://beta.vodafone.de/config/android_v1220_version.json";
    private static List listCmsDrawables = new ArrayList();
    private static String bodyKotlin = "";
    private static String bodyJava = "";


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static void main(String[] args) throws IOException, JSONException {


        JSONObject json = readJsonFromUrl(HTTPS_BETA_VODAFONE_DE_CONFIG_ANDROID_V_1220_VERSION_JSON);

        JSONArray items = json.getJSONArray("items");
        List listRes = new ArrayList();

        items.forEach(listItem -> {
            String resource = new JSONObject(listItem.toString()).getString("resource");
//            System.out.println(resource);
            listRes.add(resource);
        });


        listRes.forEach(item -> {
            try {

                /**
                 * Home
                 */
                if (item.toString().contains("home")) {
                    JSONObject jsonWW = readJsonFromUrl(item.toString());
//                System.out.println("home");
                    jsonWW.getJSONArray("components").forEach(component -> {
                        String icon =
                                new JSONObject(component.toString())
                                        .getJSONObject("config").getString("icon");
                        listCmsDrawables.add(icon);
                    });
                }

                /**
                 *Tobi
                 */
                if (item.toString().contains("tobi")) {
                    JSONObject jsonWW = readJsonFromUrl(item.toString());
                    jsonWW.getJSONArray("helpLinks").forEach(helpLink -> {
                        String icon =
                                new JSONObject(helpLink.toString()).getString("icon");
                        listCmsDrawables.add(icon);
                    });
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        });

//        System.out.println(listCmsDrawables.toString());

//        listCmsDrawables.forEach(res -> {
//            System.out.println(res);
//        });

        boolean kotlinOrJava = true;

        if (kotlinOrJava) {
            String start = "private Integer[] usedCmsDrawables = {";
            listCmsDrawables.forEach(res -> bodyJava += String.format("R.drawable.ic_%s,\n", res));
            String end = "};";
            String strFinal = start + bodyJava + end;
            System.out.println(strFinal);

        } else {
            String start = "private val usedCmsDrawables  =  mutableListOf<Int>(";
            listCmsDrawables.forEach(res -> bodyKotlin += String.format("R.drawable.ic_%s,\n", res));
            String end = ")";
            String strFinal = start + bodyKotlin + end;
            System.out.println(strFinal);
        }

    }

}